package com.smartdo.suxin.allinv2;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by suxin on 9/13/16.
 */
public class NoteBookListFragment extends ListFragment{

    private SimpleCursorAdapter adapter = null;
    private NotesDB db;
    private SQLiteDatabase dbRead;

    public static final int REQUEST_CODE_ADD_NOTE = 1;
    public static final int REQUEST_CODE_EDIT_NOTE = 2;

    @BindView(R.id.fab_notebook_add) FloatingActionButton floatingActionButton;

    public static NoteBookListFragment newInstance(int listType) {
        Bundle args = new Bundle();
        NoteBookListFragment fragment = new NoteBookListFragment();
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
        View view = inflater.inflate(R.layout.notebook, container, false);
        ButterKnife.bind(this, view);

        // Data base
        db = new NotesDB(getContext());
        dbRead = db.getReadableDatabase();

        adapter = new SimpleCursorAdapter(getContext(), R.layout.notes_list_cell, null,
                new String[] { NotesDB.COLUMN_NAME_NOTE_NAME,
                        NotesDB.COLUMN_NAME_NOTE_DATE }, new int[] {
                R.id.tvName, R.id.tvDate });
        setListAdapter(adapter);

        refreshNotesListView();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AtyEditNote.class);
                startActivityForResult(intent, REQUEST_CODE_ADD_NOTE);
            }
        });


        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        // 获取当前笔记条目的Cursor对象
        Cursor c = adapter.getCursor();
        c.moveToPosition(position);

        // 显式Intent开启编辑笔记页面
        Intent i = new Intent(getContext(), AtyEditNote.class);

        // 传入笔记id，name，content
        i.putExtra(AtyEditNote.EXTRA_NOTE_ID,
                c.getInt(c.getColumnIndex(NotesDB.COLUMN_NAME_ID)));
        i.putExtra(AtyEditNote.EXTRA_NOTE_NAME,
                c.getString(c.getColumnIndex(NotesDB.COLUMN_NAME_NOTE_NAME)));
        i.putExtra(AtyEditNote.EXTRA_NOTE_CONTENT,
                c.getString(c.getColumnIndex(NotesDB.COLUMN_NAME_NOTE_CONTENT)));

        // 有返回的开启Activity
        startActivityForResult(i, REQUEST_CODE_EDIT_NOTE);
        super.onListItemClick(l, v, position, id);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQUEST_CODE_ADD_NOTE:
            case REQUEST_CODE_EDIT_NOTE:
                if (resultCode == Activity.RESULT_OK) {
                    refreshNotesListView();
                }
                break;

            default:
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }



    public void refreshNotesListView() {
        adapter.changeCursor(dbRead.query(NotesDB.TABLE_NAME_NOTES, null, null,
                null, null, null, null));
    }

}
