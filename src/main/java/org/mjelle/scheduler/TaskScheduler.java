package org.mjelle.scheduler;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import org.jboss.logging.Logger;

import io.nats.client.JetStreamApiException;
import io.nats.client.api.StreamInfoOptions;
import io.quarkiverse.reactive.messaging.nats.jetstream.client.Connection;
import io.quarkiverse.reactive.messaging.nats.jetstream.client.ConnectionFactory;
import io.quarkiverse.reactive.messaging.nats.jetstream.configuration.JetStreamConfiguration;
import io.quarkus.scheduler.Scheduled;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TaskScheduler {
  
  private final Logger log = Logger.getLogger(TaskScheduler.class);

  private final ConnectionFactory connectionFactory;
  private final JetStreamConfiguration natsConfiguration;

  private final AtomicReference<Connection> messageConnection = new AtomicReference<>();

  public TaskScheduler(ConnectionFactory connectionFactory, JetStreamConfiguration natsConfiguration) {
    this.connectionFactory = connectionFactory;
    this.natsConfiguration = natsConfiguration;
  }

  @Scheduled(every = "10s", concurrentExecution = Scheduled.ConcurrentExecution.SKIP)
  @RunOnVirtualThread
  public void scheduleTask() {
    log.info("Task scheduler running");

    final var conn = getOrEstablishMessageConnection();

    conn.nativeConnection(c -> {
      try {
        final var streamState = c.jetStreamManagement().getStreamInfo("task-queue", StreamInfoOptions.allSubjects()).getStreamState();

        streamState.getSubjectMap().entrySet().stream().forEach(subjectState -> {
          log.infof("SubjectState name: %s, messages: %d", subjectState.getKey(), subjectState.getValue());

          final var msg = "Resource event for subject " + subjectState.getKey();
          final var subject = subjectState.getKey().replace("task.", "resource-event.");

          try {
            c.jetStream().publish(subject, null, msg.getBytes());
            log.warnf("Sent Resource event on subject: %s", subject);
          } catch (IOException | JetStreamApiException e) {
            log.debugf(e, "Exception publishing message to subject: %s", subject);
          }
        });
      } catch (IOException | JetStreamApiException e) {
        log.warnf(e, "Exception creating stream task-queue");
      }
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
