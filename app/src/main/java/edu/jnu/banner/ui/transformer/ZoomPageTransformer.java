package edu.jnu.banner.ui.transformer;

import android.view.View;

import com.nineoldandroids.view.ViewHelper;

public class ZoomPageTransformer extends PageTransformer {
    private float mMinScale = 0.85f;
    private float mMinAlpha = 0.65f;

    @Override
    public void handleInvisiblePage(View view, float position) {
        ViewHelper.setAlpha(view, 0);
    }

    @Override
    public void handleLeftPage(View view, float position) {
        float scale = Math.max(mMinScale, 1 + position);
        float vertMargin = view.getHeight() * (1 - scale) / 2;
        float horzMargin = view.getWidth() * (1 - scale) / 2;
        ViewHelper.setTranslationX(view, horzMargin - vertMargin / 2);
        ViewHelper.setScaleX(view, scale);
        ViewHelper.setScaleY(view, scale);
        ViewHelper.setAlpha(view, mMinAlpha + (scale - mMinScale) / (1 - mMinScale) * (1 - mMinAlpha));
    }

    @Override
    public void handleRightPage(View view, float position) {
        float scale = Math.max(mMinScale, 1 - position);
        float vertMargin = view.getHeight() * (1 - scale) / 2;
        float horzMargin = view.getWidth() * (1 - scale) / 2;
        ViewHelper.setTranslationX(view, -horzMargin + vertMargin / 2);
        ViewHelper.setScaleX(view, scale);
        ViewHelper.setScaleY(view, scale);
        ViewHelper.setAlpha(view, mMinAlpha + (scale - mMinScale) / (1 - mMinScale) * (1 - mMinAlpha));
    }
}