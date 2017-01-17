package com.smartdo.suxin.allinv2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by suxin on 9/20/16.
 */
public class CalculatorViewPager extends AppCompatActivity{

    private static final int int_items = 2;
    @BindView(R.id.tabs_calculator) TabLayout tabLayout;
    @BindView(R.id.viewpaper_calculator) ViewPager viewPager;
    @BindView(R.id.toolbar_calculator) Toolbar toolbar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_calculator_layout);
        ButterKnife.bind(this);


        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });
        viewPager.setAdapter(new MyCalculatorAdapter(getSupportFragmentManager()));
    }

    class MyCalculatorAdapter extends FragmentPagerAdapter {

        public MyCalculatorAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: return new Calculator_Bank();
                case 1: return new Calculator_Secret_Fragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return int_items;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.bank);
                case 1:
                    return getString(R.string.secret);

            }
            return null;
        }
    }
}
