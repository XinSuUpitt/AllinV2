package com.smartdo.suxin.allinv2;

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
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by suxin on 9/29/16.
 */
public class Secret_Edit extends AppCompatActivity {

    public static final String KEY_SECRET = "secret";
    public static final String KEY_SECRET_ID = "secret_id";

    private Secret secret;
    private Date date;
    private List<String> contents;

    @BindView(R.id.secretEdit_detail_edit) EditText secretEdit;
    @BindView(R.id.secret_delete)
    com.github.clans.fab.FloatingActionButton deleteBtn;
    @BindView(R.id.secret_detail_done)
    com.github.clans.fab.FloatingActionButton secretDetailDone;
    @BindView(R.id.secret_edit_addContent_EditText) EditText secretAddContentEdit;
    @BindView(R.id.recycler_view_secretEdit_fragment) RecyclerView recyclerView;

    public SecretListAdapter adapter;
    public ContentListAdapter adapter2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculator_secret);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        secret = getIntent().getParcelableExtra(KEY_SECRET);

        setupUI();
    }

    private void setupUI() {
        setContentView(R.layout.secret_activity_edit);
        ButterKnife.bind(this);

        if (secret != null) {
            secretEdit.setText(secret.text);
            contents = secret.content;
        }

        if (contents == null) {
            contents = new ArrayList<>();
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelOffset(R.dimen.spacing_ssmall)));

        adapter2 = new ContentListAdapter(contents, this);

        recyclerView.setAdapter(adapter2);


        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete();
            }
        });

        secretAddContentEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                if (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    String string = new String(textView.getText().toString());
                    contents.add(string.toString());
                    Handler handler = new Handler();

                    final Runnable r = new Runnable() {
                        public void run() {
                            adapter2.notifyDataSetChanged();
                        }
                    };

                    handler.post(r);
                    secretAddContentEdit.getText().clear();
                    setupUI();
                }
                return false;
            }
        });

        setupSaveButton();
    }

    private void setupSaveButton() {
        secretDetailDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAndExit();
            }
        });
    }

    private void saveAndExit() {
        if (secret == null) {
            secret = new Secret(secretEdit.getText().toString(), contents);
        } else {
            secret.text = secretEdit.getText().toString();
            secret.content = contents;
        }

        Intent result = new Intent();
        result.putExtra(KEY_SECRET, secret);
        setResult(Activity.RESULT_OK, result);
        finish();
    }

    private void delete() {
        Intent result = new Intent();
        if (secret != null) {
            result.putExtra(KEY_SECRET_ID, secret.id);
            setResult(Activity.RESULT_OK, result);
        }
        finish();
    }
}
