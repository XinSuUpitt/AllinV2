package com.smartdo.suxin.allinv2;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.smartdo.suxin.allinv2.Util.ModelUtil;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by suxin on 9/27/16.
 */

public class WishListFragment extends AppCompatActivity {

    public static final String KEY_TODO = "todo";
    public static final String KEY_TODOS = "todos";
    public static final String KEY_TODO_ID = "todo_id";
    private static final String TODOS = "todos";
    public static String WISHLISTS;

    private List<WishList> wishLists;
    private TodoListFragment todoListFragment;
    private String wishListName;

    @BindView(R.id.todotodo_detail_todo_textview) TextView textView;
    @BindView(R.id.todotodo_detail_done)
    com.github.clans.fab.FloatingActionButton floatingActionButtonDone;
    @BindView(R.id.todotodo_detail_delete)
    com.github.clans.fab.FloatingActionButton floatingActionButtonDelete;
    @BindView(R.id.wishlist_item_add) EditText editText;
    @BindView(R.id.recycler_view_todo_second_list) RecyclerView recyclerView;
    @BindView(R.id.todo_menu_pink)
    FloatingActionMenu menuPink;

    private Todo todo;

    private TodoListSecondAdapter adapter2;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_list);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        todo = getIntent().getParcelableExtra(KEY_TODO);
        wishListName = todo.name;
        WISHLISTS = "" + wishListName;
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
//        Gson gson = new Gson();
//        String json = sharedPreferences.getString(wishListName, "");
//        Type type = new TypeToken<List<WishList>>(){}.getType();
//        wishLists = gson.fromJson(json, type);
//        if (wishLists == null) {
//            wishLists = new ArrayList<>();
//        }

        loadData();

        setupUI();

    }

    private void loadData() {
        wishLists = ModelUtil.read(this, WISHLISTS, new TypeToken<List<WishList>>(){});
        if (wishLists == null) {
            wishLists = new ArrayList<>();
        }
    }

    private void updateWishList(WishList wishList) {
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        Gson gson = new Gson();
//        String json = gson.toJson(wishLists);
//        editor.putString(wishListName, json);
//        editor.commit();

        wishLists.add(wishList);

        Handler handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                adapter2.notifyDataSetChanged();
            }
        };

        handler.post(r);
        ModelUtil.save(this, WISHLISTS, wishLists);

    }


    private void setupSaveButton() {
        floatingActionButtonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    private void setupUI() {
        setContentView(R.layout.todo_activity_edit);
        ButterKnife.bind(this);

        textView.setText(todo.name);
        floatingActionButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wishLists = new ArrayList<>();
                ModelUtil.save(WishListFragment.this, WISHLISTS, wishLists);
                delele();
                Toast.makeText(WishListFragment.this, getString(R.string.item_in) + todo.name + getString(R.string.has_been_cleared), Toast.LENGTH_LONG).show();
                Toast.makeText(WishListFragment.this, getString(R.string.long_click_to_delete), Toast.LENGTH_LONG).show();
                finish();
            }
        });

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                if (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    WishList wishList = new WishList(textView.getText().toString());
                    wishList.done = false;
                    wishList.favorite = false;
                    updateWishList(wishList);
                    editText.setText(null);
                    setupUI();
                }

                return false;
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelOffset(R.dimen.spacing_ssmall)));

        adapter2 = new TodoListSecondAdapter(wishLists, this);
        recyclerView.setAdapter(adapter2);

        setupSaveButton();

        createCustomAnimation();
    }

    private void delele() {
        Intent result = new Intent();
        result.putExtra(KEY_TODO_ID, todo.id);
        setResult(Activity.RESULT_OK, result);
    }


    public void updateWishList(int position, boolean b) {
        wishLists.get(position).done = b;

        Handler handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                adapter2.notifyDataSetChanged();
            }
        };

        handler.post(r);

        ModelUtil.save(this, WISHLISTS, wishLists);

    }

    public void updateWishListFavorite(int position) {
        if (wishLists.get(position).favorite) {
            wishLists.get(position).favorite = false;
        } else {
            wishLists.get(position).favorite = true;
        }
        Handler handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                adapter2.notifyDataSetChanged();
            }
        };

        handler.post(r);

        ModelUtil.save(this, WISHLISTS, wishLists);
    }


    private void createCustomAnimation() {
        AnimatorSet set = new AnimatorSet();

        ObjectAnimator scaleOutX = ObjectAnimator.ofFloat(menuPink.getMenuIconView(), "scaleX", 1.0f, 0.2f);
        ObjectAnimator scaleOutY = ObjectAnimator.ofFloat(menuPink.getMenuIconView(), "scaleY", 1.0f, 0.2f);

        ObjectAnimator scaleInX = ObjectAnimator.ofFloat(menuPink.getMenuIconView(), "scaleX", 0.2f, 1.0f);
        ObjectAnimator scaleInY = ObjectAnimator.ofFloat(menuPink.getMenuIconView(), "scaleY", 0.2f, 1.0f);

        scaleOutX.setDuration(50);
        scaleOutY.setDuration(50);

        scaleInX.setDuration(150);
        scaleInY.setDuration(150);

        scaleInX.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                menuPink.getMenuIconView().setImageResource(menuPink.isOpened()
                        ? R.drawable.ic_clear_red_a700_24dp : R.drawable.ic_star_white_24dp);
            }
        });

        set.play(scaleOutX).with(scaleOutY);
        set.play(scaleInX).with(scaleInY).after(scaleOutX);
        set.setInterpolator(new OvershootInterpolator(2));

        menuPink.setIconToggleAnimatorSet(set);
    }
}
