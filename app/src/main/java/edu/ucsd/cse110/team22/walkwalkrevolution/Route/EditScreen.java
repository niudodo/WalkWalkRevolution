package edu.ucsd.cse110.team22.walkwalkrevolution.Route;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import edu.ucsd.cse110.team22.walkwalkrevolution.R;
import edu.ucsd.cse110.team22.walkwalkrevolution.Storage.FirebaseStoreAdapter;
import edu.ucsd.cse110.team22.walkwalkrevolution.Storage.StorageHandler;
import edu.ucsd.cse110.team22.walkwalkrevolution.Storage.StorageStore;
import edu.ucsd.cse110.team22.walkwalkrevolution.UsersClass.User;

public class EditScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_screen);

        StorageHandler sh = StorageHandler.getStorage(EditScreen.this);
        String id = sh.retrieveItem("idOfRouteToBeEdited",String.class);
        Route r = sh.retrieveItem(id,Route.class);

        Button save = (Button)findViewById(R.id.save);
        Button delete = (Button)findViewById(R.id.delete);
        loadInfo(r);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInfo(sh,id,r);
                EditScreen.this.finish();
                switchToRoutePage();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpScreen(sh,id);
            }
        });
    }

    public void switchToRoutePage(){
        Intent intent = new Intent(this, RouteActivity.class);
        startActivity(intent);
    }

    public void popUpScreen(StorageHandler sh,String id){
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface arg0,int arg1){
                sh.deleteKey(id);
                EditScreen.this.finish();
                switchToRoutePage();
            }
        });

        b.setNegativeButton("No", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface arg0,int arg1){
                arg0.dismiss();
            }
        });

        b.setMessage("Are you sure to DELETE this route record?");
        b.setTitle("Warning");
        b.show();

    }


    public void loadInfo(Route r){

        final EditText name = (EditText) findViewById(R.id.routeName);
        name.setText(r.name);

        final EditText startLoc = (EditText) findViewById(R.id.start);
        startLoc.setText(r.startLocation);

        final EditText notes = (EditText) findViewById(R.id.notes);
        notes.setText(r.notes);

        CheckBox checkBox = (CheckBox)findViewById(R.id.favorite);
        checkBox.setChecked(r.favorite);

        final Spinner Spinner1 = (Spinner) findViewById(R.id.feedback_1);
        ArrayAdapter adap1 = (ArrayAdapter) Spinner1.getAdapter();
        if(!r.loopOrOut_and_back.equals("")) {
            int spinnerPosition1 = adap1.getPosition(r.loopOrOut_and_back);
            Spinner1.setSelection(spinnerPosition1);
        }else{
            Spinner1.setSelection(0);
        }

        final Spinner Spinner2 = (Spinner) findViewById(R.id.feedback_2);
        ArrayAdapter adap2 = (ArrayAdapter) Spinner2.getAdapter();
        if(!r.terrainType.equals("")) {
            int spinnerPosition2 = adap2.getPosition(r.terrainType);
            Spinner2.setSelection(spinnerPosition2);
        }else{
            Spinner2.setSelection(0);
        }

        final Spinner Spinner3 = (Spinner) findViewById(R.id.feedback_3);
        ArrayAdapter adap3 = (ArrayAdapter) Spinner3.getAdapter();
        if(!r.streetsOrTrail.equals("")) {
            int spinnerPosition3 = adap3.getPosition(r.streetsOrTrail);
            Spinner3.setSelection(spinnerPosition3);
        }else{
            Spinner3.setSelection(0);
        }

        final Spinner Spinner4 = (Spinner) findViewById(R.id.feedback_4);
        ArrayAdapter adap4 = (ArrayAdapter) Spinner4.getAdapter();
        if(!r.surface.equals("")) {
            int spinnerPosition4 = adap4.getPosition(r.surface);
            Spinner4.setSelection(spinnerPosition4);
        }else{
            Spinner4.setSelection(0);
        }

        final Spinner Spinner5 = (Spinner) findViewById(R.id.feedback_5);
        ArrayAdapter adap5 = (ArrayAdapter) Spinner5.getAdapter();
        if(!r.difficulty.equals("")) {
            int spinnerPosition5 = adap5.getPosition(r.difficulty);
            Spinner5.setSelection(spinnerPosition5);
        }else{
            Spinner5.setSelection(0);
        }

    }

    public void saveInfo(StorageHandler sh,String id, Route r){
        //items to populate Route object with
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
        if(!(feedbackType1.equals(defaultSpinnerValue))) {
            loopOrOut_and_back = feedbackType1;
        }else{
            loopOrOut_and_back="N/A";
        }

        final Spinner Spinner2 = (Spinner) findViewById(R.id.feedback_2);
        String feedbackType2 = Spinner2.getSelectedItem().toString();

        if(!(feedbackType2.equals(defaultSpinnerValue))){
            terrainType = feedbackType2;
        }else{
            terrainType = "N/A";
        }

        final Spinner Spinner3 = (Spinner) findViewById(R.id.feedback_3);
        String feedbackType3 = Spinner3.getSelectedItem().toString();

        if(!(feedbackType3.equals(defaultSpinnerValue))){
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

        r.name = name;
        r.startLocation = startLocation;
        r.notes =  notes;
        r.favorite = favorite;
        r.terrainType = terrainType;
        r.loopOrOut_and_back = loopOrOut_and_back;
        r.streetsOrTrail = streetsOrTrail;
        r.surface = surface;
        r.difficulty = difficulty;

        /* Code to add the above route object to database */
        sh.saveItem(id,r);
        updateRemoteInfo(r);
    }

    public void updateRemoteInfo(Route route){
        StorageStore ss = new FirebaseStoreAdapter();
        ss.setUp("users", (User.myUser.getName()).replace(' ', '0'), "routes", this);
        ss.updateRoute(route);
    }
}


