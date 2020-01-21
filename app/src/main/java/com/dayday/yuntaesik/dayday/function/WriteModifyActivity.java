package com.dayday.yuntaesik.dayday.function;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dayday.yuntaesik.dayday.R;
import com.dayday.yuntaesik.dayday.adapter.CameraGridBaseAdapter;
import com.dayday.yuntaesik.dayday.adapter.ViewPagerAdapter;
import com.dayday.yuntaesik.dayday.dialog.deleteDialog;
import com.dayday.yuntaesik.dayday.slidemenu.MoreSlide;
import com.dayday.yuntaesik.dayday.sqlite.DBManager;
import com.dayday.yuntaesik.dayday.util.Contact;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by YunTaeSik on 2016-07-25.
 */
public class WriteModifyActivity extends Activity implements View.OnClickListener {
    private final static int DELETE = 2;
    private String Date_Time;
    private String Title;
    private String Content;
    private String Index;
    private DBManager dbManager;
    private GridView gallery_grid;
    private CameraGridBaseAdapter cameraGridBaseAdapter;
    private EditText title_edit;
    private EditText write_edit_text;
    private TextView month_text;
    private TextView day_text;
    private TextView year_week_text;
    private ImageView camera_image;
    private ImageView gallery_image;
    private ImageView finish_btn;
    private LinearLayout write_delete_btn;
    private Button write_delete_bt;
    private int REQ_CODE_PICK_PICTURE = 1;
    private Cursor cursor;
    private ImageView sign_image;
    private ViewPager camera_viewpager;
    private ViewPagerAdapter viewPagerAdapter;
    private LinearLayout more_layout;
    private ImageButton more_btn;
    private LinearLayout more_menu_layout;
    private String menuFlag = "on";
    private Animation alphaAni;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_writemodify);
        Date_Time = getIntent().getStringExtra("Date_Time");
        Title = getIntent().getStringExtra("Title");
        Content = getIntent().getStringExtra("Content");
        Index = getIntent().getStringExtra("Index");
        gallery_grid = (GridView) findViewById(R.id.gallery_grid);
        title_edit = (EditText) findViewById(R.id.title_edit);
        write_edit_text = (EditText) findViewById(R.id.write_edit_text);
        month_text = (TextView) findViewById(R.id.month_text);
        day_text = (TextView) findViewById(R.id.day_text);
        year_week_text = (TextView) findViewById(R.id.year_week_text);
        camera_image = (ImageView) findViewById(R.id.camera_image);
        gallery_image = (ImageView) findViewById(R.id.gallery_image);
        finish_btn = (ImageView) findViewById(R.id.finish_btn);
        write_delete_btn = (LinearLayout) findViewById(R.id.write_delete_btn);
        write_delete_bt = (Button) findViewById(R.id.write_delete_bt);
        sign_image = (ImageView) findViewById(R.id.sign_image);
        camera_viewpager = (ViewPager) findViewById(R.id.camera_viewpager);
        more_btn = (ImageButton) findViewById(R.id.more_btn);
        more_menu_layout = (LinearLayout) findViewById(R.id.more_menu_layout);
        more_layout = (LinearLayout) findViewById(R.id.more_layout);
        more_btn.setOnClickListener(this);
        more_layout.setOnClickListener(this);
        camera_image.setOnClickListener(this);
        gallery_image.setOnClickListener(this);
        finish_btn.setOnClickListener(this);
        write_delete_btn.setOnClickListener(this);
        write_delete_bt.setOnClickListener(this);
        alphaAni = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha);

        title_edit.setText(Title);
        write_edit_text.setText(Content);
        month_text.setText(Date_Time.substring(5, 6).replace("0", "") + Date_Time.substring(6, 7) + "월");
        day_text.setText(Date_Time.substring(8, 10) + "일");
        year_week_text.setText(Date_Time.substring(0, 4) + getWeek(Date_Time));

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("refresh");
        intentFilter.addAction("delete_view");
        registerReceiver(broadcastReceiver, intentFilter);

        Picasso.with(this).load("file://" + Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/TodaySignature/sign.png").into(sign_image);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            dbManager = new DBManager(getApplicationContext(), "Write", null, 1);
            SQLiteDatabase redadb = dbManager.getReadableDatabase();
            cursor = redadb.rawQuery("select * from '" + Date_Time + "'", null);

            final ArrayList<ArrayList<String>> arrayList = new ArrayList();

            while (cursor.moveToNext()) {
                final ArrayList<String> strings = new ArrayList();
                strings.add(cursor.getString(0));
                strings.add(cursor.getString(1));
                arrayList.add(strings);
            }
           /* cameraGridBaseAdapter = new CameraGridBaseAdapter(this, arrayList, Date_Time);
            gallery_grid.setAdapter(cameraGridBaseAdapter);*/
            viewPagerAdapter = new ViewPagerAdapter(this, arrayList, Date_Time);
            camera_viewpager.setAdapter(viewPagerAdapter);
            if (arrayList.size() > 0) {
                camera_viewpager.setVisibility(View.VISIBLE);
            } else {
                camera_viewpager.setVisibility(View.GONE);
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.camera_image:
                Intent CameraIntent = new Intent(getApplicationContext(), CameraActivity.class);
                CameraIntent.putExtra("dbName_Time", Date_Time);
                startActivity(CameraIntent);
                break;
            case R.id.gallery_image:
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("video/*, images/*");
                startActivityForResult(i, REQ_CODE_PICK_PICTURE);
                break;
            case R.id.finish_btn:
                SQLiteDatabase writedb = dbManager.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put("day", Date_Time);
                contentValues.put("title", title_edit.getText().toString());
                contentValues.put("write", write_edit_text.getText().toString());
                writedb.update(Contact.WRITE_LIST, contentValues, "_id=?", new String[]{Index});
                finish();
                break;
            case R.id.write_delete_bt:
            case R.id.write_delete_btn:
                Intent delete = new Intent(this, deleteDialog.class);
                startActivityForResult(delete, DELETE);
                break;
            case R.id.more_layout:
            case R.id.more_btn:
                if (menuFlag.equals("on")) {
                    MoreSlide.MenuSlideOn(getApplicationContext(), more_menu_layout);
                    menuFlag = "off";
                } else if (menuFlag.equals("off")) {
                    MoreSlide.MenuSlideOff(getApplicationContext(), more_menu_layout);
                    menuFlag = "on";
                }
                more_btn.startAnimation(alphaAni);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == REQ_CODE_PICK_PICTURE) {
            if (resultCode == Activity.RESULT_OK) {
                dbManager.insert("insert into '" + Date_Time + "' values(null, '" + data.getDataString() + "' );");
                write_edit_text.requestFocus();
            }
        } else if (requestCode == DELETE) {
            if (resultCode == 1) {
                dbManager.delete("delete from '" + Contact.WRITE_LIST + "' where day = '" + Date_Time + "';");
                dbManager.delete("delete from '" + Date_Time + "';");
                Intent mainrefresh = new Intent("refresh_main");
                sendBroadcast(mainrefresh);
                finish();
            }
        }
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("refresh")) {
                onResume();
            } else if (intent.getAction().equals("delete_view")) {
                int position = intent.getIntExtra("delete_position", 0);
                cursor.moveToPosition(position);
                dbManager.delete("delete from '" + Date_Time + "' where _id = '" + cursor.getString(0) + "';");
                onResume();
            }
        }
    };

}
