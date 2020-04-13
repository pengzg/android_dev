
package com.github.mikephil.charting.formatter;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

/**
 * This ValueFormatter is just for convenience and simply puts a "%" sign after
 * each value. (Recommeded for PieChart)
 * 
 * @author Philipp Jahoda
 */
public class CustomPercentFormatter implements ValueFormatter, YAxisValueFormatter {

    protected DecimalFormat mFormat;

    public CustomPercentFormatter() {
        mFormat = new DecimalFormat("###,###,##0.0");
    }

    // ValueFormatter
    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return "¥:"+mFormat.format(value);
    }

    // YAxisValueFormatter
    @Override
    public String getFormattedValue(float value, YAxis yAxis) {
        return "¥:"+mFormat.format(value);
    }
}
