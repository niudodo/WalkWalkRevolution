package edu.ucsd.cse110.team22.walkwalkrevolution.fitness;

import java.text.DecimalFormat;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Implement the distance calculator algorithm.
 */
public class DistanceCalculator {
    /**
     * Constant for calculating the stride. See https://www.openfit.com/how-many-steps-walk-per-mile.
     */
    public static final double HEIGHT_MULTIPLIER = 0.413;
    /**
     * Constant for calculating the stride. See https://www.openfit.com/how-many-steps-walk-per-mile.
     */
    public static final int INCHES_PER_FEET = 12;

    public static final int TO_MILES = 5280;
    /**
     * Double formatter in order to limit the maximum decimals to 3.
     */
    public static final DecimalFormat doubleFormatter = new DecimalFormat("#.###");

    private double height = Double.MIN_VALUE;

    public DistanceCalculator(final double height) {
        this.setHeight(height);
    }

    /**
     * Set the height.
     * @param height the new height
     */
    public void setHeight(final double height) {
        checkArgument(height > 0, "height cannot be a negative number.");

        this.height = height;
    }

    /**
     * Calculates the distance given the steps
     * @param steps the number of steps
     * @return the distance
     */
    public String calculateDistance(final long steps) {
        checkArgument(steps >= 0, "steps cannot be a negative number.");


        final double stride = height * HEIGHT_MULTIPLIER / INCHES_PER_FEET;
        System.out.println("Distance = " + steps * stride);
        return doubleFormatter.format(steps * stride / TO_MILES);
    }
}
