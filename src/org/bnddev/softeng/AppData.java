package org.bnddev.softeng;

import java.util.ArrayList;
import java.util.List;

public class AppData {
	
	public LoginData loginData;
	public RegisterDara registerData;
	public ProductListData productListData;
	public UpdateSuppliesData updateSuppliesData;
	public StatisticsData statisticsData;
	
	public class Page {
		public boolean enabled;
	}
	
	public class LoginData extends Page {
		public boolean isAdmin;
		public int userID;
	}
	
	public class RegisterDara extends Page {
	}
	
	public class ProductListData extends Page {
		public List rows = new ArrayList();
		public boolean updated;
	}
	
	public class UpdateSuppliesData extends Page {
		public List products;
	}
	
	public class StatisticsData extends Page {
		public long min, max, start, end;
		public int interval;
		public List suppliesIn;
		public List suppliesOut;
		public List suppliesTotal;
	}
	
	public AppData() {
		loginData = new LoginData();
		registerData = new RegisterDara();
		productListData = new ProductListData();
		updateSuppliesData = new UpdateSuppliesData();
		statisticsData = new StatisticsData();
	}
}
