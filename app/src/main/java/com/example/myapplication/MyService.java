package com.example.myapplication;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationManagerCompat;

public class MyService extends Service {
    NotificationManager Notifi_M;
    ServiceThread thread;
    Notification Notifi ;
    String real_text;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notifi_M = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel();
        myServiceHandler handler = new myServiceHandler();
        real_text= intent.getStringExtra("result");

        thread = new ServiceThread(handler);
        thread.start();

        return START_STICKY;
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("777",
                    name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the
            // or other notification behaviors after this
            NotificationManager notificationManager = (NotificationManager)
                    getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }

    //서비스가 종료될 때 할 작업

    public void onDestroy() {
        thread.stopForever();
        thread = null;//쓰레기 값을 만들어서 빠르게 회수하라고 null을 넣어줌.
    }

    class myServiceHandler extends Handler {
        private Object myServiceHandler;

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void handleMessage(android.os.Message msg) {
            Intent intent = new Intent(MyService.this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(MyService.this, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);


            Notifi = new Notification.Builder(getApplicationContext(),"777")
                    .setContentTitle("Content Title")
                    .setContentText(real_text)
                    .setSmallIcon(R.drawable.logo)
                    .setTicker("알림!!!")
                    .setContentIntent(pendingIntent)
                    .build();

            //소리추가
            Notifi.defaults = Notification.DEFAULT_SOUND;

            //알림 소리를 한번만 내도록
            Notifi.flags = Notification.FLAG_ONLY_ALERT_ONCE;

            //확인하면 자동으로 알림이 제거 되도록
            Notifi.flags = Notification.FLAG_AUTO_CANCEL;
            Log.d("######################",real_text);
            if( !real_text.equals("")) {
            Notifi_M.notify( 777 , Notifi);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

                notificationManager.notify(777, Notifi);

                //토스트 띄우기
                Toast.makeText(MyService.this, "뜸?", Toast.LENGTH_LONG).show();
            }
        }
    };
}


