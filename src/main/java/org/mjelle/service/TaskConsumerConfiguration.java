package org.mjelle.service;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import io.nats.client.api.DeliverPolicy;
import io.nats.client.api.ReplayPolicy;
import io.quarkiverse.reactive.messaging.nats.jetstream.client.configuration.ConsumerConfiguration;

public class TaskConsumerConfiguration implements ConsumerConfiguration {

  private final String subject;
  private final Duration ackWait;

  public TaskConsumerConfiguration(String subject, Duration ackWait) {
    this.subject = subject;
    this.ackWait = ackWait;
  }

  @Override
  public Optional<Duration> ackWait() {
    return Optional.of(ackWait);
  }

  @Override
  public DeliverPolicy deliverPolicy() {
    return DeliverPolicy.All;
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
  public ReplayPolicy replayPolicy() {
    return ReplayPolicy.Original;
  }

  @Override
  public Integer replicas() {
    return 1;
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
  public Optional<ZonedDateTime> pauseUntil() {
    return Optional.empty();
  }

  @Override
  public Optional<Class<?>> payloadType() {
    return Optional.empty();
  }

  @Override
  public List<String> filterSubjects() {
    return List.of(subject);
  }

  @Override
  public Optional<Duration> acknowledgeTimeout() {
    return Optional.empty();
  }

  @Override
  public Boolean durable() {
    return true;
  }

  @Override
  public Optional<List<Duration>> backoff() {
    return Optional.empty();
  }  
}
