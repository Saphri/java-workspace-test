## ADDED Requirements

### Requirement: Track onResourceEvent invocations
The system SHALL track the total number of times the `onResourceEvent` method in the `TaskScheduler` class is invoked.

#### Scenario: Event processed
- **WHEN** the `onResourceEvent` method is called
- **THEN** the invocation counter is incremented by 1