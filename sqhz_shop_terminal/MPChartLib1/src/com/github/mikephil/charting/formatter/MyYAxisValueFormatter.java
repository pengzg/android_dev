package com.github.mikephil.charting.formatter;

import com.github.mikephil.charting.components.YAxis;

import java.text.DecimalFormat;

public class MyYAxisValueFormatter implements YAxisValueFormatter {

    private DecimalFormat mFormat;

    public MyYAxisValueFormatter() {
        mFormat = new DecimalFormat("###,###,###,##0.0");
    }

    @Override
    public String getFormattedValue(float value, YAxis yAxis) {
        return mFormat.format(value) + " $";
    }
}
