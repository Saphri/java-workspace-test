## Context

The application uses the `quarkus-opentelemetry` extension, which configures an `OpenTelemetrySdk`. While baggage is propagated across boundaries using the `W3CTraceContextPropagator`, it is not automatically added as attributes to the spans. This results in a lack of visibility into baggage-carried metadata in the tracing backend unless manually added at every span creation point.

## Goals / Non-Goals

**Goals:**
- Automatically propagate all current OpenTelemetry baggage entries to the attributes of every started span.
- Ensure the implementation is transparent to the rest of the application.
- Leverage Quarkus CDI for seamless SDK integration.

**Non-Goals:**
- Implementing custom propagation formats for baggage.
- Filtering baggage entries based on complex logic (this is handled at the baggage creation site).
- Modifying existing baggage values.

## Decisions

### Use `SpanProcessor` implementation
We will implement the `io.opentelemetry.sdk.trace.SpanProcessor` interface. This is the SDK's designated extension point for synchronous hooks during a span's lifecycle.
- **Alternative**: Using a custom `Tracer` wrapper.
- **Rationale**: `SpanProcessor` is more idiomatic, requires less boilerplate, and is natively supported by the SDK's `TracerProvider`.

### Register as `@ApplicationScoped` CDI Bean
In Quarkus, the OpenTelemetry SDK is managed. By providing a `SpanProcessor` as a CDI bean, the `quarkus-opentelemetry` extension will automatically discover it and add it to the `SdkTracerProvider`.
- **Alternative**: Manual SDK configuration via `OpenTelemetrySdk.builder()`.
- **Rationale**: Maintains the "Quarkus way" and avoids overriding the auto-configuration provided by the extension.

## Risks / Trade-offs

- **[PII Leakage]** → **Mitigation**: Since all baggage is copied, any sensitive data in baggage will end up in the trace backend. We must ensure that baggage is only used for non-sensitive metadata (e.g., `tenant_id`, `session_id`) and not PII.
- **[Performance]** → **Mitigation**: Adding attributes to a span is a low-cost operation. The overhead is negligible compared to the value of the metadata.
