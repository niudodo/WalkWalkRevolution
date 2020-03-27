package edu.ucsd.cse110.team22.walkwalkrevolution;
import org.junit.Test;

import static org.junit.Assert.*;
import org.mockito.Mockito;

import edu.ucsd.cse110.team22.walkwalkrevolution.timer.Timer;

public class TimerUnitTest {

    @Test
    public void testCorrectFormatting(){
        Timer timer = Mockito.mock(Timer.class);
        Mockito.when(timer.toString()).thenCallRealMethod();

        //a day - one second
        Mockito.when(timer.secondsPassed()).thenReturn(86399L);
        assertEquals(timer.toString(), "23:59:59");

        Mockito.when(timer.secondsPassed()).thenReturn(59L);
        assertEquals(timer.toString(), "00:00:59");

        //more than a day
        Mockito.when(timer.secondsPassed()).thenReturn(86401L);
        assertEquals(timer.toString(), "00:00:01");

    }
}
