package org.mjelle.scheduler;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.jboss.logging.Logger;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TaskScheduler {
  
  private final Logger log = Logger.getLogger(TaskScheduler.class);

  @Incoming("data-in")
  public Uni<Void> onResourceEvent(Message<String> msg) {
    log.infof("onResourceEvent running: %s", msg.getPayload());
    return Uni.createFrom().completionStage(msg.ack());
  }
}
