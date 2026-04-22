package org.mjelle.scheduler;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.metrics.LongCounter;

@ApplicationScoped
public class TaskScheduler {
  
  private final Logger log = Logger.getLogger(TaskScheduler.class);
  private final LongCounter onResourceEventCounter;

  public TaskScheduler(Meter meter) {
    this.onResourceEventCounter = meter.counterBuilder("task_scheduler_on_resource_event")
        .setDescription("Counts invocations of onResourceEvent")
        .build();
  }

  @Incoming("data-in")
  public void onResourceEvent(String msg) {
    log.infof("onResourceEvent running: %s", msg);
    onResourceEventCounter.add(1);
  }
}
