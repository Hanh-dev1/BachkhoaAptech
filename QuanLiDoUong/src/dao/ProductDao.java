package dao;

import java.util.List;

import entity.Product;

public interface ProductDao {
	void insert(Product p);
	List<Product> findAll();
	Product findById(String id);
	List<Product> findByCategory(String categoryId);
	void update(Product p);
	void delete(String id);
	List<Product> sortByPrice();
	List<Product> sortByName();
	List<Product> expiringSoon();
	boolean existsByName(String name);
}


