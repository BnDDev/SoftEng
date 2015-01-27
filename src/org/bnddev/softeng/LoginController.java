package org.bnddev.softeng;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class LoginController {
	
	private AppModel model;
	private LoginView view;
	
	private KeyListener keyListener;
	private ActionListener loginListener;
	private ActionListener registerListener;
	private WindowListener windowListener;
	
	public LoginController() {
		keyListener = new KeyListener() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
					model.login(view.getUsername(), view.getPassword());
			}
			public void keyReleased(KeyEvent e) {}
			public void keyTyped(KeyEvent e) {}
		};
		
		loginListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.login(view.getUsername(), view.getPassword());
			}
		};
		
		registerListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.register(view.getUsername(), view.getPassword());
			}
		};
		
		windowListener = new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) { model.closeLogin(); }
		};
	}
	
	public void addModel(AppModel model) {
		this.model = model;
	}
	
	public void addView(LoginView view) {
		this.view = view;
	}
	
	public KeyListener getKeyListener() {
		return keyListener;
	}
	
	public ActionListener getLoginListener() {
		return loginListener;
	}
	
	public ActionListener getRegisterListener() {
		return registerListener;
	}

	public WindowListener getWindowListener() {
		return windowListener;
	}
}
