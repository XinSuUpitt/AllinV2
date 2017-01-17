package com.smartdo.suxin.allinv2;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import com.smartdo.suxin.allinv2.Util.ModelUtil;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by suxin on 9/20/16.
 */
public class Calculator_Secret_Fragment extends Fragment {

    public static final String SECRETS = "secrets";
    private List<Secret> secrets;
    public static final int REQ_CODE_SECRET_EDIT = 105;
    private SecretListAdapter adapter;


    @BindView(R.id.fab_calculator_image_home)
    FloatingActionButton floatingActionButton;
    @BindView(R.id.fab_calculator_secret_add) FloatingActionButton floatingActionButton1;
    @BindView(R.id.recycler_view_secret) RecyclerView recyclerView;
    @BindView(R.id.secret_menu_green)
    FloatingActionMenu menuGreen;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calculator_secret, container, false);

        ButterKnife.bind(this, view);

        loadData();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelOffset(R.dimen.spacing_small)));

        adapter = new SecretListAdapter(secrets, this);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().finish();
                    }
                }).start();
            }
        });

        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Secret_Edit.class);
                startActivityForResult(intent, REQ_CODE_SECRET_EDIT);
            }
        });

        createCustomAnimation();

        recyclerView.setAdapter(adapter);
        return view;
    }

    private void createCustomAnimation() {
        AnimatorSet set = new AnimatorSet();

        ObjectAnimator scaleOutX = ObjectAnimator.ofFloat(menuGreen.getMenuIconView(), "scaleX", 1.0f, 0.2f);
        ObjectAnimator scaleOutY = ObjectAnimator.ofFloat(menuGreen.getMenuIconView(), "scaleY", 1.0f, 0.2f);

        ObjectAnimator scaleInX = ObjectAnimator.ofFloat(menuGreen.getMenuIconView(), "scaleX", 0.2f, 1.0f);
        ObjectAnimator scaleInY = ObjectAnimator.ofFloat(menuGreen.getMenuIconView(), "scaleY", 0.2f, 1.0f);

        scaleOutX.setDuration(50);
        scaleOutY.setDuration(50);

        scaleInX.setDuration(150);
        scaleInY.setDuration(150);

        scaleInX.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                menuGreen.getMenuIconView().setImageResource(menuGreen.isOpened()
                        ? R.drawable.ic_clear_red_a700_24dp : R.drawable.ic_star_white_24dp);
            }
        });

        set.play(scaleOutX).with(scaleOutY);
        set.play(scaleInX).with(scaleInY).after(scaleOutX);
        set.setInterpolator(new OvershootInterpolator(2));

        menuGreen.setIconToggleAnimatorSet(set);
    }

    private void loadData() {
        Context context = getActivity();
        secrets = ModelUtil.read(context, SECRETS, new TypeToken<List<Secret>>(){});
        if (secrets == null) {
            secrets = new ArrayList<>();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_SECRET_EDIT && resultCode == Activity.RESULT_OK) {
            String secretid = data.getStringExtra(Secret_Edit.KEY_SECRET_ID);
            if (secretid != null) {
                deleteSecret(secretid);
            } else {
                Secret secret = data.getParcelableExtra(Secret_Edit.KEY_SECRET);
                updateSecret(secret);
            }
        }
    }

    private void updateSecret(Secret secret) {
        boolean found = false;
        for (int i = 0; i < secrets.size(); i++) {
            Secret item = secrets.get(i);
            if (TextUtils.equals(item.id, secret.id)) {
                found = true;
                secrets.set(i, secret);
                break;
            }
        }

        if (!found) {
            secrets.add(secret);
        }

        Handler handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                adapter.notifyDataSetChanged();
            }
        };

        handler.post(r);
        Context context = getActivity();
        ModelUtil.save(context, SECRETS, secrets);
    }

    private void deleteSecret(String secretid) {
        for (int i = 0; i < secrets.size(); i++) {
            Secret item = secrets.get(i);
            if (TextUtils.equals(item.id, secretid)) {
                secrets.remove(i);
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
        ModelUtil.save(context, SECRETS, secrets);
    }
}
