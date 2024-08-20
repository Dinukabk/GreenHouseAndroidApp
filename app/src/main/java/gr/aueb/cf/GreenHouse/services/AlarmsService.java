//package gr.aueb.cf.agronitor.services;
//
//import android.app.Notification;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.Service;
//import android.content.Intent;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.IBinder;
//import android.util.Log;
//
//import androidx.core.app.NotificationBuilderWithBuilderAccessor;
//
//import gr.aueb.cf.agronitor.R;
//
//public class AlarmsService extends Service {
//    String userId = "";
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (true) {
//                    Log.e("Alarms service", "Service is running...");
//                    try {
//                        Thread.sleep(1800000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }).start();
//
//        final String CHANNELID = "AlarmsService ID";
//        NotificationChannel notificationChannel = new NotificationChannel(
//                CHANNELID,
//                CHANNELID,
//                NotificationManager.IMPORTANCE_LOW
//        );
//        getSystemService(NotificationManager.class).createNotificationChannel(notificationChannel);
//        Notification.Builder notification = new Notification.Builder(this, CHANNELID)
//                .setContentText("Agronitor is running in the background")
//                .setContentTitle("Alarms service enabled")
//                .setSmallIcon(R.drawable.ic_launcher_background);
//        startForeground(1001, notification.build());
//        return super.onStartCommand(intent, flags, startId);
//    }
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    public AlarmsService() {
//
//    }
//}