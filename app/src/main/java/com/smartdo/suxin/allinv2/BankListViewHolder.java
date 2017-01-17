package com.smartdo.suxin.allinv2;

import android.view.View;
import android.widget.TextView;

import com.smartdo.suxin.allinv2.Util.BaseViewHolder;

import butterknife.BindView;

/**
 * Created by suxin on 9/21/16.
 */
public class BankListViewHolder extends BaseViewHolder {

    @BindView(R.id.bank_preview_card_layout) View bankPreviewCardLayout;
    @BindView(R.id.card_type) TextView cardType;
    @BindView(R.id.bank_preview_first_name) TextView bankPreviewFirstName;
    @BindView(R.id.bank_preview_last_name) TextView bankPreviewLastName;
    @BindView(R.id.bank_preview_bank_name_1) TextView bankPreviewBankName1;
    @BindView(R.id.bank_preview_bank_name_2) TextView bankPreviewBankName2;
    @BindView(R.id.bank_preview_expiration_date) TextView bankPreviewExpirationDate;

    public BankListViewHolder(View view) {
        super(view);
    }
}
