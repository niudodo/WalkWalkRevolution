package edu.ucsd.cse110.team22.walkwalkrevolution.fitness;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DistanceCalculatorTest {

    private static final double HEIGHT = 10;

    private final DistanceCalculator testInstance = new DistanceCalculator(HEIGHT);

    @Test
    public void testConstructorInvalidParameter() {
        final double invalidHeight = -10;
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new DistanceCalculator(invalidHeight));
        assertEquals("height cannot be a negative number.", ex.getMessage());
    }

    @Test
    public void testHeightSetterInvalidParameter() {
        final double invalidHeight = -10;
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> testInstance.setHeight(invalidHeight));
        assertEquals("height cannot be a negative number.", ex.getMessage());
    }

    @Test
    public void testCalculateDistanceInvalidSteps() {
        final long invalidSteps = -10;
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> testInstance.calculateDistance(invalidSteps));
        assertEquals("steps cannot be a negative number.", ex.getMessage());
    }

    private static Stream<Arguments> testCalculateDistance() {
        return Stream.of(
                Arguments.of(0, "0"),
                Arguments.of(10000, "0.652"),
                Arguments.of(100000, "6.518"),
                Arguments.of(3000, "0.196")
        );

    }

    @ParameterizedTest
    @MethodSource
    public void testCalculateDistance(final long steps, final String expectedDistance) {
        assertEquals(expectedDistance, testInstance.calculateDistance(steps));
    }
}
