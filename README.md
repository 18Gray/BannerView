# BannerView
通常ListView上面的那个Banner，就是他，自动轮播，同时可以手动滑动。
很多应用的首页会是一个列表来展示数据，在这个列表上面会放一个Banner，上面放一些产品重点推广的活动之类的内容，BannerView就是这样一个控件。
使用时，只需要把这个BannerView通过ListView的addHeader就可加到ListView上面。
在成功获取网络数据后，调用BannerView的setBannerList就完成了操作，BannerView会根据传入的图片地址数量动态生成View，包括下面的圆点。
该控件支持轮播图，可以手动设置轮播时间，支持手势滑动切换，并且可以循环轮播。
