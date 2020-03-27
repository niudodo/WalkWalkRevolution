package edu.ucsd.cse110.team22.walkwalkrevolution;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Button;
import android.widget.EditText;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowAlertDialog;

import java.util.ArrayList;
import java.util.Map;

import edu.ucsd.cse110.team22.walkwalkrevolution.Storage.StorageStore;
import edu.ucsd.cse110.team22.walkwalkrevolution.UsersClass.User;

import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@Config(sdk=28)
public class TeamPageUITest {
    @Rule
    public ActivityScenarioRule<MockTeamActivity> mActivityTestRule =
            new ActivityScenarioRule<MockTeamActivity>(MockTeamActivity.class);

    @Test
    public void plusButtonTest(){
        ActivityScenario<MockTeamActivity> scenario = mActivityTestRule.getScenario();
        scenario.onActivity(activity -> {
            Button plusBtn = activity.findViewById(R.id.add_btn);
            plusBtn.performClick();
            AlertDialog prompt = ShadowAlertDialog.getLatestAlertDialog();
            assertTrue(prompt.isShowing());
        });
    }

    @Test
    public void invitedUser(){
        ActivityScenario<MockTeamActivity> scenario = mActivityTestRule.getScenario();
        scenario.onActivity(activity -> {
            Button plusBtn = activity.findViewById(R.id.add_btn);
            plusBtn.performClick();
            AlertDialog prompt = ShadowAlertDialog.getLatestAlertDialog();
            assertTrue(prompt.isShowing());
            Button okBtn = prompt.getButton(DialogInterface.BUTTON_POSITIVE);
            EditText nameView = prompt.findViewById(R.id.team_alarmdialog_name_input);
            EditText emailView = prompt.findViewById(R.id.team_alarmdialog_email_input);
            nameView.setText("Huff law");
            emailView.setText("huff@gmail.com");
            okBtn.performClick();
            ArrayList<User> invitationList = activity.getMyUser()
                    .getTeam()
                    .getInvitations();
            boolean foundNewUserName = false;
            boolean foundNewUserEmail = false;
            for(User i: invitationList){
                if(i.getName().equals("Huff law")){
                    foundNewUserName=true;
                }
                if(i.getEmail().equals("huff@gmail.com")){
                    foundNewUserEmail=true;
                }

            }
            assertTrue(foundNewUserName&foundNewUserEmail);

        });
    }

    @Test
    public void addTeamMember(){
        ActivityScenario<MockTeamActivity> scenario = mActivityTestRule.getScenario();
        String newUserName = "Jennifer";
        String newUserEmail = "jennifer@gmail.com";
        scenario.onActivity(activity -> {
           User currUser = activity.getMyUser();
           User newUser = new User(newUserName,newUserEmail);
           currUser.getTeam().addTeamMember(newUser);
           ArrayList<User> myTeam = currUser.getTeam().getMembers();
           boolean foundNewUserName=false;
           boolean foundNewUserEmail = false;
           for(User u:myTeam){
               if(u.getName().equals(newUserName)){
                   foundNewUserName = true;
               }
               if(u.getEmail().equals(newUserEmail)){
                   foundNewUserEmail = true;
               }
           }
           assertTrue(foundNewUserEmail&foundNewUserName);
        });
    }
}

class MockTeamActivity extends TeamPageActivity {
    @Override
    protected void setUpFirebase(){
        this.invitationToMe = new MockFireBaseStoreAdapter();
    }
    @Override
    protected void sendMessage(String name){
        System.out.println("MockTeamActivity: sendMessage"+name);
    }

    protected StorageStore getStorageStore(){
        return new MockFireBaseStoreAdapter();
    }

}

class MockFireBaseStoreAdapter implements StorageStore{
    @Override
    public void subscribeToNotificationsTopic(){}
    public void sendNotification(Map<String, String> newMessage){}
    public void setUp(String collectionKey, String documentKey, String messageKey, Activity activity){}
}
