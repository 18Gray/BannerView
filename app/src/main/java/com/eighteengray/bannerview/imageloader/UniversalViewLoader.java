package com.eighteengray.bannerview.imageloader;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;


/**
 * Created by lutao on 2017/2/21.
 */
public class UniversalViewLoader implements IImageLoader
{
    private Context context;
    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(null)
            .showImageForEmptyUri(null)
            .showImageOnFail(null)
            .cacheInMemory(true)
            .cacheOnDisc(true)
            .build();


    public UniversalViewLoader(Context c)
    {
        this.context = c;
    }


    @Override
    public View loadWebImage(String url)
    {
        ImageView imageView = new ImageView(context);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(layoutParams);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ImageLoader.getInstance().displayImage(url, imageView, options);
        return null;
    }
}
