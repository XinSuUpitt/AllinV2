package com.smartdo.suxin.allinv2.Util;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import com.smartdo.suxin.allinv2.R;
import com.smartdo.suxin.allinv2.Remind;
import com.smartdo.suxin.allinv2.Reminder_edit;

/**
 * Created by suxin on 9/9/16.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Toast.makeText(context, "alarm!", Toast.LENGTH_LONG).show();

        //final int notificationId = 100; // use to cancel notification
        Remind remind = intent.getParcelableExtra(Reminder_edit.KEY_REMIND);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_notifications_active_red_a700_18dp).setContentTitle(context.getString(R.string.alarm)).setContentText(context.getString(R.string.alarm_content));

        // creates and explicit intent for an Activity in your app
        Intent resultIntent = new Intent(context, Reminder_edit.class);
        resultIntent.putExtra(Reminder_edit.KEY_REMIND, remind);
        resultIntent.putExtra(Reminder_edit.KEY_NOTIFICATION_ID, notificationId(context));

        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, Reminder_edit.getAlarmId(context), resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        //        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        //        stackBuilder.addParentStack(Reminder_edit.class);
        //        stackBuilder.addNextIntent(resultIntent);


        //        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(100, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);
        builder.setDefaults(NotificationCompat.DEFAULT_SOUND);
        builder.setAutoCancel(true);

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // notification allows you to update the notification later on, like canceling it
        nm.notify(notificationId(context), builder.build());
    }

    public static int notificationId(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int notificationId = preferences.getInt("NOTIFICATION", 1);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("NOTIFICATION", notificationId + 1).apply();
        return notificationId;
    }
}
