package com.smartdo.suxin.allinv2;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
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

public class Calculator_Bank extends Fragment {

    private static final String BANKS = "banks";

    @BindView(R.id.fab_calculator_bank_home)
    FloatingActionButton floatingActionButton;
    @BindView(R.id.fab_calculator_bank) FloatingActionButton fab_bank;
    @BindView(R.id.fab_calculator_bank_resetPW) FloatingActionButton reset_Password;
    @BindView(R.id.recycler_view_bank) RecyclerView recyclerView;
    @BindView(R.id.bank_menu_green) FloatingActionMenu menuGreen;

    private BankListAdapter adapter;
    private List<Bank> bankList;
    public static final int REQ_CODE_BANK_EDIT = 103;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calculator_bank, container, false);
        ButterKnife.bind(this, view);

        loadData();

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        //recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new SpaceItemDecorationForGrid(getResources().getDimensionPixelOffset(R.dimen.spacing_medium)));

        adapter = new BankListAdapter(bankList, this);

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

        fab_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Calculator_Bank_Add.class);
                startActivityForResult(intent, REQ_CODE_BANK_EDIT);
            }
        });

        reset_Password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getActivity();
                SharedPreferences sharedPreferences = context.getSharedPreferences(CalculatorFragment.PASSWORD, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String password = getResources().getString(R.string.default_pw);
                editor.putString(CalculatorFragment.PASSWORD, password);
                editor.commit();
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
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
        bankList = ModelUtil.read(context, BANKS, new TypeToken<List<Bank>>(){});
        if (bankList == null) {
            bankList = new ArrayList<>();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_BANK_EDIT && resultCode == Activity.RESULT_OK) {
            String remindId = data.getStringExtra(Calculator_Bank_Add.KEY_BANK_ID);
            if (remindId != null) {
                deleteBank(remindId);
            } else {
                Bank bank = data.getParcelableExtra(Calculator_Bank_Add.KEY_BANK);
                updateBank(bank);
            }
        }
    }

    private void updateBank(Bank bank) {
        boolean found = false;
        for (int i = 0; i < bankList.size(); i++) {
            Bank item = bankList.get(i);
            if (TextUtils.equals(item.id, bank.id)) {
                found = true;
                bankList.set(i, bank);
                break;
            }
        }

        if (!found) {
            bankList.add(bank);
        }
        Handler handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                adapter.notifyDataSetChanged();
            }
        };

        handler.post(r);
        Context context = getActivity();
        ModelUtil.save(context, BANKS, bankList);
    }

    public void updateBank(int index, boolean done) {

        Handler handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                adapter.notifyDataSetChanged();
            }
        };

        handler.post(r);
        Context context = getActivity();
        ModelUtil.save(context, BANKS, bankList);
    }

    private void deleteBank(String bankId) {
        for (int i = 0; i < bankList.size(); i++) {
            Bank item = bankList.get(i);
            if (TextUtils.equals(item.id, bankId)) {
                bankList.remove(i);
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
        ModelUtil.save(context, BANKS, bankList);
    }
}
