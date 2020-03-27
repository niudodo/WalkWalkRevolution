package edu.ucsd.cse110.team22.walkwalkrevolution.fitness;

import android.util.Log;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Keeps track of a static map of blueprints.
 */
public class FitnessServiceFactory {
    private static final String TAG = "[FitnessServiceFactory]";

    private static final Map<String, BluePrint> blueprints = new HashMap<>();

    /**
     * Register a blueprint with the factory under a key.
     * @param key the key
     * @param bluePrint the blueprint
     */
    public static void put(final String key, final BluePrint bluePrint) {
        checkArgument(StringUtils.isNotBlank(key), "The key cannot be null or empty.");
        checkArgument(bluePrint != null, "bluePrint cannot be null.");

        blueprints.put(key, bluePrint);
    }

    /**
     * Create a FitnessService instance using the blueprint registered under the provided key
     * @param key the key
     * @param stepViewActivity the StepViewActivity
     * @return the FitnessService
     */
    public static FitnessService create(final String key, final StepViewActivity stepViewActivity) {
        checkArgument(StringUtils.isNotBlank(key), "The key cannot be null or empty.");
        checkArgument(stepViewActivity != null, "stepViewActivity cannot be null.");

        if(!blueprints.containsKey(key)) {
            throw new IllegalStateException("The key is not present in the Factory.");
        }

        Log.i(TAG, String.format("creating FitnessService with key %s", key));
        final BluePrint bluePrint = blueprints.get(key);
        return bluePrint.create(stepViewActivity);
    }

    /**
     * Returns the blueprint associated with the input key
     * @param key the input key
     * @return the blueprint (null if the record is not present)
     */
    public static BluePrint get(final String key) {
        return blueprints.get(key);
    }

    /**
     * Clears the map of blueprints
     */
    public static void clear() {
        blueprints.clear();
    }
    /**
     * This interface maps a StepViewActivity to a FitnessService via the method create.
     */
    public interface BluePrint {
        /**
         * Maps a StepViewActivity to a FitnessService
         * @param stepViewActivity the input stepViewActivity
         * @return the FitnessService
         */
        FitnessService create(StepViewActivity stepViewActivity);
    }
}
