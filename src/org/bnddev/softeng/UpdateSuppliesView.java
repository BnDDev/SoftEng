package org.bnddev.softeng;

import javax.swing.*;

import java.awt.Color;
import java.util.Observable;
import java.util.Observer;

public class UpdateSuppliesView extends JFrame implements Observer {
	private JButton btnPlus;
	private JButton btnMinus;
	private JSpinner spnValue;

	public UpdateSuppliesView() {
		setBounds(100, 100, 175, 155);
		setResizable(false);
		setAlwaysOnTop(true);
		getContentPane().setLayout(null);

		btnPlus = new JButton("");
		btnPlus.setBackground(Color.CYAN);
		btnPlus.setIcon(new ImageIcon(UpdateSuppliesView.class.getResource("/org/bnddev/softeng/img/plus.png")));
		btnPlus.setBounds(86, 42, 64, 65);
		getContentPane().add(btnPlus);
		
		btnMinus = new JButton("");
		btnMinus.setBackground(Color.RED);
		btnMinus.setIcon(new ImageIcon(UpdateSuppliesView.class.getResource("/org/bnddev/softeng/img/minus.png")));
		btnMinus.setBounds(10, 42, 64, 65);
		getContentPane().add(btnMinus);
		
		spnValue = new JSpinner();
		spnValue.setModel(new SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
		spnValue.setBounds(10, 11, 140, 20);
		getContentPane().add(spnValue);
	}
	
	public int getValue() {
		return ((Integer)spnValue.getValue()).intValue();
	}

	public void addController(UpdateSuppliesController controller) {
		btnPlus.addActionListener(controller.getAddActionListener());
		btnMinus.addActionListener(controller.getSubActionListener());
	}
	
	public void update(Observable source, Object data) {
		AppData app = (AppData)data;
		setVisible(app.updateSuppliesData.enabled);
		if(app.updateSuppliesData.enabled) {
			spnValue.setValue(Integer.valueOf(1));
		}
	}

}
