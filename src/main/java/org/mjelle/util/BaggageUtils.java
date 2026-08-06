package org.mjelle.util;

import io.opentelemetry.api.baggage.Baggage;

public class BaggageUtils {
    
    private BaggageUtils() {
        // private constructor to prevent instantiation
    }
    
    public static String asString(final Baggage baggage) {
        if (baggage == null) {
            return "{}";
        }
        return baggage.asMap().toString();
    }
}
