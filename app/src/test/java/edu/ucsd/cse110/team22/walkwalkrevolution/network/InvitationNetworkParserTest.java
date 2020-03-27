package edu.ucsd.cse110.team22.walkwalkrevolution.network;

import android.accounts.NetworkErrorException;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import edu.ucsd.cse110.team22.walkwalkrevolution.UsersClass.Team;
import edu.ucsd.cse110.team22.walkwalkrevolution.UsersClass.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class InvitationNetworkParserTest {

    private static final HTTPRequestManager mockHTTPRequestManager = mock(HTTPRequestManager.class);

    private static final User mockUser = mock(User.class);

    private static final Team mockTeam = mock(Team.class);

    private InvitationNetworkParser classUnderTest = new InvitationNetworkParser(mockHTTPRequestManager);

    @Test
    public void testSendInvitationInvalidArguments() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> classUnderTest.sendInvitationByTeamToUser(mockTeam,null));
        assertEquals("user must not be null", ex.getMessage());
        ex = assertThrows(IllegalArgumentException.class, () -> classUnderTest.sendInvitationByTeamToUser(null, mockUser));
        assertEquals("team must not be null", ex.getMessage());
    }

    @Test
    public void testSendInvitationURL() throws IOException, NetworkErrorException {
        final String url = "https://35.223.131.33/send_invitation/TeamName/UserEmail%40gmail.com/UserName";
        when(mockUser.getName()).thenReturn("UserName");
        when(mockUser.getEmail()).thenReturn("UserEmail@gmail.com");
        when(mockTeam.getName()).thenReturn("TeamName");
        when(mockHTTPRequestManager.get(anyString())).thenReturn("YEE");
        classUnderTest.sendInvitationByTeamToUser(mockTeam, mockUser);
        verify(mockHTTPRequestManager, times(1)).get(url);
    }

    @Test
    public void testGetInvitationByUserInvalidArguments() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> classUnderTest.getInvitationsByUser(null));
        assertEquals("user must not be null", ex.getMessage());
    }

    @Test
    public void testGetInvitationByUser() throws IOException, NetworkErrorException {
        final String url = "https://35.223.131.33/get_invitation/UserEmail%40gmail.com";
        when(mockUser.getName()).thenReturn("UserName");
        when(mockUser.getEmail()).thenReturn("UserEmail@gmail.com");
        when(mockHTTPRequestManager.get(anyString())).thenReturn("Invitations");
        String returnVal = classUnderTest.getInvitationsByUser(mockUser);
        assertEquals("Invitations", returnVal);
        verify(mockHTTPRequestManager, times(1)).get(url);
    }

    @Test
    public void testRemoveInvitationInvalidArguments() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> classUnderTest.sendInvitationByTeamToUser(mockTeam,null));
        assertEquals("user must not be null", ex.getMessage());
        ex = assertThrows(IllegalArgumentException.class, () -> classUnderTest.sendInvitationByTeamToUser(null, mockUser));
        assertEquals("team must not be null", ex.getMessage());
    }

    @Test
    public void testRemoveInvitation() throws IOException, NetworkErrorException {
        final String url = "https://35.223.131.33/remove_invitation/TeamName/UserEmail%40gmail.com";
        when(mockUser.getName()).thenReturn("UserName");
        when(mockUser.getEmail()).thenReturn("UserEmail@gmail.com");
        when(mockTeam.getName()).thenReturn("TeamName");
        classUnderTest.removeInvitation(mockTeam, mockUser);
        verify(mockHTTPRequestManager, times(1)).get(url);
    }
}
