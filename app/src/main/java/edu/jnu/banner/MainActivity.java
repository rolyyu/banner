package edu.jnu.banner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import edu.jnu.banner.Entity.BannerBean;

public class MainActivity extends AppCompatActivity {

    private List<BannerBean> bannerBeans;
    private Banner banner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        bannerBeans = new ArrayList<>();
        initData();

        banner = (Banner) findViewById(R.id.banner);

        banner.setData(bannerBeans);
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
