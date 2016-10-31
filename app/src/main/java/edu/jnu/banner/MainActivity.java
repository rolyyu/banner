package edu.jnu.banner;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.jnu.banner.Entity.BannerBean;
import edu.jnu.banner.adapter.BannerAdapter;
import edu.jnu.banner.widget.LoopImagePoint;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.vp_loop_banner)
    ViewPager vpLoopBanner;
    @BindView(R.id.ll_point)
    LinearLayout llPoint;
    //自动循环显示时间
    private int INTERVAL_TIME = 3000;
    private Timer mTimer = new Timer();

    //横幅下面的点
    private List<LoopImagePoint> loopImagePoints;
    //之前显示的图片
    private int preSelect = -1;
    //当前显示图片
    private int nowSelect = 0;

    private boolean isUserTouched = false;
    private BannerAdapter bannerAdapter;
    private List<BannerBean> bannerBeans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        bannerBeans = new ArrayList<>();
        loopImagePoints = new ArrayList<>();
        initData();
        showBanner();
    }

    private void initData() {
        for(String image:Constant.images) {
            BannerBean bannerBean = new BannerBean();
            bannerBean.setImage(image);
            bannerBean.setTitle("Title");
            bannerBeans.add(bannerBean);
        }
    }

    private void showBanner() {
        for (int i = 0; i < bannerBeans.size(); i++) {
            loopImagePoints.add(new LoopImagePoint(this, llPoint, i == 0));
        }
        changeLoopPoint(nowSelect);
        if (bannerAdapter == null) {
            bannerAdapter = new BannerAdapter(this, vpLoopBanner, bannerBeans);
            bannerAdapter.setOnIndicatorChangeListener(new BannerAdapter.OnIndicatorChangeListener() {
                @Override
                public void OnIndicatorChange(int position) {
                    changeLoopPoint(position);
                }
            });
            vpLoopBanner.setAdapter(bannerAdapter);
            vpLoopBanner.setCurrentItem(nowSelect);
            vpLoopBanner.addOnPageChangeListener(bannerAdapter);
            vpLoopBanner.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int action = event.getAction();
                    if (action == MotionEvent.ACTION_DOWN
                            || action == MotionEvent.ACTION_MOVE) {
                        isUserTouched = true;
                    } else if (action == MotionEvent.ACTION_UP) {
                        isUserTouched = false;
                    }
                    return false;
                }
            });
        } else {
            bannerAdapter.notifyDataSetChanged();
        }
        mTimer.schedule(mTimerTask, INTERVAL_TIME, INTERVAL_TIME);
    }

    /**
     * 修改循环图片下方的点
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


    private TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {
            if (!isUserTouched) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int count = bannerAdapter.getCount();
                        if (count > 2) {
                            int index = vpLoopBanner.getCurrentItem();
                            index = index % (count - 2) + 1;
                            vpLoopBanner.setCurrentItem(index);
                            Log.d("index",index+"");
                        }
                    }
                });
            }
        }
    };
}
