package com.smartdo.suxin.allinv2;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.smartdo.suxin.allinv2.Util.ModelUtil;
import com.smartdo.suxin.allinv2.Util.UIUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suxin on 9/16/16.
 */
public class TodoListAdapter extends RecyclerView.Adapter{

    private List<Todo> data;
    private TodoListFragment todoListFragment;
    private List<WishList> wishLists;
    private WishListFragment wishListFragment;

    public TodoListAdapter(List<Todo> data, TodoListFragment todoListFragment) {
        this.data = data;
        this.todoListFragment = todoListFragment;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_main_list_item, parent, false);
        return new TodoListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Todo todo = data.get(position);
        TodoListViewHolder todoListViewHolder = (TodoListViewHolder) holder;

        todoListViewHolder.todoMainListItemText.setText(todo.name);
        todoListViewHolder.todoMainListItemCheck.setChecked(todo.done);
        UIUtil.setTextViewStrikeThrough(todoListViewHolder.todoMainListItemText, todo.done);
        todoListViewHolder.todoMainListItemText.setTextColor(todo.done ? Color.GRAY : Color.BLACK);

        todoListViewHolder.todoMainListItemCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                todoListFragment.updateTodo(position, b);
            }
        });

        todoListViewHolder.todoMainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(todoListFragment.getContext(), WishListFragment.class);
                intent.putExtra(WishListFragment.KEY_TODO, todo);
                todoListFragment.startActivity(intent);
            }
        });

        todoListViewHolder.todoMainLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                todoListFragment.deleteTodo(todo.id);
                wishLists = new ArrayList<WishList>();
                ModelUtil.save(todoListFragment.getContext(), todo.name, wishLists);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
