package org.mjelle.rest;

import java.util.UUID;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;

import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/hello")
@ApplicationScoped
@RunOnVirtualThread
public class GreetingResource {

  private final Emitter<String> emitter;

    public GreetingResource(final @Channel("data-out") Emitter<String> emitter) {
        this.emitter = emitter;
    }

    @GET
    public void seedTasks() {
        for (int i = 0; i < 100; i++) {
            final var uuid = UUID.randomUUID();
            emitter.send(Message.of(uuid.toString()));
        }
    }
}
