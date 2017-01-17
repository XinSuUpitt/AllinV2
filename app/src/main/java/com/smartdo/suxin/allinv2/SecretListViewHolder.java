package com.smartdo.suxin.allinv2;

import android.view.View;
import android.widget.TextView;

import com.smartdo.suxin.allinv2.Util.BaseViewHolder;

import butterknife.BindView;

/**
 * Created by suxin on 9/29/16.
 */
public class SecretListViewHolder extends BaseViewHolder {

    @BindView(R.id.secret_layout) View secretLayout;
    @BindView(R.id.secret_main_list_item_text) TextView secretText;
    @BindView(R.id.secret_main_list_item_date) TextView secretDate;

    public SecretListViewHolder(View view) {
        super(view);
        this.setIsRecyclable(false);
    }
}
