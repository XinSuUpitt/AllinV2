package com.smartdo.suxin.allinv2.Util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.smartdo.suxin.allinv2.Remind;
import com.smartdo.suxin.allinv2.Reminder_edit;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by suxin on 9/9/16.
 */
public class AlarmUtil {
    public static void setAlarm(Context context, Remind remind) {
        Calendar calendar = Calendar.getInstance();
        Date date = remind.remindDate;
        //calender will contain the current time
        if (date.compareTo(calendar.getTime()) < 0) { // check if date is small than current time
            // we only fire alarm when date is in the future
            return;
        }

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(Reminder_edit.KEY_REMIND, remind);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, date.getTime(), pendingIntent);
        // will wake up the service
    }
}
