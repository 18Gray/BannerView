package com.eighteengray.bannerview.imageloader;

import android.content.Context;
import android.net.Uri;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by lutao on 2017/2/21.
 */

public class FrescoViewLoader implements IImageLoader
{
    private Context context;

    public FrescoViewLoader(Context c)
    {
        this.context = c;
    }


    @Override
    public View loadWebImage(String url)
    {
        SimpleDraweeView simpleDraweeView = new SimpleDraweeView(context);
        simpleDraweeView.setImageURI(Uri.parse(url));
        return simpleDraweeView;
    }
}
