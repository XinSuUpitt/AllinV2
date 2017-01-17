package com.smartdo.suxin.allinv2;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.smartdo.suxin.allinv2.Util.UIUtil;

import java.util.List;

/**
 * Created by suxin on 9/9/16.
 */
public class ReminderListAdapter extends RecyclerView.Adapter {

    private List<Remind> data;
    private ReminderListFragment reminderListFragment;

    public ReminderListAdapter(List<Remind> data, ReminderListFragment reminderListFragment) {
        this.data = data;
        this.reminderListFragment = reminderListFragment;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminder_main_list_item, parent, false);
        return new ReminderListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Remind remind = data.get(position);
        final ReminderListViewHolder reminderListViewHolder = (ReminderListViewHolder) holder;

        reminderListViewHolder.reminderText.setText(remind.text);
        reminderListViewHolder.reminderDate.setText(remind.remindDate == null ? "set remind date" : remind.remindDate.toString());
        reminderListViewHolder.doneCheckBox.setChecked(remind.done);
        UIUtil.setTextViewStrikeThrough(reminderListViewHolder.reminderText, remind.done);
        reminderListViewHolder.reminderText.setTextColor(remind.done ? Color.GRAY : Color.BLACK);
        UIUtil.setTextViewStrikeThrough(reminderListViewHolder.reminderDate, remind.done);
        reminderListViewHolder.reminderDate.setTextColor(remind.done ? Color.GRAY : Color.BLACK);

        reminderListViewHolder.doneCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                reminderListFragment.updateRemind(reminderListViewHolder.getAdapterPosition(), b);
            }
        });

        reminderListViewHolder.reminderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(reminderListFragment.getContext(), Reminder_edit.class);
                intent.putExtra(Reminder_edit.KEY_REMIND, remind);
                reminderListFragment.startActivityForResult(intent, ReminderListFragment.REQ_CODE_REMIND_EDIT);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
