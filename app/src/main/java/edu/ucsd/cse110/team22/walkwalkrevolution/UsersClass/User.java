package edu.ucsd.cse110.team22.walkwalkrevolution.UsersClass;

import android.app.Activity;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.Random;

import edu.ucsd.cse110.team22.walkwalkrevolution.Route.Route;

/**
 * This class is use to store the information of user in the database
 * Each user should keep track of his or her own team member
 * Date: 3/6/2020
 * Author: SZ
 */
public class User {
    public static User myUser;

    private String name;
    private String email;
    private String initial;
    private int iconColor;
    private Team team; // Team keep list of members and invited pending members
    private ArrayList<Invitation> invited; //the pending invitation I RECIEVED
    private UserTeamView teamView;
    private ArrayList<UserObserver> observers;
    private ArrayList<Route> myRoutes;

    /**
     * Constructor, there is no setter function of this class, the name and email of user is fixed after initialization
     * @param name
     * @param email
     */
    public User(String name, String email){
        this.name = name;
        this.email = email;
        this.initial = generateInitial(name);
        this.iconColor = generateIconColor(name);
        ArrayList<User> teamMember = new ArrayList<>();
        teamMember.add(this);
        ArrayList<User> invitations = new ArrayList<>();

        /*TODO Now the constructor just construct a new team, should use a factory method or something to sync with remote data*/
        this.team = new Team(name, teamMember, invitations);
        this.invited = new ArrayList<>();

        /*Initialized view object as well as observer list*/
        teamView = new UserTeamView(this);
        observers = new ArrayList<>();
        myRoutes = new ArrayList<>();
    }

    /**
     * Constructor that will take-in team as parameter
     * @param name
     * @param email
     * @param team
     */
    public User(String name, String email, Team team){
        this.name = name;
        this.email = email;
        this.team = team;
        this.initial = generateInitial(name);
        this.iconColor = generateIconColor(name);
        this.invited = new ArrayList<>();

        teamView = new UserTeamView(this);
        observers = new ArrayList<>();
        myRoutes = new ArrayList<>();
    }
    /*-----------------------------------------------Static method----------------------------------------------*/

    /**
     * generatedInitial will generated initial of the user bases on firstname and lastname
     * @param name
     * @return
     */
    public static String generateInitial(String name){
        String firstName;
        String lastName;
        int spaceIndex = name.indexOf(' ');
        if(spaceIndex!=-1) {
            firstName = name.substring(0, spaceIndex);
            lastName = name.substring(spaceIndex + 1, name.length());
        }else{
            firstName=name.substring(0,1);
            lastName=" ";
        }
        char[] initialArray = new char[2];
        initialArray[0]= firstName.charAt(0);
        initialArray[1]= lastName.charAt(0);
        String initial = String.copyValueOf(initialArray);
        return initial;
    }

    public static int generateIconColor(String name){
        //Random rnd = new Random();
        Random rnd = new Random(name.hashCode());
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        return color;
    }


    /*------------------------------------------- Invitation Related method-------------------------------------------------*/

    /** This function will be called when "I"(this) INVITE someone else
     * @param invitee: someone else, the user I want to invite to my team
     * @require invited.contains(invitee);
     * @ensure new invitation add to pending invitation list of invitee
     * @ensure new invitation add to MY team
     * @ensure all observer registered should recieve update by calling onStateChange on them.
     */
    public void inviting(User invitee) {
        //add the new invitation to my team
        this.team.addInvitation(invitee);
        for(UserObserver observer:this.observers){
            observer.onTeamChange();
        }
    }

    /** This function will be called when "I"(this) am INVITED by someone else
     *  Inviter: Someone else, Invitee: I(this).
     * @param  invitation- object of Invitation class that pending MY RESPONSE
     * @ensure invitation param will be add to this.invitated(Arraylist<Invitation>)
     */
    public void addInvited(Invitation invitation){
        this.invited.add(invitation);
    }

    /**
     * acceptInvitation is called when this invitation is accepted
     * It will update the team of both inviter and invitee
     * as well as change the accept status of invitation
     * @param invitation
     * @require invitee.invitated.contain(invitation)
     * @ensure teamview of all user in invitation.getTeam() is update, MY name should be ungray
     * @ensure invitation.pending = false;
     * @ensure invitation.accept = true;
     * @ensure !invitee.invitated.contain(invitation)
     * @ensure invitee.getTeam() == invitation.getTeam()
     * @ensure invitation.getTeam().contain(invitee)
     */
    public void acceptInvitation(Invitation invitation){
        invitation.accept();
        Team team = invitation.getTeam();
        this.addTeam(team);

        //Notify all observers
        for(UserObserver observer : this.getObservers())
            observer.onAcceptInvitation();
    }

    /**TODO Need to implement function when the invitee reject the invitation
     * I think is should just be setting the invitation status to reject? */
    public void rejectInvitation(Invitation invitation){
        invitation.reject();

        for(UserObserver observer : this.getObservers())
            observer.onRejectInvitation();
    }

    /**
     * displayTeam() will call build method of teamView which regenerated the View of team memebr list
     * @param activity
     */
    public void displayTeam(Activity activity){
        this.teamView.buildTeamView(activity);
    }

    /** User for observer
     * register Observer, all UserObserver should implemenet onStateChange(), this method will be called
     * on all registered observer if StateChange
     * @param observer
     */
    public void register(UserObserver observer){
        this.observers.add(observer);
    }

    /**
     * Remove the observer from the list
     * @param observer
     */
    public void unregister(UserObserver observer){
        if(this.observers.contains(observer)){
            this.observers.remove(observer);
        }
    }

    /**
     * Set the team, this method can be called when team member updates, it will update observers
     * for the team change
     * @param team
     */
    public void addTeam(Team team){
        this.team = team;
        for(UserObserver observer:observers){
            observer.onTeamChange();
        }
    }

    /*-----------------------------------------Getters--------------------------------------------*/

    /**
     * getter of User's name
     * @return String - this.name
     */
    public String getName(){
        return this.name;
    }

    /** getInitial will return initial of user based the name. If only the first name is enter
     * then the initial will be single character+ empty space.
     * @return String - initial of this user
     */
    public String getInitial(){
        return this.initial;
    }

    /**
     * getEmail is a getter will return user's email address
     * @return string this.email
     */
    public String getEmail(){
        return this.email;
    }

    /**
     * getIconColor will return a unique color for this user's initial icon bases on hashcode value of
     * the user's name
     * @return int---color of the initial icon
     */
    public int getIconColor(){
        return this.iconColor;
    }

    /** This is a getter that will return the team of the user
     * @return this.team
     */
    public Team getTeam(){
        return this.team;
    }

    /**
     * This method will return the list of invitation user recieved
     * @return this.invitated---> arraylist of Invitation.class
     */
    public ArrayList<Invitation> getInvited(){
        return this.invited;
    }

    /**
     * This method will return the list of UserObserver object that is registered to observer this user
     * @return this.observers
     */
    public ArrayList<UserObserver> getObservers(){
        return this.observers;
    }

    /*----------------------------------------Routes----------------------------------------------*/
    public void addRoute(Route route){
        this.myRoutes.add(route);
    }

    public void setRouteList(ArrayList<Route> routes){
        this.myRoutes = routes;
    }

    public void removeRoute(Route route){
        this.myRoutes.remove(route);
    }

    public ArrayList<Route> getMyRoutes(){return this.myRoutes;}

}
