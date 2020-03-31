package edu.ucsd.cse110.team22.walkwalkrevolution.Route;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.team22.walkwalkrevolution.GoogleMap.GoogleMapAdapter;
import edu.ucsd.cse110.team22.walkwalkrevolution.GoogleMap.MapAdapter;
import edu.ucsd.cse110.team22.walkwalkrevolution.R;
import edu.ucsd.cse110.team22.walkwalkrevolution.Storage.StorageHandler;
import edu.ucsd.cse110.team22.walkwalkrevolution.UsersClass.User;


public class RouteActivity extends AppCompatActivity {
    public final static String ROUTE_KEY_PREFIX = "entryNo";
    public final static int ROUTE_KEY_PREFIX_INDEX=7;
    ListView lv;
    ListView teamRoutesView;

    private List<Route> routeList;
    private List<Route> teamRouteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.route_page);

        //generate mocked routes

        routeList = User.myUser.getMyRoutes();
        /*Route route1 = new Route("Jennie Zhang", "Downtown SD", "00:27:54",
                "USS Midway", "great city", false, "flat",
                "Out-and-Back", "streets", "Flat Surface", "Easy",
                "4600", "7");
        routeList.add(route1);*/

        teamRouteList = new ArrayList<>();
        /*Route testRoute1 = new Route("Alice Brown", "Downtown SD", "00:27:54",
                "USS Midway", "great city", false, "flat",
                "Out-and-Back", "streets", "Flat Surface", "Easy",
                "4600", "7");
        teamRouteList.add(testRoute1);*/

        /* get team routes from db and assign it to teamRouteList above before proceeding */
        lv  = findViewById(R.id.lv);
        RouteAdapter adapter = new RouteAdapter(this,R.layout.route_entry,routeList);
        lv.setAdapter(adapter);

        //display of team routes

        teamRoutesView = findViewById(R.id.teamRoutes);
        RouteAdapter adapterTeam = new RouteAdapter(this,R.layout.route_entry,teamRouteList);
        teamRoutesView.setAdapter(adapterTeam);

        ListUtil.setDynamicHeight(lv);
        ListUtil.setDynamicHeight(teamRoutesView);

        //Button to save a new walk from routes page
        ImageButton launchRouteActivity = findViewById(R.id.toSaveScreen);
        launchRouteActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchActivity();
            }
        });
    }

    public void launchActivity(){
        Intent intent = new Intent(this, SaveScreen.class);
        startActivity(intent);
    }

    public void loadData(StorageHandler s, String[] keyList, List<Route> routeList, String username){
        for (String key : keyList) {
            //The for loop includes other keys that is not Route.class
            if(key!=null&&key.length()>=ROUTE_KEY_PREFIX_INDEX) {
                String prefix = key.substring(0, ROUTE_KEY_PREFIX_INDEX);
                if (prefix.compareTo(ROUTE_KEY_PREFIX) == 0) {
                    Route route = s.retrieveItem(key, Route.class);
                    //so we only add the routes that are not from the user themselves
                    if(route.username == username || route.username == null) {
                        routeList.add(route);
                    }
                }
            }
        }
    }

    public void loadTeamData(StorageHandler s, String[] keyList, List<Route> teamRouteList, String username){
        for (String key : keyList) {
            //The for loop includes other keys that is not Route.class
            if(key!=null&&key.length()>=ROUTE_KEY_PREFIX_INDEX) {
                String prefix = key.substring(0, ROUTE_KEY_PREFIX_INDEX);
                if (prefix.compareTo(ROUTE_KEY_PREFIX) == 0) {
                    Route route = s.retrieveItem(key, Route.class);
                    //so we only add the routes that are not from the user themselves
                    if(route.username != username && route.username != null) {
                        teamRouteList.add(route);
                    }
                }
            }
        }
    }


    public static class ListUtil {
        public static void setDynamicHeight(ListView mListView) {
            ListAdapter mListAdapter = mListView.getAdapter();
            if (mListAdapter == null) {
                // when adapter is null
                return;
            }
            int height = 0;
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(mListView.getWidth(), View.MeasureSpec.UNSPECIFIED);
            for (int i = 0; i < mListAdapter.getCount(); i++) {
                View listItem = mListAdapter.getView(i, null, mListView);
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                height += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = mListView.getLayoutParams();
            params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount() - 1));
            mListView.setLayoutParams(params);
            mListView.requestLayout();
        }
    }

    public void mockTeamRoutes() {
        StorageHandler sh = StorageHandler.getStorage(this);
        SharedPreferences sharedPreferences = getSharedPreferences("database", MODE_PRIVATE);

        Route testRoute1 = new Route("Alice Brown", "Downtown SD", "00:27:54",
                "USS Midway", "great city", false, "flat",
                "Out-and-Back", "streets", "Flat Surface", "Easy",
                "4600", "7");
        int temp = 100000;
        testRoute1.id = ROUTE_KEY_PREFIX + temp;
        if (!sharedPreferences.contains(testRoute1.id))
            sh.saveItem(testRoute1.id, testRoute1);

        temp++;
        Route testRoute2 = new Route("Bill C", "La Jolla Shores", "00:38:43",
                "Scripps Pier", "perfect at sunset", false, "flat",
                "loop", "streets", "Flat Surface", "Moderate",
                "3600", "6");
        testRoute2.id = ROUTE_KEY_PREFIX + temp;
        if (!sharedPreferences.contains(testRoute2.id))
            sh.saveItem(testRoute2.id, testRoute2);

        temp++;
        Route testRoute3 = new Route("Gary G", "Lake Griswold", "01:12:09",
                "Lake Cafe", "very fun", true, "hilly",
                "loop", "trails", "Even Surface", "Difficult",
                "8900", "11");
        testRoute3.id = ROUTE_KEY_PREFIX + temp;
        if (!sharedPreferences.contains(testRoute3.id))
            sh.saveItem(testRoute3.id, testRoute3);
    }

    public void openMap(String location){
        MapAdapter map = new GoogleMapAdapter();
        map.openLocation(location,this);
    }

}
