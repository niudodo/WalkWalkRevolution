package edu.ucsd.cse110.team22.walkwalkrevolution.fitness;

import android.app.Activity;
import android.os.Bundle;

import edu.ucsd.cse110.team22.walkwalkrevolution.Storage.StorageHandler;

/**
 * This abstract class models a view that uses the FitnessService to gather the steps
 */
public abstract class StepViewActivity extends Activity {
    protected long steps;
    protected String distance;
    protected DistanceCalculator distCal;
    public FitnessService fitnessService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StorageHandler storageHandler = StorageHandler.getStorage(this);
        Double height = storageHandler.retrieveItem("height", Double.class);
        if(height == null) {
            height = 1d;
        }
        distCal = new DistanceCalculator(height);
    }
    /**
     * This method will be called by the FitnessService with the total steps count.
     * @param totalSteps the total daily steps provided by the FitnessService
     */
    public abstract void setStepCount(final long totalSteps);
}
