package org.mjelle.messaging;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.jbosslog.JBossLog;

@ApplicationScoped
@JBossLog
public class DataConsumer {

  @Incoming("myincoming")
  public Uni<Void> consume(Message<String> data) {
    return Uni.createFrom().item(data)
      .onItem().invoke(d -> log.info("Received message: " + d))
      .flatMap(msg -> Uni.createFrom().completionStage(msg.ack()));
  }
}
