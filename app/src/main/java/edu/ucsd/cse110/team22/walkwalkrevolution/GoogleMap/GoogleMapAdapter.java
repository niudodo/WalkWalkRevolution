package edu.ucsd.cse110.team22.walkwalkrevolution.GoogleMap;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import java.net.URLEncoder;

import edu.ucsd.cse110.team22.walkwalkrevolution.Route.Route;

/**
 * GoogleMapAdapter will be use to open google map url with the location of route
 * All instances use in Activity should have type MapAdapter for testing purpose
 */
public class GoogleMapAdapter implements MapAdapter {
    final static String urlFormat = "https://www.google.com/maps/search/?api=1&query=";//+parameters
    public void openLocation(String location, Activity activity){

        try {
            String locUrl = urlFormat + URLEncoder.encode(location, "UTF-8");
            Uri uri = Uri.parse(locUrl); // missing 'http://' will cause crashed
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setPackage("com.google.android.apps.maps");
            activity.startActivity(intent);
        }catch (Exception e ){
            System.out.println("Exception throw");
        }
    }


    public void openLocation(Route route, Activity activity){
        String location = route.name;
        openLocation(location,activity);
    }
}
