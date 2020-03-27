package edu.ucsd.cse110.team22.walkwalkrevolution.UsersClass;

import edu.ucsd.cse110.team22.walkwalkrevolution.Message.IMessage;

/**
 * Invitation class is a class that use for one user to invite another user into her team
 * NOTE: refactor by replace User inviter field with the team
 * Date: 3/6/2020
 * Author: SZ
 */
public class Invitation implements IMessage{
    //private User inviter;
    private Team team;
    private User invitee;
    private boolean pending; //true if invitee hasn't response
    private boolean accepted; //true if invitee accept

    /**
     * Constructor
     * @param team
     * @param invitee
     */
    public Invitation(Team team, User invitee){
        this.invitee = invitee;
        this.team = team;
        pending = true;
        accepted = false;
    }

    /**
     * accept, call when this invitation is accepted
     * only responsible for changing the state of this invitation
     */
    public void accept(){
        this.accepted = true;
        this.pending = false;
        // this invitation will be remove from invitee.invitated inside the method call of User.class
    }

    /**
     * reject, call when this invitation is rejected
     * only responsible for changing the state of this invitation
     */
    public void reject(){
        this.pending = false;
        this.accepted = false;
    }
/*-----------------------------Getters -------------------------------------------*/
    public boolean getPending(){
        return this.pending;
    }

    public boolean getAccepted(){
        return this.accepted;
    }

    public Team getTeam(){
        return this.team;
    }

    public User getInvitee(){
        return  this.invitee;
    }

    /*-----------------------------------Method for IMessage--------------------------------------*/
    @Override
    public String getTitle(){
        return "Invitation";
    }

    @Override
    public String getContext(){
        String context = this.getTeam().getName()+" sent you a team invitation";
        return context;
    }

    @Override
    public String getMark(){
        if(this.pending){
            return "Pending";
        }else if(this.accepted){
            return "Accepted";
        }else{
            return "Rejected";
        }
    }

    @Override
    public String getTime(){
        return "Mon 10:10am";
    }

    @Override
    public String getSender(){
        return this.getTeam().getName();
    }


}
