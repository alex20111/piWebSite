package net.project.db.sql;

import home.crypto.BCryptHash;
import home.db.DbClass;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.project.common.Constants;
import net.project.db.entity.Config;
import net.project.db.entity.FileInfo;
import net.project.db.entity.Folder;
import net.project.db.entity.FolderShare;
import net.project.db.entity.Inventory;
import net.project.db.entity.InventoryGroup;
import net.project.db.entity.InventoryRef;
import net.project.db.entity.Project;
import net.project.db.entity.ProjectFile;
import net.project.db.entity.ProjectRef;
import net.project.db.entity.SubTask;
import net.project.db.entity.Task;
import net.project.db.entity.TaskCategory;
import net.project.db.entity.Temperature;
import net.project.db.entity.User;
import net.project.enums.AccessEnum;
import net.project.enums.TempRecName;

public class CreateTables {

	private static Log log = LogFactory.getLog(CreateTables.class);
	
	@SuppressWarnings("resource")
	public static boolean createTables() throws SQLException, ClassNotFoundException{

		Connection con = null;
		PreparedStatement pst = null;
		
		try {
			if (Constants.dbType == DbClass.H2){
			Class.forName("org.h2.Driver");
			}else if (Constants.dbType == DbClass.Mysql){
				Class.forName("com.mysql.jdbc.Driver");
			}
			con = DriverManager.getConnection(Constants.url,Constants.dbUser, Constants.dbPassword);

			//check if tables exist, if not create the tables
			DatabaseMetaData meta = con.getMetaData();
			ResultSet tables = meta.getTables(null, null, User.TBL_NAME.toUpperCase(), null);
			
			if (!tables.next()) {

				log.debug("Database Does not exist, create");
				pst = con.prepareStatement(User.createTable());
				pst.executeUpdate();					
				pst = con.prepareStatement(Folder.createTable());
				pst.executeUpdate();
				pst = con.prepareStatement(FileInfo.createTable());
				pst.executeUpdate();
				pst = con.prepareStatement(FolderShare.createTable());
				pst.executeUpdate();
				pst = con.prepareStatement(Temperature.createTable());
				pst.executeUpdate();
				pst = con.prepareStatement(Config.createTable());
				pst.executeUpdate();
				pst = con.prepareStatement(Task.createTable());
				pst.executeUpdate();
				pst = con.prepareStatement(TaskCategory.createTable());
				pst.executeUpdate();
				pst = con.prepareStatement(SubTask.createTable());
				pst.executeUpdate();
				pst = con.prepareStatement(Inventory.createTable());
				pst.executeUpdate();
				pst = con.prepareStatement(InventoryGroup.createTable());
				pst.executeUpdate();
				pst = con.prepareStatement(InventoryRef.createTable());
				pst.executeUpdate();
				pst = con.prepareStatement(Project.createTable());
				pst.executeUpdate();	
				pst = con.prepareStatement(ProjectRef.createTable());
				pst.executeUpdate();	
				pst = con.prepareStatement(ProjectFile.createTable());
				pst.executeUpdate();

				Config cfg = new Config();
				
				new ConfigSql().addConfig(cfg);

				//add index
				pst = con.prepareStatement(Temperature.createRecDateIndex());
				pst.executeUpdate();				
				
				//add admin user (default user)
				UserSql uSql = new UserSql();
				User user = new User();
				user.setUserName("admin");
				user.setPassword(BCryptHash.hashString("admin"));
				user.setAccess(AccessEnum.ADMIN);
				user.setEmail("Home");
				user.setFirstName("Admin");
				user.setLastName("Boss");

				uSql.addUser(user);
				
				//add guest
				User guest = new User();
				guest.setUserName("guest");
				guest.setPassword(BCryptHash.hashString("guest"));
				guest.setAccess(AccessEnum.VIEW);
				guest.setEmail("guest");
				guest.setFirstName("Guest");
				guest.setLastName("");

				uSql.addUser(guest);
				
				
				//create temp data.
//				createTemp();
				
			}
			

		} finally {

			if (pst != null) {
				pst.close();
			}
			if (con != null) {
				con.close();
			}

		}
		return true;
	}
	
	@SuppressWarnings("unused")
	private static void createTemp(){
		
		DecimalFormat df = new DecimalFormat("#.####");
		
		float minX = 1.0f;
		float maxX = 40.0f;		
		
		List<Temperature> tempRecA = new ArrayList<Temperature>();
		List<Temperature> tempRecB = new ArrayList<Temperature>();

		Random rand = new Random();
		
		Calendar date = Calendar.getInstance();
		date.set(Calendar.HOUR_OF_DAY, 00);
		date.set(Calendar.MINUTE, 00);
		date.set(Calendar.SECOND, 00);
		date.set(Calendar.MILLISECOND, 00);
		
		
		for(int i = 0 ; i < 210000; i++){
			
			float finalX = rand.nextFloat() * (maxX - minX) + minX;
			double battLvl = rand.nextDouble() * (3.2 - 3.0) + 3.0;
			
			Temperature t = new Temperature();
			t.setTempC(finalX);
			t.setRecordedDate(date.getTime());
			t.setRecorderName(TempRecName.AA.getRecName());
			t.setBatteryLevel(df.format(battLvl));
			
			tempRecA.add(t);
			
			date.add(Calendar.MINUTE, 5);
			
//			System.out.println("AA: " + t);
		}
		
		
		Calendar date2 = Calendar.getInstance();
		date2.set(Calendar.HOUR_OF_DAY, 4);
		date2.set(Calendar.MINUTE, 30);
		date2.set(Calendar.SECOND, 00);
		date2.set(Calendar.MILLISECOND, 00);
		
		
		for(int i = 0 ; i < 200000; i++){
			
			float finalX = rand.nextFloat() * (maxX - minX) + minX;
			double battLvl = rand.nextDouble() * (3.2 - 3.0) + 3.0;
			
			Temperature t = new Temperature();
			t.setTempC(finalX);
			t.setRecordedDate(date2.getTime());
			t.setRecorderName(TempRecName.BB.getRecName());
			t.setBatteryLevel(df.format(battLvl));
			tempRecB.add(t);
			
			date2.add(Calendar.MINUTE, 5);
			
//			System.out.println("BB: " + t);
		}
		
		
		System.out.println("tempRecA size: " + tempRecA.size());
		System.out.println("tempRecB size: " + tempRecB.size());
		
		TempSql sql = new TempSql();
		try {
			try {
				sql.addTempList(tempRecA);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
			try {
				sql.addTempList(tempRecB);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	
	
}
