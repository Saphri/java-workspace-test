## Why

Currently, OpenTelemetry baggage is accessible via the context but is not automatically exported as attributes on the spans. This requires manual extraction and attribution in every resource or service, which is repetitive and error-prone.

## What Changes

- Implement a custom `SpanProcessor` (`BaggageSpanProcessor`) that intercepts span start events.
- Automatically extract all entries from the current `Baggage` context and add them as attributes to the `ReadWriteSpan`.
- Register the `SpanProcessor` as a CDI bean for automatic integration with the Quarkus OpenTelemetry SDK.

## Capabilities

### New Capabilities
- `baggage-span-attributes`: Automatically copy OpenTelemetry baggage entries to the attributes of the starting span.

### Modified Capabilities

## Impact

- **Code**: Introduction of a new `BaggageSpanProcessor` class.
- **Observability**: Spans exported to the tracing backend will now contain all baggage entries as attributes.
- **Performance**: Minimal overhead added to span creation.
