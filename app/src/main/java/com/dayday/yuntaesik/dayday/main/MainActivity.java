package com.dayday.yuntaesik.dayday.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.dayday.yuntaesik.dayday.R;
import com.dayday.yuntaesik.dayday.adapter.WriteListBaseAdapter;
import com.dayday.yuntaesik.dayday.dialog.PickerDialog;
import com.dayday.yuntaesik.dayday.dialog.SignDialog;
import com.dayday.yuntaesik.dayday.function.CalnedarActivity;
import com.dayday.yuntaesik.dayday.function.GalleryActivity;
import com.dayday.yuntaesik.dayday.function.SettingActivity;
import com.dayday.yuntaesik.dayday.function.WriteActivity;
import com.dayday.yuntaesik.dayday.function.WriteModifyActivity;
import com.dayday.yuntaesik.dayday.slidemenu.SlideMenu;
import com.dayday.yuntaesik.dayday.sqlite.DBManager;
import com.dayday.yuntaesik.dayday.util.Contact;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by YunTaeSik,박상돈 on 2016-06-10.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {
    private Context context = this;
    private ImageButton button;
    private String menuFlag = "on";
    private View slide_menu;
    private SlideMenu slideMenu = new SlideMenu();
    private LinearLayout slide_home;
    private LinearLayout slide_write;
    private LinearLayout slide_gallrery;
    private LinearLayout slide_setting;
    private LinearLayout slide_calrendar;
    private LinearLayout slide_sign;
    private LinearLayout linearLayout;
    private LinearLayout touch_layout, main_bottom_layout;
    private RelativeLayout main_layout;
    private LinearLayout write_layout;
    private DBManager dbManager;
    private WriteListBaseAdapter WriteListBaseAdapter;
    private GridView write_list;
    private LinearLayout main_grid_btn;
    private LinearLayout main_list_btn;
    private TextView nodata_text;
    private ViewFlipper day_viewflipper;
    private TextView flipper_day;
    private TextView go_today;
    private Calendar calendar;
    private float xAtDown;
    private float xAtUp;
    private String lock_flag;
    private String LOCK_SWITCH;
    private ImageView left_day_btn;
    private ImageView rigth_day_btn;

    private Animation alphaAni;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        slide_menu = (View) findViewById(R.id.slide_menu);
        linearLayout = (LinearLayout) findViewById(R.id.menuPlay);
        main_layout = (RelativeLayout) findViewById(R.id.main_layout);
        main_bottom_layout = (LinearLayout) findViewById(R.id.main_bottom_layout);
        nodata_text = (TextView) findViewById(R.id.nodata_text);
        touch_layout = (LinearLayout) slide_menu.findViewById(R.id.touch_layout);
        slide_home = (LinearLayout) slide_menu.findViewById(R.id.slide_home);
        slide_write = (LinearLayout) slide_menu.findViewById(R.id.slide_write);
        slide_gallrery = (LinearLayout) slide_menu.findViewById(R.id.slide_gallrery);
        slide_setting = (LinearLayout) slide_menu.findViewById(R.id.slide_setting);
        slide_calrendar = (LinearLayout) slide_menu.findViewById(R.id.slide_calrendar);
        slide_sign = (LinearLayout) slide_menu.findViewById(R.id.slide_sign);
        button = (ImageButton) findViewById(R.id.button);
        write_list = (GridView) findViewById(R.id.write_list);
        write_layout = (LinearLayout) findViewById(R.id.write_layout);
        main_grid_btn = (LinearLayout) findViewById(R.id.main_grid_btn);
        main_list_btn = (LinearLayout) findViewById(R.id.main_list_btn);
        day_viewflipper = (ViewFlipper) findViewById(R.id.day_viewflipper);
        flipper_day = (TextView) findViewById(R.id.flipper_day);
        go_today = (TextView) findViewById(R.id.go_today);
        left_day_btn = (ImageView) findViewById(R.id.left_day_btn);
        rigth_day_btn = (ImageView) findViewById(R.id.rigth_day_btn);
        left_day_btn.setOnClickListener(this);
        rigth_day_btn.setOnClickListener(this);
        write_layout.setOnClickListener(this);
        button.setOnClickListener(this);
        touch_layout.setOnClickListener(this);
        linearLayout.setOnClickListener(this);
        main_grid_btn.setOnClickListener(this);
        main_list_btn.setOnClickListener(this);
        slide_home.setOnClickListener(this);
        slide_write.setOnClickListener(this);
        slide_gallrery.setOnClickListener(this);
        slide_setting.setOnClickListener(this);
        slide_calrendar.setOnClickListener(this);
        slide_sign.setOnClickListener(this);

        day_viewflipper.setOnTouchListener(this);
        flipper_day.setOnTouchListener(this);
        go_today.setOnClickListener(this);
        flipper_day.setOnClickListener(this);
        calendar = Calendar.getInstance();
        flipper_day.setText(calendar.get(Calendar.YEAR) + "년 " + (calendar.get(Calendar.MONTH) + 1) + "월");

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("refresh_main");
        intentFilter.addAction("picker_select");
        registerReceiver(broadcastReceiver, intentFilter);

        alphaAni = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            dbManager = new DBManager(getApplicationContext(), "Write", null, 1);
            SQLiteDatabase db = dbManager.getWritableDatabase();
            db.execSQL("CREATE TABLE if not exists '" + Contact.WRITE_LIST + "'( _id INTEGER PRIMARY KEY AUTOINCREMENT, day TEXT not null, title TEXT not null, write TEXT not null);");
            SQLiteDatabase redadb = dbManager.getReadableDatabase();
            final Cursor scursor = db.query(Contact.WRITE_LIST, null, null, null, null, null, "_id DESC");

            final ArrayList<ArrayList<String>> arrayList = new ArrayList();
            DecimalFormat decimalFormat = new DecimalFormat("00");
            String viewTime = String.valueOf(calendar.get(Calendar.YEAR) + "." + decimalFormat.format(calendar.get(Calendar.MONTH) + 1));
            while (scursor.moveToNext()) {
                String cursorTime = scursor.getString(1).substring(0, 7);
                if (cursorTime.equals(viewTime)) {
                    ArrayList<String> strings = new ArrayList<>();
                    strings.add(scursor.getString(0));
                    strings.add(scursor.getString(1));
                    strings.add(scursor.getString(2));
                    strings.add(scursor.getString(3));
                    arrayList.add(strings);
                }
            }
            if (arrayList.size() > 0) { //데이터가 있으면
                write_list.setVisibility(View.VISIBLE);
                nodata_text.setVisibility(View.GONE);
            } else {
                write_list.setVisibility(View.GONE);
                nodata_text.setVisibility(View.VISIBLE);
            }
            WriteListBaseAdapter = new WriteListBaseAdapter(getApplicationContext(), arrayList);
            write_list.setAdapter(WriteListBaseAdapter);
            write_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent writemodify = new Intent(context, WriteModifyActivity.class);
                    writemodify.putExtra("Index", arrayList.get(position).get(0));
                    writemodify.putExtra("Date_Time", arrayList.get(position).get(1));
                    writemodify.putExtra("Title", arrayList.get(position).get(2));
                    writemodify.putExtra("Content", arrayList.get(position).get(3));
                    startActivity(writemodify);
                }
            });
        } catch (CursorIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (menuFlag.equals("off")) {
            slideMenu.SlideMenuOff(getApplicationContext(), button, menuFlag, slide_menu, main_layout, main_bottom_layout);
            menuFlag = "on";
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menuPlay:
            case R.id.button:
                if (menuFlag.equals("on")) {
                    slideMenu.SlideMenuOn(getApplicationContext(), button, menuFlag, slide_menu, main_layout, main_bottom_layout);
                    menuFlag = "off";
                } else if (menuFlag.equals("off")) {
                    slideMenu.SlideMenuOff(getApplicationContext(), button, menuFlag, slide_menu, main_layout, main_bottom_layout);
                    menuFlag = "on";
                }
                break;
            case R.id.touch_layout:
                if (menuFlag.equals("off")) {
                    slideMenu.SlideMenuOff(getApplicationContext(), button, menuFlag, slide_menu, main_layout, main_bottom_layout);
                    menuFlag = "on";
                }
                break;
            case R.id.slide_home:
                if (menuFlag.equals("off")) {
                    slideMenu.SlideMenuOff(getApplicationContext(), button, menuFlag, slide_menu, main_layout, main_bottom_layout);
                    menuFlag = "on";
                }
                break;
            case R.id.slide_write:
                if (menuFlag.equals("off")) {
                    slideMenu.SlideMenuOff(getApplicationContext(), button, menuFlag, slide_menu, main_layout, main_bottom_layout);
                    menuFlag = "on";
                }
                Intent writeintent = new Intent(getApplicationContext(), WriteActivity.class);
                startActivity(writeintent);
                break;
            case R.id.slide_gallrery:
                if (menuFlag.equals("off")) {
                    slideMenu.SlideMenuOff(getApplicationContext(), button, menuFlag, slide_menu, main_layout, main_bottom_layout);
                    menuFlag = "on";
                }
                Intent gallery = new Intent(this, GalleryActivity.class);
                startActivity(gallery);
                break;
            case R.id.slide_setting:
                if (menuFlag.equals("off")) {
                    slideMenu.SlideMenuOff(getApplicationContext(), button, menuFlag, slide_menu, main_layout, main_bottom_layout);
                    menuFlag = "on";
                }
                Intent setting = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(setting);
                break;
            case R.id.slide_calrendar:
                if (menuFlag.equals("off")) {
                    slideMenu.SlideMenuOff(getApplicationContext(), button, menuFlag, slide_menu, main_layout, main_bottom_layout);
                    menuFlag = "on";
                }
                Intent calintent = new Intent(getApplicationContext(), CalnedarActivity.class);
                startActivity(calintent);
                break;
            case R.id.slide_sign:
                if (menuFlag.equals("off")) {
                    slideMenu.SlideMenuOff(getApplicationContext(), button, menuFlag, slide_menu, main_layout, main_bottom_layout);
                    menuFlag = "on";
                }
                Intent signntent = new Intent(getApplicationContext(), SignDialog.class);
                startActivity(signntent);
                break;
            case R.id.write_layout:
                Intent intent = new Intent(getApplicationContext(), WriteActivity.class);
                startActivity(intent);
                break;
            case R.id.main_grid_btn:
                main_grid_btn.startAnimation(alphaAni);
                write_list.setNumColumns(2);
                break;
            case R.id.main_list_btn:
                main_list_btn.startAnimation(alphaAni);
                write_list.setNumColumns(1);
                break;
            case R.id.go_today:
                go_today.startAnimation(alphaAni);
                calendar = Calendar.getInstance();
                flipper_day.setText(calendar.get(Calendar.YEAR) + "년 " + (calendar.get(Calendar.MONTH) + 1) + "월");
                onResume();
                break;
            case R.id.flipper_day:
                Intent picker = new Intent(this, PickerDialog.class);
                startActivity(picker);
                break;
            case R.id.left_day_btn:
                day_viewflipper.showPrevious();
                calendar.add(Calendar.MONTH, -1);
                flipper_day.setText(calendar.get(Calendar.YEAR) + "년 " + (calendar.get(Calendar.MONTH) + 1) + "월");
                onResume();
                break;
            case R.id.rigth_day_btn:
                day_viewflipper.showNext();
                calendar.add(Calendar.MONTH, 1);
                flipper_day.setText(calendar.get(Calendar.YEAR) + "년 " + (calendar.get(Calendar.MONTH) + 1) + "월");
                onResume();
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            xAtDown = event.getX(); // 터치 시작지점 x좌표 저장
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            xAtUp = event.getX();   // 터치 끝난지점 x좌표 저장
            if (xAtUp < xAtDown) {
                day_viewflipper.setInAnimation(AnimationUtils.loadAnimation(this,
                        R.anim.push_left_in));
                day_viewflipper.setOutAnimation(AnimationUtils.loadAnimation(this,
                        R.anim.push_left_out));
                day_viewflipper.showNext();
                calendar.add(Calendar.MONTH, 1);
                flipper_day.setText(calendar.get(Calendar.YEAR) + "년 " + (calendar.get(Calendar.MONTH) + 1) + "월");
                onResume();
            } else if (xAtUp > xAtDown) {
                day_viewflipper.setInAnimation(AnimationUtils.loadAnimation(this,
                        R.anim.push_right_in));
                day_viewflipper.setOutAnimation(AnimationUtils.loadAnimation(this,
                        R.anim.push_right_out));
                day_viewflipper.showPrevious();
                calendar.add(Calendar.MONTH, -1);
                flipper_day.setText(calendar.get(Calendar.YEAR) + "년 " + (calendar.get(Calendar.MONTH) + 1) + "월");
                onResume();
            } else if (xAtUp == xAtDown) {
                if (v == flipper_day) {
                    Intent picker = new Intent(this, PickerDialog.class);
                    startActivity(picker);
                }
            }
        }
        return true;
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("refresh_main")) {
                onResume();
            } else if (intent.getAction().equals("picker_select")) {
                int year = intent.getIntExtra("year", 0);
                int mouth = intent.getIntExtra("mouth", 0);
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, mouth - 1);
                flipper_day.setText(calendar.get(Calendar.YEAR) + "년 " + (calendar.get(Calendar.MONTH) + 1) + "월");
                onResume();
            }
        }
    };
}
