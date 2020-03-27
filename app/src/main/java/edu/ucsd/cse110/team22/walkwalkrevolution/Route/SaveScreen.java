package edu.ucsd.cse110.team22.walkwalkrevolution.Route;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import edu.ucsd.cse110.team22.walkwalkrevolution.R;
import edu.ucsd.cse110.team22.walkwalkrevolution.Storage.FirebaseStoreAdapter;
import edu.ucsd.cse110.team22.walkwalkrevolution.Storage.StorageHandler;
import edu.ucsd.cse110.team22.walkwalkrevolution.Storage.StorageStore;
import edu.ucsd.cse110.team22.walkwalkrevolution.UsersClass.User;

public class SaveScreen extends AppCompatActivity {
    public static final String TIME = "TIME";
    public static final String STEPS = "STEPS";
    public static final String DISTANCE = "DISTANCE";

    StorageHandler sh;
    String time, distance, steps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_screen);
        time = getIntent().getStringExtra(TIME);
        steps = getIntent().getStringExtra(STEPS);
        distance = getIntent().getStringExtra(DISTANCE);
        Log.d("SaveScreen", time + " " + distance + " " + steps);

        Button save = (Button)findViewById(R.id.save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInfo();
                SaveScreen.this.finish();
                switchToRoutePage();
            }
        });
    }

    public void switchToRoutePage(){
        Intent intent = new Intent(this, RouteActivity.class);
        startActivity(intent);
    }

    public void saveInfo(){
        //items to populate Route object with

        String username = ""; //name of user

        String name = "";//name of of route

        String startLocation = "";
        String notes = "";
        boolean favorite  = false;//is it favorite or not?

        String loopOrOut_and_back = "";
        String terrainType ="";//flat or hilly?
        String streetsOrTrail = "";
        String surface = "";//even or flat
        String difficulty = "";

        String defaultSpinnerValue = "Select One";

        username = null; //delete this in deliverable

        String routeName = ((TextView)findViewById(R.id.routeName)).getText().toString();
        name = routeName;

        String start = ((TextView)findViewById(R.id.start)).getText().toString();

        startLocation = start;

        notes = ((TextView)findViewById(R.id.notes)).getText().toString();


        CheckBox checkBox = (CheckBox)findViewById(R.id.favorite);

        if(checkBox.isChecked()){

            favorite = true;
        }

       /* Get each answer selected in all of the five spinners
       * and check it is not the default "Select One" value
        */
        final Spinner Spinner1 = (Spinner) findViewById(R.id.feedback_1);
        String feedbackType1 = Spinner1.getSelectedItem().toString();
        //if value is actually selected
        if(!(feedbackType1.equals(defaultSpinnerValue))){
            loopOrOut_and_back = feedbackType1;
        }else{
            loopOrOut_and_back = "N/A";
        }

        final Spinner Spinner2 = (Spinner) findViewById(R.id.feedback_2);
        String feedbackType2 = Spinner2.getSelectedItem().toString();

        if(!(feedbackType2.equals(defaultSpinnerValue))) {
            terrainType = feedbackType2;
        }else{
            terrainType = "N/A";
        }

        final Spinner Spinner3 = (Spinner) findViewById(R.id.feedback_3);
        String feedbackType3 = Spinner3.getSelectedItem().toString();

        if(!(feedbackType3.equals(defaultSpinnerValue))) {
            streetsOrTrail = feedbackType3;
        }else{
            streetsOrTrail = "N/A";
        }

        final Spinner Spinner4 = (Spinner) findViewById(R.id.feedback_4);
        String feedbackType4 = Spinner4.getSelectedItem().toString();

        if(!(feedbackType4.equals(defaultSpinnerValue))) {
            surface = feedbackType4;
        }else{
            surface = "N/A";
        }

        final Spinner Spinner5 = (Spinner) findViewById(R.id.feedback_5);
        String feedbackType5 = Spinner5.getSelectedItem().toString();

        if(!(feedbackType5.equals(defaultSpinnerValue))) {
            difficulty = feedbackType5;
        }else{
            difficulty = "N/A";
        }

        if(time == null){
            time = "00:00:00";
        }

        if(distance==null){
            distance = "0";
        }

        if(steps == null){
            steps = "0";
        }

            //populate Route object
        Route route = new Route( username, name,  time, startLocation, notes, favorite, terrainType,

                loopOrOut_and_back, streetsOrTrail,  surface, difficulty, steps, distance);

        /* Code to add the above route object to database */
        StorageHandler sh = StorageHandler.getStorage(this);
        String key = "entryNo";
        route.id = key+sh.sizeOfDB()+System.currentTimeMillis();
        sh.saveItem(route.id,route);

        /* save the last walk's id in the data base*/
        String lastWalk = "idOfLastWalk";
        sh.saveItem(lastWalk, route.id);
        remoteAddRoute(route);
    }

    public void remoteAddRoute(Route route){
        StorageStore store = new FirebaseStoreAdapter();
        store.setUp("users", (User.myUser.getName()).replace(' ','0'), "routes", this);
        store.initRouteListener(User.myUser);
        store.sendNotification(Route.getDataMap(route));
    }
}


