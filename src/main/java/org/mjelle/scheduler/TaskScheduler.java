package org.mjelle.scheduler;

import java.util.concurrent.atomic.LongAdder;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.jboss.logging.Logger;

import io.smallrye.common.annotation.RunOnVirtualThread;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TaskScheduler {

  private final Logger log = Logger.getLogger(TaskScheduler.class);

  private final LongAdder concurrentTasks = new LongAdder();

  @Incoming("data-in")
  @RunOnVirtualThread
  public Uni<Void> onResourceEvent(Message<String> msg) {
    concurrentTasks.increment();
    final var count = concurrentTasks.sum();
    log.infof("onResourceEvent running: %s (concurrent: %d)", msg.getPayload(), count);
    if (count >= 20) {
      log.warnf("High concurrency detected: %d concurrent tasks", count);
    }

    try {
      try {
        Thread.sleep(2000);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    } finally {
      concurrentTasks.decrement();
      log.infof("task complete (concurrent: %d)", concurrentTasks.sum());
    }

    return Uni.createFrom().completionStage(msg.ack());
  }
}
