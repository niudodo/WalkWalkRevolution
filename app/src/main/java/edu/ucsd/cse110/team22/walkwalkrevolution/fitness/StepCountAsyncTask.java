package edu.ucsd.cse110.team22.walkwalkrevolution.fitness;

import android.app.ProgressDialog;
import android.os.AsyncTask;

/**
 * StepCountAsyncTask extends AsyncTask and it is responible to update stepCounts.
 * If it is in testMode then the task will finish within testLimit runs.
 * The asyncTask doesn't call fitnessService.updateStepCount() unless the setup is finish
 *
 */
public class StepCountAsyncTask extends AsyncTask<String, String, String>{
    protected String resp;
    protected String TAG = "StepCountAsyncTask";
    protected int lapse=5000;
    protected boolean fitSetUp = false;
    protected StepViewActivity activity;
    ProgressDialog progressDialog;

    @Override
    protected String doInBackground(String... params) {
        while(!isCancelled()){
            System.out.println(TAG + ": doInBackground");
            try{
                Thread.sleep(lapse);
                if(fitSetUp)
                    publishProgress();
            } catch (Exception e) {
                e.printStackTrace();
                resp = "ERROR";
            }
        }
        return resp;
    }
    @Override
    protected void onProgressUpdate(String... text){
        activity.fitnessService.updateStepCount(activity);
    }
    @Override
    protected void onPostExecute(String result){}
    @Override
    protected void onPreExecute(){

    }

    public void setFitSetUp(boolean fitSetUp) {
        this.fitSetUp = fitSetUp;
    }
    public void setLapse(int lapse){
        this.lapse=lapse;
    }
    public void setActivity(StepViewActivity activity){
        this.activity=activity;
    }
}