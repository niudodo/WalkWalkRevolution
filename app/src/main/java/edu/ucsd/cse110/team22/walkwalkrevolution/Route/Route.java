package edu.ucsd.cse110.team22.walkwalkrevolution.Route;

import android.annotation.SuppressLint;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import edu.ucsd.cse110.team22.walkwalkrevolution.Storage.FirebaseStoreAdapter;

public class Route {
    public static int routeIdOffset=0;
    final public static String ROUTE_ID_PREFEX = "entryNo";
    public String username; //user that created route

    public String name;//name of of route

    /* NOTE: Matteo, you can change the time to double if you want in the Route class or Even change
    it to Timer Object if there is one
     */
    public String time;//time taken to travel the route

    /*NOTE: Add dist and step count */
    public String dist;
    public String stepCount;

    public String startLocation;
    public String notes;
    public boolean favorite;//is it favorite or not?
    public String terrainType;//flat or hilly?
    public String loopOrOut_and_back;
    public String streetsOrTrail;
    public String surface;//even or flat
    public String difficulty;
    public String id ="";
    private String remoteId;
    private String lastWalkedDate="";//store in format yyyy/mm/dd


    public Route( String username,
                  String name,
                  String time,
                  String startLocation,
                  String notes,
                  boolean favorite,
                  String terrainType,
                  String loopOrOut_and_back,
                  String streetsOrTrail,
                  String surface,
                  String difficulty,
                  String stepCount,
                  String dist){

        this.username = username;
        this.name = name;
        this.time = time;
        this.dist = dist;
        this.stepCount = stepCount;
        this.startLocation = startLocation;
        this.notes = notes;
        this.favorite = favorite;
        this.terrainType = terrainType;
        this.loopOrOut_and_back = loopOrOut_and_back;
        this.streetsOrTrail = streetsOrTrail;
        this.surface = surface;
        this.difficulty = difficulty;
        this.id=ROUTE_ID_PREFEX+routeIdOffset;
        routeIdOffset++;
    }

    public String getName(){ return this.name; }

    public void setRemoteId(String remoteId){
        this.remoteId = remoteId;
    }

    public String getRemoteId(){
        return this.remoteId;
    }

    /**
     * @require date in format yyyy/mm/dd
     * @param date
     */
    public void setLastWalkedDate(String date){
        this.lastWalkedDate = date;
    }

    /**
     * @require date in millisecond after 1970
     */
    public void setLastWalkedDate(Long timeInMilles){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMilles);
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);

        @SuppressLint("DefaultLocale") String lwd = String.format("%d/%d/%d", mYear, mMonth, mDay);
        this.lastWalkedDate=lwd;
    }

    /**
     *
     * @return
     */
    public String getLastWalkedDate(){
        return this.lastWalkedDate;
    }

    /**
     * getDataMap will generate and return a Map<String,String> in the require format of firebasestore
     * @param route
     * @return
     */
    public static Map<String, String> getDataMap(Route route){
        Map<String, String> msg = new HashMap<>();
        msg.put(FirebaseStoreAdapter.NAME_KEY, route.name);
        msg.put(FirebaseStoreAdapter.DIFFICULTY_KEY, route.difficulty);
        msg.put(FirebaseStoreAdapter.DIST_KEY, route.dist);
        String favorite = "false";
        if(route.favorite)
            favorite="true";
        msg.put(FirebaseStoreAdapter.FAVORITE_KEY, favorite);
        msg.put(FirebaseStoreAdapter.LOOP_KEY, route.loopOrOut_and_back);
        msg.put(FirebaseStoreAdapter.NOTE_KEY, route.notes);
        msg.put(FirebaseStoreAdapter.STEPCOUNT_KEY, route.stepCount);
        msg.put(FirebaseStoreAdapter.TERRAIN_KEY, route.terrainType);
        msg.put(FirebaseStoreAdapter.TIME_KEY, route.time);
        msg.put(FirebaseStoreAdapter.TRAIL_KEY, route.streetsOrTrail);
        msg.put(FirebaseStoreAdapter.SURFACE_KEY, route.surface);
        msg.put(FirebaseStoreAdapter.STARTLOCATION_KEY, route.startLocation);
        msg.put(FirebaseStoreAdapter.LAST_WALKED_DATE_KEY, route.lastWalkedDate);

        return msg;
    }
}
