package com.dayday.yuntaesik.dayday.function;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.dayday.yuntaesik.dayday.R;
import com.dayday.yuntaesik.dayday.adapter.GalleryBaseAdapter;
import com.dayday.yuntaesik.dayday.dialog.SignDialog;
import com.dayday.yuntaesik.dayday.main.MainActivity;
import com.dayday.yuntaesik.dayday.slidemenu.SlideMenu;
import com.dayday.yuntaesik.dayday.sqlite.DBManager;
import com.dayday.yuntaesik.dayday.util.Contact;

import java.util.ArrayList;

/**
 * Created by YunTaeSik on 2016-08-05.
 */
public class GalleryActivity extends AppCompatActivity implements View.OnClickListener {
    private GridView gallery_grid;
    private GalleryBaseAdapter galleryBaseAdapter;
    private DBManager dbManager;
    private ArrayList<String> strings = new ArrayList<>();

    private ImageButton slide_btn;
    private LinearLayout menuPlay;
    private String menuFlag = "on";
    private View slide_menu;
    private SlideMenu slideMenu = new SlideMenu();
    private LinearLayout slide_home;
    private LinearLayout slide_write;
    private LinearLayout slide_gallrery;
    private LinearLayout slide_setting;
    private LinearLayout slide_calrendar;
    private LinearLayout slide_sign;
    private LinearLayout touch_layout;
    private RelativeLayout main_layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        gallery_grid = (GridView) findViewById(R.id.gallery_grid);
        main_layout = (RelativeLayout) findViewById(R.id.main_layout);
        slide_menu = (View) findViewById(R.id.slide_menu);
        slide_btn = (ImageButton) findViewById(R.id.slide_btn);
        menuPlay = (LinearLayout) findViewById(R.id.menuPlay);
        touch_layout = (LinearLayout) slide_menu.findViewById(R.id.touch_layout);
        slide_home = (LinearLayout) slide_menu.findViewById(R.id.slide_home);
        slide_write = (LinearLayout) slide_menu.findViewById(R.id.slide_write);
        slide_gallrery = (LinearLayout) slide_menu.findViewById(R.id.slide_gallrery);
        slide_setting = (LinearLayout) slide_menu.findViewById(R.id.slide_setting);
        slide_calrendar = (LinearLayout) slide_menu.findViewById(R.id.slide_calrendar);
        slide_sign = (LinearLayout) slide_menu.findViewById(R.id.slide_sign);

        slide_btn.setOnClickListener(this);
        touch_layout.setOnClickListener(this);
        menuPlay.setOnClickListener(this);
        slide_home.setOnClickListener(this);
        slide_write.setOnClickListener(this);
        slide_gallrery.setOnClickListener(this);
        slide_setting.setOnClickListener(this);
        slide_calrendar.setOnClickListener(this);
        slide_sign.setOnClickListener(this);

        dbManager = new DBManager(getApplicationContext(), "Write", null, 1);
        SQLiteDatabase redadb = dbManager.getReadableDatabase();
        final Cursor cursor = redadb.rawQuery("select * from '" + Contact.WRITE_LIST + "'", null);
        while (cursor.moveToNext()) {
            String a = cursor.getString(1);
            Log.e("uri", a);
            final Cursor imagecurser = redadb.rawQuery("select * from '" + a + "'", null);
            while (imagecurser.moveToNext()) {
                String image = imagecurser.getString(1);
                strings.add(image);
                galleryBaseAdapter = new GalleryBaseAdapter(this, strings);
                Log.e("uri", image);
            }
        }
        gallery_grid.setAdapter(galleryBaseAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menuPlay:
            case R.id.slide_btn:
                if (menuFlag.equals("on")) {
                    slideMenu.SlideMenuOn(getApplicationContext(), slide_btn, menuFlag, slide_menu, main_layout, null);
                    menuFlag = "off";
                } else if (menuFlag.equals("off")) {
                    slideMenu.SlideMenuOff(getApplicationContext(), slide_btn, menuFlag, slide_menu, main_layout, null);
                    menuFlag = "on";
                }
                break;
            case R.id.touch_layout:
                if (menuFlag.equals("off")) {
                    slideMenu.SlideMenuOff(getApplicationContext(), slide_btn, menuFlag, slide_menu, main_layout, null);
                    menuFlag = "on";
                }
                break;
            case R.id.slide_home:
                if (menuFlag.equals("off")) {
                    slideMenu.SlideMenuOff(getApplicationContext(), slide_btn, menuFlag, slide_menu, main_layout, null);
                    menuFlag = "on";
                }
                Intent homeintent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(homeintent);
                break;
            case R.id.slide_write:
                if (menuFlag.equals("off")) {
                    slideMenu.SlideMenuOff(getApplicationContext(), slide_btn, menuFlag, slide_menu, main_layout, null);
                    menuFlag = "on";
                }
                Intent writeintent = new Intent(getApplicationContext(), WriteActivity.class);
                startActivity(writeintent);
                break;
            case R.id.slide_gallrery:
                if (menuFlag.equals("off")) {
                    slideMenu.SlideMenuOff(getApplicationContext(), slide_btn, menuFlag, slide_menu, main_layout, null);
                    menuFlag = "on";
                }
                break;
            case R.id.slide_setting:
                if (menuFlag.equals("off")) {
                    slideMenu.SlideMenuOff(getApplicationContext(), slide_btn, menuFlag, slide_menu, main_layout, null);
                    menuFlag = "on";
                }
                Intent settingintent = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(settingintent);
                break;
            case R.id.slide_calrendar:
                if (menuFlag.equals("off")) {
                    slideMenu.SlideMenuOff(getApplicationContext(), slide_btn, menuFlag, slide_menu, main_layout, null);
                    menuFlag = "on";
                }
                Intent calintent = new Intent(getApplicationContext(), CalnedarActivity.class);
                startActivity(calintent);
                break;
            case R.id.slide_sign:
                if (menuFlag.equals("off")) {
                    slideMenu.SlideMenuOff(getApplicationContext(), slide_btn, menuFlag, slide_menu, main_layout, null);
                    menuFlag = "on";
                }
                Intent signntent = new Intent(getApplicationContext(), SignDialog.class);
                startActivity(signntent);
                break;

        }
    }
}
