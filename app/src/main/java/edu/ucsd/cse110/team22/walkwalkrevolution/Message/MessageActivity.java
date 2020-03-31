package edu.ucsd.cse110.team22.walkwalkrevolution.Message;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.team22.walkwalkrevolution.R;
import edu.ucsd.cse110.team22.walkwalkrevolution.Storage.StorageStore;
import edu.ucsd.cse110.team22.walkwalkrevolution.UsersClass.User;
import edu.ucsd.cse110.team22.walkwalkrevolution.UsersClass.UserObserver;

public class MessageActivity extends AppCompatActivity implements UserObserver {
    //The storage store shoulld subscribe to the invitation I recieved
    final static String COLLECTION_KEY = "users";
    final static String INVITATION_KEY = "invitation";
    StorageStore msgStore;
    MessageAdapter msgAdapter;
    ListView messageViewList;
    List<IMessage> messageList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        User.myUser.register(this);
        //refresh messageList
        updateMessageList();

    }


    @Override
    public void onTeamChange(){}

    @Override
    public void onAcceptInvitation(){
        messageViewList.setAdapter(msgAdapter);
        msgAdapter.notifyDataSetChanged();
        //updateMessageList();
    }

    @Override
    public void onRejectInvitation(){
        messageViewList.setAdapter(msgAdapter);
        msgAdapter.notifyDataSetChanged();
        //updateMessageList();
    }


    /**
     * updateMessageList will retrieve invitation in the user invited list and re-generate
     * message view list by using adapter
     */
    public void updateMessageList(){
        messageViewList = this.findViewById(R.id.message_list);
        messageList = new ArrayList<>();
        for(Invitation i: User.myUser.getInvited()) {
            messageList.add(i);
        }
        msgAdapter = new MessageAdapter(this, R.layout.message_entry, messageList );
        messageViewList.setAdapter(msgAdapter);
    }
}
