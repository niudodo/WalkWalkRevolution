package edu.ucsd.cse110.team22.walkwalkrevolution;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import edu.ucsd.cse110.team22.walkwalkrevolution.Storage.StorageHandler;
import edu.ucsd.cse110.team22.walkwalkrevolution.fitness.FitnessService;
import edu.ucsd.cse110.team22.walkwalkrevolution.fitness.FitnessServiceFactory;
import edu.ucsd.cse110.team22.walkwalkrevolution.fitness.GoogleFitAdapter;
import edu.ucsd.cse110.team22.walkwalkrevolution.fitness.StepViewActivity;

public class MainActivity extends AppCompatActivity {
  
    public static String fitnessServiceKey = "GOOGLE_FIT";
    private static final int permissionCode = 1;
    private double height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        displayHeightPopUp();
    }

    /** This method will launch the homepage and initialize the date in fitnessservice factory.
     */
    private void launchHomePage(){
        FitnessServiceFactory.put(fitnessServiceKey, new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create(StepViewActivity stepViewActivity) {
                return new GoogleFitAdapter();
            }
        });

        Intent intent = new Intent( this, HomePageActivity.class);
        intent.putExtra(HomePageActivity.FITNESS_SERVICE_KEY, fitnessServiceKey);
        finish();
        startActivity(intent);
    }

    private void displayHeightPopUp(){
        SharedPreferences sharedPreferences = getSharedPreferences("database", MODE_PRIVATE);
        boolean hasHeight = sharedPreferences.contains("height");
        if(hasHeight){
            launchHomePage();
        }else{
            askForHeight();
            askForNameEmail();
        }
    }

    private void askForHeight() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        final EditText inputHeight = new EditText(this);
        inputHeight.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(inputHeight);

        builder.setMessage("Please Enter your height in inches:");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try{
                    height = Double.parseDouble(inputHeight.getText().toString());
                    storeHeight();//store height in the preference
                    launchHomePage();//change order of launch, only launch after getting height
                }catch (NumberFormatException | NullPointerException e){
                    System.out.println("Invalid height entered");
                    askForHeight();
                }

            }
        });
        AlertDialog showBuilder = builder.create();
        showBuilder.show();
    }

    private void storeHeight(){
        StorageHandler storageHandler = StorageHandler.getStorage(this);
        storageHandler.saveItem("height", height);
    }

    /**Edited ask for email and name for initializing user object later on*/
    private void askForNameEmail(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);

        LinearLayout lila1= new LinearLayout(this);
        lila1.setOrientation(LinearLayout.VERTICAL);

        final EditText inputName = new EditText(this);
        inputName.setHint("Enter Name");

        final EditText inputEmail = new EditText(this);
        inputEmail.setHint("Enter Email");
        inputName.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        inputEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        inputName.setId(R.id.team_alarmdialog_name_input);
        inputEmail.setId(R.id.team_alarmdialog_email_input);

        lila1.addView(inputName);
        lila1.addView(inputEmail);

        builder.setView(lila1);

        builder.setMessage("Please Enter your name and Email address");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try{
                    String name = inputName.getText().toString();
                    String email = inputEmail.getText().toString();
                    //only add inviation if a name and email is entered
                    if(!(name.equals("")||email.equals(""))) {
                        storeNameEmail(name, email);//store height in the preference
                    }
                }catch (NullPointerException e){
                    System.out.println("Invalid EMAIL entered");
                    askForNameEmail();
                }

            }
        });
        AlertDialog showBuilder = builder.create();
        showBuilder.show();
    }

    private void storeNameEmail(String name, String email){
        StorageHandler storageHandler = StorageHandler.getStorage(this);
        storageHandler.saveItem("name", name);
        storageHandler.saveItem("email", email);
    }

}

