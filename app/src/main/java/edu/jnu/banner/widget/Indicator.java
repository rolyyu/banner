package edu.jnu.banner.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import edu.jnu.banner.R;
import edu.jnu.banner.util.ScreenUtil;

/**
 * Created by roly on 2016/10/14.
 * 自定义指示器，可以设置长、宽和间距
 */
public class Indicator {
    private Context context;
    private View point;

    public Indicator(Context context, LinearLayout parent, boolean isFirst) {
        this.context = context;
        point = new View(context);
        point.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_point_normal2));
        parent.addView(point);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) point.getLayoutParams();
        //设置长宽和间距
        params.width = ScreenUtil.dipToPx(context, 7);
        params.height = ScreenUtil.dipToPx(context, 7);
        if (!isFirst) {
            //设置间距
            params.leftMargin = ScreenUtil.dipToPx(context, 3);
        }
        point.setLayoutParams(params);
    }

    public void setFocus(boolean isFocus) {
        if (isFocus) {
            point.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_point_selected2));
        } else {
            point.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_point_normal2));
        }
    }
}
