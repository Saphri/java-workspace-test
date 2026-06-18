## ADDED Requirements

### Requirement: Automatic Baggage to Span Attribute Propagation
The system SHALL automatically copy all entries from the current OpenTelemetry Baggage context into the attributes of any span that is started.

#### Scenario: Successful propagation of baggage
- **WHEN** a span is started and the current context contains baggage `{user_id="123", session_id="abc"}`
- **THEN** the resulting span attributes MUST contain `user_id="123"` and `session_id="abc"`

#### Scenario: No baggage present
- **WHEN** a span is started and the current context contains no baggage
- **THEN** no baggage-related attributes SHALL be added to the span attributes
