package net.project.db.sql;

import home.db.DBConnection;

import java.sql.ResultSet;
import java.sql.SQLException;


import net.project.common.Constants;
import net.project.db.entity.Config;

public class ConfigSql {

	
	public Config loadConfig() throws SQLException, ClassNotFoundException{

		Config config = null;

		
		DBConnection con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, Constants.dbType);

		String query = "SELECT * FROM " + Config.TBL_NAME;// where " + Config.ID + " = :id";

		ResultSet rs = con.createSelectQuery(query)
				.getSelectResultSet();

		while (rs.next()) {
			config = new Config(rs);
		}

		con.close();
		return config;
	}
	
	public Config addConfig(Config config) throws SQLException, ClassNotFoundException{

		DBConnection con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, Constants.dbType);

		int key = con.buildAddQuery(Config.TBL_NAME)
				.setParameter(Config.EMAIL_USER, config.getEmailUserName())
				.setParameter(Config.EMAIL_PASS, config.getEmailPassword())
				.add();

		con.close();

		config.setId(key);

		return config;
	}
	
	public void updateConfig(Config config) throws SQLException, ClassNotFoundException{

		DBConnection con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, Constants.dbType);

		con.buildUpdateQuery(Config.TBL_NAME)

		.setParameter(Config.EMAIL_USER, config.getEmailUserName())
		.setParameter(Config.EMAIL_PASS, config.getEmailPassword())

		.addUpdWhereClause("WHERE " + Config.ID + " = :cfgId", config.getId())
		.update();


		con.close();
	}

}
