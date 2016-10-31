package edu.jnu.banner.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import edu.jnu.banner.R;
import edu.jnu.banner.util.ScreenUtil;

/**
 * Created by roly on 2016/10/14.
 *
 */
public class LoopImagePoint {
    private Context context;
    private View point;

    public LoopImagePoint(Context context, LinearLayout parent, boolean isFirst) {
        this.context = context;
        point = new View(context);
        point.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_point_normal));
        parent.addView(point);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) point.getLayoutParams();
        params.width = ScreenUtil.dipToPx(context, 30);
        params.height = ScreenUtil.dipToPx(context, 3);
        if (!isFirst) {
            params.leftMargin = ScreenUtil.dipToPx(context, 3);
        }
        point.setLayoutParams(params);
    }

    public void setFocus(boolean isFocus) {
        if (isFocus) {
            point.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_point_selected));
        } else {
            point.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_point_normal));
        }
    }
}
