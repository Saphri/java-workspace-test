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
  public Uni<Void> doWork(Message<String> msg, String resourceId) {
    return Uni.createFrom().item(msg)
        .emitOn(Executors.newFixedThreadPool(10))
        .onItem().invoke(m -> log.errorf("Doing work for resourceId: %s, message: %s", resourceId, msg))
        .onItem().delayIt().by(Duration.ofSeconds(15))
        .onItem().invoke(m -> log.errorf("Acknowledge work for resourceId: %s", resourceId))
        .flatMap(m -> Uni.createFrom().completionStage(msg.ack()))
        .onItem().invoke(m -> log.warnf("Work done for resourceId: %s", resourceId))
        .onFailure().invoke(e -> log.errorf(e, "Error while doing work for resourceId: %s", resourceId));
  }
}
