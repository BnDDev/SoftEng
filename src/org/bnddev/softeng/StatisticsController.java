package org.bnddev.softeng;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowAdapter;

public class StatisticsController {
	
	private AppModel model;
	private StatisticsView view;
	
	private WindowListener windowListener;
	
	public StatisticsController() {
		windowListener = new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) { model.closeStatistics(); }
		};
	}
	
	public void addModel(AppModel model) {
		this.model = model;
	}
	
	public void addView(StatisticsView view) {
		this.view = view;
	}
	
	public WindowListener getWindowListener() {
		return windowListener;
	}
}
