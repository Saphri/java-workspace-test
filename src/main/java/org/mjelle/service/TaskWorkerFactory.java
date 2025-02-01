package org.mjelle.service;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import io.quarkiverse.reactive.messaging.nats.NatsConfiguration;
import io.quarkiverse.reactive.messaging.nats.jetstream.client.Connection;
import io.quarkiverse.reactive.messaging.nats.jetstream.client.ConnectionFactory;
import io.quarkiverse.reactive.messaging.nats.jetstream.client.configuration.ConnectionConfiguration;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.Dependent;
import lombok.RequiredArgsConstructor;
import lombok.extern.jbosslog.JBossLog;

@Dependent
@RequiredArgsConstructor
@JBossLog
public class TaskWorkerFactory {
  private final ConnectionFactory connectionFactory;
  private final NatsConfiguration natsConfiguration;
  private final AtomicReference<Connection<String>> messageConnection = new AtomicReference<>();
  private final TaskWorker taskWorker;

  public Uni<Void> start(String resourceId) {
    log.infof("Inspecting possible work for resourceId: %s", resourceId);

    // read message(s) from task-queue
    var conf = new TaskConsumerConfiguration<String>("task-consumer-" + resourceId,
        "task-queue", "task." + resourceId, Duration.ofSeconds(60));
    return getOrEstablishMessageConnection()
        .flatMap(conn -> conn.next(conf, Duration.ofSeconds(1)))
        .onFailure().recoverWithNull()
        .onItem().ifNotNull().transformToUni(msg -> taskWorker.doWork(msg, resourceId));
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
