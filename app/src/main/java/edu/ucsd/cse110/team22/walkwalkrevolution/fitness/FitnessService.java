package edu.ucsd.cse110.team22.walkwalkrevolution.fitness;

/**
 * Interface used to model the Fitness Service.
 */
public interface FitnessService {

    /**
     * @return the request code.
     */
    int getRequestCode();
    /**
     * Setup the fitness service.
     * @param activity the activity that will display the signin form.
     */
    void setup(final StepViewActivity activity);
    /**
     * Updates the step count.
     * @param activity the activity that will update the steps count.
     */
    void updateStepCount(final StepViewActivity activity);

    /**
     * return boolean value regard if the service is setuped
     * @return isSetup
     */
    boolean getIsSetUp();

}
