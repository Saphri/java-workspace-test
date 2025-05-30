package org.mjelle.scheduler;

import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Message;

import io.opentelemetry.context.Context;
import io.quarkiverse.reactive.messaging.nats.NatsConfiguration;
import io.quarkiverse.reactive.messaging.nats.jetstream.client.Connection;
import io.quarkiverse.reactive.messaging.nats.jetstream.client.ConnectionFactory;
import io.quarkiverse.reactive.messaging.nats.jetstream.client.api.PublishMessageMetadata;
import io.quarkiverse.reactive.messaging.nats.jetstream.client.configuration.ConnectionConfiguration;
import io.quarkus.opentelemetry.runtime.QuarkusContextStorage;
import io.quarkus.scheduler.Scheduled;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.MutinyEmitter;
import io.smallrye.reactive.messaging.TracingMetadata;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.jbosslog.JBossLog;

@ApplicationScoped
@RequiredArgsConstructor
@JBossLog
public class TaskScheduler {
  private final ConnectionFactory connectionFactory;
  private final NatsConfiguration natsConfiguration;
  private final AtomicReference<Connection<String>> messageConnection = new AtomicReference<>();

  @Channel("resource-event-out")
  private final MutinyEmitter<String> emitter;

  @Scheduled(every = "10s", concurrentExecution = Scheduled.ConcurrentExecution.SKIP)
  public void scheduleTask() {
    log.info("Task scheduler running");

    getOrEstablishMessageConnection()
        .flatMap(conn -> conn.streamManagement())
        .flatMap(streamManagement -> streamManagement.getStreamState("task-queue"))
        .onItem().transformToMulti(steamState -> {
            var m = steamState.subjectStates();
            return Multi.createFrom().items(m.stream());
        })
        .call(subjectState -> {
            log.infof("SubjectState name: %s, messages: %d", subjectState.name(), subjectState.count());

            final var metadata = PublishMessageMetadata.builder()
                .subject(subjectState.name().replace("task.", "resource-event."))
                .build();
            final var traceMetadata = TracingMetadata.withCurrent(QuarkusContextStorage.INSTANCE.current());

            return emitter.sendMessage(Message.of("Work sheduled!").addMetadata(metadata).addMetadata(traceMetadata));              
        })
        .subscribe().with(
            item -> log.info("Task-queue inspected successfully"),
            t -> log.error("Failed to browse task-queue", t));
  }

  private Uni<Connection<String>> getOrEstablishMessageConnection() {
    return Uni.createFrom().item(() -> {
      messageConnection.get();
      return messageConnection.get();
    })
        .onItem().ifNull()
        .switchTo(() -> connectionFactory.create(ConnectionConfiguration.of(natsConfiguration)))
        .onItem().invoke(this.messageConnection::set);
  }
}
