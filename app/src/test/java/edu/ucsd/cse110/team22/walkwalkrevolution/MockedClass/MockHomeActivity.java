package edu.ucsd.cse110.team22.walkwalkrevolution.MockedClass;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import edu.ucsd.cse110.team22.walkwalkrevolution.HomePageActivity;

public class MockHomeActivity extends HomePageActivity {

    public void setBinder(IBinder binder){
        iBinder = binder;
    }

    protected ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };


}
