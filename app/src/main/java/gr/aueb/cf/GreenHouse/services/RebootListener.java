//package gr.aueb.cf.agronitor.services;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//
//public class RebootListener extends BroadcastReceiver {
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
//            Intent serviceIntent = new Intent(context, AlarmsService.class);
//            context.startForegroundService(serviceIntent);
//        }
//    }
//}
