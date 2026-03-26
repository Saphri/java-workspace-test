package org.mjelle.scheduler;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TaskScheduler {
  
  private final Logger log = Logger.getLogger(TaskScheduler.class);

  @Incoming("data-in")
  @RunOnVirtualThread
  public void onResourceEvent(String msg) {
    log.infof("onResourceEvent running: %s", msg);
  }
}
