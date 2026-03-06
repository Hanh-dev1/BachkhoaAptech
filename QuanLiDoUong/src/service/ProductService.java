package service;

import dao.CategoryDao;
import dao.ProductDao;
import entity.Category;
import entity.Product;
import utility.ValidateUtil;

import java.util.List;

public class ProductService {
	private final ProductDao productDAO;
	private final CategoryDao categoryDAO;
	
	public ProductService(ProductDao productDAO, CategoryDao categoryDAO) {
		this.productDAO = productDAO;
		this.categoryDAO = categoryDAO;
	}
	
	public boolean addProduct(Product p) {
		//ID 4 kí tự và bắt đầu bằng C
		if(!ValidateUtil.isValidProductId(p.getId())) {
			System.out.printf("\nMã đồ uống gồm 4 ký tự, bắt đầu bằng 'C'!");
			System.out.println();
			return false;
		}
		
		//Check ID trùng
		if(productDAO.findById(p.getId()) != null) {
			System.out.printf("\nMã đồ uống đã tồn tại!");
			System.out.println();
			return false;
		}
		
		//Tên 6-50 kí tự
		if(!ValidateUtil.isValidProductName(p.getName())) {
			System.out.printf("\nTên đồ uống phải từ 6-50 ký tự!");
			System.out.println();
            return false;
		}
		
		//Check tên trùng
		if(productDAO.existsByName(p.getName())) {
			System.out.printf("\nTên đồ uống đã tồn tại!");
			System.out.println();
            return false;
		}
		
		//Giá phải là số thực > 0
		if(!ValidateUtil.isValidProductPrice(p.getPrice())) {
			System.out.printf("\nGiá đồ uống phải là số thực lớn hơn 0!");
			System.out.println();
            return false;
		}
		
		//Ngày hết hạn phải lớn hơn ngày hiện tại
		if (!ValidateUtil.isValidProductExpiration(p.getExpiration())) {
            System.out.printf("\nNgày hết hạn phải lớn hơn ngày hiện tại!");
            System.out.println();
            return false;
        }
		
		//Check category ID
		if(categoryDAO.findById(p.getCategoryId()) == null) {
			System.out.printf("\nDanh mục của đồ uống không tồn tại!");
			System.out.println();
            return false;
		}
		
		productDAO.insert(p);
        System.out.printf("\nThêm đồ uống thành công!");
        System.out.println();
        return true;
	}
	
	public boolean updateProduct(Product p) {
		//Check ID đồ uống
		if(productDAO.findById(p.getId()) == null) {
			System.out.printf("\nKhông tìm thấy đồ uống để cập nhật!");
			System.out.println();
            return false;
		}
		
		//Tên 6-50 kí tự
		if (!ValidateUtil.isValidProductName(p.getName())) {
            System.out.printf("\nTên đồ uống phải từ 6-50 ký tự!");
            System.out.println();
            return false;
        }

		//Giá phải là số thực > 0
        if (!ValidateUtil.isValidProductPrice(p.getPrice())) {
            System.out.printf("\nGiá đồ uống phải là số thực lớn hơn 0!");
            System.out.println();
            return false;
        }

        //Ngày hết hạn phải lớn hơn ngày hiện tại
        if (!ValidateUtil.isValidProductExpiration(p.getExpiration())) {
            System.out.printf("\nNgày hết hạn phải lớn hơn ngày hiện tại!");
            System.out.println();
            return false;
        }

        //Check category ID
        if (categoryDAO.findById(p.getCategoryId()) == null) {
            System.out.printf("\nDanh mục của đồ uống không tồn tại!");
            System.out.println();
            return false;
        }

        productDAO.update(p);
        System.out.printf("\nCập nhật đồ uống thành công!");
        System.out.println();
        return true;
	}
	
	public boolean deleteProduct(String id) {
		//Id phải > 0
		if(!ValidateUtil.isValidProductId(id)) {
			System.out.printf("\nMã đồ uống không hợp lệ!");
			System.out.println();
            return false;
		}
		//Id phải tồn tại
		Product found = productDAO.findById(id);
		if(found == null) {
			System.out.printf("\nĐồ uống không tồn tại!");
			System.out.println();
            return false;
		}
		
		productDAO.delete(id);
        System.out.printf("\nXóa đồ uống thành công!");
        System.out.println();
        return true;
	}
	
	public List<Product> getAll() {
        return productDAO.findAll();
    }

    public List<Product> getByCategory(String categoryId) {
        return productDAO.findByCategory(categoryId);
    }

    public List<Product> sortByPriceDesc() {
        return productDAO.sortByPrice();
    }

    public List<Product> sortByNameAsc() {
        return productDAO.sortByName();
    }

    public List<Product> getExpiringSoon() {
        return productDAO.expiringSoon();
    }

    public Product getById(String id) {
        return productDAO.findById(id);
    }
}
