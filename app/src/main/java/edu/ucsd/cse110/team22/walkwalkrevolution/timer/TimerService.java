package edu.ucsd.cse110.team22.walkwalkrevolution.timer;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import edu.ucsd.cse110.team22.walkwalkrevolution.StepViewTimerActivity;

public class TimerService extends Service {
    private StepViewTimerActivity activity;
    private final IBinder iBinder = new LocalService();
    private boolean stopUpdate=false;
    Thread thread;

    public TimerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    public class LocalService extends Binder {
        public TimerService getService(){
            return TimerService.this;
        }
    }

    final class MyThread implements Runnable{
        int startId;
        private Timer timer;

        public MyThread(int startId){
            timer = new Timer();
            this.startId=startId;
        }
        @Override
        public void run(){
            timer.start();
            while (!stopUpdate) {
                synchronized (this) {
                    try {
                        wait(1000);
                        activity.runOnUiThread(() -> activity.setTime(timer.toString()));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        System.out.println("Service Started");
        return super.onStartCommand(intent,flags,startId);
    }

    @Override
    public void onDestroy() {
        thread.interrupt();
        super.onDestroy();
    }

    public void setActivity(StepViewTimerActivity activity){
        this.activity = activity;
        asyncUpdateTimer();
    }

    public void setStopUpdate(boolean arg){
        this.stopUpdate = arg;
    }

    public void asyncUpdateTimer(){
        thread = new Thread(new TimerService.MyThread(50));
        thread.start();
    }
}