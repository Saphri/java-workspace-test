## 1. Dependencies and Configuration

- [x] 1.1 Enable OpenTelemetry in `application.properties` by setting `quarkus.otel.enabled=true`.
- [x] 1.2 Verify that the metrics endpoint is available in the application.

## 2. Implementation

- [x] 2.1 Inject `Meter` into `TaskScheduler`.
- [x] 2.2 Create a `LongCounter` for `onResourceEvent` invocations.
- [x] 2.3 Increment the counter in the `onResourceEvent` method.

## 3. Verification

- [x] 3.1 Create a test case to verify that the counter increments when `onResourceEvent` is called.
- [x] 3.2 Manually verify the metric via the `lgtm` endpoint.
