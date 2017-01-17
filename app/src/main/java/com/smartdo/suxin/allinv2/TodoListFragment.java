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
import android.widget.Toast;

import com.smartdo.suxin.allinv2.Util.ModelUtil;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by suxin on 9/14/16.
 */
public class TodoListFragment extends Fragment{


    public static final int REQ_CODE_NEW_TODO = 101;

    @BindView(R.id.recycler_view_todo_list) RecyclerView recyclerView;
    @BindView(R.id.fab_todo_list) FloatingActionButton fab;

    private TodoListAdapter adapter;
    private List<Todo> todos;
    private static final String TODOS = "todos";



    public static TodoListFragment newInstance(int listType) {
        Bundle args = new Bundle();
        TodoListFragment fragment = new TodoListFragment();
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
        View view = inflater.inflate(R.layout.todo_list, container, false);
        ButterKnife.bind(this, view);

        loadData();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.spacing_medium)));

        adapter = new TodoListAdapter(todos, this);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TodoListNewDialog todoListNewDialog = TodoListNewDialog.newInstance();
                todoListNewDialog.setTargetFragment(TodoListFragment.this, REQ_CODE_NEW_TODO);
                todoListNewDialog.show(getFragmentManager(), TodoListNewDialog.NEW_DIALOG);
            }
        });

        recyclerView.setAdapter(adapter);
        return view;
    }


    private void loadData() {
        Context context = getActivity();
        todos = ModelUtil.read(context, TODOS, new TypeToken<List<Todo>>(){});
        if (todos == null) {
            todos = new ArrayList<>();
        } else {
            Toast.makeText(context, getString(R.string.long_click_to_delete), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_NEW_TODO && resultCode == Activity.RESULT_OK) {
            String todoId= data.getStringExtra(WishListFragment.KEY_TODO_ID);
            if (todoId != null) {
                deleteTodo(todoId);
            } else {
                Todo todo = data.getParcelableExtra(TodoListNewDialog.KEY_TODO_NAME);
                updateTodo(todo);
            }
        }
    }

    public void updateTodo(Todo todo) {
        boolean found = false;
        for (int i = 0; i < todos.size(); i++) {
            Todo item = todos.get(i);
            if (TextUtils.equals(item.id, todo.id)) {
                found = true;
                todos.set(i, todo);
                break;
            }
        }

        if (!found) {
            todos.add(todo);
        }

        Handler handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                adapter.notifyDataSetChanged();
            }
        };

        handler.post(r);
        Context context = getActivity();
        ModelUtil.save(context, TODOS, todos);
    }

    public void updateTodo(int index, boolean done) {
        todos.get(index).done = done;

        Handler handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                adapter.notifyDataSetChanged();
            }
        };

        handler.post(r);
        Context context = getActivity();
        ModelUtil.save(context, TODOS, todos);
    }

    public void deleteTodo(String todoId) {
        for (int i = 0; i < todos.size(); i++) {
            Todo item = todos.get(i);
            if (TextUtils.equals(item.id, todoId)) {
                todos.remove(i);
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
        ModelUtil.save(context, TODOS, todos);
    }
}
