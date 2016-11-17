package edu.jnu.banner.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import edu.jnu.banner.Entity.BannerBean;
import edu.jnu.banner.R;
import edu.jnu.banner.util.ImageUtil;

/**
 * Created by roly on 2016/10/15.
 * 循环广告页
 */
public class BannerAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener {

    private static final String TAG = "BannerAdapter";

    private Context context;

    private final int FAKE_BANNER_SIZE = 100;
    private int DEFAULT_BANNER_SIZE;
    private ViewPager mBanner;
    private List<BannerBean> bannerBeans;
    private LayoutInflater mInflater;

    public BannerAdapter(Context context, ViewPager viewPager, List<BannerBean> bannerBeans) {
        this.context = context;
        this.mBanner = viewPager;
        mInflater = LayoutInflater.from(context);
        this.bannerBeans = bannerBeans;
        DEFAULT_BANNER_SIZE = bannerBeans.size();
    }

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {
        position %= DEFAULT_BANNER_SIZE;
        View view = mInflater.inflate(R.layout.item_banner, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.img_banner);
        TextView textView = (TextView) view.findViewById(R.id.text_banner_title);
        final BannerBean bannerBean = bannerBeans.get(position);
        if (!bannerBean.getImage().equals(""))
            ImageUtil.load(context, imageView, bannerBean.getImage());
        textView.setText(bannerBean.getTitle());
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        int position = mBanner.getCurrentItem();
        if (position == 0) {
            position = DEFAULT_BANNER_SIZE;
            mBanner.setCurrentItem(position, false);
        } else if (position == FAKE_BANNER_SIZE - 1) {
            position = DEFAULT_BANNER_SIZE - 1;
            mBanner.setCurrentItem(position, false);
        }
    }

    @Override
    public int getCount() {
        return FAKE_BANNER_SIZE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        position %= DEFAULT_BANNER_SIZE;
        if (onIndicatorChangeListener != null)
            onIndicatorChangeListener.OnIndicatorChange(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    private OnIndicatorChangeListener onIndicatorChangeListener;

    public interface OnIndicatorChangeListener {
        void OnIndicatorChange(int position);
    }

    /**
     * 指示器改变
     * @param onIndicatorChangeListener
     */
    public void setOnIndicatorChangeListener(OnIndicatorChangeListener onIndicatorChangeListener) {
        this.onIndicatorChangeListener = onIndicatorChangeListener;
    }
}
