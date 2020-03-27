package edu.ucsd.cse110.team22.walkwalkrevolution.homepage;

import android.content.Intent;
import android.os.IBinder;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import edu.ucsd.cse110.team22.walkwalkrevolution.MockedClass.MockHomeActivity;
import edu.ucsd.cse110.team22.walkwalkrevolution.R;
import edu.ucsd.cse110.team22.walkwalkrevolution.fitness.StepViewActivity;
import edu.ucsd.cse110.team22.walkwalkrevolution.Storage.StorageHandler;
import edu.ucsd.cse110.team22.walkwalkrevolution.fitness.FitnessService;
import edu.ucsd.cse110.team22.walkwalkrevolution.fitness.FitnessServiceFactory;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;

@RunWith(AndroidJUnit4.class)
@Config(sdk=28)
public class HomePageUnitTest {
    private static final String TEST_SERVICE = "TEST_SERVICE";

    private Intent intent;
    private long nextStepCount;

    @Before
    public void setUp() {
        FitnessServiceFactory.put(TEST_SERVICE, TestFitnessService::new);
        intent = new Intent(ApplicationProvider.getApplicationContext(), MockHomeActivity.class);
        intent.putExtra(MockHomeActivity.FITNESS_SERVICE_KEY, TEST_SERVICE);
    }

    @Test
    public void testUpdateStepsButton() {
        nextStepCount = 1337;
        ActivityScenario<StepViewActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            IBinder mBinder = mock(IBinder.class);
            TextView textSteps = activity.findViewById(R.id.stepContext);
            assertThat(textSteps.getText().toString()).isEqualTo(String.valueOf(nextStepCount));
        });
    }


    /**
     * TestFitnessService Class mock actual fitness service class
     */
    private class TestFitnessService implements FitnessService {
        private static final String TAG = "[TestFitnessService]: ";
        private StepViewActivity stepCountActivity;

        public TestFitnessService(StepViewActivity activity) {
            StorageHandler storageHandler = StorageHandler.getStorage(activity);
            double height = 72;
            storageHandler.saveItem("height",height );
        }

        @Override
        public int getRequestCode() {
            return 0;
        }

        @Override
        public void setup(StepViewActivity activity) {
            System.out.println(TAG + "setup");
        }

        @Override
        public void updateStepCount(StepViewActivity activity) {
            System.out.println(TAG + "updateStepCount");
            activity.setStepCount(nextStepCount);
        }
        @Override
        public boolean getIsSetUp(){
            return true;
        }
    }
}