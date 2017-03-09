package com.eighteengray.bannerview.bannerview;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.eighteengray.bannerview.R;
import com.eighteengray.bannerview.action.OnItemClickListener;
import com.eighteengray.bannerview.parser.DataParser;
import com.eighteengray.bannerview.parser.DataParserImpl;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;



public class BannerView extends LinearLayout
{
	Context context;
	LayoutInflater inflater;

	//大图
	public ViewPager viewPager;
	public BannerAdapter adapter;
	private List<View> viewArrayList = new ArrayList<>();
	private OnItemClickListener onItemClickListener;
	private DataParser dataParserBanner;

	//小点
	private ViewGroup viewGroup;
	private List<View> iconViewList = new ArrayList<>();
	private AtomicInteger what = new AtomicInteger(1);

	//保留小点的选中和未选中图标，在ViewPager切换时用
	private Object selectObject, unselectObject;

	//定时任务
	private Timer timer;
	private TimerTask timerTask;

	//执行TimeTask时候，在指定时间间隔后发送的消息到handler中处理
	private final Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			viewPager.setCurrentItem(what.get());
		}
	};


	public BannerView(Context context)
	{
		super(context);
		this.context = context;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_banner, this);
		initView();
		timer = new Timer();
	}


	public BannerView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.context = context;

		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_banner, this);
		initView();
		timer = new Timer();
	}


	private void initView()
	{
		viewPager = (ViewPager) this.findViewById(R.id.viewPager);
		viewGroup = (ViewGroup) this.findViewById(R.id.viewGroup);
		//初始化Fresco
		Fresco.initialize(context);
		//初始化ImageLoader
		File cacheDir = StorageUtils.getCacheDirectory(context);
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.taskExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
				.taskExecutorForCachedImages(AsyncTask.THREAD_POOL_EXECUTOR)
				.threadPoolSize(3).threadPriority(Thread.NORM_PRIORITY - 1).tasksProcessingOrder(QueueProcessingType.FIFO)
				.denyCacheImageMultipleSizesInMemory().memoryCache(new LruMemoryCache(2 * 1024 * 1024))
				.memoryCacheSize(1 * 1024 * 1024).discCache(new UnlimitedDiscCache(cacheDir))
				.discCacheSize(10 * 1024 * 1024).discCacheFileCount(500)
				.discCacheFileNameGenerator(new HashCodeFileNameGenerator())
				// 下载
				.imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // default
				.imageDecoder(new BaseImageDecoder(true)) // default
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
				.writeDebugLogs().build();
		ImageLoader.getInstance().init(config);
	}


	/**
	 * 更新ViewPager大图
	 */
	private void updateViewPagerView()
	{
		if(this.viewArrayList != null && this.viewArrayList.size() > 0)
		{
			adapter = new BannerAdapter(context, this.viewArrayList);
			viewPager.setAdapter(adapter);
			viewPager.setOnPageChangeListener(new OnPageChangeListener()
			{
				@Override
				public void onPageScrollStateChanged(int arg0)
				{
				}

				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2)
				{
					int size = viewArrayList.size() - 2;
					//所有页面是0、1、2、3、4，中间是1、2、3。如果当前页面arg0=1，再左滑则到3，如果当前页面arg0=
					if (arg0 < 1 && arg2 == 0)
					{
						viewPager.setCurrentItem(size, false);
					} else if (arg0 > size && arg2 == 0)
					{
						viewPager.setCurrentItem(1, false);
					} else
					{
						// to do
					}
				}

				@Override
				public void onPageSelected(int arg0)
				{
					// 当viewpager换页时 改掉下面对应的小点
					what.getAndSet(arg0);
					what.incrementAndGet();
//					setPointImage(selectObject, unselectObject, arg0 - 1);
					updatePointImage(arg0 - 1);
				}
			});
		}
	}


	/**
	 * 更新小点图标
	 */
	private void setPointView()
	{
		viewGroup.removeAllViews();
		if(iconViewList != null && iconViewList.size() > 0)
		{
			for (View view:iconViewList)
			{
				viewGroup.addView(view);
			}
		}
		viewPager.setCurrentItem(1);
	}


	public void updatePointImage(int selectPosition)
	{
		//直接循环所有的point imageview，去设置他们的图像，这样就可以不删掉所有imageview，再重绘，也不会导致icon image大小和间距的效果失效
		for(int i=0;i<iconViewList.size();i++)
		{
			View view = iconViewList.get(i);
			if(view instanceof SimpleDraweeView)
			{
				if(i == selectPosition)
				{
					String selectString = (String) selectObject;
					((SimpleDraweeView)view).setImageURI(Uri.parse(selectString));
				}
				else
				{
					String unselectString = (String) unselectObject;
					((SimpleDraweeView)view).setImageURI(Uri.parse(unselectString));
				}
			}
			else if(view instanceof ImageView)
			{
				if(i == selectPosition)
				{
					((ImageView)view).setImageResource(R.drawable.seekbar_set_qian);
				}
				else
				{
					((ImageView)view).setImageResource(R.drawable.seekbar_set_hou);
				}
			}
		}
	}


	//******************************************************************************************************************************************
	//下面是可调用方法，先设置属性，最后show时去绘制所有view.
	//*****************************************************************************************************************************************


	/**
	 * 设置大图图像，目前支持四种类型的参数，String、Bitmap、Drawable、Integer
	 * @param list 图像的数据列表
     */
	public <T> void setBannerList(List<T> list)
	{
		this.viewArrayList.clear();
		DataParser dataParser = new DataParserImpl<T>(context);
		this.dataParserBanner = dataParser;
		this.viewArrayList = dataParser.parse(list, list.size() + 2);
		updateViewPagerView();
	}


    /**
	 * 设置小点的图标，目前支持四种类型的参数，String、Bitmap、Drawable、Integer
	 * @param selectObject
	 * @param unselectObject
	 * @param selectPosition
	 * @param <T>
     */
	public <T> void setPointImage(T selectObject, T unselectObject, int selectPosition)
	{
		this.selectObject = selectObject;
		this.unselectObject = unselectObject;
		//组合数据
		List<T> pointList = new ArrayList<>();
		if(viewArrayList != null && viewArrayList.size() > 0)
		{
			int size = viewArrayList.size() - 2;
			for(int i=0; i<size;i++)
			{
				if(i == selectPosition)
				{
					pointList.add(selectObject);
				}
				else
				{
					pointList.add(unselectObject);
				}
			}
		}

		iconViewList.clear();
		DataParser dataParser = new DataParserImpl<T>(context);
		this.iconViewList = dataParser.parse(pointList, pointList.size());
		setPointView();
	}


	/**
	 * 设置点击事件
	 * @param oiclr 这是个自定义事件，针对的是大图的点击事件
     */
	public void setOnItemClickListener(OnItemClickListener oiclr)
	{
		this.onItemClickListener = oiclr;
		for(int i=1; i<viewArrayList.size() - 1; i++)
		{
			final View view = viewArrayList.get(i);
			final int finalI = i;
			view.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					onItemClickListener.onItemClickListener(view, finalI);
				}
			});
		}
	}


	//上面函数设置内容，下面函数设置样式，通过改变viewArrayList和iconViewList来改变样式

	public void setViewPagerWidthHeight(int width, int height)
	{
		ViewGroup.LayoutParams layoutParams = viewPager.getLayoutParams();
		layoutParams.width = width;
		layoutParams.height = height;
		viewPager.setLayoutParams(layoutParams);
	}


	/**
	 * 设置图像显示模式
	 * @param scaleType ImageView的显示模式ScaleType
     */
	public void setScaleType(ImageView.ScaleType scaleType)
	{
		for(int i=0;i < viewArrayList.size();i++)
		{
			View view = viewArrayList.get(i);
			if(view instanceof ImageView)
			{
				((ImageView) view).setScaleType(scaleType);
			}
		}
	}


	/**
	 * 设置多个小点的容器布局的外间距
	 * @param left
	 * @param top
	 * @param right
     * @param bottom
     */
	public void setPointContainerMargin(int left, int top, int right, int bottom)
	{
		ViewGroup.MarginLayoutParams marginLayoutParams = (MarginLayoutParams) viewGroup.getLayoutParams();
		marginLayoutParams.setMargins(left, top, right, bottom);
		viewGroup.setLayoutParams(marginLayoutParams);
	}

	/**
	 * 设置小点宽高
	 * @param width
	 * @param height
     */
	public void setPointWidthHeight(int width, int height)
	{
		if(iconViewList != null && iconViewList.size() > 0)
		{
			for(int i=0;i<iconViewList.size();i++)
			{
				View view = iconViewList.get(i);
				LinearLayout.LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
				layoutParams.width = width;
				layoutParams.height = height;
				view.setLayoutParams(layoutParams);
			}
		}
	}


	/**
	 * 设置小点的间距
	 * @param left
	 * @param top
	 * @param right
     * @param bottom
     */
	public void setPointImageMargin(int left, int top, int right, int bottom)
	{
		if(iconViewList != null && iconViewList.size() > 0)
		{
			for(int i=0;i<iconViewList.size();i++)
			{
				View view = iconViewList.get(i);
				LinearLayout.LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
				layoutParams.setMargins(left, top, right, bottom);
				view.setLayoutParams(layoutParams);
			}
		}
	}


	/**
	 * 开始执行TimerTask，可以自动轮播
	 * @param delayTime 延迟多少时间后执行任务
	 * @param internal 执行任务的时间间隔
     */
	public void bannerStartPlay(long delayTime, long internal)
	{
		if (timer != null)
		{
			if (timerTask != null)
			{
				timerTask.cancel();
			}
			timerTask = new TimerTask()
			{
				@Override
				public void run()
				{
					handler.sendEmptyMessage(1);
				}
			};
			timer.schedule(timerTask, delayTime, internal);// 5秒后执行，每隔5秒执行一次
		}
	}


	/**
	 * 停止自动轮播
	 */
	public void bannerStopPlay()
	{
		if (timerTask != null)
		{
			timerTask.cancel();
		}
	}


}
