package edu.jnu.banner;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.jnu.banner.Entity.BannerBean;
import edu.jnu.banner.adapter.BannerAdapter;
import edu.jnu.banner.util.ScreenUtil;
import edu.jnu.banner.widget.BannerViewPager;
import edu.jnu.banner.widget.LoopImagePoint;
import edu.jnu.banner.widget.TimerHelper;

/**
 * Created by roly on 2016/11/16.
 * 循环广告页
 */
public class Banner extends RelativeLayout {

    private Context context;
    private LinearLayout llPoint;
    private BannerViewPager vpBanner;

    //自动循环显示时间
    private int INTERVAL_TIME = 3000;
    private TimerHelper timerHelper;

    //横幅下面的点
    private List<LoopImagePoint> loopImagePoints;
    //之前显示的图片
    private int preSelect = -1;
    //当前显示图片
    private int nowSelect = 0;

    private boolean isUserTouched = false;
    private BannerAdapter bannerAdapter;
    private List<BannerBean> bannerBeans;


    public Banner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Banner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    private void initView() {

        vpBanner = new BannerViewPager(context);
        addView(vpBanner, new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT)
        );

        llPoint = new LinearLayout(context);
        llPoint.setOrientation(LinearLayout.HORIZONTAL);
        RelativeLayout.LayoutParams rlParams = new RelativeLayout.LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT
        );
        rlParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rlParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        rlParams.bottomMargin = ScreenUtil.dipToPx(context, 16);
        addView(llPoint, rlParams);

        bannerBeans = new ArrayList<>();
        loopImagePoints = new ArrayList<>();

        initData();
        showBanner();

        timerHelper = new TimerHelper() {
            @Override
            public void run() {
                if (!isUserTouched) {
                    post(new Runnable() {
                        @Override
                        public void run() {
                            if (bannerAdapter != null) {
                                int count = bannerAdapter.getCount();
                                if (count > 2) {
                                    int index = vpBanner.getCurrentItem();
                                    index = index % (count - 2) + 1;
                                    vpBanner.setCurrentItem(index);
                                    Log.d("index", index + "");
                                }
                            }
                        }
                    });
                }
            }
        };

    }

    /**
     * 自动播放开、关
     * @param visibility
     */
    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if(visibility == VISIBLE)
            timerHelper.start(INTERVAL_TIME,INTERVAL_TIME);
        else
            timerHelper.stop();
    }

    /**
     * 处理自动播放和手动播放冲突
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if (action == MotionEvent.ACTION_DOWN
                || action == MotionEvent.ACTION_MOVE) {
            isUserTouched = true;
        } else if (action == MotionEvent.ACTION_UP) {
            isUserTouched = false;
        }
        return super.dispatchTouchEvent(ev);
    }

    private void initData() {
        for (String image : Constant.images) {
            BannerBean bannerBean = new BannerBean();
            bannerBean.setImage(image);
            bannerBean.setTitle("Title");
            bannerBeans.add(bannerBean);
        }
    }

    private void showBanner() {
        for (int i = 0; i < bannerBeans.size(); i++) {
            loopImagePoints.add(new LoopImagePoint(context, llPoint, i == 0));
        }
        changeLoopPoint(nowSelect);
        if (bannerAdapter == null) {
            bannerAdapter = new BannerAdapter(context, vpBanner, bannerBeans);
            bannerAdapter.setOnIndicatorChangeListener(new BannerAdapter.OnIndicatorChangeListener() {
                @Override
                public void OnIndicatorChange(int position) {
                    changeLoopPoint(position);
                }
            });
            vpBanner.setAdapter(bannerAdapter);
            vpBanner.setCurrentItem(nowSelect);
            vpBanner.addOnPageChangeListener(bannerAdapter);
        } else {
            bannerAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 修改指示器
     */
    private void changeLoopPoint(int position) {
        preSelect = nowSelect;
        nowSelect = position;
        if (preSelect != -1) {
            loopImagePoints.get(preSelect).setFocus(false);
        }
        if (nowSelect != -1) {
            loopImagePoints.get(nowSelect).setFocus(true);
        }
    }
}
