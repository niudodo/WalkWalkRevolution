/**
 * This File test the start walk button on the homepage that if application switches to walk screen
 * after click on start walk button
 */
package edu.ucsd.cse110.team22.walkwalkrevolution;


import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.Intents;
import androidx.test.filters.LargeTest;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.ucsd.cse110.team22.walkwalkrevolution.Storage.StorageHandler;
import edu.ucsd.cse110.team22.walkwalkrevolution.fitness.FitnessService;
import edu.ucsd.cse110.team22.walkwalkrevolution.fitness.FitnessServiceFactory;
import edu.ucsd.cse110.team22.walkwalkrevolution.fitness.StepViewActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class StartWalkButtonUITest {
    private static final String TEST_SERVICE="TEST_SERVICE";
    private Intent intent;

    @Before
    public void setup() {
        FitnessServiceFactory.put(TEST_SERVICE, TestFitnessService::new);
        intent = new Intent(ApplicationProvider.getApplicationContext(), HomePageActivity.class);
        intent.putExtra(HomePageActivity.FITNESS_SERVICE_KEY, TEST_SERVICE);
        System.out.println("SETUP:"+HomePageActivity.FITNESS_SERVICE_KEY);
        Intents.init();
    }
    @Test
    public void startWalkButtonUITest() {
        ActivityScenario<HomePageActivity> scenario = ActivityScenario.launch(intent);
        onView(withId(R.id.strWalkBtn)).perform(click());
        intended(hasComponent(WalkScreen.class.getName()));
    }
    @After
    public void finish(){
        Intents.release();
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    /*Mock of FitnessService that will setup fitnessservice for testing purpose*/
    private class TestFitnessService implements FitnessService {
        private boolean isSetup=false;
        private static final String TAG = "[TestFitnessService]: ";
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
            StorageHandler storageHandler = StorageHandler.getStorage(activity);
            storageHandler.saveItem("height", 72);
        }

        @Override
        public void updateStepCount(StepViewActivity activity) {
            System.out.println(TAG + "updateStepCount");
            activity.setStepCount(1);
            ((HomePageActivity)activity).setStopUpdate(true);
        }
        public boolean getIsSetUp(){
            return isSetup;

        }
    }

}
