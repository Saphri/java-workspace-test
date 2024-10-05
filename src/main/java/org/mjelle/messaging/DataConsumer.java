package org.mjelle.messaging;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DataConsumer {

  private static final Logger LOG = Logger.getLogger(DataConsumer.class);

  @Incoming("data")
  public void consume(String data) {
    LOG.info(data);
  }
}
