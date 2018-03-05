package net.project.db.sql;

import home.db.DBConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.project.common.Constants;
import net.project.db.entity.Inventory;
import net.project.db.entity.InventoryGroup;
import net.project.db.entity.InventoryRef;

public class InventorySql {

	/**
	 * load all inventory for the current user.  //TODO load shared in the future,.
	 * 
	 * @param userId
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public List<Inventory> loadAllInventoryForUser(int userId, boolean loadGroup) throws SQLException, ClassNotFoundException{
		List<Inventory> invList = new ArrayList<Inventory>();
		
		Map<Integer, InventoryGroup> groups = new HashMap<Integer, InventoryGroup>();
		
		DBConnection con = null;
		try{
			con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, Constants.dbType);

			String query = "SELECT * FROM " + Inventory.TBL_NAME + " where " + Inventory.OWNER + " = :owner";

			ResultSet rs = con.createSelectQuery(query)
					.setParameter("owner", userId)
					.getSelectResultSet();

			while (rs.next()) {
				Inventory inv = new Inventory(rs);
				
				//load groups 
				if (loadGroup){
					if(groups.containsKey(inv.getGroupId())){
						inv.setGroup(groups.get(inv.getGroupId()));
					}else{
						//load from db
						String queryGroup = "SELECT * FROM " + InventoryGroup.TBL_NAME + " where " + InventoryGroup.ID + " = :groupId";
						ResultSet rsGroup = con.createSelectQuery(queryGroup)
								.setParameter("groupId", inv.getGroupId())
								.getSelectResultSet();
						
						while (rsGroup.next()) {
							InventoryGroup ig = new InventoryGroup(rsGroup);
							inv.setGroup(ig);
							groups.put(inv.getGroupId(), ig);
						}
					}
				}
				
				//load references to inventory item
				inv.setReferences(loadReferences(con, inv.getId()));
				
				
				invList.add(inv);
			}

		}finally{
			if (con!=null){
				con.close();
			}
		}
		return invList;
	}
	
	public List<Inventory> loadAllInventoryByUserAndGroup(int userId, int groupId) throws SQLException, ClassNotFoundException{
		List<Inventory> invList = new ArrayList<Inventory>();
		DBConnection con = null;
		try{
			con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, Constants.dbType);

			String query = "SELECT * FROM " + Inventory.TBL_NAME + " where " + Inventory.OWNER + " = :owner AND " + Inventory.GROUP_ID + " = :group";
			
			ResultSet rs = con.createSelectQuery(query)
					.setParameter("owner", userId)
					.setParameter("group", groupId)
					.getSelectResultSet();

			while (rs.next()) {
				Inventory inv = new Inventory(rs);
				
				//load all inventory item references
				inv.setReferences(loadReferences(con, inv.getId()));
				
				invList.add(inv);
			}

		}finally{
			if (con!=null){
				con.close();
			}
		}
		return invList;
	}

	/**
	 * Load the group names for the side menu.
	 * @param userId
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public List<InventoryGroup> loadSideMenuGroupsForUser(int userId) throws SQLException, ClassNotFoundException{
		List<InventoryGroup> groupList = new ArrayList<InventoryGroup>();
		DBConnection con = null;
		try{
			con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, Constants.dbType);

			String query = "SELECT * FROM " + InventoryGroup.TBL_NAME + " where " + InventoryGroup.OWNER + " = :owner";

			ResultSet rs = con.createSelectQuery(query)
					.setParameter("owner", userId)
					.getSelectResultSet();

			while (rs.next()) {
				InventoryGroup groupName = new InventoryGroup(rs);
				groupList.add(groupName);
			}

		}finally{
			if (con!=null){
				con.close();
			}
		}
		return groupList;
	}
	/**
	 * Load the inventory group by the group ID.
	 * @param grpId
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public InventoryGroup loadInvetoryGroupById(int grpId) throws SQLException, ClassNotFoundException{
		InventoryGroup group = null;
		DBConnection con = null;
		try{
			con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, Constants.dbType);

			String query = "SELECT * FROM " + InventoryGroup.TBL_NAME + " where " + InventoryGroup.ID + " = :id";

			ResultSet rs = con.createSelectQuery(query)
					.setParameter("id", grpId)
					.getSelectResultSet();

			while (rs.next()) {
				 group = new InventoryGroup(rs);

			}

		}finally{
			if (con!=null){
				con.close();
			}
		}
		return group;
	}
	/**
	 * Load inventory item by id
	 * @param id
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public Inventory loadInventoryById(int id) throws SQLException, ClassNotFoundException{
		Inventory inv = null;
		DBConnection con = null;
		try{
			con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, Constants.dbType);

			String query = "SELECT * FROM " + Inventory.TBL_NAME + " where " + Inventory.ID + " = :id";

			ResultSet rs = con.createSelectQuery(query)
					.setParameter("id", id)
					.getSelectResultSet();

			while (rs.next()) {
				inv = new Inventory(rs);
				//load references								
				inv.setReferences(loadReferences(con, inv.getId()));		
				
			}

		}finally{
			if (con!=null){
				con.close();
			}
		}
		return inv;
	}
	
	public Inventory addInventoryItem(Inventory item) throws SQLException, ClassNotFoundException{
		DBConnection con = null;

		try{
			con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, Constants.dbType);

			int key = con.buildAddQuery(Inventory.TBL_NAME)
					.setParameter(Inventory.NAME, item.getName())
					.setParameter(Inventory.CATEGORY, item.getCategory())
					.setParameter(Inventory.OWNER, item.getOwnerId())
					.setParameter(Inventory.QTY, item.getQty())
					.setParameter(Inventory.GROUP_ID, item.getGroupId())
					.setParameter(Inventory.DETAILS, item.getDetails())
					.setParameter(Inventory.THUMB_NAME, item.getThumbBase64())
					.add();

			item.setId(key);
			
			//add the references to the item.
			if (item.getReferences() != null && !item.getReferences().isEmpty()){
				addReference(con, item.getReferences(), key);
			}			
		}finally{
			if (con!=null){
				con.close();
			}
		}
		return item;
	}

	public void updateInventoryItem(Inventory item) throws SQLException, ClassNotFoundException{
		DBConnection con = null;
		try{
			con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, Constants.dbType);

			con.buildUpdateQuery(Inventory.TBL_NAME)

			.setParameter(Inventory.CATEGORY, item.getCategory())
			.setParameter(Inventory.DETAILS, item.getDetails())
			.setParameter(Inventory.GROUP_ID, item.getGroupId())
			.setParameter(Inventory.NAME, item.getName())
			.setParameter(Inventory.OWNER, item.getOwnerId())
			.setParameter(Inventory.QTY, item.getQty())
			.setParameter(Inventory.THUMB_NAME, item.getThumbBase64())
			.addUpdWhereClause("WHERE " + Inventory.ID + " = :invId", item.getId())
			.update();
			
			
		}finally{
			if (con!=null){
				con.close();
			}
		}
	}
	
	public InventoryGroup addInvGroup(InventoryGroup ig) throws SQLException, ClassNotFoundException{
		DBConnection con = null;

		try{
			con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, Constants.dbType);

			int key = con.buildAddQuery(InventoryGroup.TBL_NAME)
					.setParameter(InventoryGroup.GROUP_NAME, ig.getGroupName())
					.setParameter(InventoryGroup.NBR_ITEMS, ig.getNumberOfItems())
					.setParameter(InventoryGroup.OWNER, ig.getOwnerId())
					.add();

			ig.setId(key);
		}finally{
			if (con!=null){
				con.close();
			}
		}

		return ig;
	}
	
	public void deleteItem(int itemId) throws SQLException, ClassNotFoundException{

		DBConnection con = null;
		try{
			con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, Constants.dbType);

			String query = "DELETE FROM " + Inventory.TBL_NAME + " where " + Inventory.ID + " = :id";

			con.createSelectQuery(query)
			.setParameter("id", itemId)
			.delete();

		}finally{
			if (con!=null){
				con.close();
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void deleteListOfItem(List<Integer> itemIds) throws SQLException, ClassNotFoundException{

		DBConnection con = null;
		try{
			con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, Constants.dbType);

			con.deleteInBatch(Inventory.TBL_NAME, Inventory.ID, (List)itemIds);
			
		}finally{
			if (con!=null){
				con.close();
			}
		}
	}

	public void deleteGroup(InventoryGroup ig) throws SQLException, ClassNotFoundException{

		DBConnection con = null;
		try{
			con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, Constants.dbType);

			String query = "DELETE FROM " + InventoryGroup.TBL_NAME + " where " + InventoryGroup.ID + " = :id";

			con.createSelectQuery(query)
			.setParameter("id", ig.getId())
			.delete();

		}finally{
			if (con!=null){
				con.close();
			}
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void deleteListOfReferences(List<Integer> refIds) throws SQLException, ClassNotFoundException{

		DBConnection con = null;
		try{
			con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, Constants.dbType);

			con.deleteInBatch(InventoryRef.TBL_NAME, InventoryRef.ID, (List)refIds);
			
		}finally{
			if (con!=null){
				con.close();
			}
		}
	}	
	
	/**
	 * add the inventory references to the table.
	 * @param con
	 * @param refs
	 * @param invId
	 * @throws SQLException
	 * @throws ClassNotFoundException 
	 */
	public void addReference(DBConnection con, List<InventoryRef> refs, int invId) throws SQLException, ClassNotFoundException{

		boolean closeCon = false;
		if (con == null){
			con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, Constants.dbType);
			closeCon = true;
		}
		
		con.buildAddQuery(InventoryRef.TBL_NAME);

		for(InventoryRef ref : refs){		 
			con.setParameter(InventoryRef.REF_NAME, ref.getReferenceName())
			.setParameter(InventoryRef.REF_TYPE, ref.getType().name())
			.setParameter(InventoryRef.INVENTORY_ID_FK, invId)				
			.addToBatch();
		}
		
		con.executeBatchQuery();
		
		if (closeCon){
			con.close();
		}
		
	}
	public void updateReference(DBConnection con, List<InventoryRef> refs, int invId) throws SQLException, ClassNotFoundException{

		boolean closeCon = false;
		if (con == null){
			con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, Constants.dbType);
			closeCon = true;
		}		

		for(InventoryRef ref : refs){	

			con.buildUpdateQuery(InventoryRef.TBL_NAME)

			.setParameter(InventoryRef.REF_NAME, ref.getReferenceName())
			.setParameter(InventoryRef.REF_TYPE, ref.getType().name())
			.setParameter(InventoryRef.INVENTORY_ID_FK, invId)

			.addUpdWhereClause("WHERE " + InventoryRef.ID + " = :invId", ref.getId())
			.update();
		}

		if (closeCon){
			con.close();
		}	
	}
	
	public List<InventoryRef> loadReferences(DBConnection con, int invId) throws SQLException, ClassNotFoundException{
		
		boolean closeCon = false;
		if (con == null){
			con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, Constants.dbType);
			closeCon = true;
		}		
		
		String refQuery = "Select * from " + InventoryRef.TBL_NAME + " WHERE " + InventoryRef.INVENTORY_ID_FK + " = :itemId";
		ResultSet refRs = con.createSelectQuery(refQuery).setParameter("itemId", invId).getSelectResultSet();
		
		List<InventoryRef> refs = new ArrayList<InventoryRef>();
		while(refRs.next()){
			InventoryRef ref = new InventoryRef(refRs);
			refs.add(ref);
		}
		
		if (closeCon){
			con.close();
		}
		
		return refs;
	}
	
	public void updateInvGroup(InventoryGroup grp) throws SQLException, ClassNotFoundException{

		DBConnection con = null;
		try{
			con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, Constants.dbType);

			con.buildUpdateQuery(InventoryGroup.TBL_NAME)
			.setParameter(InventoryGroup.GROUP_NAME, grp.getGroupName())
			.setParameter(InventoryGroup.NBR_ITEMS, grp.getNumberOfItems())
			.addUpdWhereClause("WHERE " + InventoryGroup.ID + " = :invId", grp.getId())
			.update();
			
			
		}finally{
			if (con!=null){
				con.close();
			}
		}
		
	}
}
