package org.bnddev.softeng;

import java.awt.Color;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;

import org.jfree.chart.*;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.RectangleInsets;

public class StatisticsView extends JFrame implements Observer {

	private ChartPanel chartPanel;

	public StatisticsView() {
		setBounds(100, 100, 800, 600);
		setResizable(false);
		chartPanel = new ChartPanel(null);
		setContentPane(chartPanel);
	}
	
	public void addController(StatisticsController controller) {
		addWindowListener(controller.getWindowListener());
	}
	
	public void update(Observable source, Object data) {
		AppData app = (AppData)data;
		setVisible(app.statisticsData.enabled);
		if(app.statisticsData.enabled) {
			TimeSeriesCollection dataset = new TimeSeriesCollection();
			TimeSeries sIn = new TimeSeries("\u0391\u03C5\u03BE\u03AE\u03C3\u03B5\u03B9\u03C2");
			TimeSeries sOut = new TimeSeries("\u039C\u03B5\u03B9\u03CE\u03C3\u03B5\u03B9\u03C2");
			TimeSeries sTotal = new TimeSeries("\u03A3\u03CD\u03BD\u03BF\u03BB\u03BF");
			Calendar cal = Calendar.getInstance();
			for(Iterator it = app.statisticsData.suppliesIn.iterator(); it.hasNext();) {
				Object[] obj = (Object[])it.next();
				Date dt = (Date)obj[0];
				int val = ((Integer)obj[1]).intValue();
				try {
					cal.setTime(dt);
					cal.add(Calendar.SECOND, -app.statisticsData.interval);
					sIn.add(new Second(cal.getTime()), 0);
				} catch(Exception e) {}
				try {
					cal.setTime(dt);
					cal.add(Calendar.SECOND, app.statisticsData.interval);
					sIn.add(new Second(cal.getTime()), 0);
				} catch(Exception e) {}
				sIn.addOrUpdate(new Second(dt), val);
			}
			for(Iterator it = app.statisticsData.suppliesOut.iterator(); it.hasNext();) {
				Object[] obj = (Object[])it.next();
				Date dt = (Date)obj[0];
				int val = ((Integer)obj[1]).intValue();
				try {
					cal.setTime(dt);
					cal.add(Calendar.SECOND, -app.statisticsData.interval);
					sOut.add(new Second(cal.getTime()), 0);
				} catch(Exception e) {}
				try {
					cal.setTime(dt);
					cal.add(Calendar.SECOND, app.statisticsData.interval);
					sOut.add(new Second(cal.getTime()), 0);
				} catch(Exception e) {}
				sOut.addOrUpdate(new Second(dt), val);
			}
			int lastVal = 0;
			for(Iterator it = app.statisticsData.suppliesTotal.iterator(); it.hasNext();) {
				Object[] obj = (Object[])it.next();
				Date dt = (Date)obj[0];
				int val = ((Integer)obj[1]).intValue();
				try {
					cal.setTime(dt);
					cal.add(Calendar.SECOND, -app.statisticsData.interval);
					sTotal.add(new Second(cal.getTime()), lastVal);
				} catch(Exception e) {}
				try {
					cal.setTime(dt);
					cal.add(Calendar.SECOND, app.statisticsData.interval);
					sTotal.add(new Second(cal.getTime()), val);
				} catch(Exception e) {}
				sTotal.addOrUpdate(new Second(dt), val);
				lastVal = val;
			}
			dataset.addSeries(sIn);
			dataset.addSeries(sOut);
			dataset.addSeries(sTotal);
			JFreeChart chart = ChartFactory.createTimeSeriesChart("\u03A0\u03C1\u03BF\u03CA\u03CC\u03BD\u03C4\u03B1", "\u03A7\u03C1\u03CC\u03BD\u03BF\u03C2", "\u0391\u03C0\u03BF\u03B8\u03AD\u03BC\u03B1\u03C4\u03B1", dataset);
			chart.setBackgroundPaint(Color.white);
			XYAreaRenderer renderer = new XYAreaRenderer();
			renderer.setAutoPopulateSeriesFillPaint(false);
			renderer.setAutoPopulateSeriesPaint(true);
			XYPlot plot = (XYPlot) chart.getPlot();
			plot.setRenderer(new XYAreaRenderer());
			plot.setBackgroundPaint(Color.lightGray);
			plot.setDomainGridlinePaint(Color.white);
			plot.setRangeGridlinePaint(Color.white);
			plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
			plot.setDomainCrosshairVisible(true);
			plot.setRangeCrosshairVisible(true);

			chartPanel.setChart(chart);
		}
	}
}
