package org.bnddev.softeng;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class AppModelTest {
	
	public AppModelTest() {
	}
	
	@Test
	public void testAppModel() {
	}

	@Test
	public void testInit() {
		AppModel model = new AppModel();
		model.init();
		assertTrue(model.getAppData().loginData.enabled);
	}
	
	@Test
	public void testGetAppData() {
		AppModel model = new AppModel();
		model.init();
		assertNotNull(model.getAppData());
	}

	@Test
	public void testQuit() {
		AppModel model = new AppModel();
		model.init();
		assertTrue(model.getAppData().loginData.enabled);
		model.quit();
	}

	@Test
	public void testTryLogin() {
		AppModel model = new AppModel();
		model.init();
		assertTrue(model.getAppData().loginData.enabled);
		assertTrue(model.tryLogin("admin", "admin".toCharArray()));
	}

	@Test
	public void testLogin() {
		AppModel model = new AppModel();
		model.init();
		assertTrue(model.getAppData().loginData.enabled);
		model.login("admin", "admin".toCharArray());
		assertTrue(model.getAppData().loginData.isAdmin);
		assertFalse(model.getAppData().loginData.enabled);
	}

	@Test
	public void testRegister() {
		AppModel model = new AppModel();
		model.init();
		assertTrue(model.getAppData().loginData.enabled);
		model.register("admin", "admin".toCharArray());
		assertTrue(model.getAppData().loginData.isAdmin);
		assertTrue(model.getAppData().registerData.enabled);
	}

	@Test
	public void testCloseLogin() {
		AppModel model = new AppModel();
		model.init();
		assertTrue(model.getAppData().loginData.enabled);
		model.closeLogin();
		assertNull(model.getAppData());
	}

	@Test
	public void testCloseRegister() {
		AppModel model = new AppModel();
		model.init();
		assertTrue(model.getAppData().loginData.enabled);
		model.closeRegister();
		assertFalse(model.getAppData().registerData.enabled);
	}

	@Test
	public void testCloseProductList() {
		AppModel model = new AppModel();
		model.init();
		assertTrue(model.getAppData().loginData.enabled);
		model.closeProductList();
		assertFalse(model.getAppData().productListData.enabled);
	}

	@Test
	public void testCloseStatistics() {
		AppModel model = new AppModel();
		model.init();
		assertTrue(model.getAppData().loginData.enabled);
		model.closeStatistics();
		assertFalse(model.getAppData().statisticsData.enabled);
	}

	@Test
	public void testSubmitNewUser() {
		AppModel model = new AppModel();
		model.init();
		assertTrue(model.getAppData().loginData.enabled);
		model.register("admin", "admin".toCharArray());
		assertTrue(model.getAppData().registerData.enabled);
		model.submitNewUser("junit3415", "Junit Test", "1234".toCharArray(), "1234".toCharArray(), false);
		assertFalse(model.getAppData().registerData.enabled);
	}

	@Test
	public void testNewProduct() {
		AppModel model = new AppModel();
		model.init();
		assertTrue(model.getAppData().loginData.enabled);
		model.login("admin", "admin".toCharArray());
		assertFalse(model.getAppData().loginData.enabled);
		model.newProduct("12343", "Unicorn");
		assertTrue(model.getAppData().productListData.updated);
	}

	@Test
	public void testShowStats() {
		AppModel model = new AppModel();
		model.init();
		assertTrue(model.getAppData().loginData.enabled);
		model.login("admin", "admin".toCharArray());
		assertFalse(model.getAppData().loginData.enabled);
		List products = new ArrayList();
		model.showStats(products, new Date(0), new Date(), 1000);
		assertTrue(model.getAppData().statisticsData.enabled);
	}

	@Test
	public void testInitUpdateSupplies() {
		AppModel model = new AppModel();
		model.init();
		assertTrue(model.getAppData().loginData.enabled);
		model.login("admin", "admin".toCharArray());
		assertFalse(model.getAppData().loginData.enabled);
		List products = new ArrayList();
		model.initUpdateSupplies(products);
		assertNotNull(model.getAppData().updateSuppliesData.products);
	}

	@Test
	public void testUpdateSupplies() {
		AppModel model = new AppModel();
		model.init();
		assertTrue(model.getAppData().loginData.enabled);
		model.login("admin", "admin".toCharArray());
		assertFalse(model.getAppData().loginData.enabled);
		List products = new ArrayList();
		model.initUpdateSupplies(products);
		assertNotNull(model.getAppData().updateSuppliesData.products);
		model.updateSupplies(1);
		assertFalse(model.getAppData().updateSuppliesData.enabled);
	}
}
