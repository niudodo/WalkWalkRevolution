package edu.ucsd.cse110.team22.walkwalkrevolution.GoogleMap;

import android.app.Activity;

import edu.ucsd.cse110.team22.walkwalkrevolution.Route.Route;

public interface MapAdapter {
    void openLocation(String location, Activity activity);
    void openLocation(Route route, Activity activity);
}
