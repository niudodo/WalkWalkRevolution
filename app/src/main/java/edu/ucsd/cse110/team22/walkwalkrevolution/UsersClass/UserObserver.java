package edu.ucsd.cse110.team22.walkwalkrevolution.UsersClass;

public interface UserObserver {
    public void onTeamChange();

    public void onAcceptInvitation();
    public void onRejectInvitation();
}
