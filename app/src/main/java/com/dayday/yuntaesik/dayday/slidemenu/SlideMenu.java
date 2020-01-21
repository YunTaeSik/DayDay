package com.dayday.yuntaesik.dayday.slidemenu;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.dayday.yuntaesik.dayday.R;


/**
 * Created by 박상돈 on 2016-07-16.
 */
public class SlideMenu {

    public void SlideMenuOn(final Context context, final ImageButton imageButton, final String flag, final View view, final RelativeLayout relativeLayout, final LinearLayout linearLayout1) {

        Animation rotatemenubar = AnimationUtils.loadAnimation(context, R.anim.rotate_menubar);
        imageButton.startAnimation(rotatemenubar);

        Animation translateLeftAnim = AnimationUtils.loadAnimation(context, R.anim.translate_left);
        view.setVisibility(View.VISIBLE);
        view.startAnimation(translateLeftAnim);


        if (flag.equals("on")) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    imageButton.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.arrow_image));
                    try {
                        relativeLayout.setAlpha(0.1f);
                        linearLayout1.setAlpha(0.1f);
                    } catch (NullPointerException e) {
                    }
                }
            }, 250);

        }
    }

    public void SlideMenuOff(final Context context, final ImageButton imageButton, final String flag, final View view, final RelativeLayout relativeLayout, final LinearLayout linearLayout1) {

        Animation rotatemenubar = AnimationUtils.loadAnimation(context, R.anim.rotate_menubar);
        imageButton.startAnimation(rotatemenubar);

        Animation translateRightAnim = AnimationUtils.loadAnimation(context, R.anim.translate_right);
        view.startAnimation(translateRightAnim);
        view.setVisibility(View.GONE);
        try {
            relativeLayout.setAlpha(1.0f);
            linearLayout1.setAlpha(1.0f);
        } catch (NullPointerException e) {

        }

        if (flag.equals("off")) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    imageButton.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.menu_image));
                }
            }, 250);

        }
    }
}
