package com.example.myapplication;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class MyService extends Service {

    final static String ACTION_UPDATE_CNT = "UPDATE_CNT";
    final static String KEY_INT_FROM_SERVICE = "KEY_INT_FROM_SERVICE";


    final static String ACTION_STOP_THREAD = "ACTION_STOP_THREAD";
    final static String ACTION_STOP_THREAD2 = "ACTION_STOP_THREAD2";
    final static String ACTION_RESTART_THREAD = "ACTION_RESTART_THREAD";

    final static String TAG = "MyService";
    MyServiceReceiver myServiceReceiver;
    MyServiceThread myServiceThread;

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "onBind");
        return null;
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");
//        Toast.makeText(getApplicationContext(), "onCreate", Toast.LENGTH_LONG).show();
        myServiceReceiver = new MyServiceReceiver();

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
//        Toast.makeText(getApplicationContext(), "onStartCommand", Toast.LENGTH_LONG).show();


        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_STOP_THREAD);
        intentFilter.addAction(ACTION_RESTART_THREAD);
        intentFilter.addAction(ACTION_STOP_THREAD2);

        registerReceiver(myServiceReceiver, intentFilter);

        Log.e(TAG, String.valueOf(myServiceThread));

        if("startForeground".equals(intent.getAction())){
            startForegroundService();
        }
        else if(myServiceThread == null){
            Log.e(TAG, "Thread Maked");
            myServiceThread = new MyServiceThread();
            myServiceThread.start();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    public class MyServiceReceiver extends BroadcastReceiver {

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, "onReceive");
            String action = intent.getAction();

            if(action.equals(ACTION_RESTART_THREAD)){

//                Toast.makeText(getApplicationContext(), "thread restart", Toast.LENGTH_SHORT).show();
                int time = intent.getIntExtra("time", 0);
                int position = intent.getIntExtra("position", 0);
                myServiceThread.setPosition(position);
                myServiceThread.setTime(time);
                myServiceThread.setRunning(true);

            }

            else if(action.equals(ACTION_STOP_THREAD)){
//                Toast.makeText(getApplicationContext(), "thread stop", Toast.LENGTH_SHORT).show();
                int position = intent.getIntExtra("position", 0);

                stopForeground(STOP_FOREGROUND_REMOVE);

                if(position == myServiceThread.getPosition()){
                    myServiceThread.setRunning(false);
                }
            }

            else if(action.equals(ACTION_STOP_THREAD2)){
//                Toast.makeText(getApplicationContext(), "thread stop 2", Toast.LENGTH_SHORT).show();
                int position = intent.getIntExtra("position", 0);
                if(position == myServiceThread.getPosition()){
                    if(myServiceThread != null){
                        stopForeground(STOP_FOREGROUND_REMOVE);
                        myServiceThread.setRunning(false);
                    }
                }
            }

        }
    }

    private class MyServiceThread extends Thread{
        int time = 0;
        int position;
        private boolean running = false;

        public int getTime() {
            return this.time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public void setRunning(boolean running){
            this.running = running;
        }

        public int getPosition() {
            return this.position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        @Override
        public void run() {

            while (true){



                while (running){
                    try {

                        Thread.sleep(1000);
                        Intent intent = new Intent();
                        intent.setAction(ACTION_UPDATE_CNT);
                        intent.putExtra(KEY_INT_FROM_SERVICE, time);
                        intent.putExtra("position", position);
                        sendBroadcast(intent);

                        time++;
                        Log.d("Service", "time: " + time);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return;
                    }
                }

            }
        }
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
//        Toast.makeText(getApplicationContext(), "onDestroy", Toast.LENGTH_LONG).show();
        unregisterReceiver(myServiceReceiver);
        super.onDestroy();
    }


    /**
     * Foreground Service 메서드
     */
    private void startForegroundService(){




        // 포그라운드 서비스를 위한 notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("스톱워치");
        builder.setContentText("시간 측정 중...");



        Intent notificationIntent = new Intent(this, ActivityTimeRecord.class);

        // notification을 클릭했을 때 호출되는 인텐트
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        builder.setContentIntent(pendingIntent);

        // notification 채널을 만드는 로직
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
        }

        startForeground(1, builder.build());
    }





}
