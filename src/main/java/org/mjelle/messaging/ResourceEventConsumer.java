package org.mjelle.messaging;

import java.util.concurrent.CompletionStage;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.jboss.logging.Logger;
import org.mjelle.service.TaskWorkerFactory;

import io.quarkiverse.reactive.messaging.nats.jetstream.client.api.SubscribeMessageMetadata;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ResourceEventConsumer {

  private final Logger log = Logger.getLogger(ResourceEventConsumer.class);

  private final TaskWorkerFactory worker;

  public ResourceEventConsumer(TaskWorkerFactory worker) {
    this.worker = worker;
  }

  @Incoming("resource-event-in")
  @RunOnVirtualThread
  public CompletionStage<Void> consume(Message<String> resourceEvent) {
    log.infof("Received ResourceEvent message: %s", resourceEvent);

    try {
      final var metadata = resourceEvent.getMetadata(SubscribeMessageMetadata.class).orElse(null);
      if (metadata != null) {
        worker.start(metadata.subject().replace("resource-event.", ""));
      }
      return resourceEvent.ack();
    } catch (Exception e) {
      log.errorf(e, "Exception processing ResourceEvent message: %s", resourceEvent);
      return resourceEvent.nack(e);
    }
  }
}
