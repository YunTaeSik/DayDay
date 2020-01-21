package com.dayday.yuntaesik.dayday.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.MediaController;

import com.dayday.yuntaesik.dayday.R;
import com.dayday.yuntaesik.dayday.sqlite.DBManager;
import com.dayday.yuntaesik.dayday.video.MyVideoView;
import com.squareup.picasso.Picasso;


/**
 * Created by User on 2016-03-08.
 */
public class CameraGridAdapter extends CursorAdapter {

    private Context context;
    private ImageView grid_item_image;
    private MyVideoView grid_item_video;
    private ImageView delete_btn;
    private String uri;
    private String dbName_Time;
    private DBManager dbManager;


    public CameraGridAdapter(Context c, Cursor cursor, String Time) {
        super(c, cursor);
        this.context = c;
        this.dbName_Time = Time;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View v = layoutInflater.inflate(R.layout.grid_item, parent, false);
        dbManager = new DBManager(context, "Write", null, 1);
        return v;
    }

    @Override
    public void bindView(View v, final Context context, final Cursor cursor) {
        grid_item_image = (ImageView) v.findViewById(R.id.grid_item_image);
        grid_item_video = (MyVideoView) v.findViewById(R.id.grid_item_video);
        delete_btn = (ImageView) v.findViewById(R.id.delete_btn);
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbManager.delete("delete from '" + dbName_Time + "' where _id = '" + cursor.getInt(0) + "';");
                Intent refresh = new Intent("refresh");
                context.sendBroadcast(refresh);
            }
        });
        try {
            uri = cursor.getString(1);
            if (!(uri.indexOf("images") == -1)) {
                Picasso.with(context).load(Uri.parse(uri)).into(grid_item_image);
                grid_item_image.setVisibility(View.VISIBLE);
                grid_item_video.setVisibility(View.GONE);
            } else if (!(uri.indexOf("video") == -1)) {
                MediaController controller = new MediaController(context);
                grid_item_video.setMediaController(controller);
                grid_item_video.setVideoURI(Uri.parse(uri));
                grid_item_image.setVisibility(View.GONE);
                grid_item_video.setVisibility(View.VISIBLE);
            } else if (!(uri.indexOf("Today_Record") == -1)) {
                Log.e("uri", uri);
                grid_item_image.setVisibility(View.GONE);
                grid_item_video.setVisibility(View.VISIBLE);
                MediaController mediaController = new MediaController(context);
                mediaController.setAnchorView(grid_item_video);
                grid_item_video.setMediaController(new MediaController(context));
                grid_item_video.setVideoPath("/sdcard/" + uri);
                grid_item_video.requestFocus();
            }
        } catch (CursorIndexOutOfBoundsException e) {
        }
    }
}
