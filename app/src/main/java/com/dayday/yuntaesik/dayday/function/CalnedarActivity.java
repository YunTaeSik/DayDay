package com.dayday.yuntaesik.dayday.function;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dayday.yuntaesik.dayday.R;
import com.dayday.yuntaesik.dayday.decorators.EventDecorator;
import com.dayday.yuntaesik.dayday.decorators.HighlightWeekendsDecorator;
import com.dayday.yuntaesik.dayday.decorators.OneDayDecorator;
import com.dayday.yuntaesik.dayday.dialog.SignDialog;
import com.dayday.yuntaesik.dayday.main.MainActivity;
import com.dayday.yuntaesik.dayday.slidemenu.SlideMenu;
import com.dayday.yuntaesik.dayday.sqlite.DBManager;
import com.dayday.yuntaesik.dayday.util.Contact;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by YunTaeSik on 2016-07-22.
 */
public class CalnedarActivity extends AppCompatActivity implements OnDateSelectedListener, OnMonthChangedListener, View.OnClickListener {
    private SlideMenu slideMenu = new SlideMenu();
    private View slide_menu;
    private LinearLayout slide_home;
    private LinearLayout slide_write;
    private LinearLayout slide_gallrery;
    private LinearLayout slide_setting;
    private LinearLayout slide_calrendar;
    private LinearLayout slide_sign;
    private LinearLayout touch_layout;
    private LinearLayout slide_layout;
    private ImageButton slide_btn;
    private RelativeLayout calendar_main;
    private String menuFlag = "on";

    private DBManager dbManager;

    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();

    @Bind(R.id.calendarView)
    MaterialCalendarView widget;

    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();
    TextView calendar_textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calnedar);
        ButterKnife.bind(this);
        dbManager = new DBManager(getApplicationContext(), "Write", null, 1);
        widget.setOnMonthChangedListener(this);
        widget.setOnDateChangedListener(this);
        widget.setShowOtherDates(MaterialCalendarView.SHOW_ALL);

        calendar_textView = (TextView) findViewById(R.id.calendar_textView);
        calendar_textView.setText(getSelectedDatesString());


        Calendar instance = Calendar.getInstance();
        widget.setSelectedDate(instance.getTime());
        Calendar instance1 = Calendar.getInstance();
        instance1.set(instance1.get(Calendar.YEAR), Calendar.JANUARY, 1);

        Calendar instance2 = Calendar.getInstance();
        instance2.set(instance2.get(Calendar.YEAR), Calendar.DECEMBER, 31);

        widget.state().edit()
                .setMinimumDate(instance1.getTime())
                .setMaximumDate(instance2.getTime())
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        widget.addDecorators(
                //new MySelectorDecorator(this),
                new HighlightWeekendsDecorator(this),
                oneDayDecorator
        );

        new ApiSimulator().executeOnExecutor(Executors.newSingleThreadExecutor());

        slide_menu = (View) findViewById(R.id.slide_menu);
        slide_layout = (LinearLayout) findViewById(R.id.slide_layout);
        slide_btn = (ImageButton) findViewById(R.id.slide_btn);
        calendar_main = (RelativeLayout) findViewById(R.id.calendar_main);
        touch_layout = (LinearLayout) slide_menu.findViewById(R.id.touch_layout);
        slide_home = (LinearLayout) slide_menu.findViewById(R.id.slide_home);
        slide_write = (LinearLayout) slide_menu.findViewById(R.id.slide_write);
        slide_gallrery = (LinearLayout) slide_menu.findViewById(R.id.slide_gallrery);
        slide_setting = (LinearLayout) slide_menu.findViewById(R.id.slide_setting);
        slide_calrendar = (LinearLayout) slide_menu.findViewById(R.id.slide_calrendar);
        slide_sign = (LinearLayout) slide_menu.findViewById(R.id.slide_sign);
        slide_home.setOnClickListener(this);
        slide_write.setOnClickListener(this);
        slide_gallrery.setOnClickListener(this);
        slide_setting.setOnClickListener(this);
        slide_calrendar.setOnClickListener(this);
        touch_layout.setOnClickListener(this);
        slide_layout.setOnClickListener(this);
        slide_btn.setOnClickListener(this);
        calendar_main.setOnClickListener(this);
        slide_sign.setOnClickListener(this);

        Log.e("widget", String.valueOf(widget.getCurrentDate()));
    }


    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        //If you change a decorate, you need to invalidate decorators
        //  oneDayDecorator.setDate(date.getDate());
        // widget.invalidateDecorators();
        calendar_textView.setText(getSelectedDatesString());
    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
        try {
            //  getSupportActionBar().setTitle(FORMATTER.format(date.getDate()));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private String getSelectedDatesString() {
        CalendarDay date = widget.getSelectedDate();
        if (date == null) {
            return "No Selection";
        }
        return FORMATTER.format(date.getDate());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.slide_layout:
            case R.id.slide_btn:
                if (menuFlag.equals("on")) {
                    slideMenu.SlideMenuOn(getApplicationContext(), slide_btn, menuFlag, slide_menu, calendar_main, null);
                    menuFlag = "off";
                } else if (menuFlag.equals("off")) {
                    slideMenu.SlideMenuOff(getApplicationContext(), slide_btn, menuFlag, slide_menu, calendar_main, null);
                    menuFlag = "on";
                }
                break;
            case R.id.touch_layout:
                if (menuFlag.equals("off")) {
                    slideMenu.SlideMenuOff(getApplicationContext(), slide_btn, menuFlag, slide_menu, calendar_main, null);
                    menuFlag = "on";
                }
                break;
            case R.id.slide_home:
                if (menuFlag.equals("off")) {
                    slideMenu.SlideMenuOff(getApplicationContext(), slide_btn, menuFlag, slide_menu, calendar_main, null);
                    menuFlag = "on";
                }
                Intent homeintent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(homeintent);
                break;
            case R.id.slide_write:
                if (menuFlag.equals("off")) {
                    slideMenu.SlideMenuOff(getApplicationContext(), slide_btn, menuFlag, slide_menu, calendar_main, null);
                    menuFlag = "on";
                }
                Intent writeintent = new Intent(getApplicationContext(), WriteActivity.class);
                startActivity(writeintent);
                break;
            case R.id.slide_gallrery:
                if (menuFlag.equals("off")) {
                    slideMenu.SlideMenuOff(getApplicationContext(), slide_btn, menuFlag, slide_menu, calendar_main, null);
                    menuFlag = "on";
                }
                Intent gallery = new Intent(this, GalleryActivity.class);
                startActivity(gallery);
                break;
            case R.id.slide_setting:
                if (menuFlag.equals("off")) {
                    slideMenu.SlideMenuOff(getApplicationContext(), slide_btn, menuFlag, slide_menu, calendar_main, null);
                    menuFlag = "on";
                }
                Intent setting = new Intent(this, SettingActivity.class);
                startActivity(setting);
                break;
            case R.id.slide_calrendar:
                if (menuFlag.equals("off")) {
                    slideMenu.SlideMenuOff(getApplicationContext(), slide_btn, menuFlag, slide_menu, calendar_main, null);
                    menuFlag = "on";
                }
                break;
            case R.id.slide_sign:
                if (menuFlag.equals("off")) {
                    slideMenu.SlideMenuOff(getApplicationContext(), slide_btn, menuFlag, slide_menu, calendar_main, null);
                    menuFlag = "on";
                }
                Intent signntent = new Intent(getApplicationContext(), SignDialog.class);
                startActivity(signntent);
                break;
        }
    }

    /**
     * Simulate an API call to show how to add decorators
     */
    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -2);
            ArrayList<CalendarDay> dates = new ArrayList<>();
            SQLiteDatabase redadb = dbManager.getReadableDatabase();
            Cursor cursor = redadb.rawQuery("select * from '" + Contact.WRITE_LIST + "'", null);
            while (cursor.moveToNext()) {
                String day = cursor.getString(1);
                Log.e("day", day.substring(0, 4) + day.substring(5, 7) + day.substring(8, 10));
                CalendarDay cday = CalendarDay.from(Integer.parseInt(day.substring(0, 4)), Integer.parseInt(day.substring(5, 7)) - 1, Integer.parseInt(day.substring(8, 10)));
                dates.add(cday);
            }

            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            if (isFinishing()) {
                return;
            }
            Log.e("day", String.valueOf(calendarDays));
            widget.addDecorator(new EventDecorator(Color.parseColor("#" + Integer.toHexString(getResources().getColor(R.color.theme))), calendarDays));
        }
    }
}

