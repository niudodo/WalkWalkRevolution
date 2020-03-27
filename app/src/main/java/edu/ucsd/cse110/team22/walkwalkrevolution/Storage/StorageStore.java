package edu.ucsd.cse110.team22.walkwalkrevolution.Storage;

import android.app.Activity;

import java.util.Map;

import edu.ucsd.cse110.team22.walkwalkrevolution.Route.Route;
import edu.ucsd.cse110.team22.walkwalkrevolution.UsersClass.Team;
import edu.ucsd.cse110.team22.walkwalkrevolution.UsersClass.User;

public interface StorageStore {
    void subscribeToNotificationsTopic();
    void sendNotification(Map<String, String> newMessage);
    void setUp(String collectionKey, String documentKey, String messageKey, Activity activity);
    void initTeamMessageListener(Team team, User user);
    void initInvitationListener(User user);
    void initRouteListener(User user);
    void deleteMessage(String id);
    void updateRoute(Route route);
}
