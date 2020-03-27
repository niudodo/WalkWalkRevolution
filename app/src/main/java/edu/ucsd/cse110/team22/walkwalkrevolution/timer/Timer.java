package edu.ucsd.cse110.team22.walkwalkrevolution.timer;


import androidx.annotation.NonNull;

import com.google.common.base.Stopwatch;
import com.google.common.base.Ticker;

import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class Timer {
    private final Stopwatch stopwatch =  Stopwatch.createStarted(
            new Ticker() {
                public long read() {
                    return android.os.SystemClock.elapsedRealtimeNanos();
                }
            }
    );

    public void start() {
        stopwatch.stop();
        stopwatch.start();
    }

    public void stop() {
        stopwatch.stop();
    }

    public long secondsPassed() {
        return stopwatch.elapsed(TimeUnit.SECONDS);
    }

    @NonNull
    @Override
    public String toString() {
        long seconds = secondsPassed();
        long s = seconds % 60;
        long m = (seconds / 60) % 60;
        long h = (seconds / (60 * 60)) % 24;
        return String.format(Locale.US, "%02d:%02d:%02d", h,m,s);
    }
}
