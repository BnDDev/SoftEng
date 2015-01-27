package org.bnddev.softeng;

import java.util.Observable;
import java.util.Observer;

import javax.swing.*;

public class RegisterView extends JFrame implements Observer {

	private JTextField txtUsername;
	private JTextField txtFullname;
	private JPasswordField pwfPassword1;
	private JPasswordField pwfPassword2;
	private JRadioButton rdAdmin;
	private JRadioButton rdEmployee;
	private JButton btnSubmit;

	public RegisterView() {
		setBounds(100, 100, 1184, 662);
		setResizable(false);
		getContentPane().setLayout(null);
		
		btnSubmit = new JButton("\u0394\u03B7\u03BC\u03B9\u03BF\u03C5\u03B3\u03AF\u03B1  \u039D\u03AD\u03BF\u03C5 \u03A7\u03C1\u03AE\u03C3\u03C4\u03B7");
		btnSubmit.setBounds(640, 386, 223, 32);
		getContentPane().add(btnSubmit);
		
		rdEmployee = new JRadioButton("");
		rdEmployee.setSelected(true);
		rdEmployee.setBounds(645, 437, 21, 23);
		rdEmployee.setOpaque(false);
		getContentPane().add(rdEmployee);
		
		rdAdmin = new JRadioButton("");
		rdAdmin.setBounds(423, 437, 21, 23);
		rdAdmin.setOpaque(false);
		getContentPane().add(rdAdmin);
		
		ButtonGroup group = new ButtonGroup();
		group.add(rdEmployee);
		group.add(rdAdmin);
		
		pwfPassword2 = new JPasswordField();
		pwfPassword2.setColumns(10);
		pwfPassword2.setBounds(640, 348, 223, 27);
		getContentPane().add(pwfPassword2);
		
		pwfPassword1 = new JPasswordField();
		pwfPassword1.setColumns(10);
		pwfPassword1.setBounds(640, 310, 223, 27);
		getContentPane().add(pwfPassword1);
		
		txtFullname = new JTextField();
		txtFullname.setColumns(10);
		txtFullname.setBounds(640, 272, 223, 27);
		getContentPane().add(txtFullname);
		
		txtUsername = new JTextField();
		txtUsername.setBounds(640, 230, 223, 27);
		getContentPane().add(txtUsername);
		txtUsername.setColumns(10);
		
		JLabel lblbg = new JLabel("New label");
		lblbg.setIcon(new ImageIcon(RegisterView.class.getResource("/org/bnddev/softeng/img/registerpage.png")));
		lblbg.setBounds(0, 0, 1184, 662);
		getContentPane().add(lblbg);
	}
	
	public String getUsername() {
		return txtUsername.getText();
	}
	
	public String getFullname() {
		return txtFullname.getText();
	}
	
	public char[] getPassword1() {
		return pwfPassword1.getPassword();
	}
	
	public char[] getPassword2() {
		return pwfPassword2.getPassword();
	}
	
	public boolean isAdmin() {
		return rdAdmin.isSelected();
	}
	
	public void addController(RegisterController controller) {
		btnSubmit.addActionListener(controller.getSubmitListener());
		addWindowListener(controller.getWindowListener());
	}
	
	public void update(Observable source, Object data) {
		AppData app = (AppData)data;
		setVisible(app.registerData.enabled);
		if(app.loginData.enabled) {
			txtUsername.setText("");
			txtFullname.setText("");
			pwfPassword1.setText("");
			pwfPassword2.setText("");
			rdEmployee.setSelected(true);
		}
	}
}
