package edu.ucsd.cse110.team22.walkwalkrevolution;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.ucsd.cse110.team22.walkwalkrevolution.Storage.FirebaseStoreAdapter;
import edu.ucsd.cse110.team22.walkwalkrevolution.Storage.StorageStore;
import edu.ucsd.cse110.team22.walkwalkrevolution.UsersClass.Team;
import edu.ucsd.cse110.team22.walkwalkrevolution.UsersClass.User;
import edu.ucsd.cse110.team22.walkwalkrevolution.UsersClass.UserObserver;

/**
 * TeamActivity is the team screen
 * Dependency: firebase
 * Date: 3/6/2020
 */
public class TeamPageActivity extends AppCompatActivity implements UserObserver {
    User me;
    StorageStore teamStore;
    StorageStore invitationToMe;
    final String COLLECTION_KEY = "users";
    final String MSG_TEAM_KEY ="team";
    final String MESSAGES_KEY = "invitation";
    final String FROM_KEY = "from";
    final String TEXT_KEY = "text";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        /**display mocking code*/

        me = createUser();
        me.register(this);

        me.displayTeam(this);
        /*end of mocking code*/

        /*get "+" button and set listener on it*/
        Button addBtn = findViewById(R.id.add_btn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptInvitation();
            }
        });


        /*Store use to recieve invitation to me, the store object here should only store the invitation sent to me!
        * using name as document key */
        setUpFirebase();
        /* setup team notification that will recieve notification when there is a change in the team member*/
        setUpTeamNotification();
    }

    /**
     * promtInvitation will pop up a AlerDialog that prompt the user to enter the other user he or she want to invite
     * if user enter empty email and name that it will do nothing
     * @ensure AlertDialog is prompted
     */
    private void promptInvitation(){

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

        builder.setMessage("Please Enter name and Email address of the user you want to invite");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try{
                    String name = inputName.getText().toString();
                    String email = inputEmail.getText().toString();
                    //only add inviation if a name and email is entered
                    if(!(name.equals("")||email.equals(""))) {
                        updateInvitationStorage(name, email);//store height in the preference
                        sendMessage(name);
                    }
                }catch (NullPointerException e){
                    System.out.println("Invalid EMAIL entered");
                    promptInvitation();
                }

            }
        });
        AlertDialog showBuilder = builder.create();
        showBuilder.show();
}


    /* TODO Need to implement the function */
    private void updateInvitationStorage(String name, String email){
        /**MOCKING CODE8*/
        User newUser = new User(name, email);
        /*I just realize I should probably have a invitation factory but I am too lazy to change it*/
        me.inviting(newUser);
    }



    /**This function will just return the current user of the app, which is me
     * @return user-----this .
     */
    public User getMyUser(){
        return this.me;
    }


    /** This method will be called when a new invitation is created, the same logic as lab7
     * method will initialzed a firebaseStoreAdapter connects to the document belong to parameter name user
     * @require user with "name@param" subscribe to collection(name) Document(DOCUMENT_KEY) Collection(MESSAGE_KEY)
     * @param name -- the name of user to send a message
     */

    protected void sendMessage(String name) {
        Map<String, String> newMessage = new HashMap<>();
        newMessage.put(FROM_KEY, me.getName());
        newMessage.put(TEXT_KEY, "sent you a team initation");
        StorageStore inviteeStore = getStorageStore();

        String document = name.replace(' ', '0');

        inviteeStore.setUp( COLLECTION_KEY, document, MESSAGES_KEY, this);
        inviteeStore.sendNotification(newMessage);
    }

    /**
     * setupFirebase is a inner factory method that call to initialize invitationToMe
     */
    protected void setUpFirebase() {
        FirebaseApp.initializeApp(this);
        invitationToMe = getStorageStore();
        String document = getMyUser().getName().replace(' ', '0');

        invitationToMe.setUp(COLLECTION_KEY, document, MESSAGES_KEY, this);
        invitationToMe.subscribeToNotificationsTopic();
        /*MOCK CODE, use to test if notification can be successfully sent*/
        Map<String, String> newMessage = new HashMap<>();
        newMessage.put(FROM_KEY, me.getName());
        newMessage.put(TEXT_KEY, "this is a testing message for invitationToMe");
        invitationToMe.sendNotification(newMessage);
        /*End of MOCK CODE */
    }

    /**This method will setup notification that recieve notification when a new member is add to the team
     *
     */
    public void setUpTeamNotification(){
       teamStore = getStorageStore();
       /*MOCK CODE,fix becasue we assume there is only one team*/
       teamStore.setUp("team", "team1", "members", this);
       teamStore.subscribeToNotificationsTopic();
    }

    /**
     * send message to the team document( adding member by accept invitation
     * CAUTION: I still cannot understand how to use the network package, and this function should be called when user accept the team invitation
     * it will send message to document in firebase and all team member should recieve notification
     * @param: message should be either "Accept" or "Reject"
     */
    public void sendTeamMessage(String message){
        Map<String, String> newMessage = new HashMap<>();
        newMessage.put(FROM_KEY, me.getName());
        String info = message+ "your invitation";
        newMessage.put(TEXT_KEY, info);
        teamStore.sendNotification(newMessage);
    }

    /**
     * inner factory method to return a new Storage store object
     * @return default firebaseStoreAdapter
     */
    protected StorageStore getStorageStore(){
        return new FirebaseStoreAdapter();
    }

    /**
     * createUser is a factory method that will return a new constructed user object
     */
    public User createUser(){
        User tempUser;
        if(User.myUser != null){
            tempUser = User.myUser;
            buildRemoteTeam(tempUser.getName(),tempUser);
        }else {
            tempUser = new User("Mock User", "mock@gmail.com");
        }
        return tempUser;
        //TODO:modify this part of the code to use constructor User(name, email, team)
    }

    public void buildRemoteTeam(String name, User user){
        StorageStore remoteTeam = new FirebaseStoreAdapter();
        remoteTeam.setUp(COLLECTION_KEY, name, MSG_TEAM_KEY, this);

        ArrayList<User> invitedMember = new ArrayList<>();
        ArrayList<User> teamMember = new ArrayList<User>();
        Team team = new Team(name,teamMember,invitedMember);
        remoteTeam.initTeamMessageListener(team, user);
        user.addTeam(team);
    }


    /* Method for UserObserver*/
    @Override
    public void onAcceptInvitation(){
        this.me.displayTeam(this);
    }

    @Override
    public void onRejectInvitation(){

    }

    /**This method will be called when team member status of user change*/
    @Override
    public void onTeamChange(){
        this.me.displayTeam(this);
    }
}
