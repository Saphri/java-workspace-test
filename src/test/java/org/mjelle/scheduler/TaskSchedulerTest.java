package org.mjelle.scheduler;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.metrics.LongCounter;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@QuarkusTest
class TaskSchedulerTest {

    @Inject
    TaskScheduler taskScheduler;

    @InjectMock
    Meter meter;

    @Test
    void testOnResourceEventIncrementsCounter() {
        // Given
        LongCounter mockCounter = mock(LongCounter.class);
        var counterBuilder = mock(io.opentelemetry.api.metrics.LongCounterBuilder.class);

        when(meter.counterBuilder(anyString())).thenReturn(counterBuilder);
        when(counterBuilder.setDescription(anyString())).thenReturn(counterBuilder);
        when(counterBuilder.build()).thenReturn(mockCounter);

        // Act
        taskScheduler.onResourceEvent("test payload");

        // Assert
        verify(mockCounter).add(1);
    }
}