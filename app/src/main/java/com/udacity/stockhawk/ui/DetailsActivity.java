package com.udacity.stockhawk.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.udacity.stockhawk.R;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Activity to display graph of the stock.
 */

public class DetailsActivity extends AppCompatActivity {

    private static final String INTENT_SYMBOL = "symbol";
    private static final String INTENT_HISTORY = "history";

    private static final String DELIMITER_NEWLINE = "\n";
    private static final String DELIMITER_COMMA = ",";

    @BindView(R.id.linechart) LineChart chart;

    String history, symbol;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        if (getIntent().getExtras() != null) {
            symbol = getIntent().getStringExtra(INTENT_SYMBOL);
            history = getIntent().getStringExtra(INTENT_HISTORY);
        }

        setUpToolbar();
        setUpLineChart();

        initChart(formatHistoryData(history));

    }

    private TreeMap<Float, Float> formatHistoryData(String history) {
        String[] historyData = history.split(DELIMITER_NEWLINE);

        TreeMap<Float, Float> treeMap = new TreeMap<>();
        for (String historyString : historyData) {
            String[] day = historyString.split(DELIMITER_COMMA);
            treeMap.put(Float.valueOf(day[0]), Float.valueOf(day[1]));
        }

        return treeMap;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setUpToolbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(symbol);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void setUpLineChart() {
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
    }

    /**
     * Code from MPAndroidChart Sample : https://goo.gl/6xSxUu
     */
    private void initChart(TreeMap<Float, Float> map) {
        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new IAxisValueFormatter() {

        private SimpleDateFormat dateFormatter = new SimpleDateFormat("yy-MM-dd");

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
                return dateFormatter.format(new Date((long) value));
            }
        });

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        chart.getAxisRight().setEnabled(false);
        chart.setDescription(null);

        setData(map);
    }

    private void setData(TreeMap<Float, Float> map) {
        ArrayList<Entry> values = new ArrayList<>();

        for (HashMap.Entry<Float, Float> entry : map.entrySet()) {
            values.add(new Entry(entry.getKey(), entry.getValue()));
        }

        LineDataSet set1 = new LineDataSet(values, symbol);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        // create data object
        LineData data = new LineData(dataSets);

        chart.setData(data);
    }


}
