package edu.ucsd.cse110.team22.walkwalkrevolution.fitness;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FitnessServiceFactoryTest {
    @BeforeEach
    private void clear() {
        FitnessServiceFactory.clear();
    }

    private static Stream<Arguments> testPutInvalidArguments() {
        final String key = "key";
        final FitnessServiceFactory.BluePrint mockBluePrint = mock(FitnessServiceFactory.BluePrint.class);
        return Stream.of(
                Arguments.of(null, mockBluePrint, "The key cannot be null or empty."),
                Arguments.of("", mockBluePrint, "The key cannot be null or empty."),
                Arguments.of(" ", mockBluePrint, "The key cannot be null or empty."),
                Arguments.of(key, null, "bluePrint cannot be null.")
        );
    }
    @ParameterizedTest
    @MethodSource
    public void testPutInvalidArguments(final String key, final FitnessServiceFactory.BluePrint bluePrint, final String errorMessage) {
        IllegalArgumentException ex =  assertThrows(IllegalArgumentException.class, () -> FitnessServiceFactory.put(key, bluePrint));
        assertEquals(errorMessage, ex.getMessage());
    }

    @Test
    public void testPut() {
        final String key = "key";
        final FitnessServiceFactory.BluePrint mockBluePrint = mock(FitnessServiceFactory.BluePrint.class);

        FitnessServiceFactory.put(key, mockBluePrint);
        assertEquals(mockBluePrint, FitnessServiceFactory.get(key));
        assertNull(FitnessServiceFactory.get(null));
    }

    private static Stream<Arguments> testCreateInvalidArguments() {
        final String key = "key";
        final StepViewActivity mockActivity = mock(StepViewActivity.class);
        return Stream.of(
                Arguments.of(null, mockActivity, "The key cannot be null or empty."),
                Arguments.of("", mockActivity, "The key cannot be null or empty."),
                Arguments.of(" ", mockActivity, "The key cannot be null or empty."),
                Arguments.of(key, null, "stepViewActivity cannot be null.")
        );
    }
    @ParameterizedTest
    @MethodSource
    public void testCreateInvalidArguments(final String key, final StepViewActivity stepViewActivity, final String errorMessage) {
        IllegalArgumentException ex =  assertThrows(IllegalArgumentException.class, () -> FitnessServiceFactory.create(key, stepViewActivity));
        assertEquals(errorMessage, ex.getMessage());
    }

    @Test
    public void testCreateKeyNotPresent() {
        final String key = "key";
        final StepViewActivity mockActivity = mock(StepViewActivity.class);
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> FitnessServiceFactory.create(key, mockActivity));
        assertEquals("The key is not present in the Factory.", ex.getMessage());
    }

    @Test
    public void testCreate() {
        final String key = "key";
        final StepViewActivity mockActivity = mock(StepViewActivity.class);
        final FitnessServiceFactory.BluePrint mockBluePrint = mock(FitnessServiceFactory.BluePrint.class);
        final FitnessService mockFitnessService = mock(FitnessService.class);
        when(mockBluePrint.create(mockActivity)).thenReturn(mockFitnessService);
        FitnessServiceFactory.put(key, mockBluePrint);
        assertEquals(mockFitnessService, FitnessServiceFactory.create(key, mockActivity));
    }
}
