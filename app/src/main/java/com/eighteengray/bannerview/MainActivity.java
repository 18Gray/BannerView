package com.eighteengray.bannerview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import com.eighteengray.bannerview.action.OnItemClickListener;
import com.eighteengray.bannerview.bannerview.BannerView;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements OnItemClickListener
{
    BannerView bv_main;
    ArrayList<String> picStringList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bv_main = (BannerView) findViewById(R.id.bv_main);

        //设置图片网址即可让图片显示
        picStringList.add("http://pic3.qiyipic.com/common/lego/20160118/a250dea3ef1848ee8ebcb5bfbca6c63f.jpg");
        picStringList.add("http://pic2.qiyipic.com/common/lego/20160118/f1b23e53753c46ffa968468431725568.jpg");
        picStringList.add("http://pic9.qiyipic.com/common/lego/20160118/42c62b77afe94a03977f41cb4a691bfe.jpg");
        bv_main.setBannerList(picStringList);
        bv_main.setPointImage(R.drawable.seekbar_set_qian, R.drawable.seekbar_set_hou, 0);
        bv_main.setOnItemClickListener(this);

        bv_main.setViewPagerWidthHeight(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        bv_main.setScaleType(ImageView.ScaleType.CENTER);
        bv_main.setPointContainerMargin(0, 0, 0, 10);
        bv_main.setPointWidthHeight(20, 20);
        bv_main.setPointImageMargin(10, 10, 10, 10);
        bv_main.bannerStartPlay(1000, 3000);
    }


    @Override
    protected void onResume()
    {
        super.onResume();

    }





    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        bv_main.bannerStopPlay();
    }

    @Override
    public void onItemClickListener(View view, int position)
    {
        String url = picStringList.get(position);
        Toast.makeText(MainActivity.this, "第"+position+"张图" + "  url=" + url, Toast.LENGTH_LONG).show();
    }

}
