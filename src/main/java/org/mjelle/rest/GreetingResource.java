package org.mjelle.rest;

import java.util.UUID;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Message;

import io.quarkiverse.reactive.messaging.nats.jetstream.client.api.PublishMessageMetadata;
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

    @Channel("task-queue-out")
    private final MutinyEmitter<String> emitter;

    private final UUID uuid = UUID.randomUUID();

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<Void> seedTasks() {
        var metadata= PublishMessageMetadata.builder()
                .subject("task." + uuid.toString()).build();

        emitter.sendMessageAndForget(Message.of("Task added!").addMetadata(metadata));
        return Uni.createFrom().voidItem();
    }
}
