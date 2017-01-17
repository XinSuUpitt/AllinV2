package com.smartdo.suxin.allinv2;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionMenu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by suxin on 9/25/16.
 */
public class AtyEditNote extends ListActivity{

    private int noteId = -1;
    private EditText etName, etContent;
    private MediaAdapter adapter;
    private NotesDB db;
    private SQLiteDatabase dbRead, dbWrite;
    private String currentPath = null;

    public static final int REQUEST_CODE_GET_PHOTO = 1;
    public static final int REQUEST_CODE_GET_VIDEO = 2;

    public static final String EXTRA_NOTE_ID = "noteId";
    public static final String EXTRA_NOTE_NAME = "noteName";
    public static final String EXTRA_NOTE_CONTENT = "noteContent";

    @BindView(R.id.notebook_menu_green) FloatingActionMenu menuGreen;


    private View.OnClickListener btnClickHandler = new View.OnClickListener() {


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnAddPhoto:// 添加照片按钮
                    // 使用Intent调用系统照相机，传入图像保存路径和名称
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // Ensure that there's a camera activity to handle the intent
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        // Create the File where the photo should go
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException ex) {
                            // Error occurred while creating the File
                            ex.getStackTrace();
                        }
                        currentPath = photoFile.getAbsolutePath();
                        // Continue only if the File was successfully created
                        if (photoFile != null) {

                            Uri photoURI = FileProvider.getUriForFile(AtyEditNote.this,
                                    "com.example.android.fileprovider",
                                    photoFile);
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                            startActivityForResult(takePictureIntent, REQUEST_CODE_GET_PHOTO);
                        }
                    }
                    break;
                case R.id.btnSave:// 保存按钮
                    // 保存多媒体信息和笔记信息到数据库，然后关闭当前页面，返回到笔记列表页面/主页面
                    saveMedia(saveNote());
                    setResult(RESULT_OK);
                    finish();
                    break;
                case R.id.btnCancel:// 取消按钮
                    // 关闭当前页面，返回到笔记列表页面/主页面
                    setResult(RESULT_CANCELED);
                    finish();
                    break;
                case R.id.btnDelete:
                    delete(noteId);
                    setResult(RESULT_OK);
                    finish();
                    break;

                default:
                    break;
            }
        }
    };

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_edit_note);
        ButterKnife.bind(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        db = new NotesDB(this);
        dbRead = db.getReadableDatabase();
        dbWrite = db.getWritableDatabase();

        // 显示多媒体列表
        adapter = new MediaAdapter(this);
        setListAdapter(adapter);

        etName = (EditText) findViewById(R.id.etName);
        etContent = (EditText) findViewById(R.id.etContent);

        // 获取Activity传递过来的noteId
        noteId = getIntent().getIntExtra(EXTRA_NOTE_ID, -1);

        if (noteId > -1) {
            etName.setText(getIntent().getStringExtra(EXTRA_NOTE_NAME));
            etContent.setText(getIntent().getStringExtra(EXTRA_NOTE_CONTENT));

            // 查询本笔记的noteId并且检查是否有对应的多媒体，有则遍历显示在MediaList中
            Cursor c = dbRead.query(NotesDB.TABLE_NAME_MEDIA, null,
                    NotesDB.COLUMN_NAME_MEDIA_OWNER_NOTE_ID + "=?",
                    new String[] { noteId + "" }, null, null, null);
            while (c.moveToNext()) {
                adapter.add(new MediaListCellData(c.getString(c
                        .getColumnIndex(NotesDB.COLUMN_NAME_MEDIA_PATH)), c
                        .getInt(c.getColumnIndex(NotesDB.COLUMN_NAME_ID))));
            }


            adapter.notifyDataSetChanged();
        }

        findViewById(R.id.btnSave).setOnClickListener(btnClickHandler);
        findViewById(R.id.btnCancel).setOnClickListener(btnClickHandler);
        findViewById(R.id.btnAddPhoto).setOnClickListener(btnClickHandler);
        findViewById(R.id.btnDelete).setOnClickListener(btnClickHandler);

        createCustomAnimation();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        MediaListCellData data = adapter.getItem(position);
        Intent i;

        i = new Intent(this, AtyPhotoViewer.class);
        i.putExtra(AtyPhotoViewer.EXTRA_PATH, data.path);
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        System.out.println(data);

        switch (requestCode) {
            case REQUEST_CODE_GET_PHOTO:
            case REQUEST_CODE_GET_VIDEO:
                if (resultCode == RESULT_OK) {
                    adapter.add(new MediaListCellData(currentPath));
                    adapter.notifyDataSetChanged();
                }
                break;
            default:
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    public void saveMedia(int noteId) {

        MediaListCellData data;
        ContentValues cv;

        for (int i = 0; i < adapter.getCount(); i++) {
            data = adapter.getItem(i);

            if (data.id <= -1) {
                cv = new ContentValues();
                cv.put(NotesDB.COLUMN_NAME_MEDIA_PATH, data.path);
                cv.put(NotesDB.COLUMN_NAME_MEDIA_OWNER_NOTE_ID, noteId);
                dbWrite.insert(NotesDB.TABLE_NAME_MEDIA, null, cv);
            }
        }

    }

    public int delete(int noteId) {
        String[] args = {String.valueOf(noteId)};
        return dbWrite.delete(NotesDB.TABLE_NAME_NOTES, NotesDB.COLUMN_NAME_ID
                + "=?", args);
    }

    public int saveNote() {

        ContentValues cv = new ContentValues();
        cv.put(NotesDB.COLUMN_NAME_NOTE_NAME, etName.getText().toString());
        cv.put(NotesDB.COLUMN_NAME_NOTE_CONTENT, etContent.getText().toString());
        cv.put(NotesDB.COLUMN_NAME_NOTE_DATE, new SimpleDateFormat(
                "yyyy-MM-dd hh:mm:ss").format(new Date()));

        if (noteId > -1) {
            dbWrite.update(NotesDB.TABLE_NAME_NOTES, cv, NotesDB.COLUMN_NAME_ID
                    + "=?", new String[] { noteId + "" });
            return noteId;
        } else {
            return (int) dbWrite.insert(NotesDB.TABLE_NAME_NOTES, null, cv);
        }
    }


    @Override
    protected void onDestroy() {
        dbRead.close();
        dbWrite.close();
        super.onDestroy();
    }


    class MediaAdapter extends BaseAdapter {
        private Context context;
        private List<MediaListCellData> list = new ArrayList<MediaListCellData>();

        public MediaAdapter(Context context) {
            this.context = context;
        }

        public void add(MediaListCellData data) {
            list.add(data);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public MediaListCellData getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.media_list_cell, null);
            }

            final MediaListCellData data = getItem(position);

            ImageView ivIcon = (ImageView) convertView
                    .findViewById(R.id.ivIcon);


            ivIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i;

                    i = new Intent(AtyEditNote.this, AtyPhotoViewer.class);
                    i.putExtra(AtyPhotoViewer.EXTRA_PATH, data.path);
                    startActivity(i);
                }
            });


            TextView textView = (TextView) convertView.findViewById(R.id.tvPath);
            textView.setText(getString(R.string.click_image_to_show_full_image));
            //ivIcon.setImageURI(data.uri);
            //ivIcon.setImageResource(data.iconId);
            BitmapWorkerTask task = new BitmapWorkerTask(ivIcon);
            task.execute(data.uri);
            return convertView;
        }

    }


    class BitmapWorkerTask extends AsyncTask<Uri, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;
        private Uri uriuri;

        public BitmapWorkerTask(ImageView imageView) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(Uri... params) {
            uriuri = params[0];
            try {
                return decodeSampledBitmapFromResource(AtyEditNote.this, uriuri, 100, 100);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return BitmapFactory.decodeResource(getResources(), R.drawable.icon_photo);
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }

//    class BitmapWorkerTask extends AsyncTask<Uri, Void, Uri> {
//        private final WeakReference<ImageView> imageViewReference;
//        private Uri uriuri;
//
//        public BitmapWorkerTask(ImageView imageView) {
//            // Use a WeakReference to ensure the ImageView can be garbage collected
//            imageViewReference = new WeakReference<ImageView>(imageView);
//        }
//
//        // Decode image in background.
//        @Override
//        protected Uri doInBackground(Uri... params) {
//            return params[0];
//        }
//
//        // Once complete, see if ImageView is still around and set bitmap.
//        @Override
//        protected void onPostExecute(Uri uri) {
//            if (uri != null) {
//                final ImageView imageView = imageViewReference.get();
//                if (imageView != null) {
//                    imageView.setImageURI(uri);
//                } else {
//                    imageView.setImageResource(R.drawable.icon_photo);
//                }
//            }
//        }
//    }


    static class MediaListCellData {
        int type = 0;
        int id = -1;
        String path = "";
        //int iconId = R.mipmap.ic_launcher;
        Uri uri;

        public MediaListCellData(String path, int id) {
            this(path);

            this.id = id;
        }

        public MediaListCellData(String path) {
            this.path = path;

            if (path.endsWith(".jpg")) {
                //iconId = R.drawable.icon_photo;
                uri = Uri.fromFile(new File(path));
                type = MediaType.PHOTO;
            }
        }

    }

    public static Bitmap decodeSampledBitmapFromResource(Context context, Uri uri, int reqWidth, int reqHeight) throws FileNotFoundException {
        ContentResolver contentResolver = context.getContentResolver();
        InputStream inputStream = contentResolver.openInputStream(uri);
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inputStream, null, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        inputStream = contentResolver.openInputStream(uri);
        return BitmapFactory.decodeStream(inputStream, null, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


    static class MediaType {
        static final int PHOTO = 1;
    }

    private void createCustomAnimation() {
        AnimatorSet set = new AnimatorSet();

        ObjectAnimator scaleOutX = ObjectAnimator.ofFloat(menuGreen.getMenuIconView(), "scaleX", 1.0f, 0.2f);
        ObjectAnimator scaleOutY = ObjectAnimator.ofFloat(menuGreen.getMenuIconView(), "scaleY", 1.0f, 0.2f);

        ObjectAnimator scaleInX = ObjectAnimator.ofFloat(menuGreen.getMenuIconView(), "scaleX", 0.2f, 1.0f);
        ObjectAnimator scaleInY = ObjectAnimator.ofFloat(menuGreen.getMenuIconView(), "scaleY", 0.2f, 1.0f);

        scaleOutX.setDuration(50);
        scaleOutY.setDuration(50);

        scaleInX.setDuration(150);
        scaleInY.setDuration(150);

        scaleInX.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                menuGreen.getMenuIconView().setImageResource(menuGreen.isOpened()
                        ? R.drawable.ic_clear_red_a700_24dp : R.drawable.ic_star_white_24dp);
            }
        });

        set.play(scaleOutX).with(scaleOutY);
        set.play(scaleInX).with(scaleInY).after(scaleOutX);
        set.setInterpolator(new OvershootInterpolator(2));

        menuGreen.setIconToggleAnimatorSet(set);
    }
}
