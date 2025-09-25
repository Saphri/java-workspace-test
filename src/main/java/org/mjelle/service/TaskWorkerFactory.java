package org.mjelle.service;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;

import io.quarkiverse.reactive.messaging.nats.jetstream.client.Connection;
import io.quarkiverse.reactive.messaging.nats.jetstream.client.ConnectionFactory;
import io.quarkiverse.reactive.messaging.nats.jetstream.configuration.JetStreamConfiguration;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.Dependent;
import lombok.RequiredArgsConstructor;
import lombok.extern.jbosslog.JBossLog;

@Dependent
@RequiredArgsConstructor
@JBossLog
public class TaskWorkerFactory {
  private final ConnectionFactory connectionFactory;
  private final JetStreamConfiguration natsConfiguration;
  private final AtomicReference<Connection> messageConnection = new AtomicReference<>();
  private final TaskWorker taskWorker;

  public Uni<Void> start(final String resourceId) {
    log.infof("Inspecting possible work for resourceId: %s", resourceId);

    // read message(s) from task-queue
    final var conf = new TaskConsumerConfiguration("task." + resourceId, Duration.ofSeconds(60));
    return getOrEstablishMessageConnection()
        .flatMap(conn -> conn.next("task-queue", "task-consumer-" + resourceId, conf, Duration.ofSeconds(1)))
        .onFailure().recoverWithNull()
        .onItem().ifNotNull().transformToUni(msg -> taskWorker.doWork(msg, resourceId));
  }

  private Uni<Connection> getOrEstablishMessageConnection() {
    return Uni.createFrom().item(() -> {
      messageConnection.get();
      return messageConnection.get();
    })
        .onItem().ifNull()
        .switchTo(() -> connectionFactory.create(natsConfiguration.connection()))
        .invoke(this.messageConnection::set);
  }
}
