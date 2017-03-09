package com.eighteengray.bannerview.parser;

import android.view.View;
import java.util.List;



/**
 * Created by lutao on 2017/2/21.
 */

public interface DataParser<T>
{
    List<View> parse(List<T> dataList, int size);
    View createView(T data);
}
