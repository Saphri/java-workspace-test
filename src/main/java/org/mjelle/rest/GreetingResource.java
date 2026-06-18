package org.mjelle.rest;

import java.util.UUID;
import java.util.stream.Collectors;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.jboss.logging.Logger;
import org.mjelle.scheduler.TaskScheduler;

import io.opentelemetry.api.baggage.Baggage;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/hello")
@ApplicationScoped
@RunOnVirtualThread
public class GreetingResource {
  private final Logger log = Logger.getLogger(GreetingResource.class);

  private final Emitter<String> emitter;

    public GreetingResource(@Channel("data-out") Emitter<String> emitter) {
        this.emitter = emitter;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public void seedTasks() {
        final var uuid = UUID.randomUUID();
    Baggage currentBaggage = Baggage.current();
    log.infof("current baggage: %s", asString(currentBaggage));

        emitter.send(Message.of(uuid.toString()));
    }
private static String asString(Baggage baggage) {
    return baggage.asMap().entrySet().stream()
        .map(
            entry ->
                String.format(
                    "%s=%s(%s)",
                    entry.getKey(),
                    entry.getValue().getValue(),
                    entry.getValue().getMetadata().getValue()))
        .collect(Collectors.joining(", ", "{", "}"));
  }
}
