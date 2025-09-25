package org.mjelle.service;

import java.time.Duration;
import java.util.concurrent.Executors;

import org.eclipse.microprofile.reactive.messaging.Message;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.Dependent;
import lombok.extern.jbosslog.JBossLog;

@Dependent
@JBossLog
public class TaskWorker {
  public Uni<Void> doWork(Message<Object> msg, String resourceId) {
    log.errorf("Doing work for resourceId: %s, message: %s", resourceId, msg);
    return Uni.createFrom().item(msg)
        .emitOn(Executors.newFixedThreadPool(10))
        .onItem().delayIt().by(Duration.ofSeconds(15))
        .invoke(m -> log.warnf("Work done for resourceId: %s", resourceId))
        .replaceWithVoid()
        .onFailure().invoke(e -> log.errorf(e, "Error while doing work for resourceId: %s", resourceId))
        .eventually(() -> Uni.createFrom().completionStage(msg.ack()));
  }
}
