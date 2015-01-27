package org.bnddev.softeng;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.bnddev.softeng.ProductListView.ProductTableModel;

public class ProductListController {

	private AppModel model;
	private ProductListView view;
	
	private ActionListener newProductListener;
	private ActionListener showStatsListener;
	private ActionListener clearFilterListener;
	private ActionListener toggleFilterListener;
	private DocumentListener filterListener;
	private MouseListener productMouseListener;
	private WindowListener windowListener;
	
	public ProductListController() {
		newProductListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.newProduct(view.getNewProductCode(), view.getNewProductName());
			}
		};
		
		showStatsListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JTable tbl = view.getProductTable();
				ProductTableModel tblModel = (ProductTableModel)tbl.getModel();
				ArrayList products = new ArrayList();
				int[] rows = tbl.getSelectedRows();
				for(int i = 0; i < rows.length; i++) {
					products.add(tblModel.getRow(tbl.convertRowIndexToModel(rows[i])));
				}
				model.showStats(products, view.getStartDate(), view.getEndDate(), view.getInterval());
			}
		};
		
		
		clearFilterListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				view.clearFilter();
				view.filter();
			}
		};
		
		toggleFilterListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				view.filter();
			}
		};
		
		filterListener = new DocumentListener() {
			public void changedUpdate(DocumentEvent e) { view.filter(); }
			public void insertUpdate(DocumentEvent e) { view.filter(); }
			public void removeUpdate(DocumentEvent e) { view.filter(); }
		};
		
		productMouseListener = new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if(e.getClickCount() == 2) {
					JTable tbl = (JTable)e.getSource();
					ProductTableModel tblModel = (ProductTableModel)tbl.getModel();
					ArrayList products = new ArrayList();
					int[] rows = tbl.getSelectedRows();
					for(int i = 0; i < rows.length; i++) {
						products.add(tblModel.getRow(tbl.convertRowIndexToModel(rows[i])));
					}
					model.initUpdateSupplies(products);
				}
			}
		};
		
		windowListener = new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) { model.closeProductList(); }
		};
	}
	
	public void addModel(AppModel model) {
		this.model = model;
	}
	
	public void addView(ProductListView view) {
		this.view = view;
	}
	
	public ActionListener getNewProductListener() {
		return newProductListener;
	}
	
	public ActionListener getClearFilterListener() {
		return clearFilterListener;
	}
	
	public ActionListener getToggleListener() {
		return toggleFilterListener;
	}
	
	public DocumentListener getFilterListener() {
		return filterListener;
	}
	
	public MouseListener getProductMouseListener() {
		return productMouseListener;
	}
	
	public WindowListener getWindowListener() {
		return windowListener;
	}

	public ActionListener getShowStatsListener() {
		return showStatsListener;
	}
	
}
