package org.mjelle.scheduler;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;

import io.quarkiverse.reactive.messaging.nats.jetstream.client.Connection;
import io.quarkiverse.reactive.messaging.nats.jetstream.client.ConnectionFactory;
import io.quarkiverse.reactive.messaging.nats.jetstream.client.api.PublishMessageMetadata;
import io.quarkiverse.reactive.messaging.nats.jetstream.configuration.JetStreamConfiguration;
import io.quarkus.scheduler.Scheduled;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.jbosslog.JBossLog;

@ApplicationScoped
@RequiredArgsConstructor
@JBossLog
public class TaskScheduler {
  private final ConnectionFactory connectionFactory;
  private final JetStreamConfiguration natsConfiguration;
  private final AtomicReference<Connection> messageConnection = new AtomicReference<>();

  @Channel("resource-event-out")
  private final Emitter<String> emitter;

  @Scheduled(every = "10s", concurrentExecution = Scheduled.ConcurrentExecution.SKIP)
  @RunOnVirtualThread
  public void scheduleTask() {
    log.info("Task scheduler running");

    final var conn = getOrEstablishMessageConnection();
    final var streamManagement = conn.streamManagement().await().indefinitely();
    final var streamState = streamManagement.getStreamState("task-queue").await().indefinitely();
    streamState.subjectStates().stream().forEach(subjectState -> {
        log.infof("SubjectState name: %s, messages: %d", subjectState.name(), subjectState.count());

        final var metadata = PublishMessageMetadata.builder()
            .subject(subjectState.name().replace("task.", "resource-event."))
            .build();

        emitter.send(Message.of("Resource event for subject " + subjectState.name(),
          () -> {
            // Called when the message is acknowledged.
            log.infof("Message to subject %s was acknowledged", subjectState.name());
            return CompletableFuture.completedStage(null);
          },
          failure -> {
            // Called when the message is acknowledged negatively.
            log.warnf(failure, "Message to subject %s was not acknowledged: %s", subjectState.name());
            return CompletableFuture.completedStage(null);
          }).addMetadata(metadata)
        );

        log.info("Sent Resource event");
    });
  }

  private Connection getOrEstablishMessageConnection() {
    var connection = messageConnection.get();
    if (connection == null) {
      connection = connectionFactory.create(natsConfiguration.connection()).await().indefinitely();
      messageConnection.set(connection);
    }
    return connection;
  }
}
