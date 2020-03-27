package edu.ucsd.cse110.team22.walkwalkrevolution;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Locale;

import edu.ucsd.cse110.team22.walkwalkrevolution.Message.MessageActivity;
import edu.ucsd.cse110.team22.walkwalkrevolution.MockClass.MockStepsHomePage;
import edu.ucsd.cse110.team22.walkwalkrevolution.Route.Route;
import edu.ucsd.cse110.team22.walkwalkrevolution.Route.RouteActivity;
import edu.ucsd.cse110.team22.walkwalkrevolution.Storage.FirebaseStoreAdapter;
import edu.ucsd.cse110.team22.walkwalkrevolution.Storage.StorageHandler;
import edu.ucsd.cse110.team22.walkwalkrevolution.Storage.StorageStore;
import edu.ucsd.cse110.team22.walkwalkrevolution.UsersClass.User;
import edu.ucsd.cse110.team22.walkwalkrevolution.fitness.DistanceCalculator;
import edu.ucsd.cse110.team22.walkwalkrevolution.fitness.FitnessServiceFactory;
import edu.ucsd.cse110.team22.walkwalkrevolution.fitness.StepViewActivity;
import edu.ucsd.cse110.team22.walkwalkrevolution.fitness.UpdateStepService;

import static edu.ucsd.cse110.team22.walkwalkrevolution.UsersClass.User.myUser;

public class HomePageActivity extends StepViewActivity {
    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";

    public static final String COLLECTIONS_KEY = "users";
    public static final String MSG_TEAM_KEY = "team";
    public static final String MSG_ROUTE_KEY = "routes";

    private static final String TAG = "HomePageActivity";

    private static final int RETURNCODE_MOCK = 10;

    private HomePageActivity that;
    private boolean fitSetUp = false;
    private TextView stepContext;
    private TextView distContext;

    private UpdateStepService updateStepService;
    private boolean isBound; // true if updateStepService is bounded
    protected IBinder iBinder;
    private boolean stopUpdate=false;

    //Use to recieve message
    private long steps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        stepContext = findViewById(R.id.stepContext);
        distContext = findViewById(R.id.distContext);
        that = this;

        if(getIntent().hasExtra("steps")){
            steps = getIntent().getLongExtra("steps", 0);
        }

        /* setup fitnessService*/
        String fitnessServiceKey = getIntent().getStringExtra(FITNESS_SERVICE_KEY);
        fitnessService = FitnessServiceFactory.create(fitnessServiceKey, that);
        fitnessService.setup(that);
        fitSetUp=fitnessService.getIsSetUp();

        /*Setup UpdateStepService*/
        Intent intent = new Intent(this, UpdateStepService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        /* Get height from the storage*/
        StorageHandler storageHandler = StorageHandler.getStorage(that);
        double height = storageHandler.retrieveItem("height", Double.class);
        distCal = new DistanceCalculator(height);

        /* save the last walk's id in the data base*/
        SharedPreferences sharedPreferences = getSharedPreferences("database", MODE_PRIVATE);
        String lastWalk = "idOfLastWalk";
        boolean hasLastWalk = sharedPreferences.contains(lastWalk);
        if(hasLastWalk) {
            String idOfLastWalk = storageHandler.retrieveItem(lastWalk, String.class);
            Route route = storageHandler.retrieveItem(idOfLastWalk, Route.class);
            displayWalk(route);
        }

        System.out.println("Get height from storage:"+height);

        //in case reopen app and already setup, then update immediately
        if(fitSetUp) {
            fitnessService.updateStepCount(that);
        }

        setBtnOnClickListener();

        initialMyUser();
        initializeInvitationStore();
        initializedRouteStore();
    }

    @Override
    protected void onResume(){
        /* save the last walk's id in the data base*/
        SharedPreferences sharedPreferences = getSharedPreferences("database", MODE_PRIVATE);
        String lastWalk = "idOfLastWalk";
        boolean hasLastWalk = sharedPreferences.contains(lastWalk);
        StorageHandler storageHandler = StorageHandler.getStorage(this);
        if(hasLastWalk) {
            String idOfLastWalk = storageHandler.retrieveItem(lastWalk, String.class);
            Route route = storageHandler.retrieveItem(idOfLastWalk, Route.class);
            displayWalk(route);
        }
        super.onResume();
    }

    /**
     * This function will be passed to bindService, it will:
     * set in HomePageActivity: updateStepService
     * set in UpdateStepService: activity
     */
    protected ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if(service!=null){
                iBinder = service;
                UpdateStepService.LocalService localService = (UpdateStepService.LocalService)iBinder;
                updateStepService = localService.getService();
                updateStepService.setActivity(that);
                updateStepService.setFitnessService(fitnessService);
                updateStepService.setStopUpdate(stopUpdate);
                isBound = true;
            }else{
                System.out.println("ServiceConnection: IBinder is null" );
                return;
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

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
                fitSetUp=true;
            }
            if(requestCode == RETURNCODE_MOCK){
                long mocked_steps = data.getLongExtra("steps",0);
                this.steps = this.steps + mocked_steps;
                setStepCount(this.steps);

            }
        } else {
            Log.e(TAG, "ERROR, google fit result code: " + resultCode);
        }
    }

    /* setter of stepCount*/
    @Override
    public void setStepCount(final long totalSteps){
        this.steps = totalSteps;
        System.out.println(TAG+"SetStepCount"+steps);
        this.distance = distCal.calculateDistance(this.steps);
        this.displayStepsNDist();
    }

    /**
     * displayStepsNDist is use to set step and dist count context in the view context
     */
    public void displayStepsNDist() {
        stepContext.setText(String.format(Locale.US, "%d", this.steps));
        distContext.setText(this.distance);
    }

    /**
     * this method is use to display the last walked walk of user
     * @param route
     */
    public void displayWalk(Route route){
        if(route == null){
            return;
        }
        TextView lastWalk_context = findViewById(R.id.lastWalk);
        String name = route.name;
        String dist = route.dist;
        String step = route.stepCount;
        String time = route.time;
        if(name==null) name = "";
        if(dist==null) dist = "0";
        if(step==null) step = "0";
        if(time == null)time = "00:00:00";
        String routeInfo = "Last Walk: "+name + ", Distance: "+ dist
                + ", Step Count: " + step + ", Time: "+time;
        lastWalk_context.setText(routeInfo);
    }

    /**
     * getter of updateStepService
     * @return
     */
    public UpdateStepService getUpdateStepService(){
        return updateStepService;
    }

    /**
     * setStopUpdate method will change boolean value in stopUpdate, by default it is true, then it
     * will change to false after fitness service is setup
     * @param arg
     */
    public void setStopUpdate(boolean arg){
        this.stopUpdate = arg;
    }

    /**
     * initialMyUser will initialized static variable myUser in User class, this user instance
     * represent the current user. This method will initialized myUser using the name and email address
     * store in the local storage, entered at the first usage of app
     * @ensure StorageHandler.contains("name")
     * @ensure StorageHandler.contains("email)
     * @ensure myUser!=null
     */
    public void initialMyUser(){
        StorageHandler storageHandler = StorageHandler.getStorage(this);
        String name = storageHandler.retrieveItem("name", String.class);
        String email = storageHandler.retrieveItem("email", String.class);

        User me = new User(name, email);
        myUser = me;
    }

    /**
     * initializeInvitationStore initializes Firebase store for invitation. It will retrieve all remote invitation of current user
     * and update intiation arraylist of myUser
     */
    public void initializeInvitationStore(){
        StorageStore msgStore = new FirebaseStoreAdapter();
        msgStore.setUp("users", (User.myUser.getName()).replace(' ','0'), "invitation", this);
        msgStore.subscribeToNotificationsTopic();
        msgStore.initInvitationListener(myUser);
    }

    /**
     * initializedRouteStore initializes Firebase store for Route. It will retrieve all remote route of current user...
     */
    public void initializedRouteStore(){
        StorageStore routeStore = new FirebaseStoreAdapter();
        routeStore.setUp("users", (User.myUser.getName()).replace(' ','0'), "routes", this);
        routeStore.subscribeToNotificationsTopic();
        routeStore.initRouteListener(myUser);
    }



    /**
     * setBtnOnClickListener will set clicklisten to buttons in the context view of home page
     */
    public void setBtnOnClickListener(){
        /*button  link to walk screen*/
        Button strWalkBtn = findViewById(R.id.strWalkBtn);
        strWalkBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                launchWalkScreen();
            }
        });

        /*Button to enter route page*/
        Button routeBtn = findViewById(R.id.routePageBtn);
        routeBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                launchRouteScreen();
            }
        });

        Button switchToMockScreen = findViewById(R.id.toMockStepsPage);
        switchToMockScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchMockScreen();
            }
        });

        /*Team Activity*/
        Button teamBtn = findViewById(R.id.team_btn);
        teamBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                launchTeamScreen();
            }
        });

        ImageButton msgBtn = findViewById(R.id.msg_btn);
        msgBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                launchMessageScreen();
            }
        });
    }

    /**
     * launcher of messageScreen
     */
    public void launchMessageScreen(){
        Intent intent = new Intent(this, MessageActivity.class);
        startActivity(intent);
    }
    public void launchRouteScreen(){
        Intent intent = new Intent(this, RouteActivity.class);
        startActivity(intent);
    }
    public void launchWalkScreen(){
        Intent intent = new Intent( this, WalkScreen.class);
        intent.putExtra(WalkScreen.STEPS_BEFORE_START, String.format(Locale.US, "%d", steps));
        intent.putExtra(WalkScreen.FITNESS_SERVICE_KEY, getIntent().getStringExtra(FITNESS_SERVICE_KEY));
        startActivity(intent);
    }
    public void launchMockScreen(){
        Intent intent = new Intent( this, MockStepsHomePage.class);
        startActivityForResult(intent, RETURNCODE_MOCK);
    }
    public void launchTeamScreen(){
        Intent intent = new Intent(this, TeamPageActivity.class);
        startActivity(intent);
    }

    /**This method will build a list of mock user*/
    public void mockUser(){
        StorageHandler storageHandler = StorageHandler.getStorage(this);
        SharedPreferences sharedPreferences = getSharedPreferences("database", MODE_PRIVATE);
        boolean hasName = sharedPreferences.contains("name");
        User me;
        if(hasName) {
            String name = storageHandler.retrieveItem("name", String.class);
            String email = storageHandler.retrieveItem("email", String.class);
            me = new User(name, email);
        }else{
            me = new User("default name", "default@gmail.com");
        }

        User other = new User("Alice Brown", "alice@gmail.com");
        User other2 = new User("Bill C", "bill@gmail.com");
        me.getTeam().addTeamMember(other);
        me.getTeam().addTeamMember(other2);

        User pend1 = new User("Gary G", "gary@gmail.com");
        me.inviting(pend1);
        myUser = me;
    }
}
