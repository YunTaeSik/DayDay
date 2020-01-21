package com.dayday.yuntaesik.dayday.setting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.dayday.yuntaesik.dayday.R;

/**
 * Created by 박상돈 on 2016-08-02.
 */
public class InfoActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout park, pig, yun, lee;
    private Button park1, pig1, yun1, lee1;
    private LinearLayout back_layout;
    private Button back_btn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        park = (LinearLayout) findViewById(R.id.park);
        lee = (LinearLayout) findViewById(R.id.lee);
        pig = (LinearLayout) findViewById(R.id.pig);
        yun = (LinearLayout) findViewById(R.id.yun);

        park.setOnClickListener(this);
        lee.setOnClickListener(this);
        pig.setOnClickListener(this);
        yun.setOnClickListener(this);


        park1 = (Button) findViewById(R.id.park1);
        lee1 = (Button) findViewById(R.id.lee1);
        pig1 = (Button) findViewById(R.id.pig1);
        yun1 = (Button) findViewById(R.id.yun1);

        park1.setOnClickListener(this);
        lee1.setOnClickListener(this);
        pig1.setOnClickListener(this);
        yun1.setOnClickListener(this);

        back_layout = (LinearLayout) findViewById(R.id.back_layout);
        back_btn = (Button) findViewById(R.id.back_btn);
        back_layout.setOnClickListener(this);
        back_btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.park:
            case R.id.park1:
                Uri uri = Uri.parse("mailto:psdkei@naver.com");
                Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                startActivity(it);
                break;
            case R.id.yun:
            case R.id.yun1:
                Uri uri1 = Uri.parse("mailto:sky877kr@gmail.com");
                Intent it1 = new Intent(Intent.ACTION_SENDTO, uri1);
                startActivity(it1);
                break;
            case R.id.lee:
            case R.id.lee1:
                Uri uri2 = Uri.parse("mailto:steven15@naver.com");
                Intent it2 = new Intent(Intent.ACTION_SENDTO, uri2);
                startActivity(it2);
                break;
            case R.id.pig:
            case R.id.pig1:
                Uri uri3 = Uri.parse("mailto:chl8263@naver.com");
                Intent it3 = new Intent(Intent.ACTION_SENDTO, uri3);
                startActivity(it3);
                break;
            case R.id.back_layout:
            case R.id.back_btn:
                finish();
                break;
        }
    }
}
