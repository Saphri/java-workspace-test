package org.mjelle.rest;

import java.util.UUID;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;

import io.quarkiverse.reactive.messaging.nats.jetstream.client.api.PublishMessageMetadata;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/hello")
@RunOnVirtualThread
public class GreetingResource {
    private final Emitter<String> emitter;

    public GreetingResource(@Channel("data-out") Emitter<String> emitter) {
        this.emitter = emitter;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public void seedTasks() {
        final var uuid = UUID.randomUUID();

        emitter.send(Message.of(uuid.toString()));
    }
}
