## Context

The `TaskScheduler` class is responsible for processing resource events from NATS JetStream. Currently, there is no visibility into the number of events processed by the `onResourceEvent` method. To improve observability, we need to introduce a metric counter to track these invocations.

## Goals / Non-Goals

**Goals:**
- Implement a counter metric for `onResourceEvent` invocations.
- Ensure the metric is exposed via a standard Quarkus metrics endpoint.
- Maintain minimal overhead on the event processing path.

**Non-Goals:**
- Implement complex alerting or dashboards (out of scope for this change).
- Implement detailed event-type specific metrics (only a total count is currently requested).

## Decisions

- **Metric Library**: Use OpenTelemetry, as it is the standard for observability in the LGTM stack.
- **Implementation**: Inject the `Meter` from OpenTelemetry into `TaskScheduler` and use it to create a `LongCounter`.
- **Dependency**: The `quarkus-opentelemetry` extension is already present in `pom.xml`.
- **Configuration**: Enable OpenTelemetry in `application.properties` by setting `quarkus.otel.enabled=true`.

## Risks / Trade-offs

- **Overhead**: Adding a metric increment operation is generally low overhead, but since `onResourceEvent` is called frequently, we should ensure the use of a standard, high-performance counter.
- **Configuration**: Enabling OpenTelemetry requires a configuration change in `application.properties`.
