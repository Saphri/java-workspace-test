package org.mjelle.service;

import java.time.Duration;
import java.util.concurrent.CompletionStage;

import org.eclipse.microprofile.reactive.messaging.Message;

import jakarta.enterprise.context.Dependent;
import lombok.extern.jbosslog.JBossLog;

@Dependent
@JBossLog
public class TaskWorker {

  /**
   * Performs work asynchronously for the given resource.
   *
   * @param msg the reactive message to acknowledge or nack
   * @param resourceId the resource identifier
   */
  public CompletionStage<Void> doWork(final Message<Object> msg, final String resourceId) {
    try {
      Thread.sleep(Duration.ofSeconds(15).toMillis());
      log.errorf("Work done for resourceId: %s", resourceId);
      return msg.ack();
    } catch (InterruptedException e) {
      log.errorf(e, "Error while doing work for resourceId: %s", resourceId);
      return msg.nack(e);
    }
  }
}
