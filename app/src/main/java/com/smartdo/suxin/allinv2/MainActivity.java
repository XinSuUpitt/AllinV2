package com.smartdo.suxin.allinv2;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private List<Remind> reminds;

    private static final int REQ_CODE_EDIT_BASIC_INFO = 103;
    private static final String MODEL_BASIC_INFO = "basic_info";
    private Background_Image background_image;
    private Setting_Main setting_main;
    String drawable;
    private static final String TITLE = "title";
    String title;





    @BindView(R.id.drawerLayout) DrawerLayout drawerLayout;
    @BindView(R.id.nv_view) NavigationView navigationView;
    @BindView(R.id.toolbar) Toolbar toolbar;

    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(R.color.white));

        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.string.open_drawer,
                R.string.close_drawer);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);


        SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(this);
        drawable = mSharedPreference.getString(Background_Image.BACKGROUND, "background4");
        title = mSharedPreference.getString(TITLE, getString(R.string.reminder));

        if(title == getString(R.string.reminder)) {
            ReminderListFragment reminderListFragment = ReminderListFragment.newInstance(0);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.containerView, reminderListFragment)
                    .commit();
            setTitle(getString(R.string.reminder));
        } else if (title == getString(R.string.todo)) {
            TodoListFragment todoListFragment = TodoListFragment.newInstance(1);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.containerView, todoListFragment)
                    .commit();
            setTitle(getString(R.string.todo));
        } else if (title == getString(R.string.notebook)) {
            NoteBookListFragment noteBookListFragment = NoteBookListFragment.newInstance(2);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.containerView, noteBookListFragment)
                    .commit();
            setTitle(getString(R.string.notebook));
        } else if (title == getString(R.string.calculator)) {
            CalculatorFragment calculatorFragment = CalculatorFragment.newInstance(3);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.containerView, calculatorFragment);
            setTitle(getString(R.string.calculator));
        } else {
            Setting_Main settingMain = Setting_Main.newInstance(4);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.containerView, settingMain);
            setTitle(getString(R.string.action_settings));
        }

        setupDrawer(drawerLayout);

        if (savedInstanceState == null) {
            ReminderListFragment reminderListFragment = ReminderListFragment.newInstance(0);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.containerView, reminderListFragment)
                    .commit();
            setTitle(getString(R.string.reminder));
        }


        if (drawable.equals("background1")) {
            drawerLayout.setBackgroundResource(R.drawable.background1);
        }
        else if (drawable.equals("background2")) {
            drawerLayout.setBackgroundResource(R.drawable.background2);
        }
        else if (drawable.equals("background3")) {
            drawerLayout.setBackgroundResource(R.drawable.background3);
        }
        else if (drawable.equals("background4")) {
            drawerLayout.setBackgroundResource(R.drawable.background4);
        }
        else if (drawable.equals("background5")) {
            drawerLayout.setBackgroundResource(R.drawable.background5);
        }
        else if (drawable.equals("background6")) {
            drawerLayout.setBackgroundResource(R.drawable.background6);
        }
        else if (drawable.equals("background7")) {
            drawerLayout.setBackgroundResource(R.drawable.background7);
        }
        else if (drawable.equals("background8")) {
            drawerLayout.setBackgroundResource(R.drawable.background8);
        }
        else if (drawable.equals("background9")) {
            drawerLayout.setBackgroundResource(R.drawable.background9);
        }
        else if (drawable.equals("background10")) {
            drawerLayout.setBackgroundResource(R.drawable.background10);
        }
        else if (drawable.equals("background11")) {
            drawerLayout.setBackgroundResource(R.drawable.background11);
        }
        else if (drawable.equals("background12")) {
            drawerLayout.setBackgroundResource(R.drawable.background12);
        } else {
            drawerLayout.setBackgroundResource(R.drawable.background1);
        }

    }




    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setupDrawer(final DrawerLayout drawerLayout) {



        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.isChecked()) {
                    drawerLayout.closeDrawers();
                    return true;
                }

                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.reminder_todo:
                        fragment = ReminderListFragment.newInstance(0);
                        setTitle(getString(R.string.reminder));
                        title = getString(R.string.reminder);
                        break;
                    case R.id.todo_list_item:
                        fragment = TodoListFragment.newInstance(1);
                        setTitle(getString(R.string.todo));
                        title = getString(R.string.todo);
                        break;
                    case R.id.notebook:
                        fragment = NoteBookListFragment.newInstance(2);
                        setTitle(getString(R.string.notebook));
                        title = getString(R.string.notebook);
                        break;
                    case R.id.calculator:
                        fragment = CalculatorFragment.newInstance(4);
                        setTitle(getString(R.string.calculator));
                        title = getString(R.string.calculator);
                        break;
                    case R.id.setting:
                        fragment = Setting_Main.newInstance(5);
                        setTitle(getString(R.string.settings));
                        title = getString(R.string.settings);
                        break;
                    case R.id.help:
                        fragment = Help_Main.newInstance(6);
                        setTitle(getString(R.string.help));
                        title = getString(R.string.help);
                        break;
                    case R.id.about_us:
                        fragment = About_Me_main.newInstance(7);
                        setTitle(getString(R.string.aboutme));
                        title = getString(R.string.aboutme);
                        break;
                }

                SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                SharedPreferences.Editor editor = mSharedPreference.edit();
                editor.putString(TITLE, title);
                editor.commit();

                drawerLayout.closeDrawers();

                if (fragment != null) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.containerView, fragment)
                            .commit();
                    return true;
                }

                return false;
            }
        });
    }
}
