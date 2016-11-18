package edu.jnu.banner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import edu.jnu.banner.Entity.BannerBean;
import edu.jnu.banner.util.ImageUtil;

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

        banner.setAdapter(R.layout.item_banner, bannerBeans.size(), new Banner.Adapter() {
            @Override
            public void fillBannerItem(View view, final int position) {
                ImageView imageView = (ImageView) view.findViewById(R.id.img_banner);
                TextView textView = (TextView) view.findViewById(R.id.text_banner_title);
                final BannerBean bannerBean = bannerBeans.get(position);
                if (!bannerBean.getImage().equals(""))
                    ImageUtil.load(MainActivity.this, imageView, bannerBean.getImage());
                textView.setText(bannerBean.getTitle());

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this,"item"+position+"click",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        banner.setIsAutoPlay(false);
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
