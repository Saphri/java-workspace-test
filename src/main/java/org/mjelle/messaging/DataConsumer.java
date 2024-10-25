package org.mjelle.messaging;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.jbosslog.JBossLog;

@ApplicationScoped
@JBossLog
public class DataConsumer {

  @Incoming("data")
  public void consume(String data) {
    log.info(data);
  }
}
