package ro.cosma.andrei.sunnydays.pages.hourly;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.List;

import ro.cosma.andrei.sunnydays.R;
import ro.cosma.andrei.sunnydays.bean.ChartEntryBean;

/*Created by Cosma Andrei
* 9/22/2017*/
@Deprecated
public class ChartPageFragment extends Fragment {

    List<ChartEntryBean> beanList;

    String title = "";
    String chartUnit = "";

    private TextView chartNameTv;
    private LineChart lineChartView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chart, container, false);

        chartNameTv = (TextView) rootView.findViewById(R.id.chart_title);
        chartNameTv.setText(title);

        lineChartView = (LineChart) rootView.findViewById(R.id.chart);

        if (beanList.size() > 0) {
            setChartData();
        }
        configChart();

        return rootView;
    }

    public static ChartPageFragment newInstance(List<ChartEntryBean> beanList, String title, String chartUnit) {
        ChartPageFragment fragment = new ChartPageFragment();
        fragment.beanList = beanList;
        fragment.title = title;
        fragment.chartUnit = chartUnit;
        return fragment;
    }


    private void configChart() {
        lineChartView.getLegend().setEnabled(false);

        // right axis settings
        lineChartView.getAxisRight().setDrawAxisLine(false);
        lineChartView.getAxisRight().setEnabled(false);

        // left axis settings
        lineChartView.getAxisLeft().setDrawAxisLine(false);
        lineChartView.getAxisLeft().setLabelCount(0);
        lineChartView.getAxisLeft().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return chartUnit;
            }
        });

        // x axis settings
        XAxis xAxis = lineChartView.getXAxis();
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setEnabled(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(8, true);
        xAxis.setTextSize(8f);


        lineChartView.setDescription(null);

        // Another settings for chart
        lineChartView.setTouchEnabled(false);
        lineChartView.setScaleMinima(1f, 1f);
        lineChartView.setBackgroundColor(Color.WHITE);
        lineChartView.setDrawGridBackground(false);
        lineChartView.setDrawBorders(false);


        lineChartView.invalidate();

    }

    public void setChartData() {

        lineChartView.clear();


        List<ILineDataSet> dataSets = new ArrayList<>();
        ArrayList<Entry> entries = new ArrayList<>();
        LineDataSet dataSet;


        for (int i = 0; i < beanList.size(); i++) {

            entries.add(new Entry(i, (float) beanList.get(i).getValue()));

        }

        lineChartView.getXAxis().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if (value >= 0 && value < beanList.size()) {
                    return beanList.get((int) value).getTime();
                } else {
                    return "";
                }
            }
        });

        dataSet = new LineDataSet(entries, "");
        dataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return String.valueOf(((int) value));
            }
        });
        dataSet.setLineWidth(1f);
        dataSet.setCircleRadius(5f);
        dataSet.setColor(Color.parseColor("#63a4ff"));
        dataSets.add(dataSet);
        LineData cd = new LineData(dataSets);
        cd.setValueTextColor(Color.BLACK);
        cd.setValueTextSize(10f);
        lineChartView.setData(cd);

    }

}
