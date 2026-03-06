package dao;

import entity.Category;
import utility.DBUtility;

import java.sql.*;
import java.util.*;

public class CategoryDaoImpl implements CategoryDao {
	@Override
	public void insert(Category c) {
		try(Connection conn = DBUtility.openConnection()){
			PreparedStatement ps = conn.prepareStatement("INSERT INTO category VALUES (?,?,?,?)"); 
			
			ps.setString(1, c.getId());
			ps.setString(2, c.getName());
			ps.setBoolean(3, c.isStatus());
			ps.setString(4, c.getParentId());
			
			ps.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public List<Category> findAll() {
		List<Category> list = new ArrayList<>();
		
		try(Connection conn = DBUtility.openConnection()) {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM category");
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				Category c = new Category();
				c.setId(rs.getString("id"));
				c.setName(rs.getString("name"));
				c.setStatus(rs.getBoolean("status"));
				c.setParentId(rs.getString("parent_id"));
				list.add(c);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	@Override
	public Category findById(String id) {
		Category c = null;
		 
		try(Connection conn = DBUtility.openConnection()){
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM category WHERE id = ?");
			
			ps.setString(1, id);
			
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				c = new Category();
				c.setId(rs.getString("id"));
				c.setName(rs.getString("name"));
				c.setStatus(rs.getBoolean("status"));
				c.setParentId(rs.getString("parent_id"));
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return c;
	}
	
	@Override
	public void update(Category c) {
		try(Connection conn = DBUtility.openConnection()) {
			PreparedStatement ps = conn.prepareStatement("UPDATE category SET name=?, status=?, parent_id=? WHERE id=?");
			
			ps.setString(1, c.getId());
			ps.setString(2, c.getName());
			ps.setBoolean(3, c.isStatus());
			ps.setString(4, c.getParentId());
			
			ps.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete(String id) {
		try (Connection conn = DBUtility.openConnection()) {
			PreparedStatement ps = conn.prepareStatement("DELETE FROM category WHERE id = ?");
			
			ps.setString(1, id);
			
			ps.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean existsByName(String name) {
		try(Connection conn = DBUtility.openConnection()){
			PreparedStatement ps  = conn.prepareStatement("SELECT COUNT(1) FROM category WHERE name = ?");
			
			ps.setString(1,name);
			
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				return rs.getInt(1) > 0;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public List<Category> searchByName(String keyword) {
		List<Category> list = new ArrayList<>();
		
		try(Connection conn = DBUtility.openConnection()){
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM category WHERE name LIKE ?");
			
			ps.setString(1, "%" + keyword + "%");
			
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				Category c = new Category();
				c.setId(rs.getString("id"));
				c.setName(rs.getString("name"));
				c.setStatus(rs.getBoolean("status"));
				c.setParentId(rs.getString("parent_id"));
				list.add(c);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public boolean hasChildren(String id) {
		try(Connection conn = DBUtility.openConnection()){
			PreparedStatement ps = conn.prepareStatement("SELECT COUNT(1) FROM category WHERE parent_id = ?");
			
			ps.setString(1, id);
			
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				return rs.getInt(1) > 0;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
