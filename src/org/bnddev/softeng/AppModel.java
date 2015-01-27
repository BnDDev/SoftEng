package org.bnddev.softeng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Random;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.*;

import javax.swing.JOptionPane;

public class AppModel extends Observable {
	
	private AppData app;
	private Random rand;
	private MessageDigest md5;
	private Connection conn;
	private PreparedStatement stmtLogin;
	private PreparedStatement stmtNewUser;
	private PreparedStatement stmtProducts;
	private PreparedStatement stmtNewProduct;
	private PreparedStatement stmtInsertUpdate;
	private PreparedStatement stmtCheckUsername;
	private PreparedStatement stmtGetUpdatesRange;
	private PreparedStatement stmtUpdateProductName;
	private PreparedStatement stmtUpdateProductSupplies;
	
	public AppModel() {
		try {
			rand = new Random();
			md5 = MessageDigest.getInstance("MD5");
			conn = DriverManager.getConnection("jdbc:mysql://bnddev.no-ip.org:3306/softeng?useUnicode=true&characterEncoding=UTF-8", "softeng", "SomeRandomPass");
			stmtLogin = conn.prepareStatement("SELECT `id`, `is_admin` FROM `users` WHERE `username` = ? AND `password` = MD5(CONCAT(?, `salt`))");
			stmtNewUser = conn.prepareStatement("INSERT INTO `users` (`username`, `fullname`, `password`, `salt`, `is_admin`) VALUES (?, ?, MD5(?), ?, ?)");
			stmtProducts = conn.prepareStatement("SELECT `id`, `name`, `supplies` FROM `products`;");
			stmtNewProduct = conn.prepareStatement("INSERT INTO `products` (`id`, `name`) values (?, ?)");
			stmtInsertUpdate = conn.prepareStatement("INSERT INTO `updates` (`product_id`, `author`, `supply_diff`) VALUES (?, ?, ?)");
			stmtCheckUsername = conn.prepareStatement("SELECT `is_admin` FROM `users` WHERE `username` = ?");
			stmtGetUpdatesRange = conn.prepareStatement("SELECT UNIX_TIMESTAMP(MIN(`date`)) as `min`, UNIX_TIMESTAMP(MAX(`date`)) as `max` FROM `updates`");
			stmtUpdateProductName = conn.prepareStatement("UPDATE `products` SET `name` = ? WHERE `id` = ?");
			stmtUpdateProductSupplies = conn.prepareStatement("UPDATE `products` SET `supplies` = ? WHERE `id` = ?");
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void init() {
		app = new AppData();
		app.loginData.enabled = true;
		setChanged();
		notifyObservers(app);
	}
	
	public AppData getAppData() {
		return app;
	}
	
	public void quit() {
		try {
			stmtUpdateProductSupplies.close();
			stmtUpdateProductName.close();
			stmtGetUpdatesRange.close();	
			stmtCheckUsername.close();
			stmtInsertUpdate.close();
			stmtNewProduct.close();
			stmtProducts.close();
			stmtNewUser.close();
			stmtLogin.close();
			conn.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		app = null;
	}

	public boolean tryLogin(String username, char[] password) {
		try {
			if(!username.isEmpty() && password.length > 0) {
				byte[] bytes = new byte[password.length];
				for(int i = 0; i < bytes.length; i++)
					bytes[i] = (byte)password[i];
				String hash = new BigInteger(1, md5.digest(bytes)).toString(16);
				stmtLogin.setString(1, username);
				stmtLogin.setString(2, hash);
				ResultSet rs = stmtLogin.executeQuery();
				if(rs.next()) {
					app.loginData.userID = rs.getInt(1);
					app.loginData.isAdmin = rs.getBoolean(2);
					return true;
				} else JOptionPane.showMessageDialog(null, "\u039B\u03AC\u03B8\u03BF\u03C2 \u03A3\u03C4\u03BF\u03B9\u03C7\u03B5\u03AF\u03B1", "\u03A3\u03C6\u03AC\u03BB\u03BC\u03B1 \u03C3\u03CD\u03BD\u03B4\u03B5\u03C3\u03B7\u03C2", JOptionPane.ERROR_MESSAGE);
			} else JOptionPane.showMessageDialog(null, "\u03A0\u03B1\u03C1\u03B1\u03BA\u03B1\u03BB\u03CE \u03B5\u03B9\u03C3\u03AC\u03B3\u03B1\u03C4\u03B5 \u03C4\u03B1 \u03C3\u03C4\u03BF\u03B9\u03C7\u03B5\u03AF\u03B1 \u03C3\u03B1\u03C2", "\u03A3\u03C6\u03AC\u03BB\u03BC\u03B1 \u03C3\u03CD\u03BD\u03B4\u03B5\u03C3\u03B7\u03C2", JOptionPane.ERROR_MESSAGE);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private boolean checkUsername(String username) {
		try {
			stmtCheckUsername.setString(1, username);
			ResultSet rs = stmtCheckUsername.executeQuery();
			if(rs.next()) {
				rs.close();
				return true;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void login(String username, char[] password) {
		try {
			if(tryLogin(username, password)) {
				ResultSet rs = stmtProducts.executeQuery();
				while(rs.next())
					app.productListData.rows.add(new Object[]{new Integer(rs.getInt(1)), rs.getString(2), new Integer(rs.getInt(3))});
				rs.close();
				rs = stmtGetUpdatesRange.executeQuery();
				if(rs.next()) {
					app.statisticsData.start = app.statisticsData.min = rs.getLong(1);
					app.statisticsData.end = app.statisticsData.max = rs.getLong(2);
					rs.close();
				} else JOptionPane.showMessageDialog(null, "\u0386\u03B3\u03BD\u03C9\u03C3\u03C4\u03BF \u03A3\u03C6\u03AC\u03BB\u03BC\u03B1", "\u03A3\u03C6\u03AC\u03BB\u03BC\u03B1 MySQL", JOptionPane.ERROR_MESSAGE);
				app.loginData.enabled = false;
				app.productListData.enabled = true;
				setChanged();
				notifyObservers(app);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void register(String username, char[] password) {
		if(tryLogin(username, password)) {
			if(app.loginData.isAdmin) {
				app.loginData.enabled = false;
				app.registerData.enabled = true;
				setChanged();
				notifyObservers(app);
			} else JOptionPane.showMessageDialog(null, "\u0394\u03B5\u03BD \u03AD\u03C7\u03B5\u03C4\u03B5 \u03B4\u03B9\u03BA\u03B1\u03B9\u03CE\u03BC\u03B1\u03C4\u03B1 \u03B4\u03B7\u03BC\u03B9\u03BF\u03C5\u03C1\u03B3\u03AF\u03B1\u03C2 \u03BD\u03AD\u03C9\u03BD \u03C7\u03C1\u03B7\u03C3\u03C4\u03CE\u03BD", "\u03A3\u03C6\u03AC\u03BB\u03BC\u03B1 \u03B5\u03B3\u03B3\u03C1\u03B1\u03C6\u03AE\u03C2", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void closeLogin() {
		quit();
	}
	
	public void closeRegister() {
		init();
	}
	
	public void closeProductList() {
		init();
	}
	
	public void closeStatistics() {
		app.statisticsData.enabled = false;
		setChanged();
		notifyObservers(app);
	}

	public void submitNewUser(String username, String fullname, char[] password1, char[] password2, boolean admin) {
		try {
			if(!username.isEmpty() && !fullname.isEmpty() && password1.length != 0) {
				if(!checkUsername(username)) {
					if(Arrays.equals(password1, password2)) {
						char[] randomChars = new char[8];
						char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();
						for(int i = 0; i < randomChars.length; i++)
							randomChars[i] = chars[rand.nextInt(64)];
						String salt = new String(randomChars);
						byte[] bytes = new byte[password1.length];
						for(int i = 0; i < bytes.length; i++)
							bytes[i] = (byte)password1[i];
						String hash = new BigInteger(1, md5.digest(bytes)).toString(16);
						stmtNewUser.setString(1, username);
						stmtNewUser.setString(2, fullname);
						stmtNewUser.setString(3, hash + salt);
						stmtNewUser.setString(4, salt);
						stmtNewUser.setBoolean(5, admin);
						if(stmtNewUser.executeUpdate() != 0) {
							app.productListData.updated = true;
						} else JOptionPane.showMessageDialog(null, "\u0386\u03B3\u03BD\u03C9\u03C3\u03C4\u03BF \u03A3\u03C6\u03AC\u03BB\u03BC\u03B1", "\u03A3\u03C6\u03AC\u03BB\u03BC\u03B1 MySQL", JOptionPane.ERROR_MESSAGE);
						app.loginData.enabled = true;
						app.registerData.enabled = false;
						setChanged();
						notifyObservers(app);
					} else JOptionPane.showMessageDialog(null, "\u039F\u03B9 \u03BA\u03C9\u03B4\u03B9\u03BA\u03BF\u03AF \u03B4\u03B5\u03BD \u03B5\u03AF\u03BD\u03B1\u03B9 \u03AF\u03B4\u03B9\u03BF\u03B9", "\u03A3\u03C6\u03AC\u03BB\u03BC\u03B1 \u03B5\u03B3\u03B3\u03C1\u03B1\u03C6\u03AE\u03C2", JOptionPane.ERROR_MESSAGE);
				} else JOptionPane.showMessageDialog(null, "\u03A4\u03BF \u03CC\u03BD\u03BF\u03BC\u03B1 \u03C7\u03C1\u03AE\u03C3\u03C4\u03B7 \u03C7\u03C1\u03B7\u03C3\u03B9\u03BC\u03BF\u03C0\u03BF\u03B9\u03B5\u03AF\u03C4\u03B1\u03B9 \u03AE\u03B4\u03B7", "\u03A3\u03C6\u03AC\u03BB\u03BC\u03B1 \u03B5\u03B3\u03B3\u03C1\u03B1\u03C6\u03AE\u03C2", JOptionPane.ERROR_MESSAGE);
			} else JOptionPane.showMessageDialog(null, "\u03A0\u03B1\u03C1\u03B1\u03BA\u03B1\u03BB\u03CE \u03C3\u03C5\u03BC\u03C0\u03BB\u03B7\u03C1\u03CE\u03C3\u03C4\u03B5 \u03CC\u03BB\u03B1 \u03C4\u03B1 \u03B1\u03C0\u03B1\u03C1\u03B1\u03AF\u03C4\u03B7\u03C4\u03B1 \u03C3\u03C4\u03BF\u03B9\u03C7\u03B5\u03AF\u03B1", "\u03A3\u03C6\u03AC\u03BB\u03BC\u03B1 \u03B5\u03B3\u03B3\u03C1\u03B1\u03C6\u03AE\u03C2", JOptionPane.ERROR_MESSAGE);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void newProduct(String code, String name) {
		try {
			if(!code.isEmpty() && !name.isEmpty()) {
				int id = Integer.parseInt(code);
				boolean alreadyInList = false;
				String[] confirmOptions = {"\u039D\u03B1\u03B9", "\u038C\u03C7\u03B9"};
				String confirmTitle = "\u039F \u03BA\u03C9\u03B4\u03B9\u03BA\u03CC\u03C2 \u03C0\u03C1\u03BF\u03CA\u03CC\u03BD\u03C4\u03BF\u03C2 \u03C5\u03C0\u03AC\u03C1\u03C7\u03B5\u03B9 \u03AE\u03B4\u03B7!";
				String confirmMessage = "\u0398\u03AD\u03BB\u03B5\u03C4\u03B5 \u03BD\u03B1 \u03B1\u03BB\u03BB\u03AC\u03BE\u03B5\u03C4\u03B5 \u03C4\u03BF \u03CC\u03BD\u03BF\u03BC\u03B1 \u03C4\u03BF\u03C5 \u03C0\u03C1\u03BF\u03CA\u03CC\u03BD\u03C4\u03BF\u03C2;";
				
				for(Iterator it = app.productListData.rows.iterator(); it.hasNext();) {
					Object[] row = (Object[])it.next();
					if(id == ((Integer)(row[0])).intValue()) {
						alreadyInList = true;
						if(JOptionPane.showOptionDialog(null, confirmMessage, confirmTitle, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, confirmOptions, confirmOptions[0]) == JOptionPane.YES_OPTION) {
							stmtUpdateProductName.setInt(2, id);
							stmtUpdateProductName.setString(1, name);
							if(stmtUpdateProductName.executeUpdate() != 0) {
								row[1] = name;
								app.productListData.updated = true;
							} else JOptionPane.showMessageDialog(null, "\u0386\u03B3\u03BD\u03C9\u03C3\u03C4\u03BF \u03A3\u03C6\u03AC\u03BB\u03BC\u03B1", "\u03A3\u03C6\u03AC\u03BB\u03BC\u03B1 MySQL", JOptionPane.ERROR_MESSAGE);
						}
						break;
					}
				}

				if(!alreadyInList) {
					stmtNewProduct.setInt(1, id);
					stmtNewProduct.setString(2, name);
					if(stmtNewProduct.executeUpdate() != 0) {
						app.productListData.rows.add(new Object[]{new Integer(id), name, new Integer(0)});
						app.productListData.updated = true;
					} else JOptionPane.showMessageDialog(null, "\u0386\u03B3\u03BD\u03C9\u03C3\u03C4\u03BF \u03A3\u03C6\u03AC\u03BB\u03BC\u03B1", "\u03A3\u03C6\u03AC\u03BB\u03BC\u03B1 MySQL", JOptionPane.ERROR_MESSAGE);
				}
				setChanged();
				notifyObservers(app);
			} else JOptionPane.showMessageDialog(null, "\u03A0\u03B1\u03C1\u03B1\u03BA\u03B1\u03BB\u03CE \u03C3\u03C5\u03BC\u03C0\u03BB\u03B7\u03C1\u03CE\u03C3\u03C4\u03B5 \u03CC\u03BB\u03B1 \u03C4\u03B1 \u03B1\u03C0\u03B1\u03C1\u03B1\u03AF\u03C4\u03B7\u03C4\u03B1 \u03C3\u03C4\u03BF\u03B9\u03C7\u03B5\u03AF\u03B1", "\u03A3\u03C6\u03AC\u03BB\u03BC\u03B1 \u03B5\u03B9\u03C3\u03B1\u03B3\u03C9\u03B3\u03AE\u03C2 \u03BD\u03AD\u03BF\u03C5 \u03C0\u03C1\u03BF\u03CA'\u03CC\u03BD\u03C4\u03BF\u03C2", JOptionPane.ERROR_MESSAGE);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void showStats(List products, Date startDate, Date endDate, int interval) {
		try {
			app.statisticsData.enabled = true;
			app.statisticsData.start = startDate.getTime() / 1000;
			app.statisticsData.end = endDate.getTime() / 1000;
			app.statisticsData.interval = interval;
			app.statisticsData.suppliesIn = new ArrayList();
			app.statisticsData.suppliesOut = new ArrayList();
			app.statisticsData.suppliesTotal = new ArrayList();
			
			String query1 = "SELECT (UNIX_TIMESTAMP(`date`) DIV " + interval + " * " + interval + ") AS `time_frame`, SUM(`supply_diff`) AS `value` FROM `updates` WHERE UNIX_TIMESTAMP(`date`) >= " + app.statisticsData.start + " AND UNIX_TIMESTAMP(`date`) <= " + app.statisticsData.end;
			String query2 = " ";
			if(products != null && !products.isEmpty()) {
				query2 += "AND `product_id` IN (";
				for(int i = products.size() - 1; i >= 0; i--) {
					query2 += ((Integer)(((Object[])products.get(i))[0])).intValue();
					if(i != 0)
						query2 += ",";
				}
				query2 += ") ";
			}
			query2 += "GROUP BY `time_frame`";

			PreparedStatement stmtShowStatisticsIn = conn.prepareStatement(query1 + " AND `supply_diff` > 0" + query2);
			ResultSet rs = stmtShowStatisticsIn.executeQuery();
			while(rs.next())
				app.statisticsData.suppliesIn.add(new Object[]{ new Date(rs.getLong(1) * 1000), new Integer(rs.getInt(2)) });
			rs.close();
			stmtShowStatisticsIn.close();
			
			PreparedStatement stmtShowStatisticsOut = conn.prepareStatement(query1 + " AND `supply_diff` < 0" + query2);
			rs = stmtShowStatisticsOut.executeQuery();
			while(rs.next())
				app.statisticsData.suppliesOut.add(new Object[]{ new Date(rs.getLong(1) * 1000), new Integer(rs.getInt(2)) });
			rs.close();
			stmtShowStatisticsOut.close();
			
			String queryTotal = "SELECT (UNIX_TIMESTAMP(`date`) DIV " + interval + " * " + interval + ") AS `time_frame`, SUM(`supply_diff`) AS `value`, (SELECT SUM(`supply_diff`) FROM `updates` WHERE `date` <= MAX(`a`.`date`)" + query2 + ") AS `running_total` FROM `updates` AS `a` WHERE UNIX_TIMESTAMP(`date`) >= " + app.statisticsData.start + " AND UNIX_TIMESTAMP(`date`) <= " + app.statisticsData.end + query2;
			PreparedStatement stmtShowStatisticsTotal = conn.prepareStatement(queryTotal);
			rs = stmtShowStatisticsTotal.executeQuery();
			while(rs.next())
				app.statisticsData.suppliesTotal.add(new Object[]{ new Date(rs.getLong(1) * 1000), new Integer(rs.getInt(3)) });
			rs.close();
			stmtShowStatisticsTotal.close();
			
			setChanged();
			notifyObservers(app);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void initUpdateSupplies(List products) {
		app.updateSuppliesData.enabled = true;
		app.updateSuppliesData.products = products;
		setChanged();
		notifyObservers(app);
	}
	
	public void updateSupplies(int diff) {
		try {
			app.updateSuppliesData.enabled = false;
			for(Iterator it = app.updateSuppliesData.products.iterator(); it.hasNext();) {
				Object[] product = (Object[])it.next();
				int productId = ((Integer)(product[0])).intValue();
				int productDiff = diff;
				int productSupplies = ((Integer)(product[2])).intValue() + diff;
				if(productSupplies < 0) {
					productDiff -= productSupplies;
					productSupplies = 0;
				}
				stmtUpdateProductSupplies.setInt(2, productId);
				stmtUpdateProductSupplies.setInt(1, productSupplies);
				if(stmtUpdateProductSupplies.executeUpdate() != 0) {
					stmtInsertUpdate.setInt(1, productId);
					stmtInsertUpdate.setInt(2, app.loginData.userID);
					stmtInsertUpdate.setInt(3, productDiff);
					if(stmtInsertUpdate.executeUpdate() != 0) {
						product[2] = new Integer(productSupplies);
						app.productListData.updated = true;
					} else JOptionPane.showMessageDialog(null, "\u0386\u03B3\u03BD\u03C9\u03C3\u03C4\u03BF \u03A3\u03C6\u03AC\u03BB\u03BC\u03B1", "\u03A3\u03C6\u03AC\u03BB\u03BC\u03B1 MySQL", JOptionPane.ERROR_MESSAGE);
				} else JOptionPane.showMessageDialog(null, "\u0386\u03B3\u03BD\u03C9\u03C3\u03C4\u03BF \u03A3\u03C6\u03AC\u03BB\u03BC\u03B1", "\u03A3\u03C6\u03AC\u03BB\u03BC\u03B1 MySQL", JOptionPane.ERROR_MESSAGE);
			}
			setChanged();
			notifyObservers(app);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
