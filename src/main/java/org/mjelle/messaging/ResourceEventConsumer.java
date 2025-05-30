package org.mjelle.messaging;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.mjelle.service.TaskWorkerFactory;

import io.quarkiverse.reactive.messaging.nats.jetstream.client.api.SubscribeMessageMetadata;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.jbosslog.JBossLog;

@ApplicationScoped
@RequiredArgsConstructor
@JBossLog
public class ResourceEventConsumer {

  private final TaskWorkerFactory worker;

  @Incoming("resource-event-in")
  public Uni<Void> consume(Message<String> resourceEvent) {
    log.infof("Received ResourceEvent message: %s", resourceEvent);
    return 
      Uni.createFrom().item(resourceEvent.getMetadata(SubscribeMessageMetadata.class).orElse(null))
      .onItem().ifNotNull().transformToUni(metadata -> worker.start(metadata.subject().replace("resource-event.", "")))
      .invoke(() -> log.infof("Acknowledge ResourceEvent message: %s", resourceEvent))
      .eventually(() -> Uni.createFrom().completionStage(resourceEvent.ack()));
  }
}
