package edu.ucsd.cse110.team22.walkwalkrevolution.network;

import android.accounts.NetworkErrorException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;

import edu.ucsd.cse110.team22.walkwalkrevolution.UsersClass.Team;
import edu.ucsd.cse110.team22.walkwalkrevolution.UsersClass.User;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * This parser that handles the team related communication between the application instance and the cloud service.
 */
public class TeamNetworkParser extends AbstractNetworkParser {

    private final HTTPRequestManager httpRequestManager;

    public TeamNetworkParser(HTTPRequestManager httpRequestManager) {
        this.httpRequestManager = httpRequestManager;
    }

    /**
     * Creates a team called user.getName() with the user as a member.
     * @param user the user who creates the group
     * @return the team name
     */
    public String createTeam(final User user) throws NetworkErrorException, UnsupportedEncodingException {
        checkArgument(user != null, "user must not be null");
        final String path = MessageFormat.format("{0}/{1}/{2}/{3}/{4}",
                entryUrl,
                "add",
                URLEncoder.encode(user.getName(), "UTF-8"),
                URLEncoder.encode(user.getEmail(), "UTF-8"),
                URLEncoder.encode(user.getName(), "UTF-8"));
        try {
            httpRequestManager.get(path);
        } catch (final IOException e) {
            throw new NetworkErrorException(e.getMessage());
        }
        return user.getName();
    }

    /**
     * Retrieves the team with the input user
     * @param user the user
     * @return the team as JSON (to be parsed using Team.getTeamFromJson)
     * @throws UnsupportedEncodingException
     * @throws NetworkErrorException
     * @throws TeamNotPresentException
     */
    public String retrieveTeamByUser(final User user) throws UnsupportedEncodingException,
            NetworkErrorException, TeamNotPresentException {
        checkArgument(user != null, "user must not be null");
        final String path = MessageFormat.format("{0}/{1}/{2}",
                entryUrl,
                "retrieve_team",
                URLEncoder.encode(user.getEmail(), "UTF-8"));
        try {
            return httpRequestManager.get(path);
        } catch(final FileNotFoundException e) {
            throw new TeamNotPresentException(e.getMessage());
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
    public boolean userHasTeam(final User user) throws UnsupportedEncodingException {
        checkArgument(user != null, "user must not be null");
        final String path = MessageFormat.format("{0}/{1}/{2}",
                entryUrl,
                "retrieve_team",
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

    /**
     * Adds a user to a team.
     * @param team the team
     * @param user the user
     * @throws UnsupportedEncodingException
     * @throws NetworkErrorException
     */
    public void addUserToTeam(final Team team, final User user) throws UnsupportedEncodingException, NetworkErrorException {
        checkArgument(user != null, "user must not be null");
        final String path = MessageFormat.format("{0}/{1}/{2}/{3}/{4}",
                entryUrl,
                "add",
                URLEncoder.encode(team.getName(), "UTF-8"),
                URLEncoder.encode(user.getEmail(), "UTF-8"),
                URLEncoder.encode(user.getName(), "UTF-8"));
        try {
            httpRequestManager.get(path);
        } catch (final IOException e) {
            throw new NetworkErrorException(e.getMessage());
        }
    }
}
