package edu.ucsd.cse110.team22.walkwalkrevolution.Message;

import edu.ucsd.cse110.team22.walkwalkrevolution.Route.Route;
import edu.ucsd.cse110.team22.walkwalkrevolution.UsersClass.User;

public class RouteInvitation implements IMessage {
    final static String TITLE_TEXT = "Route Invitation";
    final static String CONTEXT_FORMAT = "%s invited you for a walk, %s, on %s";

    private User sender;
    private User invitee;
    private boolean pending; //true if invitee hasn't response
    private boolean accepted; //true if invitee accept
    private Route route;
    private String time;

    public RouteInvitation(User sender, User invitee, Route route, String time){
        this.sender = sender;
        this.invitee = invitee;
        this.route = route;
        this.time = time;
        pending = true;
        accepted = false;
    }
    public String getTitle(){
        return TITLE_TEXT;
    }
    public String getContext(){
        String context = String.format(CONTEXT_FORMAT, sender.getName(), route.name, this.getTime());
        return context;
    }
    public String getMark(){

        if(pending)
            return "Pending";
        else if (accepted)
            return "accepted";
        else
            return "Rejected";

    }
    public String getTime(){
        return time;
    }
    public String getSender(){
        return this.sender.getName();
    }

}
