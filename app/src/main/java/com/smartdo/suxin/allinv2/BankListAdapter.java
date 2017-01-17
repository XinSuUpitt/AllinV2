package com.smartdo.suxin.allinv2;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by suxin on 9/21/16.
 */
public class BankListAdapter extends RecyclerView.Adapter{

    private List<Bank> data;
    private Calculator_Bank calculator_bank;

    public BankListAdapter(List<Bank> data, Calculator_Bank calculator_bank) {
        this.data = data;
        this.calculator_bank = calculator_bank;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.calculator_bank_cardview, parent, false);
        return new BankListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Bank bank = data.get(position);
        BankListViewHolder bankListViewHolder = (BankListViewHolder) holder;

        if (bank.position == 10) {
            bankListViewHolder.cardType.setText(bank.bank_Name);
        } else {
            bankListViewHolder.cardType.setText(bank.credit_categary);
        }
        bankListViewHolder.bankPreviewFirstName.setText(bank.holder_first_name);
        bankListViewHolder.bankPreviewLastName.setText(bank.holder_last_name);
        if (bank.bank_Account_Number != null) {
            if (bank.bank_Account_Number.length() < 4) {
                bankListViewHolder.bankPreviewBankName2.setText("XXXX".substring(4-bank.bank_Account_Number.length(), 4) + bank.bank_Account_Number.toString());
            }
            bankListViewHolder.bankPreviewBankName2
                    .setText(bank.bank_Account_Number
                            .substring(bank.bank_Account_Number.length() - 4 >= 0 ? bank.bank_Account_Number.length() - 4 : 0, bank.bank_Account_Number.length()));
        } else {
            bankListViewHolder.bankPreviewBankName2.setText("XXXX");
        }

        bankListViewHolder.bankPreviewExpirationDate.setText(bank.expiration_Date);

        bankListViewHolder.bankPreviewCardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(calculator_bank.getContext(), Calculator_Bank_Add.class);
                intent.putExtra(Calculator_Bank_Add.KEY_BANK, bank);
                calculator_bank.startActivityForResult(intent, Calculator_Bank.REQ_CODE_BANK_EDIT);
            }
        });

    }



    @Override
    public int getItemCount() {
        return data.size();
    }
}
