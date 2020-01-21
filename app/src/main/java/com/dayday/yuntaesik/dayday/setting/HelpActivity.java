package com.dayday.yuntaesik.dayday.setting;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dayday.yuntaesik.dayday.R;

/**
 * Created by 박상돈 on 2016-08-02.
 */
public class HelpActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout helplinear1, helplinear2, helplinear3, helplinear4;
    private TextView helptext1, helptext2, helptext3, helptext4;
    private Button helpbutton1, helpbutton2, helpbutton3, helpbutton4;
    private String menuFlag1 = "on", menuFlag2 = "on", menuFlag3 = "on", menuFlag4 = "on";
    private HelpActivitySetting slideMenuSetting = new HelpActivitySetting();
    private LinearLayout back_layout;
    private Button back_btn;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        helplinear1 = (LinearLayout) findViewById(R.id.helplinear1);
        helplinear2 = (LinearLayout) findViewById(R.id.helplinear2);
        helplinear3 = (LinearLayout) findViewById(R.id.helplinear3);
        helplinear4 = (LinearLayout) findViewById(R.id.helplinear4);

        helpbutton1 = (Button) findViewById(R.id.helpbutton1);
        helpbutton2 = (Button) findViewById(R.id.helpbutton2);
        helpbutton3 = (Button) findViewById(R.id.helpbutton3);
        helpbutton4 = (Button) findViewById(R.id.helpbutton4);

        helpbutton1.setOnClickListener(this);
        helpbutton2.setOnClickListener(this);
        helpbutton3.setOnClickListener(this);
        helpbutton4.setOnClickListener(this);

        helptext1 = (TextView) findViewById(R.id.helptext1);
        helptext2 = (TextView) findViewById(R.id.helptext2);
        helptext3 = (TextView) findViewById(R.id.helptext3);
        helptext4 = (TextView) findViewById(R.id.helptext4);

        helplinear1.setOnClickListener(this);
        helplinear2.setOnClickListener(this);
        helplinear3.setOnClickListener(this);
        helplinear4.setOnClickListener(this);

        back_layout = (LinearLayout) findViewById(R.id.back_layout);
        back_btn = (Button) findViewById(R.id.back_btn);
        back_layout.setOnClickListener(this);
        back_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.helplinear1:
            case R.id.helpbutton1:
                if (menuFlag1.equals("on")) {

                    HelpActivitySetting.HelpMenuOn(getApplicationContext(), helptext1, helpbutton1, menuFlag1);
                    menuFlag1 = "off";

                } else if (menuFlag1.equals("off")) {

                    HelpActivitySetting.HelpMenuOff(getApplicationContext(), helptext1, helpbutton1, menuFlag1);
                    menuFlag1 = "on";
                }
                break;
            case R.id.helplinear2:
            case R.id.helpbutton2:
                if (menuFlag2.equals("on")) {

                    HelpActivitySetting.HelpMenuOn(getApplicationContext(), helptext2, helpbutton2, menuFlag2);
                    menuFlag2 = "off";

                } else if (menuFlag2.equals("off")) {

                    HelpActivitySetting.HelpMenuOff(getApplicationContext(), helptext2, helpbutton2, menuFlag2);
                    menuFlag2 = "on";
                }
                break;
            case R.id.helplinear3:
            case R.id.helpbutton3:
                if (menuFlag3.equals("on")) {

                    HelpActivitySetting.HelpMenuOn(getApplicationContext(), helptext3, helpbutton3, menuFlag3);
                    menuFlag3 = "off";

                } else if (menuFlag3.equals("off")) {

                    HelpActivitySetting.HelpMenuOff(getApplicationContext(), helptext3, helpbutton3, menuFlag3);
                    menuFlag3 = "on";
                }
                break;
            case R.id.helplinear4:
            case R.id.helpbutton4:
                if (menuFlag4.equals("on")) {

                    HelpActivitySetting.HelpMenuOn(getApplicationContext(), helptext4, helpbutton4, menuFlag4);
                    menuFlag4 = "off";

                } else if (menuFlag4.equals("off")) {

                    HelpActivitySetting.HelpMenuOff(getApplicationContext(), helptext4, helpbutton4, menuFlag4);
                    menuFlag4 = "on";
                }
                break;
            case R.id.back_layout:
            case R.id.back_btn:
                finish();
                break;


        }
    }
}