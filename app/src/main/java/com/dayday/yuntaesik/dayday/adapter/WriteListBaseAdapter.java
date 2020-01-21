package com.dayday.yuntaesik.dayday.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dayday.yuntaesik.dayday.R;
import com.dayday.yuntaesik.dayday.sqlite.DBManager;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by YunTaeSik on 2016-08-02.
 */
public class WriteListBaseAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ArrayList<String>> arrayList;
    private String time;
    private String title;
    private String content;
    private String imageuri;
    private DBManager dbManager;
    private ViewHolder viewHolder;

    public WriteListBaseAdapter(Context context, ArrayList<ArrayList<String>> arrayList) {
        super();
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View v = layoutInflater.inflate(R.layout.main_list_item, parent, false);
        viewHolder = new ViewHolder();
        viewHolder.day_text = (TextView) v.findViewById(R.id.day_text);
        viewHolder.week_text = (TextView) v.findViewById(R.id.week_text);
        viewHolder.contents_text = (TextView) v.findViewById(R.id.contents_text);
        viewHolder.title_text = (TextView) v.findViewById(R.id.title_text);
        viewHolder.time_text = (TextView) v.findViewById(R.id.time_text);
        viewHolder.image_item = (ImageView) v.findViewById(R.id.image_item);
        viewHolder.list_main_layout = (RelativeLayout) v.findViewById(R.id.list_main_layout);

        time = arrayList.get(position).get(1);
        title = arrayList.get(position).get(2);
        content = arrayList.get(position).get(3);
        String week = getWeek(time);

        if (week.equals("토요일")) {
            viewHolder.day_text.setTextColor(Color.parseColor("#488DCC"));
            viewHolder.week_text.setTextColor(Color.parseColor("#488DCC"));
        } else if (week.equals("일요일")) {
            viewHolder.day_text.setTextColor(Color.parseColor("#CC4E48"));
            viewHolder.week_text.setTextColor(Color.parseColor("#CC4E48"));
        }
        viewHolder.day_text.setText(time.substring(8, 10));
        viewHolder.week_text.setText(week);

        viewHolder.time_text.setText(time.substring(11, 16));
        if (title.length() > 0) {
            viewHolder.title_text.setText(title);
        } else {
            viewHolder.title_text.setText("제목 없음");
        }
        viewHolder.contents_text.setText(content);

        dbManager = new DBManager(context, "Write", null, 1);
        SQLiteDatabase redadb = dbManager.getReadableDatabase();
        final Cursor uri = redadb.rawQuery("select * from '" + time + "'", null);
        uri.moveToFirst();
        try {
            imageuri = uri.getString(1);
        } catch (CursorIndexOutOfBoundsException e) {
            imageuri = "empty";
        }
        if (!(imageuri.indexOf("images") == -1)) { //uri에 image를 포함하면
            Picasso.with(context).load(Uri.parse(imageuri)).into(viewHolder.image_item);
            viewHolder.image_item.setScaleType(ImageView.ScaleType.FIT_XY);
            viewHolder.image_item.setVisibility(View.VISIBLE);
        } else if (!(imageuri.indexOf("video") == -1)) { // uri에 video를 포함하면
            Picasso.with(context).load(R.drawable.video_camera_image).into(viewHolder.image_item);
            viewHolder.image_item.setScaleType(ImageView.ScaleType.CENTER);
            viewHolder.image_item.setVisibility(View.VISIBLE);
        } else if (!(imageuri.indexOf("Today_Record") == -1)) {
            if (!(imageuri.indexOf("RC") == -1)) {
                Picasso.with(context).load(R.drawable.video_camera_image).into(viewHolder.image_item);
                viewHolder.image_item.setScaleType(ImageView.ScaleType.CENTER);
                viewHolder.image_item.setVisibility(View.VISIBLE);
            } else if (!(imageuri.indexOf("P") == -1)) {
                Picasso.with(context).load(new File(Environment.getExternalStorageDirectory()
                        .getAbsolutePath() + "/" + imageuri)).into(viewHolder.image_item);
                viewHolder.image_item.setVisibility(View.VISIBLE);
            }
        }
        return v;
    }

    private String getWeek(String time) {
        String strWeek = null;
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy.MM.dd:HH:mm:ss", Locale.KOREA);
        try {
            java.util.Date date = mSimpleDateFormat.parse(time);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int week = calendar.get(Calendar.DAY_OF_WEEK);
            if (week == 1) strWeek = "일요일";
            else if (week == 2) strWeek = "월요일";
            else if (week == 3) strWeek = "화요일";
            else if (week == 4) strWeek = "수요일";
            else if (week == 5) strWeek = "목요일";
            else if (week == 6) strWeek = "금요일";
            else if (week == 7) strWeek = "토요일";
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return strWeek;
    }

    private class ViewHolder {
        private RelativeLayout list_main_layout;
        private TextView day_text;
        private TextView week_text;
        private TextView contents_text;
        private TextView title_text;
        private TextView time_text;
        private ImageView image_item;
    }
}
