## 1. Implementation

- [x] 1.1 Create `org.mjelle.observability.BaggageSpanProcessor` class implementing `SpanProcessor`.
- [x] 1.2 Implement `isStartRequired()` returning `true` and `isEndRequired()` returning `false`.
- [x] 1.3 Implement `onStart()` to extract `Baggage` from `parentContext` and set attributes on the `span`.
- [x] 1.4 Annotate `BaggageSpanProcessor` with `@ApplicationScoped`.

## 2. Verification

- [x] 2.1 Create an integration test that:
    - Sets a value in the current OpenTelemetry Baggage.
    - Triggers a request to a REST endpoint.
    - Verifies (via a mock or SDK check) that the resulting span contains the baggage value as an attribute.
