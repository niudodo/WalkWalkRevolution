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
 * This parser that handles the invitation related communication between the application instance and the cloud service.
 */
public class InvitationNetworkParser extends AbstractNetworkParser {

    private final HTTPRequestManager httpRequestManager;

    public InvitationNetworkParser(HTTPRequestManager httpRequestManager) {
        this.httpRequestManager = httpRequestManager;
    }

    /**
     * Sends an invitation from a team to a new user.
     * @param team the team that sends the notification
     * @param user the final user
     * @throws UnsupportedEncodingException
     * @throws NetworkErrorException
     */
    public void sendInvitationByTeamToUser(Team team, User user) throws UnsupportedEncodingException, NetworkErrorException {
        checkArgument(team != null, "team must not be null");
        checkArgument(user != null, "user must not be null");
        final String path = MessageFormat.format("{0}/{1}/{2}/{3}/{4}",
                entryUrl,
                "send_invitation",
                URLEncoder.encode(team.getName(), "UTF-8"),
                URLEncoder.encode(user.getEmail(), "UTF-8"),
                URLEncoder.encode(user.getName(), "UTF-8"));
        try {
            httpRequestManager.get(path);
        } catch (final IOException e) {
            throw new NetworkErrorException(e.getMessage());
        }
    }

    /**
     * Returns the list of invitations for the user.
     * @param user the user
     * @return the list of invitations as JSON (to be parsed using Invitation.getInvitationsFromJson)
     * @throws UnsupportedEncodingException
     * @throws NetworkErrorException
     * @throws NoInvitationsForUserException
     */
    public String getInvitationsByUser(User user) throws UnsupportedEncodingException, NetworkErrorException, NoInvitationsForUserException {
        checkArgument(user != null, "user must not be null");
        final String path = MessageFormat.format("{0}/{1}/{2}",
                entryUrl,
                "get_invitation",
                URLEncoder.encode(user.getEmail(), "UTF-8"));
        try {
            return httpRequestManager.get(path);
        }
        catch(final FileNotFoundException e) {
            throw new NoInvitationsForUserException(e.getMessage());
        } catch (final IOException e) {
            throw new NetworkErrorException(e.getMessage());
        }
    }

    /**
     * Removes an invitation
     * @param team the team that sent the invitation
     * @param user the user that was invited
     * @throws UnsupportedEncodingException
     * @throws NetworkErrorException
     */
    public void removeInvitation(Team team, User user) throws UnsupportedEncodingException, NetworkErrorException {
        checkArgument(team != null, "team must not be null");
        checkArgument(user != null, "user must not be null");
        final String path = MessageFormat.format("{0}/{1}/{2}/{3}",
                entryUrl,
                "remove_invitation",
                URLEncoder.encode(team.getName(), "UTF-8"),
                URLEncoder.encode(user.getEmail(), "UTF-8"));
        try {
            httpRequestManager.get(path);
        } catch (final IOException e) {
            throw new NetworkErrorException(e.getMessage());
        }
    }
}
