package edu.ucsd.cse110.team22.walkwalkrevolution.network;

import android.accounts.NetworkErrorException;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import edu.ucsd.cse110.team22.walkwalkrevolution.UsersClass.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TeamNetworkParserTest {

    private static final HTTPRequestManager mockHTTPRequestManager = mock(HTTPRequestManager.class);

    private static final User mockUser = mock(User.class);

    private TeamNetworkParser classUnderTest = new TeamNetworkParser(mockHTTPRequestManager);

    @Test
    public void testCreateTeam() throws IOException, NetworkErrorException {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> classUnderTest.createTeam(null));
        assertEquals("user must not be null", ex.getMessage());
        when(mockHTTPRequestManager.get(anyString())).thenReturn("YEE");
        when(mockUser.getName()).thenReturn("TeamName");
        when(mockUser.getEmail()).thenReturn("UserEmail@gmail.com");
        assertEquals("TeamName", classUnderTest.createTeam(mockUser));
    }

    @Test
    public void testCreateTeamURL() throws IOException, NetworkErrorException {
        final String url = "https://34.69.118.71/add/TeamName/UserEmail%40gmail.com/TeamName";
        when(mockUser.getName()).thenReturn("TeamName");
        when(mockUser.getEmail()).thenReturn("UserEmail@gmail.com");
        when(mockHTTPRequestManager.get(anyString())).thenReturn("YEE");
        classUnderTest.createTeam(mockUser);
        //verify(mockHTTPRequestManager, times(1)).get(url);
    }

    @Test
    public void testRetrieveTeamByUser() throws IOException, NetworkErrorException {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> classUnderTest.retrieveTeamByUser(null));
        assertEquals("user must not be null", ex.getMessage());
        when(mockHTTPRequestManager.get(anyString())).thenReturn("TeamData");
        when(mockUser.getName()).thenReturn("TeamName");
        when(mockUser.getEmail()).thenReturn("UserEmail@gmail.com");
        assertEquals("TeamData", classUnderTest.retrieveTeamByUser(mockUser));
    }

    @Test
    public void testRetrieveTeamByUserNetworkException() throws IOException, NetworkErrorException {
        IOException mockIOException = mock(IOException.class);
        when(mockHTTPRequestManager.get(anyString())).thenThrow(mockIOException);
        assertThrows(NetworkErrorException.class, () -> classUnderTest.retrieveTeamByUser(mockUser));
    }
}
