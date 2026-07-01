package org.mjelle.rest;

import java.util.UUID;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.jboss.logging.Logger;
import org.mjelle.util.BaggageUtils;

import io.opentelemetry.api.baggage.Baggage;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/hello")
@ApplicationScoped
@RunOnVirtualThread
public class GreetingResource {
  private final Logger log = Logger.getLogger(GreetingResource.class);

  private final Emitter<String> emitter;

    public GreetingResource(final @Channel("data-out") Emitter<String> emitter) {
        this.emitter = emitter;
    }

    @GET
    public void seedTasks() {
        final var uuid = UUID.randomUUID();
        final var currentBaggage = Baggage.current();
        log.infof("current baggage: %s", BaggageUtils.asString(currentBaggage));

        emitter.send(Message.of(uuid.toString()));
    }
}
