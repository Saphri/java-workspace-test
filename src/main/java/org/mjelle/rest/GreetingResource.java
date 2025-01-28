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

    @Channel("myoutgoing")
    private final MutinyEmitter<String> emitter;

    private final UUID uuid = UUID.randomUUID();

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<Void> hello() {
        var metadata= PublishMessageMetadata.builder()
                .subject("mysubject." + uuid.toString()).build();

        return emitter.sendMessage(Message.of("Hello called!").addMetadata(metadata))
            .onFailure().recoverWithItem(e -> {
                return null;
            });
    }
}
