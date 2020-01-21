package com.dayday.yuntaesik.dayday.slidemenu;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.dayday.yuntaesik.dayday.R;

/**
 * Created by 박상돈 on 2016-08-17.
 */
public class MoreSlide {

    public static void MenuSlideOn(final Context context, final LinearLayout linearLayout) {

        Animation traslateRight = AnimationUtils.loadAnimation(context, R.anim.more_translate_right);
        linearLayout.setVisibility(View.VISIBLE);
        linearLayout.startAnimation(traslateRight);


    }

    public static void MenuSlideOff(final Context context, final LinearLayout linearLayout) {
        Animation traslateLeft = AnimationUtils.loadAnimation(context, R.anim.more_translate_left);
        linearLayout.setVisibility(View.GONE);
        linearLayout.startAnimation(traslateLeft);

    }

}
