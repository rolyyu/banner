package edu.jnu.banner.transformer;

import android.view.View;

import com.nineoldandroids.view.ViewHelper;


public class AlphaPageTransformer extends PageTransformer {
    private float mMinScale = 0.0f;

    @Override
    public void handleInvisiblePage(View view, float position) {
        ViewHelper.setAlpha(view, 0);
    }

    @Override
    public void handleLeftPage(View view, float position) {
        ViewHelper.setAlpha(view, mMinScale + (1 - mMinScale) * (1 + position));
    }

    @Override
    public void handleRightPage(View view, float position) {
        ViewHelper.setAlpha(view, mMinScale + (1 - mMinScale) * (1 - position));
    }
}