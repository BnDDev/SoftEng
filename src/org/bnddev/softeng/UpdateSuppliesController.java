package org.bnddev.softeng;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UpdateSuppliesController {
	private AppModel model;
	private UpdateSuppliesView view;
	
	private ActionListener addActionListener;
	private ActionListener subActionListener;
	
	public UpdateSuppliesController() {
		addActionListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.updateSupplies(view.getValue());
			}	
		};
		
		subActionListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.updateSupplies(-view.getValue());
			}
		};
	}
	
	public void addModel(AppModel model) {
		this.model = model;
	}
	
	public void addView(final UpdateSuppliesView view) {
		this.view = view;
	}
	
	public ActionListener getAddActionListener() {
		return addActionListener;
	}
	
	public ActionListener getSubActionListener() {
		return subActionListener;
	}
}
