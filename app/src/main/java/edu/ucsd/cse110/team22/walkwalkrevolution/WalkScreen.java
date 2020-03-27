package edu.ucsd.cse110.team22.walkwalkrevolution;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

import edu.ucsd.cse110.team22.walkwalkrevolution.MockClass.MockStepsWalkScreen;
import edu.ucsd.cse110.team22.walkwalkrevolution.Route.SaveScreen;
import edu.ucsd.cse110.team22.walkwalkrevolution.fitness.FitnessService;
import edu.ucsd.cse110.team22.walkwalkrevolution.fitness.FitnessServiceFactory;
import edu.ucsd.cse110.team22.walkwalkrevolution.fitness.UpdateStepService;
import edu.ucsd.cse110.team22.walkwalkrevolution.timer.TimerService;

public class WalkScreen extends StepViewTimerActivity {

    public static final String STEPS_BEFORE_START = "STEP_BEFORE_START";

    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";

    private static final String TAG = "WalkScreenActivity";

    private static final int RETURNCODE_MOCK = 20;



    public FitnessService fitnessService;

    private TextView stepCount;
    private TextView walkDistance;
    private TextView timeUsed;
    private Long stepsBeforeStart;
    private String time;
    private TimerService timerService;
    private boolean isTimerServiceBound;
    private UpdateStepService updateStepService;
    private boolean isUpdateStepServiceBound; // true if updateStepService is bounded
    private Button stop;
    private StepViewTimerActivity that;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.walk_screen);
        that = this;

        stepCount = findViewById(R.id.stepCount);
        walkDistance = findViewById(R.id.walkDistance);
        timeUsed = findViewById(R.id.timeUsed);

        try {
            String stepsBeforeStartExtra = getIntent().getStringExtra(STEPS_BEFORE_START);
            Log.d(TAG, stepsBeforeStartExtra);
            stepsBeforeStart = Long.parseLong(stepsBeforeStartExtra);
        } catch (final Exception ex) {
            Log.d("ERROR", "Cannot get the extra STEPS_BEFORE_START");
        }

        String fitnessServiceKey = getIntent().getStringExtra(FITNESS_SERVICE_KEY);
        fitnessService = FitnessServiceFactory.create(fitnessServiceKey, this);
        fitnessService.setup(this);

        stop = findViewById(R.id.stop);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchSaveScreen();
            }
        });
        Intent intentUpdateStepService = new Intent(this, UpdateStepService.class);
        bindService(intentUpdateStepService, updateStepServiceConnection, Context.BIND_AUTO_CREATE);
        Intent intentTimerService = new Intent(this, TimerService.class);
        bindService(intentTimerService, timerServiceConnection, Context.BIND_AUTO_CREATE);

        Button switchToMockScreen = findViewById(R.id.toMockScreenPage);
        switchToMockScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isTimerServiceBound = false;
                isUpdateStepServiceBound = false;
                unbindService(timerServiceConnection);
                unbindService(updateStepServiceConnection);
                launchMockScreen();
            }
        });
    }

    /**
     * This function will be passed to bindService, it will:
     * set in HomePageActivity: updateStepService
     * set in UpdateStepService: activity
     */
    private ServiceConnection updateStepServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            UpdateStepService.LocalService localService = (UpdateStepService.LocalService)service;
            updateStepService = localService.getService();
            updateStepService.setActivity(that);
            updateStepService.setFitnessService(fitnessService);
            isTimerServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isTimerServiceBound = false;
        }
    };

    /**
     * This function will be passed to bindService, it will:
     * set in HomePageActivity: updateStepService
     * set in UpdateStepService: activity
     */
    private ServiceConnection timerServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            TimerService.LocalService localService = (TimerService.LocalService) service;
            timerService = localService.getService();
            timerService.setActivity(that);
            isUpdateStepServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isUpdateStepServiceBound = false;
        }
    };

    /**
     * onActivityResult will be called after the google adapter is setup
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//       If authentication was required during google fit setup, this will be called after the user authenticates
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == fitnessService.getRequestCode()) {
                fitnessService.updateStepCount(that);
                updateStepService.setFitnessService(fitnessService);
            }
            if (requestCode == RETURNCODE_MOCK){
                long mocked_steps = data.getLongExtra("steps",0);
                setStepCount(mocked_steps);

                //In the current implementation the time does change to what we set in MockStepsWalkScreen
                //but it changes right back to the time of the tim service

                final String mocked_time = data.getStringExtra("time");
                System.out.println(mocked_time); //Log to see that time is passed to this activity
                setTime(mocked_time);

            }
        } else {
            Log.e(TAG, "ERROR, google fit result code: " + resultCode);
        }
    }

    @Override
    public void setTime(final String time) {
        this.time = time;
        this.timeUsed.setText(time);
    }

    @Override
    public void setStepCount(long totalSteps) {
        steps = totalSteps - stepsBeforeStart;
        this.stepCount.setText(String.format(Locale.US,"%d", steps));
        this.distance = distCal.calculateDistance(steps);
        this.walkDistance.setText(this.distance);
    }

    public void launchSaveScreen(){
        Intent intent = new Intent( this, SaveScreen.class);
        intent.putExtra(SaveScreen.TIME, time);
        intent.putExtra(SaveScreen.STEPS, String.format(Locale.US, "%d", steps));
        intent.putExtra(SaveScreen.DISTANCE, distance);
        startActivity(intent);
        finish();
    }

    public void launchMockScreen(){
        Intent intent = new Intent( this, MockStepsWalkScreen.class);
        startActivityForResult(intent, RETURNCODE_MOCK);
    }
}
