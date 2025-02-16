package org.mjelle.rest;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.mjelle.service.TaskConsumerConfiguration;

import io.quarkiverse.reactive.messaging.nats.NatsConfiguration;
import io.quarkiverse.reactive.messaging.nats.jetstream.client.Connection;
import io.quarkiverse.reactive.messaging.nats.jetstream.client.ConnectionFactory;
import io.quarkiverse.reactive.messaging.nats.jetstream.client.api.PublishMessageMetadata;
import io.quarkiverse.reactive.messaging.nats.jetstream.client.configuration.ConnectionConfiguration;
import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.MutinyEmitter;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;
import lombok.extern.jbosslog.JBossLog;

@Path("/hello")
@RequiredArgsConstructor
@JBossLog
public class GreetingResource {
  private final ConnectionFactory connectionFactory;
  private final NatsConfiguration natsConfiguration;
  private final AtomicReference<Connection<String>> messageConnection = new AtomicReference<>();

    @Channel("task-queue-out")
    private final MutinyEmitter<String> emitter;

    private final UUID uuid = UUID.randomUUID();

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<Void> seedTasks() {
        return getOrEstablishMessageConnection()
            .onItem().invoke(conn -> {
                final var conf = new TaskConsumerConfiguration<String>("task-consumer-" + uuid.toString(),
                        "task-queue", "task." + uuid.toString(), Duration.ofSeconds(60));
                conn.addConsumer(conf);
            })
            .flatMap(conn -> {
                final var metadata = PublishMessageMetadata.builder()
                        .subject("task." + uuid.toString()).build();

                return emitter.sendMessage(Message.of("Task added!").addMetadata(metadata));
        });
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
