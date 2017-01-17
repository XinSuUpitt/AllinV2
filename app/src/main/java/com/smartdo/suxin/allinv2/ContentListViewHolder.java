package com.smartdo.suxin.allinv2;

import android.view.View;
import android.widget.TextView;

import com.smartdo.suxin.allinv2.Util.BaseViewHolder;

import butterknife.BindView;

/**
 * Created by suxin on 9/29/16.
 */
public class ContentListViewHolder extends BaseViewHolder {

    @BindView(R.id.secret_edit_contents_cardView) TextView textView;

    public ContentListViewHolder(View view) {
        super(view);
    }
}
