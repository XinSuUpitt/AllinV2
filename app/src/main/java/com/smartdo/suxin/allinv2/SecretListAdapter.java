package com.smartdo.suxin.allinv2;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by suxin on 9/29/16.
 */
public class SecretListAdapter extends RecyclerView.Adapter{

    private List<Secret> data;
    private Calculator_Secret_Fragment calculator_secret_fragment;

    public SecretListAdapter(List<Secret> data, Calculator_Secret_Fragment calculator_secret_fragment) {
        this.data = data;
        this.calculator_secret_fragment = calculator_secret_fragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.secret_main_list_item, parent, false);
        return new SecretListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Secret secret = data.get(position);
        SecretListViewHolder secretListViewHolder = (SecretListViewHolder) holder;
        secretListViewHolder.secretText.setText(secret.text);
        secretListViewHolder.secretDate.setText(new SimpleDateFormat(
                "yyyy-MM-dd hh:mm:ss").format(new Date()));
        //secretListViewHolder.secretDate.setText(secret.date);

        secretListViewHolder.secretLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(calculator_secret_fragment.getContext(), Secret_Edit.class);
                intent.putExtra(Secret_Edit.KEY_SECRET, secret);
                calculator_secret_fragment.startActivityForResult(intent, Calculator_Secret_Fragment.REQ_CODE_SECRET_EDIT);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
