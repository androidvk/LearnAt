package com.coremacasia.learnat.utility;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.coremacasia.learnat.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ImageSetterGlide {
    public void circleImg(Context context, String link, CircleImageView imageView) {

        Glide
                .with(context.getApplicationContext())
                .load(link)
                .fitCenter()
                .override(200, 200)
                .skipMemoryCache(true)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);

        // chatEx headerIMg
        //forum user img
        //chat User Img

    }

    public void userCircleImage(Context context, String link, CircleImageView imageView) {

        Glide
                .with(context.getApplicationContext())
                .load(link)
                .fitCenter()
                .override(250, 250)
                .skipMemoryCache(true)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);


    }

    public void defaultImg(Context context, String link, ImageView imageView) {

        Glide.with(context).load(link)
                .thumbnail(0.1f)
                .override(500, 500)
                .skipMemoryCache(true)
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);

    }

    public void localImg(Context context, Uri link, ImageView imageView) {

        Glide.with(context).load(link)
                .thumbnail(0.1f)
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(imageView);

    }


    public static boolean isValidContextForGlide(final Context context) {
        if (context == null) {
            return false;
        }
        if (context instanceof Activity) {
            final Activity activity = (Activity) context;
            if (activity.isDestroyed() || activity.isFinishing()) {
                return false;
            }
        }
        return true;
    }
}
