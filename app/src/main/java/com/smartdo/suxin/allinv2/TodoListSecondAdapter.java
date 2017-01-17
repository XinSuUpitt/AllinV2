package com.smartdo.suxin.allinv2;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.smartdo.suxin.allinv2.Util.UIUtil;

import java.util.List;

/**
 * Created by suxin on 9/19/16.
 */
public class TodoListSecondAdapter extends RecyclerView.Adapter{

    private List<WishList> data;
    private  WishListFragment wishListFragment;


    public TodoListSecondAdapter(List<WishList> data, WishListFragment wishListFragment) {
        this.data = data;
        this.wishListFragment = wishListFragment;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_second_main_list_item, parent, false);
        return new TodoListSecondViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        WishList wishList = data.get(position);
        final TodoListSecondViewHolder todoListSecondViewHolder = (TodoListSecondViewHolder) holder;
        todoListSecondViewHolder.todoSecondMainListItemText.setText(wishList.name);
        todoListSecondViewHolder.todoSecondMainListItemCheck.setChecked(wishList.done);
        UIUtil.setTextViewStrikeThrough(todoListSecondViewHolder.todoSecondMainListItemText, wishList.done);
        todoListSecondViewHolder.todoSecondMainListItemText.setTextColor(wishList.done ? Color.GRAY : Color.BLACK);
//        if (wishList.favorite) {
//            todoListSecondViewHolder.todoFavouriteChosen.setVisibility(View.VISIBLE);
//            todoListSecondViewHolder.todoFavouriteChosen.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    todoListSecondViewHolder.todoFavouriteChosen.setVisibility(View.GONE);
//                    wishListFragment.updateWishListFavorite(position);
//                }
//            });
//        } else {
//            todoListSecondViewHolder.todoFavouriteChosen.setVisibility(View.GONE);
//        }

        todoListSecondViewHolder.todoSecondMainListItemCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                wishListFragment.updateWishList(position, b);
            }
        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
