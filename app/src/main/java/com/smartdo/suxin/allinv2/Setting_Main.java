package com.smartdo.suxin.allinv2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by suxin on 9/30/16.
 */

public class Setting_Main extends Fragment {

    public final static String CHANGE_BACKGROUND_IMAGE = "change_background_image";
    Locale myLocale;
    public final static String LANGUAGE = "language";

    @BindView(R.id.setting_background_image) View settingBackGroundImage;
    @BindView(R.id.setting_language) View setting_Language;
    @BindView(R.id.spinner) Spinner spinner;


    public static Setting_Main newInstance(int intType) {
        Bundle args = new Bundle();
        Setting_Main fragment = new Setting_Main();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting_main, container, false);
        ButterKnife.bind(this, view);

        settingBackGroundImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Background_Image.class);
                startActivity(intent);
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    setLocale("en");
                } else if (position == 2) {
                    setLocale("zh");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    public void setLocale(String lang) {

//        myLocale = new Locale(lang);
//        Resources res = getResources();
//        DisplayMetrics dm = res.getDisplayMetrics();
//        Configuration conf = res.getConfiguration();
//        conf.locale = myLocale;
//        res.updateConfiguration(conf, dm);
//        Intent refresh = new Intent(getContext(), MainActivity.class);
//        startActivity(refresh);
        myLocale = new Locale(lang);
        Locale.setDefault(myLocale);
        Configuration configuration = new Configuration();
        configuration.locale = myLocale;
        getContext().getResources().updateConfiguration(configuration, getContext().getResources().getDisplayMetrics());
        Intent refresh = new Intent(getContext(), MainActivity.class);
        startActivity(refresh);
        Context context = getActivity();
        SharedPreferences sharedPreferences = context.getSharedPreferences(LANGUAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LANGUAGE, lang);
        editor.commit();
    }
}
