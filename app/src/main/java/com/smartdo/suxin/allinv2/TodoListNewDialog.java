package com.smartdo.suxin.allinv2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by suxin on 9/27/16.
 */

public class TodoListNewDialog extends DialogFragment{

    public static final String KEY_TODO_NAME = "todo_name";

    @BindView(R.id.new_todo_name) EditText name;

    public static final String NEW_DIALOG = "new_dialog";

    public static TodoListNewDialog newInstance() {return new TodoListNewDialog();}

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.todo_dialog, null);
        ButterKnife.bind(this,view);

        return new AlertDialog.Builder(getContext())
                .setView(view)
                .setTitle(getString(R.string.new_todo_list))
                .setPositiveButton(getString(R.string.add), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent();
                        Todo todo = new Todo(name.getText().toString());
                        intent.putExtra(KEY_TODO_NAME, todo);
                        getTargetFragment().onActivityResult(TodoListFragment.REQ_CODE_NEW_TODO, Activity.RESULT_OK, intent);

                        dismiss();
                    }
                })
                .setNegativeButton(getString(R.string.cancel), null)
                .show();
    }
}
