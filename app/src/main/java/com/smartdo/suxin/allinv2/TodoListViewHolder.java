package com.smartdo.suxin.allinv2;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.smartdo.suxin.allinv2.Util.BaseViewHolder;

import butterknife.BindView;

/**
 * Created by suxin on 9/16/16.
 */
public class TodoListViewHolder extends BaseViewHolder {
    @BindView(R.id.todo_main_layout) View todoMainLayout;
    @BindView(R.id.todo_main_list_item_text) TextView todoMainListItemText;
    @BindView(R.id.todo_main_list_item_check) CheckBox todoMainListItemCheck;


    public TodoListViewHolder(View itemView) {
        super(itemView);
    }
}
