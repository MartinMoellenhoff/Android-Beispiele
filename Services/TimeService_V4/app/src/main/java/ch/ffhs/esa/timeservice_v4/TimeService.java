package ch.ffhs.esa.timeservice_v4;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class TimeService extends Service {
    public static final String TAG = "TimeService";
    private Timer myTimer = null;
    private int counter = 0;
    private long time = 0;

    private Handler timeCallbackHandler = null;
    private final IBinder timeServiceBinder = new TimeServiceBinder();


    /** inner class implements the broadcast timer*/
    private class TimeServiceTimerTask extends TimerTask {
        private static final String TAG = TimeService.TAG + "::TimerTask";
        public void run() {
            time   = + System.currentTimeMillis();
            counter++;
            Message msg = new Message();
            Bundle bundle = new Bundle();

            bundle.putLong("TIME", System.currentTimeMillis());
            bundle.putInt("COUNTER", counter);
            bundle.putInt("PID", android.os.Process.myPid());
            bundle.putInt("TID", android.os.Process.myTid());
            Log.d(TAG, "counter = " + counter + "PID; " + android.os.Process.myPid() + "; TID " + android.os.Process.myTid());
            msg.setData(bundle);
            if(timeCallbackHandler != null){
                timeCallbackHandler.sendMessage(msg);
            }
        }
    };


    public TimeService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        Log.d(TAG, "PID" + android.os.Process.myPid() + "; TID " + android.os.Process.myTid());
        myTimer = new Timer("myTimer");
    }


    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        myTimer.scheduleAtFixedRate( new TimeServiceTimerTask(), 0, 1000);
        return timeServiceBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.d(TAG, "onRebind");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind");
        timeCallbackHandler = null;
        myTimer.cancel();
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        myTimer = null;
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    public class TimeServiceBinder extends Binder {
        private final String TAG = TimeService.TAG + "Binder";

        public int getCounter() {
            Log.d(TAG, "getCounter");
            return counter;
        }

        public long getTime() {
            Log.d(TAG, "getTime");
            return time;
        }

        public int getPID(){
            Log.d(TAG, "getPID");
            return android.os.Process.myPid();
        }


        public int getTID(){
            Log.d(TAG, "getTID");
            return android.os.Process.myTid();
        }


        public void sendCounter() {
            Log.d(TAG, "sendCounter");
        }

        public void sendTime() {
            Log.d(TAG, "sendTime");
        }

        public void setActivityCallbackHandler(Handler callback){
            Log.d(TAG, "setActivityCallbackHandler");
            timeCallbackHandler = callback;
        }
    }

}
