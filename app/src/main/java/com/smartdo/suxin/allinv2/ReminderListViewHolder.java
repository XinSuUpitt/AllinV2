package com.smartdo.suxin.allinv2;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.smartdo.suxin.allinv2.Util.BaseViewHolder;

import butterknife.BindView;

/**
 * Created by suxin on 9/10/16.
 */
public class ReminderListViewHolder extends BaseViewHolder {
    @BindView(R.id.reminder_layout) View reminderLayout;
    @BindView(R.id.main_list_item_text) TextView reminderText;
    @BindView(R.id.main_list_item_remindDate) TextView reminderDate;
    @BindView(R.id.main_list_item_check) CheckBox doneCheckBox;

    public ReminderListViewHolder(View view) {
        super(view);
        this.setIsRecyclable(false);
    }

}
