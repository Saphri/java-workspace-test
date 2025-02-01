package org.mjelle.service;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import io.nats.client.api.AckPolicy;
import io.nats.client.api.DeliverPolicy;
import io.nats.client.api.ReplayPolicy;
import io.quarkiverse.reactive.messaging.nats.jetstream.client.configuration.ConsumerConfiguration;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TaskConsumerConfiguration<T> implements ConsumerConfiguration<T> {

  private final String name;
  private final String stream;
  private final String subject;
  private final Duration ackWait;

  @Override
  public String name() {
    return name;
  }

  @Override
  public String stream() {
    return stream;
  }

  @Override
  public Optional<String> durable() {
    return Optional.of(name);
  }

  @Override
  public String subject() {
    return subject;
  }

  @Override
  public Optional<Duration> ackWait() {
    return Optional.of(ackWait);
  }

  @Override
  public Optional<DeliverPolicy> deliverPolicy() {
    return Optional.of(DeliverPolicy.All);
  }

  @Override
  public Optional<Long> startSequence() {
    return Optional.empty();
  }

  @Override
  public Optional<ZonedDateTime> startTime() {
    return Optional.empty();
  }

  @Override
  public Optional<String> description() {
    return Optional.empty();
  }

  @Override
  public Optional<Duration> inactiveThreshold() {
    return Optional.empty();
  }

  @Override
  public Optional<Long> maxAckPending() {
    return Optional.of(1L);
  }

  @Override
  public Optional<Long> maxDeliver() {
    return Optional.of(5L);
  }

  @Override
  public Optional<ReplayPolicy> replayPolicy() {
    return Optional.empty();
  }

  @Override
  public Optional<Integer> replicas() {
    return Optional.empty();
  }

  @Override
  public Optional<Boolean> memoryStorage() {
    return Optional.empty();
  }

  @Override
  public Optional<String> sampleFrequency() {
    return Optional.empty();
  }

  @Override
  public Map<String, String> metadata() {
    return Map.of();
  }

  @Override
  public List<Duration> backoff() {
    return List.of();
  }

  @Override
  public Optional<AckPolicy> ackPolicy() {
    return Optional.of(AckPolicy.Explicit);
  }

  @Override
  public Optional<ZonedDateTime> pauseUntil() {
    return Optional.empty();
  }

  @Override
  public Optional<Class<T>> payloadType() {
    return Optional.empty();
  }
  
}
