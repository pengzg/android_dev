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

public class MyYAxisValueDecimalFormatter extends ValueFormatter{

    private DecimalFormat mFormat;

    public MyYAxisValueDecimalFormatter() {
        mFormat = new DecimalFormat("###,###,###,##0.00");
    }

    @Override
    public String getFormattedValue(float value) {
        return "¥"+mFormat.format(value);
    }
}
