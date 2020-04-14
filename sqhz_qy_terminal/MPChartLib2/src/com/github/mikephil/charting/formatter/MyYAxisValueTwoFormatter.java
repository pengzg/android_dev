package com.github.mikephil.charting.formatter;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/8/1
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class MyYAxisValueTwoFormatter implements ValueFormatter{
    private DecimalFormat mFormat;

    public MyYAxisValueTwoFormatter() {
        mFormat = new DecimalFormat("###,###,###,##0");
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return mFormat.format(value);
    }
}
