package com.dayday.yuntaesik.dayday.function;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by YunTaeSik on 2016-07-18.
 */
public class WriteActivity extends AppCompatActivity implements View.OnClickListener {
    private final static int DELETE = 2;
    private ImageView camera_image;
    private ImageView gallery_image;
    private EditText write_edit_text;
    private int year, month, day, hour, minute, second, week;
    private String week_kor;
    private TextView month_text;
    private TextView day_text;
    private TextView year_week_text;
    private EditText title_edit;
    private int REQ_CODE_PICK_PICTURE = 1;
    private DBManager dbManager;
    private String dbName_Time;
    private GridView gallery_grid;
    private CameraGridBaseAdapter cameraGridBaseAdapter;
    private ImageView finish_btn;
    private LinearLayout write_focuslayout;
    private Cursor cursor;
    private LinearLayout write_delete_btn;
    private Button write_delete_bt;
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
        setContentView(R.layout.activity_write);
        write_edit_text = (EditText) findViewById(R.id.write_edit_text);
        month_text = (TextView) findViewById(R.id.month_text);
        day_text = (TextView) findViewById(R.id.day_text);
        year_week_text = (TextView) findViewById(R.id.year_week_text);
        camera_image = (ImageView) findViewById(R.id.camera_image);
        gallery_image = (ImageView) findViewById(R.id.gallery_image);
        gallery_grid = (GridView) findViewById(R.id.gallery_grid);
        finish_btn = (ImageView) findViewById(R.id.finish_btn);
        title_edit = (EditText) findViewById(R.id.title_edit);
        write_focuslayout = (LinearLayout) findViewById(R.id.write_focuslayout);
        write_delete_btn = (LinearLayout) findViewById(R.id.write_delete_btn);
        write_delete_bt = (Button) findViewById(R.id.write_delete_bt);
        sign_image = (ImageView) findViewById(R.id.sign_image);
        camera_viewpager = (ViewPager) findViewById(R.id.camera_viewpager);
        more_btn = (ImageButton) findViewById(R.id.more_btn);
        more_layout = (LinearLayout) findViewById(R.id.more_layout);
        more_menu_layout = (LinearLayout) findViewById(R.id.more_menu_layout);
        gallery_image.setOnClickListener(this);
        camera_image.setOnClickListener(this);
        finish_btn.setOnClickListener(this);
        write_focuslayout.setOnClickListener(this);
        write_delete_btn.setOnClickListener(this);
        write_delete_bt.setOnClickListener(this);
        more_btn.setOnClickListener(this);
        more_layout.setOnClickListener(this);

        alphaAni = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha);
        getCalendar();
        month_text.setText(String.valueOf(month) + "월");
        day_text.setText(String.valueOf(day) + "일");
        year_week_text.setText(String.valueOf(year) + "\n" + week_kor);

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
        dbManager = new DBManager(getApplicationContext(), "Write", null, 1);
        try {
            SQLiteDatabase db = dbManager.getWritableDatabase();
            db.execSQL("CREATE TABLE if not exists '" + dbName_Time + "'( _id INTEGER PRIMARY KEY AUTOINCREMENT, uri TEXT not null);");
            SQLiteDatabase redadb = dbManager.getReadableDatabase();
            cursor = redadb.rawQuery("select * from '" + dbName_Time + "'", null);

            final ArrayList<ArrayList<String>> arrayList = new ArrayList();

            while (cursor.moveToNext()) {
                final ArrayList<String> strings = new ArrayList();
                strings.add(cursor.getString(0));
                strings.add(cursor.getString(1));
                arrayList.add(strings);
            }
            viewPagerAdapter = new ViewPagerAdapter(this, arrayList, dbName_Time);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.camera_image:
                Intent CameraIntent = new Intent(getApplicationContext(), CameraActivity.class);
                CameraIntent.putExtra("dbName_Time", dbName_Time);
                startActivity(CameraIntent);
                break;
            case R.id.gallery_image:
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("video/*, images/*");
                startActivityForResult(i, REQ_CODE_PICK_PICTURE);
                break;
            case R.id.write_focuslayout:
                write_edit_text.requestFocus();
                break;
            case R.id.finish_btn:
                dbManager.insert("INSERT INTO " + Contact.WRITE_LIST
                        + " VALUES(null, '" + dbName_Time + "', '" + title_edit.getText().toString() + "', '" + write_edit_text.getText().toString() + "' );");
                Intent refresh = new Intent("refresh_main");
                getApplicationContext().sendBroadcast(refresh);
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
                dbManager.insert("insert into '" + dbName_Time + "' values(null, '" + data.getDataString() + "' );");
                write_edit_text.requestFocus();
                gallery_grid.setVisibility(View.VISIBLE); //한개이상이미지 등록시 보임
                Log.e("dbName_Time", dbName_Time);
            }
        } else if (requestCode == DELETE) {
            if (resultCode == 1) {
                dbManager.delete("delete from '" + Contact.WRITE_LIST + "' where _id = '" + dbName_Time + "';");
                dbManager.delete("delete from '" + dbName_Time + "';");
                Intent mainrefresh = new Intent("refresh_main");
                sendBroadcast(mainrefresh);
                finish();
            }
        }
    }

    private void getCalendar() {

        Calendar calendar = new GregorianCalendar(Locale.KOREA);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        second = calendar.get(Calendar.SECOND);
        week = calendar.get(Calendar.DAY_OF_WEEK);
        week_kor = getWeek(week);

        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy.MM.dd:HH:mm:ss", Locale.KOREA);
        Date currentTime = new Date();
        dbName_Time = mSimpleDateFormat.format(currentTime);

    }

    private String getWeek(int week) {
        String strWeek = null;
        if (week == 1) strWeek = "일요일";
        else if (week == 2) strWeek = "월요일";
        else if (week == 3) strWeek = "화요일";
        else if (week == 4) strWeek = "수요일";
        else if (week == 5) strWeek = "목요일";
        else if (week == 6) strWeek = "금요일";
        else if (week == 7) strWeek = "토요일";
        return strWeek;
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("refresh")) {
                onResume();
            } else if (intent.getAction().equals("delete_view")) {
                int position = intent.getIntExtra("delete_position", 0);
                cursor.moveToPosition(position);
                dbManager.delete("delete from '" + dbName_Time + "' where _id = '" + cursor.getString(0) + "';");
                onResume();
            }
        }
    };


}
