package edu.ucsd.cse110.team22.walkwalkrevolution.UsersClass;

import java.util.ArrayList;

public class Team {
    private String name;
    private ArrayList<User> members;
    private ArrayList<User> invitedMembers;

    /**
     * Constructor
     * @param name
     * @param members
     * @param invitedMembers
     */
    public Team(String name, ArrayList<User> members, ArrayList<User> invitedMembers){
        this.name = name;
        this.members = members;
        this.invitedMembers = invitedMembers;
    }

    public void addTeamMember(User user){
        this.members.add(user);
    }

    public void removeInvitation(User user){
        this.invitedMembers.remove(user);
    }

    public void addInvitation(User user){
        this.invitedMembers.add(user);
    }

    public ArrayList<User> getMembers(){
        return this.members;
    }

    public ArrayList<User> getInvitations(){
        return this.invitedMembers;
    }

    public String getName() {
        return name;
    }

}
