package com.smartdo.suxin.allinv2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smartdo.suxin.allinv2.Util.ModelUtil;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by suxin on 9/10/16.
 */


public class ReminderListFragment extends Fragment{



    private static final String REMINDS = "reminds";

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.fab) FloatingActionButton floatingActionButton;

    private ReminderListAdapter adapter;
    private List<Remind> reminds;
    public static final int REQ_CODE_REMIND_EDIT = 100;



    public static ReminderListFragment newInstance(int intType) {
        // todo
        Bundle args = new Bundle();
        args.putStringArrayList(REMINDS, new ArrayList<String>());
        ReminderListFragment fragment = new ReminderListFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reminder,container, false);
        ButterKnife.bind(this,view);


        loadData();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.spacing_medium)));

        adapter = new ReminderListAdapter(reminds, this);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Reminder_edit.class);
                startActivityForResult(intent, REQ_CODE_REMIND_EDIT);
            }
        });

        recyclerView.setAdapter(adapter);
        return view;
    }

    private void loadData() {
        Context context = getActivity();
        reminds = ModelUtil.read(context, REMINDS, new TypeToken<List<Remind>>(){});
        if (reminds == null) {
            reminds = new ArrayList<>();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_REMIND_EDIT && resultCode == Activity.RESULT_OK) {
            String remindId = data.getStringExtra(Reminder_edit.KEY_REMIND_ID);
            if (remindId != null) {
                deleteRemind(remindId);
            } else {
                Remind remind = data.getParcelableExtra(Reminder_edit.KEY_REMIND);
                updateRemind(remind);
            }
        }
    }

    private void updateRemind(Remind remind) {
        boolean found = false;
        for (int i = 0; i < reminds.size(); i++) {
            Remind item = reminds.get(i);
            if (TextUtils.equals(item.id, remind.id)) {
                found = true;
                reminds.set(i, remind);
                break;
            }
        }

        if (!found) {
            reminds.add(remind);
        }
        Handler handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                adapter.notifyDataSetChanged();
            }
        };

        handler.post(r);
        Context context = getActivity();
        ModelUtil.save(context, REMINDS, reminds);
    }

    public void updateRemind(int index, boolean done) {
        reminds.get(index).done = done;

        Handler handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                adapter.notifyDataSetChanged();
            }
        };

        handler.post(r);
        Context context = getActivity();
        ModelUtil.save(context, REMINDS, reminds);
    }

    private void deleteRemind(String remindId) {
        for (int i = 0; i < reminds.size(); i++) {
            Remind item = reminds.get(i);
            if (TextUtils.equals(item.id, remindId)) {
                reminds.remove(i);
                break;
            }
        }

        Handler handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                adapter.notifyDataSetChanged();
            }
        };

        handler.post(r);
        Context context = getActivity();
        ModelUtil.save(context, REMINDS, reminds);
    }

}
