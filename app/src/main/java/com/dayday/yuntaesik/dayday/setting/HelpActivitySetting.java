package com.dayday.yuntaesik.dayday.setting;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.dayday.yuntaesik.dayday.R;

/**
 * Created by 박상돈 on 2016-08-08.
 */
public class HelpActivitySetting {

    public static void HelpMenuOn(final Context context, TextView textView, final Button button, String flag) {

        Animation helprotate = AnimationUtils.loadAnimation(context, R.anim.rotate_menubar);
        button.startAnimation(helprotate);

        textView.setVisibility(View.VISIBLE);



        if (flag.equals("on")) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    button.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.uparrow));
                }
            }, 250);


        }
    }

    public static void HelpMenuOff(final Context context, TextView textView, final Button button, String flag) {

        Animation helprotate = AnimationUtils.loadAnimation(context, R.anim.rotate_menubar);
        button.startAnimation(helprotate);

        textView.setVisibility(View.GONE);

        if (flag.equals("off")) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    button.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.helparrow));
                }
            }, 250);


        }


    }

}
