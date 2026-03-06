package dao;

import java.util.List;

import entity.Category;

public interface CategoryDao {
	void insert(Category c);
	List<Category> findAll();
	Category findById(String id);
	void update(Category c);
	void delete(String id);
	boolean existsByName(String name);
	List<Category> searchByName(String keyword);
	boolean hasChildren(String id);
}
