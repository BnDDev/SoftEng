package org.bnddev.softeng;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class RegisterController {

	private AppModel model;
	private RegisterView view;
	
	private ActionListener submitListener;
	private WindowListener windowListener;
	
	public RegisterController() {
		submitListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.submitNewUser(view.getUsername(), view.getFullname(), view.getPassword1(), view.getPassword2(), view.isAdmin());
			}
		};

		windowListener = new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) { model.closeRegister(); }
		};
	}
	
	public void addModel(AppModel model) {
		this.model = model;
	}
	
	public void addView(RegisterView view) {
		this.view = view;
	}

	public WindowListener getWindowListener() {
		return windowListener;
	}

	public ActionListener getSubmitListener() {
		return submitListener;
	}
	
}
