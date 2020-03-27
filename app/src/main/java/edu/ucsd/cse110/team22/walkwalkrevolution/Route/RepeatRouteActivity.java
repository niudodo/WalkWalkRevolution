package edu.ucsd.cse110.team22.walkwalkrevolution.Route;

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

import edu.ucsd.cse110.team22.walkwalkrevolution.R;
import edu.ucsd.cse110.team22.walkwalkrevolution.StepViewTimerActivity;
import edu.ucsd.cse110.team22.walkwalkrevolution.Storage.StorageHandler;
import edu.ucsd.cse110.team22.walkwalkrevolution.timer.TimerService;
import edu.ucsd.cse110.team22.walkwalkrevolution.fitness.FitnessServiceFactory;
import edu.ucsd.cse110.team22.walkwalkrevolution.fitness.UpdateStepService;

public class RepeatRouteActivity extends StepViewTimerActivity {
    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";
    public static final String TAG = "[Repeat Route Activity]";
    private TextView routeName;
    private TextView timeContext;
    private Button stopBtn;
    private TextView stepContext;
    private TextView distContext;
    private String time;
    private StepViewTimerActivity that;
    private boolean fitSetUp=false;

    private Route route;
    private long stepOffset=-1; //store the original stepcount for first update and use as an offset

    private TimerService timerService;
    private boolean isTimerServiceBound;
    private UpdateStepService updateStepService;
    private boolean isUpdateStepServiceBound; // true if updateStepService is bounded


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //the layout of repeatRouteActivity is the same as walkscreen
        setContentView(R.layout.walk_screen);

        /*set view component in the xml*/
        routeName = findViewById(R.id.route_name);
        stopBtn = findViewById(R.id.stop);
        stepContext = findViewById(R.id.stepCount);
        distContext = findViewById(R.id.walkDistance);
        timeContext = findViewById(R.id.timeUsed);

        StorageHandler sh = StorageHandler.getStorage(this);
        String id = sh.retrieveItem(StorageHandler.idOfRouteToBeRepeated,String.class);
        route = sh.retrieveItem(id,Route.class);
        that = this;

        routeName.setText(route.name);

        /* setup fitnessService*/
        String fitnessServiceKey = getIntent().getStringExtra(FITNESS_SERVICE_KEY);
        fitnessService = FitnessServiceFactory.create(fitnessServiceKey, that);
        fitnessService.setup(that);
        fitSetUp=fitnessService.getIsSetUp();

        Intent intentUpdateStepService = new Intent(this, UpdateStepService.class);
        bindService(intentUpdateStepService, updateStepServiceConnection, Context.BIND_AUTO_CREATE);
        Intent intentTimerService = new Intent(this, TimerService.class);
        bindService(intentTimerService, timerServiceConnection, Context.BIND_AUTO_CREATE);

        //in case reopen app and already setup, then update immediately
        if(fitSetUp) {
            fitnessService.updateStepCount(that);
        }

        /*add clicklistener to stop btn*/
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //save and update info
                updateInfo();
                //switch to route page
                switchToRoutePage();
            }
        });
    }

    private void updateInfo(){
        TextView time = findViewById(R.id.timeUsed);
        route.dist=(String)distContext.getText();
        route.stepCount=(String)stepContext.getText();
        route.time=(String)timeContext.getText();
        StorageHandler sh = StorageHandler.getStorage(this);
        sh.saveItem(route.id, route);

        /* save the last walk's id in the data base*/
        String lastWalk = "idOfLastWalk";
        sh.saveItem(lastWalk, route.id);
    }

    /**
     * switchToRoutePage is call to finish the repeat route page and return back to route page
     */
    private void switchToRoutePage(){
       finish();
    }

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
                fitSetUp=true;
            }
        } else {
            Log.e(TAG, "ERROR, google fit result code: " + resultCode);
        }
    }


    /**
     * setStepCount implement the way to update the step count
     * @param stepCount
     */
    @Override
    public void setStepCount(long stepCount){
        this.distance = distCal.calculateDistance(stepCount);
        this.displayStepsAndDistance(stepCount);
    }

    public void displayStepsAndDistance(final long steps) {
        stepContext.setText(String.format(Locale.US, "%d", steps));
        distContext.setText(distance);
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

    @Override
    public void setTime(final String time) {
        this.time = time;
        this.timeContext.setText(time);
    }

}
