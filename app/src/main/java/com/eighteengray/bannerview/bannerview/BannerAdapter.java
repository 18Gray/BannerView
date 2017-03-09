package com.eighteengray.bannerview.bannerview;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;


public class BannerAdapter extends PagerAdapter
{
    Context context;
    private List<View> viewArrayList;

    public BannerAdapter(Context c, List<View> arrayList)
    {
        this.context = c;
        this.viewArrayList = arrayList;
    }

    @Override
    public int getCount()
    {
        return viewArrayList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o)
    {
        return view == o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        container.addView(viewArrayList.get(position), 0);
        return viewArrayList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        container.removeView(viewArrayList.get(position));
    }
}
