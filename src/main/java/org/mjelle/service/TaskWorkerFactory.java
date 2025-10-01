package org.mjelle.service;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicReference;

import org.jboss.logging.Logger;

import io.quarkiverse.reactive.messaging.nats.jetstream.client.Connection;
import io.quarkiverse.reactive.messaging.nats.jetstream.client.ConnectionFactory;
import io.quarkiverse.reactive.messaging.nats.jetstream.configuration.JetStreamConfiguration;
import io.quarkus.virtual.threads.VirtualThreads;
import jakarta.enterprise.context.Dependent;

@Dependent
public class TaskWorkerFactory {
  
  private final Logger log = Logger.getLogger(TaskWorkerFactory.class);

  private final ConnectionFactory connectionFactory;
  private final JetStreamConfiguration natsConfiguration;
  private final TaskWorker taskWorker;
  private final ExecutorService executor;

  private final AtomicReference<Connection> messageConnection = new AtomicReference<>();

  public TaskWorkerFactory(final ConnectionFactory connectionFactory,
      final JetStreamConfiguration natsConfiguration,
      final TaskWorker taskWorker, @VirtualThreads ExecutorService executor) {
    this.connectionFactory = connectionFactory;
    this.natsConfiguration = natsConfiguration;
    this.taskWorker = taskWorker;
    this.executor = executor;
  }

  public void start(final String resourceId) {
    log.errorf("Inspecting possible work for resourceId: %s", resourceId);

    // read message(s) from task-queue
    final var conf = new TaskConsumerConfiguration("task." + resourceId, Duration.ofSeconds(60));
    final var conn = getOrEstablishMessageConnection();

    try {
      final var msg = conn.next("task-queue", "task-consumer-" + resourceId, conf, Duration.ofSeconds(1)).await().indefinitely();
      executor.submit(() -> {
        try {
          taskWorker.doWork(msg, resourceId);
          return msg.ack();
        } catch (Exception e) {
          return msg.nack(e);
        }
      }).get();
    } catch (Exception e) {
      log.errorf(e, "No work found for resourceId: %s", resourceId);
    }
  }

  private Connection getOrEstablishMessageConnection() {
    var connection = messageConnection.get();
    if (connection == null) {
      connection = connectionFactory.create(natsConfiguration.connection()).await().indefinitely();
      messageConnection.set(connection);
    }
    return messageConnection.get();
  }
}
