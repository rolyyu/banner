package edu.jnu.banner.ui.transformer;

import android.view.View;

import com.nineoldandroids.view.ViewHelper;

public class ZoomStackPageTransformer extends PageTransformer {

    @Override
    public void handleInvisiblePage(View view, float position) {
    }

    @Override
    public void handleLeftPage(View view, float position) {
        ViewHelper.setTranslationX(view, -view.getWidth() * position);

        ViewHelper.setPivotX(view, view.getWidth() * 0.5f);
        ViewHelper.setPivotY(view, view.getHeight() * 0.5f);
        ViewHelper.setScaleX(view, 1 + position);
        ViewHelper.setScaleY(view, 1 + position);

        if (position < -1.0f) {
            ViewHelper.setAlpha(view, 0);
        } else {
            ViewHelper.setAlpha(view, 1);
        }
    }

    @Override
    public void handleRightPage(View view, float position) {
        ViewHelper.setTranslationX(view, -view.getWidth() * position);

        ViewHelper.setPivotX(view, view.getWidth() * 0.5f);
        ViewHelper.setPivotY(view, view.getHeight() * 0.5f);
        ViewHelper.setScaleX(view, 1 + position);
        ViewHelper.setScaleY(view, 1 + position);

        if (position > 1.0f) {
            ViewHelper.setAlpha(view, 0);
        } else {
            ViewHelper.setAlpha(view, 1);
        }
    }

}