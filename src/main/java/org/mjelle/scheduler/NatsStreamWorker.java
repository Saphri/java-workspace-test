package org.mjelle.scheduler;

import java.io.IOException;

import org.jboss.logging.Logger;

import io.nats.client.Connection;
import io.nats.client.JetStreamApiException;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class NatsStreamWorker {
  
  private final Logger log = Logger.getLogger(NatsStreamWorker.class);

  private final Connection connection;

  public NatsStreamWorker(final Connection connection) {
    log.infof("NatsStreamWorker created: %s", this);
    this.connection = connection;
  }

  public void scheduledTask() {
    log.info("scheduledTask running");
    try {
      final var jsm = connection.jetStreamManagement();
      jsm.getStreamNames().forEach(this::logStreamInfo);
    } catch (final IOException | JetStreamApiException e) {
      log.error("Error fetching stream info", e);
    }
  }

  private void logStreamInfo(final String streamName) {
    try {
      final var jsm = connection.jetStreamManagement();
      log.infof("Stream info for: %s", jsm.getStreamInfo(streamName));
    } catch (final IOException | JetStreamApiException e) {
      log.error("Error fetching stream info", e);
    }
  }
}
