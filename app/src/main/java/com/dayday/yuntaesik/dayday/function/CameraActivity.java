package com.dayday.yuntaesik.dayday.function;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dayday.yuntaesik.dayday.R;
import com.dayday.yuntaesik.dayday.sqlite.DBManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
/**
 * Created by YunTaeSik on 2016-07-22.
 */
public class CameraActivity extends AppCompatActivity implements SurfaceHolder.Callback, View.OnClickListener, Camera.PictureCallback {
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private MediaRecorder mediaRecorder;
    private String mFilePath;
    private String mFileName_R;
    private String mFileName_P;
    private Button change_btn;
    private Button recode_btn;
    private Button picture_btn;
    private Camera mCamera;
    private int Camera_Flag = Camera.CameraInfo.CAMERA_FACING_FRONT;
    private String Record_Flag = "OFF";
    private String dbName_Time;
    private DBManager dbManager;
    private File pictureFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        int permissionCheck_Camera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int permissionCheck_Read = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionCheck_Write = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionCheck_Record = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);

        if (permissionCheck_Camera == PackageManager.PERMISSION_DENIED || permissionCheck_Read == PackageManager.PERMISSION_DENIED ||
                permissionCheck_Write == PackageManager.PERMISSION_DENIED || permissionCheck_Record == PackageManager.PERMISSION_DENIED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, 1);
            }
        } else {
            Log.e("권한", "있음");
        }
        dbName_Time = getIntent().getStringExtra("dbName_Time");
        dbManager = new DBManager(getApplicationContext(), "Write", null, 1);

        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        change_btn = (Button) findViewById(R.id.change_btn);
        recode_btn = (Button) findViewById(R.id.recode_btn);
        picture_btn = (Button) findViewById(R.id.picture_btn);

        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mFilePath = makeDir("Today_Record");
        change_btn.setOnClickListener(this);
        recode_btn.setOnClickListener(this);
        picture_btn.setOnClickListener(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        OpenCamera();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        CloseCamera();
    }

    private void setMediaRecorder() {
        mediaRecorder = new MediaRecorder();
        mCamera.lock();
        mCamera.unlock();
        mediaRecorder.setCamera(mCamera);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_480P));
        if (Camera_Flag == Camera.CameraInfo.CAMERA_FACING_BACK) {
            mediaRecorder.setOrientationHint(90);
        } else if (Camera_Flag == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            mediaRecorder.setOrientationHint(270);
        }
        mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy.MM.dd:HH:mm:ss", Locale.KOREA);
        Date currentTime = new Date();
        String mTime = mSimpleDateFormat.format(currentTime);
        //  Toast.makeText(getApplicationContext(), mTime, Toast.LENGTH_SHORT).show();
        mFileName_R = mTime + "RC.mp4";
        mediaRecorder.setOutputFile(mFilePath + mFileName_R);
        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaRecorder.start();
    }

    private void takePicture() {
        try {
            mCamera.takePicture(null, null, this);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
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

    private void OpenCamera() {
        try {
            if (Camera_Flag == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                Camera_Flag = Camera.CameraInfo.CAMERA_FACING_BACK;
            } else if (Camera_Flag == Camera.CameraInfo.CAMERA_FACING_BACK) {
                Camera_Flag = Camera.CameraInfo.CAMERA_FACING_FRONT;
            }
            mCamera = Camera.open(Camera_Flag);
            mCamera.setPreviewDisplay(surfaceHolder);
        } catch (Exception e) {
        }
        try {
            Camera.Parameters parameters = mCamera.getParameters();
            List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
            Camera.Size cs = previewSizes.get(0);
            parameters.setRotation(90);
            parameters.setPreviewSize(cs.width, cs.height);
            mCamera.setDisplayOrientation(90);
            mCamera.setParameters(parameters);
            mCamera.startPreview();
        } catch (Exception e) {

        }
    }

    private void CloseCamera() {
        try {
            if (mediaRecorder != null) {
                mediaRecorder.stop();     // stop recording
                mediaRecorder.reset();    // set state to idle
                mediaRecorder.release();  // release resources back to the system
                mediaRecorder = null;
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + mFilePath + mFileName_R)));
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + mFilePath + mFileName_P)));
            }
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        } catch (Exception e) {
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.change_btn:
                surfaceView.setVisibility(View.INVISIBLE); //
                surfaceView.setVisibility(View.VISIBLE); //카메라 전후방 변경
                break;
            case R.id.recode_btn:
                if (Record_Flag.equals("OFF")) {
                    try {
                        setMediaRecorder();
                        picture_btn.setVisibility(View.GONE);
                        recode_btn.setSelected(true);
                        Record_Flag = "ON";
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                } else if (Record_Flag.equals("ON")) {
                    picture_btn.setVisibility(View.VISIBLE);
                    try {
                        mediaRecorder.stop();     // stop recording
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    }
                    mediaRecorder.reset();    // set state to idle
                    mediaRecorder.release();  // release resources back to the system
                    mediaRecorder = null;
                    try {
                        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + mFilePath + mFileName_R)));
                        //  Toast.makeText(this, mFilePath + mFileName_R, Toast.LENGTH_SHORT).show();
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
                    recode_btn.setSelected(false);
                    Record_Flag = "OFF";
                    dbManager.insert("insert into '" + dbName_Time + "' values(null, '" + "Today_Record/" + mFileName_R + "' );");
                    Intent write_refresh = new Intent("refresh");
                    sendBroadcast(write_refresh);
                    finish();
                }
                break;
            case R.id.picture_btn:
                takePicture();
                break;
        }
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        //Toast.makeText(getApplicationContext(), "onPictureTaken", Toast.LENGTH_SHORT).show();
        try {
            File pictureFile = getOutputMediaFile();
            FileOutputStream fos = new FileOutputStream(pictureFile);
            if (Camera_Flag == Camera.CameraInfo.CAMERA_FACING_BACK) {
                fos.write(data);
            } else if (Camera_Flag == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                Matrix mat = new Matrix();
                mat.postRotate(270);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mat, true);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                fos.write(byteArray);
            }
            fos.close();
            mCamera.startPreview();
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + mFilePath + mFileName_P)));
            dbManager.insert("insert into '" + dbName_Time + "' values(null, '" + "Today_Record/" + mFileName_P + "' );");
            Intent write_refresh = new Intent("refresh");
            sendBroadcast(write_refresh);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private File getOutputMediaFile() {
        mFileName_P = new SimpleDateFormat("yyyy.MM.dd:HH:mm:ss", Locale.KOREA).format(new Date()) + "P.jpg";
        File mediaFile;
        mediaFile = new File(mFilePath + mFileName_P);
        return mediaFile;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:

                if (grantResults.length > 0) {

                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED ||
                            grantResults[2] == PackageManager.PERMISSION_GRANTED || grantResults[3] == PackageManager.PERMISSION_GRANTED) {
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


