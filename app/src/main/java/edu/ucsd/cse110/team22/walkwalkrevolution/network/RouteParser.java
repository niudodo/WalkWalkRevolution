package edu.ucsd.cse110.team22.walkwalkrevolution.network;

import android.accounts.NetworkErrorException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;

import edu.ucsd.cse110.team22.walkwalkrevolution.Route.Route;
import edu.ucsd.cse110.team22.walkwalkrevolution.UsersClass.User;

import static com.google.common.base.Preconditions.checkArgument;

public class RouteParser extends AbstractNetworkParser{
    private final HTTPRequestManager httpRequestManager;

    public RouteParser(HTTPRequestManager httpRequestManager) {
        this.httpRequestManager = httpRequestManager;
    }

    /**
     * Creates a team called user.getName() with the user as a member.
     * @param user the user who creates the group
     * @return the team name
     */
    public String createRoute(final User user, final Route route) throws NetworkErrorException, UnsupportedEncodingException {
        checkArgument(user != null, "user must not be null");
        final String path = MessageFormat.format("{0}/{1}/{2}/{3}/{4}",
                entryUrl,
                "add",
                URLEncoder.encode(user.getName(), "UTF-8"),
                URLEncoder.encode(user.getEmail(), "UTF-8"),

                //we save the route under its name getName in Route has been implemented, returns a the name as String
                URLEncoder.encode(route.getName(), "UTF-8"));
        try {
            httpRequestManager.get(path);
        } catch (final IOException e) {
            throw new NetworkErrorException(e.getMessage());
        }
        return user.getName();
    }

    /**
     * Retrieves the route of the input user
     * @param user the user
     * @return the team as JSON (to be parsed using Team.getTeamFromJson)
     * @throws UnsupportedEncodingException
     * @throws NetworkErrorException
     * @throws RouteNotPresentException
     */
    public String retrieveRoutesByUser(final User user) throws UnsupportedEncodingException,
            NetworkErrorException, RouteNotPresentException {
        checkArgument(user != null, "user must not be null");
        final String path = MessageFormat.format("{0}/{1}/{2}",
                entryUrl,

                //'retrieve_route' I assume has to be implemented in the server side

                "retrieve_route",
                URLEncoder.encode(user.getEmail(), "UTF-8"));
        try {
            return httpRequestManager.get(path);
        } catch(final FileNotFoundException e) {
            throw new RouteNotPresentException(e.getMessage());
        } catch (final IOException e) {
            e.printStackTrace();
            throw new NetworkErrorException(e.getMessage());
        }
    }

    /**
     * @param user the user
     * @return true if the user has a team, otherwise false
     * @throws UnsupportedEncodingException
     */
    public boolean userHasRoute(final User user) throws UnsupportedEncodingException {
        checkArgument(user != null, "user must not be null");
        final String path = MessageFormat.format("{0}/{1}/{2}",
                entryUrl,
                "retrieve_route",
                URLEncoder.encode(user.getEmail(), "UTF-8"));
        try {
            httpRequestManager.get(path);
            return true;
        } catch(final FileNotFoundException e) {
            return false;
        } catch (final IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
