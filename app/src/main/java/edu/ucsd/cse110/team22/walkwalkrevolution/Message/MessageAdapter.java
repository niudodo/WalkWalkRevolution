package edu.ucsd.cse110.team22.walkwalkrevolution.Message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import edu.ucsd.cse110.team22.walkwalkrevolution.R;
import edu.ucsd.cse110.team22.walkwalkrevolution.UsersClass.User;

public class MessageAdapter extends ArrayAdapter<IMessage> {

    Context context;
    int resourceId;
    public MessageAdapter(Context context, int resource, List<IMessage> objects){
        super(context, resource, objects);
        this.context = context;
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        IMessage msg = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);

        TextView msg_title = view.findViewById(R.id.msg_title);
        TextView msg_context = view.findViewById(R.id.msg_context);
        TextView msg_mark = view.findViewById(R.id.msg_mark);
        Button rejectBtn = view.findViewById(R.id.msg_reject_btn);
        rejectBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                User.myUser.rejectInvitation((Invitation)msg);
            }
        });
        Button acceptBtn = view.findViewById(R.id.msg_accept_btn);
        acceptBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                User.myUser.acceptInvitation((Invitation)msg);
            }
        });

        msg_title.setText(msg.getTitle());
        msg_context.setText(msg.getContext());
        msg_mark.setText(msg.getMark());

        return view;
    }
}
