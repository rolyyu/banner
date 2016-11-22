package edu.jnu.banner;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import edu.jnu.banner.util.ScreenUtil;
import edu.jnu.banner.widget.BannerViewPager;
import edu.jnu.banner.widget.Indicator;
import edu.jnu.banner.widget.TimerHelper;

/**
 * Created by roly on 2016/11/16.
 * 循环广告页
 */
public class Banner extends RelativeLayout {

    private Context context;
    private BannerViewPager vpBanner;
    private BannerAdapter bannerAdapter;

    //指示器
    private LinearLayout llPoint;
    private List<Indicator> indicators;

    private boolean isAutoPlay = false;
    //自动循环显示时间
    private long TIME_PERIOD = 3000;
    private TimerHelper timerHelper;
    //之前显示的图片
    private int preSelect = -1;
    //当前显示图片
    private int nowSelect = 0;
    private boolean isUserTouched = false;

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
     * 设置自动播放时间周期
     *
     * @param period 周期
     */
    public void setAutoPlayTimePeriod(long period) {
        TIME_PERIOD = period;
    }

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
        if (isAutoPlay) {
            if (visibility == VISIBLE)
                timerHelper.start(TIME_PERIOD, TIME_PERIOD);
            else
                timerHelper.stop();
        }
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
            isUserTouched = true;
        } else if (action == MotionEvent.ACTION_UP) {
            isUserTouched = false;
        }
        return super.dispatchTouchEvent(ev);
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
        if (dataSize > 0 && dataSize < 200) {
            bannerAdapter = new BannerAdapter();
            showBanner();
        } else {
            throw new IllegalArgumentException("dataSize out of range");
        }
    }

    private void showBanner() {
        if (dataSize == 1) {
            vpBanner.setScrollable(false);
            setIsAutoPlay(false);
        } else {
            for (int i = 0; i < dataSize; i++) {
                indicators.add(new Indicator(context, llPoint, i == 0));
            }
            changeIndicator(nowSelect);
            vpBanner.setCurrentItem(nowSelect);
            vpBanner.addOnPageChangeListener(bannerAdapter);
        }
        vpBanner.setAdapter(bannerAdapter);
    }

    private class BannerAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener {

        private final int FAKE_BANNER_SIZE = 200;
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
            if (onPageChangeListener != null){
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
}
