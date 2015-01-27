package org.bnddev.softeng;

public class Main {
	public static void main(String[] args) {
		AppModel model = new AppModel();
		
		LoginView loginView = new LoginView();
		LoginController loginCtrl = new LoginController();
		loginCtrl.addModel(model);
		loginCtrl.addView(loginView);
		loginView.addController(loginCtrl);
		model.addObserver(loginView);
		
		RegisterView registerView = new RegisterView();
		RegisterController registerCtrl = new RegisterController();
		registerCtrl.addModel(model);
		registerCtrl.addView(registerView);
		registerView.addController(registerCtrl);
		model.addObserver(registerView);
		
		ProductListView productListView = new ProductListView();
		ProductListController productListCtrl = new ProductListController();
		productListCtrl.addModel(model);
		productListCtrl.addView(productListView);
		productListView.addController(productListCtrl);
		model.addObserver(productListView);
		
		StatisticsView statisticsView = new StatisticsView();
		StatisticsController statisticsCtrl = new StatisticsController();
		statisticsCtrl.addModel(model);
		statisticsCtrl.addView(statisticsView);
		statisticsView.addController(statisticsCtrl);
		model.addObserver(statisticsView);
		
		UpdateSuppliesView updateSuppliesView = new UpdateSuppliesView();
		UpdateSuppliesController updateSuppliesCtrl = new UpdateSuppliesController();
		updateSuppliesCtrl.addModel(model);
		updateSuppliesCtrl.addView(updateSuppliesView);
		updateSuppliesView.addController(updateSuppliesCtrl);
		model.addObserver(updateSuppliesView);
		
		model.init();
	}
}
