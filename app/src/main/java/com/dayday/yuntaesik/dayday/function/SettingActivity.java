package com.dayday.yuntaesik.dayday.function;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.dayday.yuntaesik.dayday.R;
import com.dayday.yuntaesik.dayday.setting.HelpActivity;
import com.dayday.yuntaesik.dayday.setting.InfoActivity;
import com.dayday.yuntaesik.dayday.setting.LockActivity;
import com.dayday.yuntaesik.dayday.util.Contact;
import com.dayday.yuntaesik.dayday.util.SharedPrefsUtils;

/**
 * Created by YunTaeSik on 2016-08-09.
 */
public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView switchStatus;
    private Switch onoffSwitch;
    private String lock_flag;
    private String LOCK_SWITCH;
    private LinearLayout setting_info, setting_help;
    private LinearLayout back_layout;
    private Button back_btn;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        switchStatus = (TextView) findViewById(R.id.statusSwitch);

        onoffSwitch = (Switch) findViewById(R.id.onoffSwitch);
        onoffSwitch.setChecked(false);
        setting_help = (LinearLayout) findViewById(R.id.setting_help);
        setting_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HelpActivity.class);
                startActivity(intent);
            }
        });

        setting_info = (LinearLayout) findViewById(R.id.setting_info);
        setting_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InfoActivity.class);
                startActivity(intent);
            }
        });

        back_layout = (LinearLayout) findViewById(R.id.back_layout);
        back_btn = (Button) findViewById(R.id.back_btn);
        back_layout.setOnClickListener(this);
        back_btn.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        LOCK_SWITCH = SharedPrefsUtils.getStringPreference(getApplicationContext(), Contact.LOCK_SWITCH);
        try {
            if (LOCK_SWITCH.equals("ON")) {
                onoffSwitch.setChecked(true);
            } else if (LOCK_SWITCH.equals("OFF")) {
                onoffSwitch.setChecked(false);
            }
        } catch (NullPointerException e) {

        }
        onoffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    lock_flag = "first_lock";
                    switchStatus.setText("잠금설정");
                    Intent intent = new Intent(getApplicationContext(), LockActivity.class);
                    intent.putExtra("lock_flag", lock_flag);
                    startActivity(intent);
                } else {
                    switchStatus.setText("잠금해제");

                    SharedPrefsUtils.setStringPreference(getApplicationContext(), Contact.LOCK_SWITCH, "OFF");
                }
            }
        });
        if (onoffSwitch.isChecked()) {
            switchStatus.setText("잠금설정");
            switchStatus.setTextColor(Color.parseColor("#000000"));

        } else {
            switchStatus.setText("잠금해제");
            switchStatus.setTextColor(Color.parseColor("#000000"));
        }
    }

    @Override
    protected void onDestroy() {
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
            case R.id.back_layout:
                finish();
                break;
        }

    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("finish")) {
                onResume();
            }
        }
    };

}
