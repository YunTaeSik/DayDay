package com.dayday.yuntaesik.dayday.dialog;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.dayday.yuntaesik.dayday.R;

/**
 * Created by YunTaeSik on 2016-08-10.
 */
public class deleteDialog extends Activity implements View.OnClickListener {
    private Button ok_btn;
    private Button cancel_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        super.onCreate(savedInstanceState);
        setFinishOnTouchOutside(false);
        setContentView(R.layout.activity_deletedialog);
        ok_btn = (Button) findViewById(R.id.ok_btn);
        cancel_btn = (Button) findViewById(R.id.cancel_btn);
        ok_btn.setOnClickListener(this);
        cancel_btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok_btn:
                setResult(1);
                finish();
                break;
            case R.id.cancel_btn:
                setResult(2);
                finish();
                break;
        }
    }
}
