package net.project.db.manager;

import java.sql.SQLException;

import net.project.db.entity.Config;
import net.project.db.sql.ConfigSql;

public class ConfigManager {

	private ConfigSql sql;
	
	public ConfigManager(){
		sql = new ConfigSql();
	}
	
	public Config loadConfig() throws SQLException, ClassNotFoundException{
		return sql.loadConfig();
	}
	
	public Config addConfig(Config config) throws SQLException, ClassNotFoundException{
		return sql.addConfig(config);
	}
	
	public void updateConfig(Config config) throws SQLException, ClassNotFoundException{
		sql.updateConfig(config);
	}
}
