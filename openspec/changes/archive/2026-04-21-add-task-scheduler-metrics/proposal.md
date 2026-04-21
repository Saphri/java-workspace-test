## Why

To monitor the throughput and activity of the `TaskScheduler` by tracking how many resource events are being processed. This provides visibility into the event processing rate and helps in identifying traffic spikes or potential bottlenecks.

## What Changes

- Introduce a metric counter to track the number of times `onResourceEvent` is invoked in the `TaskScheduler` class.
- Update the `onResourceEvent` method to increment this counter upon every call.

## Capabilities

### New Capabilities
- `task-scheduler-metrics`: Tracking the invocation count of the `onResourceEvent` method in `TaskScheduler`.

### Modified Capabilities
- None

## Impact

- `org.mjelle.scheduler.TaskScheduler`: Modified to include and update the metric counter.
- Application metrics endpoint: A new metric will be exposed for monitoring.
