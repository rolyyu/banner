package com.roly.bannerdemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.roly.banner.Banner;
import com.roly.banner.transformer.TransitionEffect;
import com.roly.bannerdemo.Entity.GuideBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by roly on 2016/11/21.
 * 引导页
 */
public class GuideActivity extends Activity {
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.btn_enter)
    Button btnEnter;
    @BindView(R.id.tv_skip)
    TextView tvSkip;
    @BindView(R.id.bg_banner)
    Banner bgBanner;
    private List<GuideBean> guideBeans;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);

        guideBeans = new ArrayList<>();
        initData();

        final int dataSize = guideBeans.size();
        banner.setAdapter(R.layout.item_guide, false, dataSize, new Banner.Adapter() {
            @Override
            public void fillBannerItem(View view, int position) {
                ImageView imageView = (ImageView) view.findViewById(R.id.img_banner);
                imageView.setImageResource(guideBeans.get(position).getUpperGuideImg());
            }
        });
        banner.setPageTransformer(TransitionEffect.Rotate);

        bgBanner.setAdapter(R.layout.item_bg_guide, false, dataSize, new Banner.Adapter() {
            @Override
            public void fillBannerItem(View view, int position) {
                ImageView imageView = (ImageView) view.findViewById(R.id.img_banner);
                imageView.setImageResource(guideBeans.get(position).getLowerGuideImg());
                TextView textView = (TextView) view.findViewById(R.id.tv_intro);
                textView.setText(guideBeans.get(position).getLowerGuideIntro());
            }
        });
        bgBanner.setPageTransformer(TransitionEffect.Cube);

        banner.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == dataSize - 1) {
                    btnEnter.setVisibility(View.VISIBLE);
                    tvSkip.setVisibility(View.GONE);
                } else {
                    btnEnter.setVisibility(View.GONE);
                    tvSkip.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void initData() {
        for(int i = 0;i < Constant.upperGuideImg.length;i++){
            GuideBean guideBean = new GuideBean();
            guideBean.setUpperGuideImg(Constant.upperGuideImg[i]);
            guideBean.setLowerGuideImg(Constant.lowerGuideImg[i]);
            guideBean.setLowerGuideIntro(Constant.lowerGuideIntro[i]);
            guideBeans.add(guideBean);
        }
    }

    @OnClick({R.id.tv_skip, R.id.btn_enter})
    public void onClick(View view) {
        MainActivity.launch(GuideActivity.this);
        finish();
    }
}
