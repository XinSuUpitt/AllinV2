package com.smartdo.suxin.allinv2;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.smartdo.suxin.allinv2.Util.AlarmReceiver;
import com.smartdo.suxin.allinv2.Util.DateUtil;
import com.smartdo.suxin.allinv2.Util.UIUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by suxin on 9/9/16.
 */
public class Reminder_edit extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    public static final String KEY_REMIND = "remind";
    public static final String KEY_REMIND_ID = "remind_id";
    public static final String KEY_NOTIFICATION_ID = "notification_id";

    private Remind remind;
    private Date remindDate;
    private ReminderListFragment reminderListFragment;


    @BindView(R.id.todo_detail_todo_edit) EditText remindEdit;
    @BindView(R.id.todo_detail_date) TextView dateTV;
    @BindView(R.id.todo_detail_time) TextView timeTV;
    @BindView(R.id.todo_detail_complete) CheckBox completeCB;
    @BindView(R.id.todo_delete) TextView deleteBtn;

    public static final int REQ_CODE_TODO_EDIT = 100;
    public ReminderListAdapter adapter;
    public List<Remind> reminds;
    public static final String REMINDS = "reminds";

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reminder);

        remind = getIntent().getParcelableExtra(KEY_REMIND);
        remindDate = remind != null ? remind.remindDate : null;

        setupUI();

        //cancelNotificationNeeded();
    }
    private void cancelNotificationNeeded() {
        int notificationId = getIntent().getIntExtra(KEY_NOTIFICATION_ID, -1);
        if (notificationId != -1) {
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).cancel(notificationId);
        }
    }

    private void setupUI() {
        setContentView(R.layout.reminder_activity_edit);
        ButterKnife.bind(this);

        if (remind != null) {
            remindEdit.setText(remind.text);
            UIUtil.setTextViewStrikeThrough(remindEdit, remind.done);
            completeCB.setChecked(remind.done);

            deleteBtn.setVisibility(View.VISIBLE);
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    delete();
                }
            });
        } else {
            deleteBtn.setVisibility(View.GONE);
        }

        if (remindDate != null) {
            dateTV.setText(DateUtil.dateToStringDate(remindDate));
            timeTV.setText(DateUtil.dateToStringTime(remindDate));
        } else {
            dateTV.setText(R.string.set_date);
            timeTV.setText(R.string.set_time);
        }

        setupDatePicker();
        setupCheckBox();
        setupSaveButton();
    }


    private void setupSaveButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.todo_detail_done);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAndExit();
            }
        });
    }

    private void saveAndExit() {
        if (remind == null) {
            remind = new Remind(remindEdit.getText().toString(), remindDate);
        } else {
            remind.text = remindEdit.getText().toString();
            remind.remindDate = remindDate;
            setAlarm(remind);
        }

        remind.done = completeCB.isChecked();



        Intent result = new Intent();
        result.putExtra(KEY_REMIND, remind);
//        if (remindDate != null) {
//            // fire alarm when saving the todo item
//            setAlarm(remind);
//        }
        setResult(Activity.RESULT_OK, result);
        finish();
    }

    private void setAlarm(Remind remind) {
        Calendar calendar = Calendar.getInstance();
        Date date = remind.remindDate;
        if (date.compareTo(calendar.getTime()) < 0) { // check if date is small than current time
            return;
        }

        if (remind.done) {
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).cancel(getAlarmId(this));
            return;
        }
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(Reminder_edit.this, AlarmReceiver.class);
        intent.putExtra(KEY_REMIND, remind);


        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, getAlarmId(this), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT >= 23) {
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, date.getTime(), pendingIntent);
        } else if (Build.VERSION.SDK_INT >= 16) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, date.getTime(), pendingIntent);
        }


        //alarmManager.set(AlarmManager.RTC_WAKEUP, date.getTime(), pendingIntent);
    }

    public static int getAlarmId(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int alarmId = preferences.getInt("ALARM", 1);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("ALARM", alarmId + 1).apply();
        return alarmId;
    }

    private void setupCheckBox() {
        completeCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                UIUtil.setTextViewStrikeThrough(remindEdit, b);
                remindEdit.setTextColor(b ? Color.GRAY : Color.WHITE);
            }
        });

        // use this wrapper to make it possible for users to click on the entire row
        // to change the check box
        View completeWrapper = findViewById(R.id.todo_detail_complete_wrapper);
        completeWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                completeCB.setChecked(!completeCB.isChecked());
            }
        });


    }

    private void setupDatePicker() {
        dateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = getCalenderFromRemindDate();
                Dialog dialog = new DatePickerDialog(Reminder_edit.this,
                        Reminder_edit.this,
                        c.get(Calendar.YEAR),
                        c.get(Calendar.MONTH),
                        c.get(Calendar.DAY_OF_MONTH));
                dialog.show();

            }
        });

        timeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = getCalenderFromRemindDate();
                Dialog dialog = new TimePickerDialog(
                        Reminder_edit.this,
                        Reminder_edit.this,
                        c.get(Calendar.HOUR_OF_DAY),
                        c.get(Calendar.MINUTE),
                        true);
                dialog.show();
            }
        });
    }

    private void delete() {
        Intent result = new Intent();
        result.putExtra(KEY_REMIND_ID, remind.id);
        setResult(Activity.RESULT_OK, result);
        finish();
    }


    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        // this method will be called after user has chosen date from the DatePickerDialog
        Calendar c = getCalenderFromRemindDate();
        c.set(year, month, day);

        remindDate = c.getTime();
        dateTV.setText(DateUtil.dateToStringDate(remindDate));
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        Calendar c = getCalenderFromRemindDate();
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);

        remindDate = c.getTime();
        timeTV.setText(DateUtil.dateToStringTime(remindDate));
    }

    public Calendar getCalenderFromRemindDate() {
        Calendar c = Calendar.getInstance();
        if (remindDate != null) {
            c.setTime(remindDate);
        }
        return c;
    }


}
