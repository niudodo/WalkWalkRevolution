package edu.ucsd.cse110.team22.walkwalkrevolution;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.ucsd.cse110.team22.walkwalkrevolution.MockedClass.MockSharedPreference;
import edu.ucsd.cse110.team22.walkwalkrevolution.Storage.StorageHandler;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;


public class StorageHandlerUnitTest {
    private Context mockContext;

    private MockSharedPreference mockPrefs;
    private MockSharedPreference.Editor mockPrefsEditor;

    @Before
    public void before() {
        mockContext = mock(Context.class);
        mockPrefs = new MockSharedPreference();
        mockPrefsEditor = mockPrefs.edit();

        Mockito.when(mockContext.getSharedPreferences(anyString(), anyInt())).thenReturn(mockPrefs);
    }

    @Test
    public void testStorage() {
        StorageHandler storageHandler = StorageHandler.getStorage(mockContext);

        Long number = 150L;
        storageHandler.saveItem("number", number);
        assertEquals(storageHandler.retrieveItem("number", Long.class), number);

        List<String> complexObject = new ArrayList<>(
                Arrays.asList("Ciao", "How are you", "Everything fine?")
        );
        storageHandler.saveItem("list", complexObject);
        assertEquals(storageHandler.retrieveItem("list", complexObject.getClass()), complexObject);
        assertNotNull(storageHandler.retrieveItem("number", Long.class));
    }

    @Test
    public void testNull(){
        StorageHandler storageHandler = StorageHandler.getStorage(mockContext);
        assertNull(storageHandler.retrieveItem("someStringINeverUsed", Number.class));
    }
}
