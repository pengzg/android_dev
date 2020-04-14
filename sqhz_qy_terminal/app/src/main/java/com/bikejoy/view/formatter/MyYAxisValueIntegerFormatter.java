package com.bikejoy.view.formatter;

import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DecimalFormat;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2019/4/18
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class MyYAxisValueIntegerFormatter extends ValueFormatter{

    private DecimalFormat mFormat;

    public MyYAxisValueIntegerFormatter() {
        mFormat = new DecimalFormat("###,###,###,##0");
    }

    @Override
    public String getFormattedValue(float value) {
        return mFormat.format(value);
    }
}
