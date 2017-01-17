package com.smartdo.suxin.allinv2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by suxin on 9/30/16.
 */

public class Help_Main extends Fragment {

    @BindView(R.id.help_reminder) View helpReminder;
    @BindView(R.id.help_todo) View helpTodo;
    @BindView(R.id.help_notebook) View helpNoteBook;
    @BindView(R.id.help_calculator) View helpCalculator;

    public static Help_Main newInstance(int intType) {
        Bundle args = new Bundle();
        Help_Main help_main = new Help_Main();
        help_main.setArguments(args);
        return help_main;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.help_main, container, false);
        ButterKnife.bind(this, view);

        helpReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Help_Reminder_dialog help_reminder_dialog = Help_Reminder_dialog.newInstance();
                help_reminder_dialog.show(getFragmentManager(), "Reminder");
            }
        });

        helpTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Help_Todo_dialog help_todo_dialog = Help_Todo_dialog.newInstance();
                help_todo_dialog.show(getFragmentManager(), "Todo");
            }
        });

        helpNoteBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Help_Notebook_Dialog help_notebook_dialog = Help_Notebook_Dialog.newInstance();
                help_notebook_dialog.show(getFragmentManager(), "Notebook");
            }
        });

        helpCalculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Help_Calculator_Dialog help_calculator_dialog = Help_Calculator_Dialog.newInstance();
                help_calculator_dialog.show(getFragmentManager(), "Calculator");
            }
        });

        return view;
    }
}
