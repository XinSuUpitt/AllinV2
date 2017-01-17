package com.smartdo.suxin.allinv2;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by suxin on 9/29/16.
 */
public class ContentListAdapter extends RecyclerView.Adapter{

    private List<String> data;
    private Secret_Edit secret_edit;

    public ContentListAdapter(List<String> data, Secret_Edit secret_edit) {
        this.data = data;
        this.secret_edit = secret_edit;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.secret_content_cardview, parent, false);
        return new ContentListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final String string = data.get(position);
        ContentListViewHolder contentListViewHolder = (ContentListViewHolder) holder;
        contentListViewHolder.textView.setText(string);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
