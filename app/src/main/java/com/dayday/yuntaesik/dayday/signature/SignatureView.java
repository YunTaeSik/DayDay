package com.dayday.yuntaesik.dayday.signature;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by YunTaeSik on 2016-08-05.
 */
public class SignatureView extends View {
    Paint paint = new Paint();
    Path path = new Path();    // 자취를 저장할 객체

    public SignatureView(Context context) {
        super(context);
        paint.setStyle(Paint.Style.STROKE); // 선이 그려지도록
        paint.setStrokeWidth(10f); // 선의 굵기 지정
    }

    @Override
    protected void onDraw(Canvas canvas) { // 화면을 그려주는 메서드
        canvas.drawPath(path, paint); // 저장된 path 를 그려라
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(x, y); // 자취에 그리지 말고 위치만 이동해라
                break;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(x, y); // 자취에 선을 그려라
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        invalidate(); // 화면을 다시그려라

        return true;
    }
}
