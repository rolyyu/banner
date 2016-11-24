package com.roly.bannerdemo.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.roly.bannerdemo.R;
import com.squareup.picasso.Picasso;

public class ImageUtil {

    public static void load(Context context, ImageView imageView, String url) {
        if (TextUtils.isEmpty(url)) {
            imageView.setImageResource(R.drawable.ic_shadow);
            return;
        }
        Picasso.with(context).load(url)
                .placeholder(R.drawable.ic_shadow)
                .error(R.drawable.ic_shadow)
                .into(imageView);
    }

}
