package org.bnddev.softeng;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;
import javax.swing.table.*;

public class ProductListView extends JFrame implements Observer {
	private JTextField txtCode;
	private JTextField txtName;
	private JCheckBox chkToggle;
	private JTable tblProducts;
	private JButton btnClearFilter;
	private JButton btnNewProduct;
	private JButton btnShowStats;
	private JSpinner spnFrom;
	private JSpinner spnTo;
	private JPanel panelAdmin;
	private JComboBox cmbInterval;
	
	private TableRowSorter sorter;
	private JTextField txtNewName;
	private JTextField txtNewCode;
	
	class ProductTableModel extends AbstractTableModel {
		private String[] columnNames = new String[] { "\u039A\u03C9\u03B4\u03B9\u03BA\u03CC\u03C2", "\u038C\u03BD\u03BF\u03BC\u03B1", "\u0391\u03C0\u03CC\u03B8\u03B5\u03BC\u03B1" };
		Class[] columnTypes = new Class[] { Integer.class, String.class, Integer.class };
		
		public List rows;

		public int getColumnCount() { return columnNames.length; }
		public String getColumnName(int col) { return columnNames[col]; }
		public Class getColumnClass(int columnIndex) { return columnTypes[columnIndex]; }

		public int getRowCount() {
			if(rows == null) return 0;
			return rows.size();
		}

		public Object getValueAt(int row, int col) {
			if(rows == null) return null;
			return ((Object[])rows.get(row))[col];
		}
		
		public class CustomRowFilter extends RowFilter {
			int fltId;
			int fltIdStrlen;
			int fltIdMod;
			
			public boolean include(Entry e) {
				if(fltIdStrlen == 0) return true;
				int id = ((Integer)e.getValue(0)).intValue();
				for(int fltIdDiv = 1; fltIdDiv < id; fltIdDiv *= 10)
					if((id / fltIdDiv) % fltIdMod == fltId)
						return true;
				return false;
			}
			
			public RowFilter create(String fltIdStr, String fltNameStr, boolean fltHaveSupplies) {
				List filterList = new ArrayList();
				if(!fltIdStr.isEmpty()) {
					try {
						fltId = Integer.parseInt(fltIdStr);
						fltIdStrlen = fltIdStr.length();
						fltIdMod = (int)Math.pow(10, fltIdStrlen);
						filterList.add(this);
					} catch(Exception e) {
						// ignore
					}
				}
				if(!fltNameStr.isEmpty()) {
					try {
						filterList.add(RowFilter.regexFilter("(?i)" + txtName.getText(), new int[]{1}));
					} catch(Exception e) {
						// ignore
					}
				}
				if(fltHaveSupplies) {
					filterList.add(RowFilter.numberFilter(RowFilter.ComparisonType.AFTER, Integer.valueOf(0), new int[]{2}));
				}
				return RowFilter.andFilter(filterList);
			}
		}

		public Object getRow(int i) {
			if(rows == null) return null;
			return rows.get(i);
		}
	}
	
	public ProductListView() {
		setBounds(100, 100, 950, 700);
		setResizable(false);
		getContentPane().setLayout(null);
		
		panelAdmin = new JPanel();
		panelAdmin.setBounds(0, 0, 950, 700);
		panelAdmin.setOpaque(false);
		panelAdmin.setLayout(null);
		getContentPane().add(panelAdmin);
		
		JLabel lblNewLabel3 = new JLabel("\u0391\u03C0\u03CC:");
		lblNewLabel3.setBounds(618, 128, 46, 14);
		panelAdmin.add(lblNewLabel3);
		
		JLabel lblNewLabel4 = new JLabel("\u039C\u03AD\u03C7\u03C1\u03B9:");
		lblNewLabel4.setBounds(618, 159, 46, 14);
		panelAdmin.add(lblNewLabel4);
		
		spnFrom = new JSpinner();
		spnFrom.setModel(new SpinnerDateModel());
		spnFrom.setEditor(new JSpinner.DateEditor(spnFrom, "dd/MM/yyyy HH:mm"));
		spnFrom.setBounds(674, 125, 163, 20);
		panelAdmin.add(spnFrom);
		
		spnTo = new JSpinner();
		spnTo.setModel(new SpinnerDateModel());
		spnTo.setEditor(new JSpinner.DateEditor(spnTo, "dd/MM/yyyy HH:mm"));
		spnTo.setBounds(674, 156, 163, 20);
		panelAdmin.add(spnTo);
		
		JLabel lblNewLabel2 = new JLabel("\u038C\u03BD\u03BF\u03BC\u03B1:");
		lblNewLabel2.setBounds(60, 159, 46, 14);
		panelAdmin.add(lblNewLabel2);
		
		JLabel lblNewLabel1 = new JLabel("\u039A\u03C9\u03B4\u03B9\u03BA\u03CC\u03C2:");
		lblNewLabel1.setBounds(60, 128, 54, 14);
		panelAdmin.add(lblNewLabel1);
		
		txtNewCode = new JTextField();
		txtNewCode.setToolTipText("New code");
		txtNewCode.setBounds(116, 125, 88, 20);
		panelAdmin.add(txtNewCode);
		txtNewCode.setColumns(10);
		
		txtNewName = new JTextField();
		txtNewName.setBounds(116, 156, 168, 20);
		panelAdmin.add(txtNewName);
		txtNewName.setColumns(10);
		
		btnClearFilter = new JButton("\u039A\u03B1\u03B8\u03B1\u03C1\u03B9\u03C3\u03BC\u03CC\u03C2");
		btnClearFilter.setBounds(427, 261, 104, 23);
		getContentPane().add(btnClearFilter);
		
		ProductTableModel model = new ProductTableModel();
		sorter = new TableRowSorter(model);
		tblProducts = new JTable();
		tblProducts.setRowSorter(sorter);
		tblProducts.setFillsViewportHeight(true);
		tblProducts.setModel(model);
		tblProducts.getColumnModel().getColumn(0).setMaxWidth(75);
		tblProducts.getColumnModel().getColumn(2).setMaxWidth(75);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(35, 293, 875, 351);
		getContentPane().add(scrollPane);
		scrollPane.setViewportView(tblProducts);
		
		JLabel label = new JLabel("\u03A6\u03AF\u03BB\u03C4\u03C1\u03BF:");
		label.setBounds(36, 231, 65, 23);
		getContentPane().add(label);
		
		txtName = new JTextField();
		txtName.setBounds(122, 262, 257, 20);
		getContentPane().add(txtName);
		txtName.setColumns(10);
		
		chkToggle = new JCheckBox("\u03A3\u03B5 \u03B1\u03C0\u03CC\u03B8\u03B5\u03BC\u03B1");
		chkToggle.setBounds(819, 261, 91, 23);
		getContentPane().add(chkToggle);
		
		txtCode = new JTextField();
		txtCode.setBounds(35, 262, 77, 20);
		getContentPane().add(txtCode);
		txtCode.setColumns(10);
		
		btnShowStats = new JButton("\u03A3\u03C4\u03B1\u03C4\u03B9\u03C3\u03C4\u03B9\u03BA\u03AC \u03C3\u03C4\u03BF\u03B9\u03C7\u03B5\u03AF\u03B1");
		btnShowStats.setBounds(618, 218, 219, 33);
		panelAdmin.add(btnShowStats);
		
		btnNewProduct = new JButton("\u0395\u03B9\u03C3\u03B1\u03B3\u03C9\u03B3\u03AE \u03BD\u03AD\u03BF\u03C5 \u03C0\u03C1\u03BF\u03CA\u03CC\u03BD\u03C4\u03BF\u03C2");
		btnNewProduct.setBounds(60, 187, 224, 33);
		panelAdmin.add(btnNewProduct);
		
		cmbInterval = new JComboBox();
		cmbInterval.setModel(new DefaultComboBoxModel(new String[] {"-", "\u0391\u03BD\u03AC \u03BB\u03B5\u03C0\u03C4\u03CC", "\u0391\u03BD\u03AC \u03CE\u03C1\u03B1", "\u0391\u03BD\u03AC \u03BC\u03AD\u03C1\u03B1", "\u0391\u03BD\u03AC \u03B5\u03B2\u03B4\u03BF\u03BC\u03AC\u03B4\u03B1"}));
		cmbInterval.setSelectedIndex(2);
		cmbInterval.setBounds(674, 187, 163, 20);
		panelAdmin.add(cmbInterval);
		
		JLabel lblNewLabel = new JLabel();
		lblNewLabel.setIcon(new ImageIcon(ProductListView.class.getResource("/org/bnddev/softeng/img/mainpagebg.png")));
		lblNewLabel.setBounds(-189, 0, 1184, 662);
		getContentPane().add(lblNewLabel);
	}

	public void addController(ProductListController controller) {
		tblProducts.addMouseListener(controller.getProductMouseListener());
		btnNewProduct.addActionListener(controller.getNewProductListener());
		btnShowStats.addActionListener(controller.getShowStatsListener());
		btnClearFilter.addActionListener(controller.getClearFilterListener());
		chkToggle.addActionListener(controller.getToggleListener());
		txtCode.getDocument().addDocumentListener(controller.getFilterListener());
		txtName.getDocument().addDocumentListener(controller.getFilterListener());
		addWindowListener(controller.getWindowListener());
	}
	
	public void clearFilter() {
		txtCode.setText("");
		txtName.setText("");
	}
	
	public void filter() {
		ProductTableModel model = (ProductTableModel)tblProducts.getModel();
		sorter.setRowFilter(model.new CustomRowFilter().create(txtCode.getText(), txtName.getText(), chkToggle.isSelected()));
	}
	
	public String getNewProductCode() {
		return txtNewCode.getText();
	}
	
	public String getNewProductName() {
		return txtNewName.getText();
	}
	
	public void clearNewProduct() {
		txtNewCode.setText("");
		txtNewName.setText("");
	}
	
	public Date getStartDate() {
		return (Date)spnFrom.getValue();
	}
	
	public Date getEndDate() {
		return (Date)spnTo.getValue();
	}
	
	public JTable getProductTable() {
		return tblProducts;
	}
	
	public int getInterval() {
		int ret = 1;
		switch(cmbInterval.getSelectedIndex()) {
		case 4: ret *= 7;
		case 3: ret *= 24;
		case 2: ret *= 60;
		case 1: ret *= 60;
		default:
		}
		return ret;
	}
	
	public void update(Observable source, Object data) {
		AppData app = (AppData)data;
		setVisible(app.productListData.enabled);
		if(app.productListData.enabled) {
			ProductTableModel model = (ProductTableModel)tblProducts.getModel();
			model.rows = app.productListData.rows;
			if(app.productListData.updated) {
				model.fireTableDataChanged();
				clearNewProduct();
			}
			if(app.loginData.isAdmin) {
				panelAdmin.setVisible(true);
				SpinnerDateModel spnModelFrom = (SpinnerDateModel)spnFrom.getModel();
				spnModelFrom.setStart(new Date((app.statisticsData.min - 60) * 1000));
				spnModelFrom.setEnd(new Date((app.statisticsData.max + 60) * 1000));
				spnModelFrom.setValue(new Date(app.statisticsData.start * 1000));
				SpinnerDateModel spnModelTo = (SpinnerDateModel)spnTo.getModel();
				spnModelTo.setStart(new Date((app.statisticsData.min - 60) * 1000));
				spnModelTo.setEnd(new Date((app.statisticsData.max + 60) * 1000));
				spnModelTo.setValue(new Date(app.statisticsData.end * 1000));
			} else panelAdmin.setVisible(false);
		}
	}
}
