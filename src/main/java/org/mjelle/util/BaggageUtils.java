package org.mjelle.util;

import io.opentelemetry.api.baggage.Baggage;
import java.util.stream.Collectors;

public class BaggageUtils {
    private BaggageUtils() {
        // private constructor to prevent instantiation
    }
    
    public static String asString(final Baggage baggage) {
        if (baggage == null) return "{}";
        return baggage.asMap().entrySet().stream()
            .map(
                entry ->
                    String.format(
                        "%s=%s(%s)",
                        entry.getKey(),
                        entry.getValue().getValue(),
                        entry.getValue().getMetadata().getValue()))
            .collect(Collectors.joining(", ", "{", "}"));
    }
}
