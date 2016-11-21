package edu.jnu.banner.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.jnu.banner.Banner;
import edu.jnu.banner.Constant;
import edu.jnu.banner.Entity.BannerBean;
import edu.jnu.banner.R;
import edu.jnu.banner.util.ImageUtil;

/**
 * Created by roly on 2016/11/21.
 * 引导页
 */
public class GuideActivity extends AppCompatActivity {
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.btn_enter)
    Button btnEnter;
    private List<BannerBean> bannerBeans;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);

        bannerBeans = new ArrayList<>();
        initData();

        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.launch(GuideActivity.this);
                finish();
            }
        });

        final int dataSize = bannerBeans.size();
        banner.setAdapter(R.layout.item_guide, false, dataSize, new Banner.Adapter() {
            @Override
            public void fillBannerItem(View view, int position) {
                ImageView imageView = (ImageView) view.findViewById(R.id.img_banner);

                final BannerBean bannerBean = bannerBeans.get(position);
                if (!bannerBean.getImage().equals(""))
                    ImageUtil.load(GuideActivity.this, imageView, bannerBean.getImage());

            }
        });
        banner.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if(position == dataSize - 1)
                    btnEnter.setVisibility(View.VISIBLE);
                else
                    btnEnter.setVisibility(View.GONE);
            }
        });
    }

    private void initData() {
        for (String image : Constant.images) {
            BannerBean bannerBean = new BannerBean();
            bannerBean.setImage(image);
            bannerBean.setTitle("Title");
            bannerBeans.add(bannerBean);
        }
    }
}
