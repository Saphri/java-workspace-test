package org.mjelle.scheduler;

import java.util.stream.Collectors;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.jboss.logging.Logger;

import io.opentelemetry.api.baggage.Baggage;
import io.smallrye.common.annotation.RunOnVirtualThread;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TaskScheduler {
  
  private final Logger log = Logger.getLogger(TaskScheduler.class);

  @Incoming("data-in")
  @RunOnVirtualThread
  public Uni<Void> onResourceEvent(Message<String> msg) {
    log.infof("onResourceEvent running: %s", msg.getPayload());

    Baggage currentBaggage = Baggage.current();
    log.infof("current baggage: %s", asString(currentBaggage));

    return Uni.createFrom().completionStage(msg.ack());
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
