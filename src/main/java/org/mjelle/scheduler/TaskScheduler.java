package org.mjelle.scheduler;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.jboss.logging.Logger;
import org.mjelle.util.BaggageUtils;

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

    final var currentBaggage = Baggage.current();
    log.infof("current baggage: %s", BaggageUtils.asString(currentBaggage));

    return Uni.createFrom().completionStage(msg.ack());
  }
  

}
