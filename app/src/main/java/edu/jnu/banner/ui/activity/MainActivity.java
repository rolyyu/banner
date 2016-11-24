package edu.jnu.banner.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.jnu.banner.Banner;
import edu.jnu.banner.Constant;
import edu.jnu.banner.Entity.BannerBean;
import edu.jnu.banner.R;
import edu.jnu.banner.util.ImageUtil;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.banner)
    Banner banner;

    private List<BannerBean> bannerBeans;

    public static void launch(Context context){
        Intent starter = new Intent(context,MainActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        bannerBeans = new ArrayList<>();
        initData();

        banner.setAdapter(R.layout.item_banner,true , bannerBeans.size(), new Banner.Adapter() {
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
        banner.setIsAutoPlay(true);
        banner.setIndicators(R.drawable.ic_point_normal_1, R.drawable.ic_point_selected_1, 20, 2, 4);

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
