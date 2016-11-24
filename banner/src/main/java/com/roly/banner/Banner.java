package com.roly.banner;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.roly.banner.transformer.PageTransformer;
import com.roly.banner.transformer.TransitionEffect;
import com.roly.banner.util.ScreenUtil;
import com.roly.banner.widget.BannerViewPager;
import com.roly.banner.widget.TimerHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by roly on 2016/11/16.
 * 循环广告页
 */
public class Banner extends RelativeLayout {

    //banner适配器大小的倍数
    private final int MULTIPLES = 10;

    private Context context;
    private BannerViewPager vpBanner;
    private BannerAdapter bannerAdapter;

    //指示器
    private LinearLayout llPoint;
    private List<Indicator> indicators;
    //指示器默认参数
    private int indicatorNormalRes = R.drawable.ic_point_normal;
    private int indicatorSelectedRes = R.drawable.ic_point_selected;
    private int indicatorWidth = 7;
    private int indicatorHeight = 7;
    private int indicatorInterval = 4;

    //自动循环显示时间
    private long timePeriod = 5000;
    private TimerHelper timerHelper;
    private boolean isAutoPlay = false;
    //之前显示的图片
    private int preSelect = -1;
    //当前显示图片
    private int nowSelect = 0;

    private int layoutResId;
    private boolean isCyclePlay;
    private int dataSize;
    private Adapter adapter;

    private ViewPager.OnPageChangeListener onPageChangeListener;

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

        indicators = new ArrayList<>();

        timerHelper = new TimerHelper() {
            @Override
            public void run() {
                if (isAutoPlay)
                    post(new Runnable() {
                        @Override
                        public void run() {
                            if (bannerAdapter != null) {
                                int count = bannerAdapter.getCount();
                                if (count > 2) {
                                    int index = vpBanner.getCurrentItem();
                                    index = index % (count - 2) + 1;
                                    vpBanner.setCurrentItem(index);
                                }
                            }
                        }
                    });
            }
        };

    }

    /**
     * 设置自动播放时间周期
     *
     * @param period 周期
     */
    public void setAutoPlayTimePeriod(long period) {
        timePeriod = period;
    }

    /**
     * 是否自动播放
     *
     * @param isAutoPlay 默认false
     */
    public void setIsAutoPlay(boolean isAutoPlay) {
        this.isAutoPlay = isAutoPlay;
    }

    /**
     * 自动播放开、关
     *
     * @param visibility
     */
    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == VISIBLE)
            timerHelper.start(timePeriod, timePeriod);
        else
            timerHelper.stop();
    }

    /**
     * 处理自动播放和手动播放冲突
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if (action == MotionEvent.ACTION_DOWN
                || action == MotionEvent.ACTION_MOVE) {
            timerHelper.stop();
        } else if (action == MotionEvent.ACTION_UP) {
            timerHelper.start(timePeriod, timePeriod);
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 设置页面切换动画
     *
     * @param effect
     */
    public void setPageTransformer(TransitionEffect effect) {
        if (vpBanner != null) {
            vpBanner.setPageTransformer(true, PageTransformer.getPageTransformer(effect));
        }
    }

    public interface Adapter {
        void fillBannerItem(View view, int position);
    }

    /**
     * 设置自定义布局、数据大小、适配器
     *
     * @param layoutResId 自定义布局
     * @param isCyclePlay 是否循环播放
     * @param dataSize    数据大小
     * @param adapter     适配器
     */
    public void setAdapter(@LayoutRes int layoutResId, boolean isCyclePlay, int dataSize, Adapter adapter) {
        this.layoutResId = layoutResId;
        this.isCyclePlay = isCyclePlay;
        this.dataSize = dataSize;
        this.adapter = adapter;
        if (dataSize > 0 && MULTIPLES * dataSize < Integer.MAX_VALUE) {
            bannerAdapter = new BannerAdapter();
            showBanner();
        } else {
            throw new IllegalArgumentException("dataSize out of range");
        }
    }

    private void showBanner() {
        if (dataSize == 1) {
            vpBanner.setScrollable(false);
        } else {
            showIndicator();
            vpBanner.setCurrentItem(nowSelect);
            vpBanner.addOnPageChangeListener(bannerAdapter);
        }
        vpBanner.setAdapter(bannerAdapter);
    }

    /**
     * 显示指示器
     */
    private void showIndicator() {
        indicators.clear();
        llPoint.removeAllViews();
        for (int i = 0; i < dataSize; i++) {
            indicators.add(new Indicator(context, llPoint, i == 0));
        }
        changeIndicator(nowSelect);
    }

    /**
     * 修改指示器
     */
    private void changeIndicator(int position) {
        preSelect = nowSelect;
        nowSelect = position;
        if (preSelect != -1) {
            indicators.get(preSelect).setFocus(false);
        }
        if (nowSelect != -1) {
            indicators.get(nowSelect).setFocus(true);
        }
    }

    private class BannerAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener {

        private int FAKE_BANNER_SIZE = dataSize * MULTIPLES;
        private int DEFAULT_BANNER_SIZE = dataSize;

        @Override
        public Object instantiateItem(final ViewGroup container, int position) {
            position %= DEFAULT_BANNER_SIZE;
            View view = LayoutInflater.from(context).inflate(layoutResId, container, false);
            adapter.fillBannerItem(view, position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public void finishUpdate(ViewGroup container) {
            if (isCyclePlay) {
                int position = vpBanner.getCurrentItem();
                if (position == 0) {
                    position = DEFAULT_BANNER_SIZE;
                    vpBanner.setCurrentItem(position, false);
                } else if (position == FAKE_BANNER_SIZE - 1) {
                    position = DEFAULT_BANNER_SIZE - 1;
                    vpBanner.setCurrentItem(position, false);
                }
            }
        }

        @Override
        public int getCount() {
            return isCyclePlay ? FAKE_BANNER_SIZE : DEFAULT_BANNER_SIZE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void onPageSelected(int position) {
            position %= DEFAULT_BANNER_SIZE;
            changeIndicator(position);
            if (onPageChangeListener != null) {
                onPageChangeListener.onPageSelected(position);
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (onPageChangeListener != null) {
                onPageChangeListener.onPageScrolled(position % DEFAULT_BANNER_SIZE, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (onPageChangeListener != null) {
                onPageChangeListener.onPageScrollStateChanged(state);
            }
        }
    }

    /**
     * 添加ViewPager滚动监听器
     *
     * @param onPageChangeListener 滚动监听
     */
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }

    /**
     * 设置指示器
     *
     * @param indicatorWidth    指示器宽度
     * @param indicatorHeight   指示器高度
     * @param indicatorInterval 指示器间距
     */
    public void setIndicators(int indicatorWidth, int indicatorHeight, int indicatorInterval) {
        setIndicators(indicatorNormalRes, indicatorSelectedRes, indicatorWidth, indicatorHeight, indicatorInterval);
    }

    /**
     * 设置指示器
     *
     * @param indicatorNormalRes   未选中状态图标
     * @param indicatorSelectedRes 选中状态图标
     */
    public void setIndicators(@DrawableRes int indicatorNormalRes, @DrawableRes int indicatorSelectedRes) {
        setIndicators(indicatorNormalRes, indicatorSelectedRes, indicatorWidth, indicatorHeight, indicatorInterval);
    }

    /**
     * 设置指示器
     *
     * @param indicatorNormalRes   未选中状态图标
     * @param indicatorSelectedRes 选中状态图标
     * @param indicatorWidth       指示器宽度
     * @param indicatorHeight      指示器高度
     * @param indicatorInterval    指示器间距
     */
    public void setIndicators(@DrawableRes int indicatorNormalRes, @DrawableRes int indicatorSelectedRes, int indicatorWidth, int indicatorHeight, int indicatorInterval) {
        this.indicatorNormalRes = indicatorNormalRes;
        this.indicatorSelectedRes = indicatorSelectedRes;
        this.indicatorWidth = indicatorWidth;
        this.indicatorHeight = indicatorHeight;
        this.indicatorInterval = indicatorInterval;
        showIndicator();
    }

    /**
     * 自定义指示器，可以设置长、宽和间距
     */
    private class Indicator {
        private Context context;
        private View point;

        public Indicator(Context context, LinearLayout parent, boolean isFirst) {
            this.context = context;
            point = new View(context);
            point.setBackgroundDrawable(context.getResources().getDrawable(indicatorNormalRes));
            parent.addView(point);
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) point.getLayoutParams();
            //设置长宽和间距
            params.width = ScreenUtil.dipToPx(context, indicatorWidth);
            params.height = ScreenUtil.dipToPx(context, indicatorHeight);
            if (!isFirst) {
                //设置间距
                params.leftMargin = ScreenUtil.dipToPx(context, indicatorInterval);
            }
            point.setLayoutParams(params);
        }

        public void setFocus(boolean isFocus) {
            if (isFocus) {
                point.setBackgroundDrawable(context.getResources().getDrawable(indicatorSelectedRes));
            } else {
                point.setBackgroundDrawable(context.getResources().getDrawable(indicatorNormalRes));
            }
        }
    }
}
