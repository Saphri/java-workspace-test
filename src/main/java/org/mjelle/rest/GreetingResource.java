package org.mjelle.rest;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.mjelle.service.TaskConsumerConfiguration;

import io.quarkiverse.reactive.messaging.nats.jetstream.client.Connection;
import io.quarkiverse.reactive.messaging.nats.jetstream.client.ConnectionFactory;
import io.quarkiverse.reactive.messaging.nats.jetstream.client.api.PublishMessageMetadata;
import io.quarkiverse.reactive.messaging.nats.jetstream.configuration.JetStreamConfiguration;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;
import lombok.extern.jbosslog.JBossLog;

@Path("/hello")
@RunOnVirtualThread
@RequiredArgsConstructor
@JBossLog
public class GreetingResource {
  private final ConnectionFactory connectionFactory;
  private final JetStreamConfiguration natsConfiguration;
  private final AtomicReference<Connection> messageConnection = new AtomicReference<>();

    @Channel("task-queue-out")
    private final Emitter<String> emitter;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public void seedTasks() {
        final var uuid = UUID.randomUUID();
        final var conn = getOrEstablishMessageConnection();
        
        final var conf = new TaskConsumerConfiguration("task." + uuid.toString(), Duration.ofSeconds(30));
        conn.addConsumerIfAbsent("task-queue", "task-consumer-" + uuid.toString(), conf).await().indefinitely();

        final var metadata = PublishMessageMetadata.builder()
          .subject("task." + uuid.toString()).build();

        emitter.send(Message.of("Task added!").addMetadata(metadata));
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
