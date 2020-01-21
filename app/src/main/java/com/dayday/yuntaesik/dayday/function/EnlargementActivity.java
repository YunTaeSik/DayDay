package com.dayday.yuntaesik.dayday.function;

import android.content.ContentResolver;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;

import com.dayday.yuntaesik.dayday.R;
import com.dayday.yuntaesik.dayday.video.MyVideoView;

/**
 * Created by YunTaeSik on 2016-08-17.
 */
public class EnlargementActivity extends AppCompatActivity {
    private MyVideoView video_view;
    private String uri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enlargement);
        uri = getIntent().getStringExtra("uri");
        video_view = (MyVideoView) findViewById(R.id.video_view);
        MediaController controller = new MediaController(this);
        video_view.setMediaController(controller);
        if (!(uri.indexOf("video") == -1)) {
            video_view.setVideoURI(Uri.parse(uri));
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                String test = "content://media/external/video/media/"
                        + String.valueOf(getVideoIdFromFilePath(Environment.getExternalStorageDirectory()
                        .getAbsolutePath() + "/" + uri, getContentResolver()));
                video_view.setVideoURI(Uri.parse(test));
            } else {
                video_view.setVideoPath("/sdcard/" + uri);
            }
            video_view.requestFocus();
        }
        video_view.start();

    }

    private long getVideoIdFromFilePath(String filePath, ContentResolver contentResolver) {
        try {
            long videoId;
            Uri videosUri = MediaStore.Video.Media.getContentUri("external");
            String[] projection = {MediaStore.Video.VideoColumns._ID};
            // TODO This will break if we have no matching item in the MediaStore.
            Cursor cursor = contentResolver.query(videosUri, projection, MediaStore.Video.VideoColumns.DATA + " LIKE ?", new String[]{filePath}, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(projection[0]);
            videoId = cursor.getLong(columnIndex);
            cursor.close();
            return videoId;
        } catch (CursorIndexOutOfBoundsException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
