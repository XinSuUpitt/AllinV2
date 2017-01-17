package com.smartdo.suxin.allinv2;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.smartdo.suxin.allinv2.Util.BaseViewHolder;

import butterknife.BindView;

/**
 * Created by suxin on 9/19/16.
 */
public class TodoListSecondViewHolder extends BaseViewHolder {

    @BindView(R.id.todo_second_main_layout) View todoSecondMainLayout;
    @BindView(R.id.todo_second_main_list_item_text) TextView todoSecondMainListItemText;
    @BindView(R.id.todo_second_main_list_item_check) CheckBox todoSecondMainListItemCheck;
    //@BindView(R.id.todo_favourite_chosen) ImageView todoFavouriteChosen;


    public TodoListSecondViewHolder(View view) {
        super(view);
        this.setIsRecyclable(false);
    }
}
