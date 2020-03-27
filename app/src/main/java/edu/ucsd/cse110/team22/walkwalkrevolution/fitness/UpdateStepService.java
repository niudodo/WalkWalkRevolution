package edu.ucsd.cse110.team22.walkwalkrevolution.fitness;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class UpdateStepService extends Service {
    private FitnessService fitnessService;
    private StepViewActivity activity;
    private final IBinder iBinder = new LocalService();
    private boolean stopUpdate=false;
    private Thread thread;

    /**
     * Default constructor of Update Step Service
     */
    public UpdateStepService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    /**
     * inner class use for Update Step Service, LocalService will be used to return
     * a reference to the UpdateStepService
     */
    public class LocalService extends Binder {
        public UpdateStepService getService(){
            return UpdateStepService.this;
        }
    }

    /**
     * Inner class use to create thread that the service doesn't interrupt main
     * or UI thread
     */
    final class MyThread implements Runnable{
        int startId;
        public MyThread(int startId, FitnessService fitnessService){
            this.startId=startId;
        }
        @Override
        public void run(){
            System.out.println(stopUpdate);
            while (!stopUpdate) {
                synchronized (this) {
                    try {
                        wait(5000);
                        activity.runOnUiThread(new Runnable(){
                            public void run(){
                                fitnessService.updateStepCount(activity);
                            };
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            stopSelf();
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        System.out.println("Service Started");
        return super.onStartCommand(intent,flags,startId);
    }

    @Override
    public void onDestroy(){
        thread.interrupt();
        super.onDestroy();
    }

    public void setFitnessService(FitnessService fitnessService){
        this.fitnessService = fitnessService;
        AsyncUpdateStep();
    }

    public void setActivity(StepViewActivity activity){
        this.activity = activity;
    }

    public void setStopUpdate(boolean arg){
        this.stopUpdate = arg;
    }

    public void AsyncUpdateStep(){
        thread = new Thread(new MyThread(10, this.fitnessService));
        thread.start();
    }

}