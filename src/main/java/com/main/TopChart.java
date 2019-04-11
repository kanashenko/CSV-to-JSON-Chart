package com.main;

import java.awt.Color;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class TopChart extends ApplicationFrame {
	
	private static SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	private String title;
	
    public TopChart(final String title, Map<String, List<String[]>> data) {
        super(title);
        this.title = title;
        final XYDataset dataset = createDataset(data);
        final JFreeChart chart = createChart(dataset);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
        chartPanel.setMouseZoomable(true, false);
        setContentPane(chartPanel);
    }

    private JFreeChart createChart(final XYDataset dataset) {
    	
        final JFreeChart chart = ChartFactory.createTimeSeriesChart(
        	title,
            "Date", 
            "Bytes",
            dataset,
            true,
            true,
            false
        );

        chart.setBackgroundPaint(Color.white);
        final XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(false);
        return chart;
    }
    
    private XYDataset createDataset(Map<String, List<String[]>> data) {
		TimeSeriesCollection dataset = new TimeSeriesCollection();
		setSeries(data,dataset);
        return dataset;
    }
    
    private void setSeries(Map<String, List<String[]>> data, TimeSeriesCollection dataset) {
		 for(Entry<String, List<String[]>> entry: data.entrySet()) {
			 TimeSeries series = new TimeSeries(entry.getKey());
	        	for(String[] arr: entry.getValue()) {
	        		Long bytes = Long.parseLong(arr[1]);
	        		Date date = null;
	        		try {
						 date = format.parse(arr[0]);
					} catch (ParseException e) {
						e.printStackTrace();
					}
	        		series.addOrUpdate(new Millisecond(date),bytes);
	        	}
	        	dataset.addSeries(series);
	        }
    }
    
    public static void showChart(List<List<String>> records) {
    	CSVHandler handler = new CSVHandler();
		handler.computeTop(records);
		handler.computeDataforTopChart(records);
		
    	TopChart chart = new TopChart("Top 10 Receivers", handler.getDataReceivers());
    	chart.pack();
        RefineryUtilities.centerFrameOnScreen(chart);
        chart.setVisible(true);
        
        chart = new TopChart("Top 10 Transmitters", handler.getDataTransmitters());
        chart.pack();
        RefineryUtilities.centerFrameOnScreen(chart);
        chart.setVisible(true);
    }
}
