package org.mjelle.observability;

import jakarta.enterprise.context.ApplicationScoped;
import io.opentelemetry.api.baggage.Baggage;
import io.opentelemetry.context.Context;
import io.opentelemetry.sdk.trace.ReadWriteSpan;
import io.opentelemetry.sdk.trace.SpanProcessor;

@ApplicationScoped
public class BaggageSpanProcessor implements SpanProcessor {

    @Override
    public boolean isStartRequired() {
        return true;
    }

    @Override
    public void onStart(Context parentContext, ReadWriteSpan span) {
        final var baggage = Baggage.fromContext(parentContext);
        baggage.forEach((key, entry) -> span.setAttribute(key, entry.getValue()));
    }

    @Override
    public boolean isEndRequired() {
        return false;
    }

    @Override
    public void onEnd(io.opentelemetry.sdk.trace.ReadableSpan span) {
    }
}
