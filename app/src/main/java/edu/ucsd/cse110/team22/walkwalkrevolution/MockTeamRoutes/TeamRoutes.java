package edu.ucsd.cse110.team22.walkwalkrevolution.MockTeamRoutes;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.team22.walkwalkrevolution.Route.Route;

public class TeamRoutes {

    private List<Route> teamRoutes;


    public TeamRoutes(){
        Route testRouteAlice = new Route("Alice Brown", "Downtown SD", "00:27:54",
                "USS Midway", "great city", false, "flat",
                "Out-and-Back", "streets", "Flat Surface", "Easy",
                "4600", "7");

        Route testRouteBill = new Route("Bill C", "La Jolla Shores", "00:38:43",
                "Scripps Pier", "perfect at sunset", false, "flat",
                "loop", "streets", "Flat Surface", "Moderate",
                "3600", "6");

        Route testRouteGary = new Route("Gary G", "Lake Griswold", "01:12:09",
                "Lake Cafe", "very fun", true, "hilly",
                "loop", "trails", "Even Surface", "Difficult",
                "8900", "11");

        teamRoutes = new ArrayList<>();

        teamRoutes.add(testRouteAlice);
        teamRoutes.add(testRouteBill);
        teamRoutes.add(testRouteGary);
    }

    public List<Route> getTeamRoutes(){
        return this.teamRoutes;
    }
}
