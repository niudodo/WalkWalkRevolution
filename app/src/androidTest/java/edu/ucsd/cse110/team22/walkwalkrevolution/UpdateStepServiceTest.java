package edu.ucsd.cse110.team22.walkwalkrevolution;

import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ServiceTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeoutException;

import edu.ucsd.cse110.team22.walkwalkrevolution.fitness.FitnessService;
import edu.ucsd.cse110.team22.walkwalkrevolution.fitness.FitnessServiceFactory;
import edu.ucsd.cse110.team22.walkwalkrevolution.fitness.StepViewActivity;
import edu.ucsd.cse110.team22.walkwalkrevolution.fitness.UpdateStepService;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class UpdateStepServiceTest {
    public static final String TEST_SERVICE = "TEST_SERVICE";
    private Intent intent;

    @Rule
    public final ServiceTestRule serviceRule = new ServiceTestRule();
    @Test
    public void testWithBoundService() throws TimeoutException {
        TestFitnessService fitnessService = new TestFitnessService(null);
        fitnessService.setup(null);
        Intent serviceIntent =
                new Intent(ApplicationProvider.getApplicationContext(),
                        UpdateStepService.class);

        IBinder binder = serviceRule.bindService(serviceIntent);
        UpdateStepService service = ((UpdateStepService.LocalService)binder).getService();
        service.setStopUpdate(true);
        service.setFitnessService(fitnessService);
        fitnessService.updateStepCount(null);
        long expected = 20;
        long actual = fitnessService.stepCount;
        assertEquals(expected, actual);
    }
}

class MockStepViewActivity extends StepViewActivity {
    private long stepCount;
    FitnessService fitnessService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fitnessService = FitnessServiceFactory.create(UpdateStepServiceTest.TEST_SERVICE, this);
        fitnessService.setup(this);
        //empty
    }
    @Override
    public void setStepCount(long stepCount){
        this.stepCount=stepCount;
    }
}

/*Mock of FitnessService that will setup fitnessservice for testing purpose*/
class TestFitnessService implements FitnessService {
    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";
    private boolean isSetup=false;
    private static final String TAG = "[TestFitnessService]: ";
    public long stepCount=10;
    public TestFitnessService(StepViewActivity activity){

    }

    @Override
    public int getRequestCode() {
        return 0;
    }

    @Override
    public void setup(StepViewActivity activity) {
        isSetup=true;
        System.out.println(TAG + "setup");
    }

    @Override
    public void updateStepCount(StepViewActivity activity) {
        stepCount = 20;
        System.out.println(TAG + "updateStepCount:");
    }
    public boolean getIsSetUp(){
        return isSetup;
    }
}


