package com.dayday.yuntaesik.dayday.dialog;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dayday.yuntaesik.dayday.R;
import com.dayday.yuntaesik.dayday.signature.SignatureView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by YunTaeSik on 2016-08-05.
 */
public class SignDialog extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout sign_layout;
    private SignatureView signatureView;
    private Button ok_btn;
    private Button cancel_btn;
    private String fodername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.activity_signdialog);
        setFinishOnTouchOutside(false);
        int permissionCheck_Read = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionCheck_Write = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck_Read == PackageManager.PERMISSION_DENIED || permissionCheck_Write == PackageManager.PERMISSION_DENIED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        } else {
            Log.e("권한", "있음");
        }

        sign_layout = (LinearLayout) findViewById(R.id.sign_layout);
        ok_btn = (Button) findViewById(R.id.ok_btn);
        cancel_btn = (Button) findViewById(R.id.cancel_btn);

        fodername = makeDir("TodaySignature");

        ok_btn.setOnClickListener(this);
        cancel_btn.setOnClickListener(this);

        signatureView = new SignatureView(this);
        sign_layout.addView(signatureView);
    }

    private static String makeDir(String dirName) {
        String mRootPath = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/" + dirName;
        File fRoot = new File(mRootPath);
        if (!fRoot.exists()) {
            fRoot.mkdir();
        }
        return mRootPath + "/";
    }

    public void screenshot(View view) throws Exception {

        view.setDrawingCacheEnabled(true);

        Bitmap screenshot = view.getDrawingCache();

        String filename = "sign.png";

        try {
            FileOutputStream fos = new FileOutputStream(fodername + filename);
            screenshot.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + fodername + filename)));

        } catch (IOException e) {
            e.printStackTrace();
        }
        view.setDrawingCacheEnabled(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok_btn:
                try {
                    screenshot(sign_layout);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
                break;
            case R.id.cancel_btn:
                finish();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                        recreate();
                    } else {
                        finish();
                        Toast.makeText(getApplicationContext(), "권한을 허용을 하셔야 이용하실수 있습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
                return;
        }
    }
}
