package com.smartdo.suxin.allinv2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by suxin on 10/7/16.
 */

public class Background_Image extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{

    private MainActivity mainActivity;
    private String description;
    public int drawableId;
    public static final String BACKGROUND = "background";



    @BindView(R.id.background_slider) SliderLayout mDemoSlider;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.background_image);
        ButterKnife.bind(this);

        Toast.makeText(this, "Click to set background", Toast.LENGTH_LONG).show();

        HashMap<String, Integer> backGroundHash = new HashMap<String, Integer>();
        backGroundHash.put("background1", R.drawable.background1);
        backGroundHash.put("background2", R.drawable.background2);
        backGroundHash.put("background3", R.drawable.background3);
        backGroundHash.put("background4", R.drawable.background4);
        backGroundHash.put("background5", R.drawable.background5);
        backGroundHash.put("background6", R.drawable.background6);
        backGroundHash.put("background7", R.drawable.background7);
        backGroundHash.put("background8", R.drawable.background8);
        backGroundHash.put("background9", R.drawable.background9);
        backGroundHash.put("background10", R.drawable.background10);
        backGroundHash.put("background11", R.drawable.background11);
        backGroundHash.put("background12", R.drawable.background12);

        for(String name : backGroundHash.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(backGroundHash.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            mDemoSlider.addSlider(textSliderView);
        }

        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.stopAutoCycle();
        mDemoSlider.addOnPageChangeListener(this);

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(this,slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(BACKGROUND, slider.getBundle().get("extra") + "");
        editor.commit();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(BACKGROUND, slider.getBundle().get("extra") + "");
        startActivity(intent);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
