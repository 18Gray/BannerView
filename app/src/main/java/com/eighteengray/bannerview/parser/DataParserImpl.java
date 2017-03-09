package com.eighteengray.bannerview.parser;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.eighteengray.bannerview.imageloader.FrescoViewLoader;
import com.eighteengray.bannerview.imageloader.IImageLoader;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.resource;


/**
 * Created by lutao on 2017/2/21.
 */
public class DataParserImpl<T> implements DataParser<T>
{
    private Context context;

    public DataParserImpl(Context c)
    {
        this.context = c;
    }

    @Override
    public List<View> parse(List<T> dataList, int size)
    {
        List<View> viewList = new ArrayList<>();
        viewList.clear();
        if(dataList.size() != size) //如果不等说明是要绘制大图
        {
            //-1图
            viewList.add(createView(dataList.get(dataList.size() - 1)));

            //中间图
            for (T data:dataList)
            {
                viewList.add(createView(data));
            }
            //+1图
            viewList.add(createView(dataList.get(0)));
        }
        else   //相等说明要绘制小点
        {
            for (T data:dataList)
            {
                viewList.add(createView(data));
            }
        }
        return viewList;
    }

    @Override
    public View createView(T data)
    {
        if(data instanceof String)
        {
            IImageLoader viewLoader = new FrescoViewLoader(context);  //这里用的是Fresco加载图像，可以换做UniversalImageLoader
            return viewLoader.loadWebImage((String) data);
        } else if (data instanceof Bitmap)
        {
            ImageView imageView = new ImageView(context);
            imageView.setImageBitmap((Bitmap) data);
            return imageView;
        }
        else if(data instanceof Drawable)
        {
            ImageView imageView = new ImageView(context);
            imageView.setImageDrawable((Drawable) data);
            return imageView;
        }
        else if(data instanceof Integer)
        {
            ImageView imageView = new ImageView(context);
            imageView.setImageResource((Integer) data);
            return imageView;
        }
        else
        {
            return null;
        }
    }


}
