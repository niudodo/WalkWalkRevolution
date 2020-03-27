package edu.ucsd.cse110.team22.walkwalkrevolution.MockTeamRoutes;

import android.content.Context;

import edu.ucsd.cse110.team22.walkwalkrevolution.Route.Route;
import edu.ucsd.cse110.team22.walkwalkrevolution.Storage.StorageHandler;

public class TeamRoutesGenerator {
   // StorageHandler storageHandler;
    String key = "entryNo";
    Context c;

    public TeamRoutesGenerator(Context context) {
        c = context;
    }

    // saves the routes that we create in TeamRoutes.java in the sharedprefs
    public void createMockRoutes(TeamRoutes teamRoutes){
        for (Route route : teamRoutes.getTeamRoutes()) {
            saveTeamRoute(route);
        }
    }

    // clears storage in sharedprefs
    public void clearMockRoutes(TeamRoutes teamRoutes){
        for (Route route : teamRoutes.getTeamRoutes()) {
            deleteTeamRoute(route);
        }
    }

    public void saveTeamRoute(Route route) {
        StorageHandler storageHandler = StorageHandler.getStorage(c);
        route.id = key+storageHandler.sizeOfDB()+System.currentTimeMillis();
        storageHandler.saveItem(route.id, route);

        String lastWalk = "idOfLastWalk";
        storageHandler.saveItem(lastWalk, route.id);
    }

    public void deleteTeamRoute(Route route){
        StorageHandler storageHandler = StorageHandler.getStorage(c);
        storageHandler.deleteKey(route.id);
    }
}

