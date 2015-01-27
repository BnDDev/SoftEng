package org.bnddev.softeng;

import java.util.Observable;
import java.util.Observer;

import javax.swing.*;

public class LoginView extends JFrame implements Observer {

	private JTextField txtUsername;
	private JPasswordField pwfPassword;
	private JButton btnRegister;
	private JButton btnLogin;

	public LoginView() {
		super();
		setBounds(100, 100, 1193, 662);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		
		pwfPassword = new JPasswordField("");
		pwfPassword.setBounds(661, 296, 325, 32);
		getContentPane().add(pwfPassword);
		
		txtUsername = new JTextField("");
		txtUsername.setBounds(661, 216, 326, 33);
		getContentPane().add(txtUsername);
		txtUsername.setColumns(10);
		
		btnRegister = new JButton("");
		btnRegister.setIcon(new ImageIcon(LoginView.class.getResource("/org/bnddev/softeng/img/registerbtn.png")));
		btnRegister.setBounds(369, 366, 277, 131);
		getContentPane().add(btnRegister);
		
		btnLogin = new JButton("");
		btnLogin.setIcon(new ImageIcon(LoginView.class.getResource("/org/bnddev/softeng/img/loginbtn.png")));
		btnLogin.setBounds(661, 366, 277, 131);
		getContentPane().add(btnLogin);
		
		JLabel bgimg = new JLabel("");
		bgimg.setIcon(new ImageIcon(LoginView.class.getResource("/org/bnddev/softeng/img/login.png")));
		bgimg.setDisabledIcon(new ImageIcon(LoginView.class.getResource("/org/bnddev/softeng/img/login.png")));
		bgimg.setBounds(0, 0, 1193, 662);
		getContentPane().add(bgimg);
	}

	public void addController(LoginController controller) {
		btnLogin.addActionListener(controller.getLoginListener());
		btnRegister.addActionListener(controller.getRegisterListener());
		pwfPassword.addKeyListener(controller.getKeyListener());
		addWindowListener(controller.getWindowListener());
	}
	
	public String getUsername() {
		return txtUsername.getText();
	}
	
	public char[] getPassword() {
		return pwfPassword.getPassword();
	}

	public void update(Observable source, Object data) {
		AppData app = (AppData)data;
		setVisible(app.loginData.enabled);
		if(app.loginData.enabled) {
			txtUsername.setText("");
			pwfPassword.setText("");
		}
	}
}
