package service;

import dao.CategoryDao;
import entity.Category;
import utility.ValidateUtil;

import java.util.List;

public class CategoryService {
	private final CategoryDao categoryDAO;
	
	public CategoryService(CategoryDao categoryDAO) {
		this.categoryDAO = categoryDAO;
	}
	
	public boolean addCategory(Category c) {
		//ID > 0
		if(!ValidateUtil.isValidCategoryId(c.getId())) {
			System.out.printf("\nMã danh mục phải là số nguyên > 0!");
			System.out.println();
			return false;
		}
		//Check ID trùng
		if(categoryDAO.findById(c.getId()) != null) {
			System.out.printf("\nMã danh mục đã tồn tại!");
			System.out.println();
			return false;
		}
		//Validate tên 6-30 kí tự
		if(!ValidateUtil.isValidCategoryName(c.getName())) {
			System.out.printf("\nTên danh mục phải từ 6-30 ký tự!");
			System.out.println();
            return false;
		}
		//Check tên trùng
		if(categoryDAO.existsByName(c.getName())) {
			System.out.printf("\nTên danh mục đã tồn tại!");
			System.out.println();
            return false;
		}
		//Validate parentId
		if(!isValidParentId(c.getParentId())) {
			return false;
		}
		
		categoryDAO.insert(c);
        System.out.printf("\nThêm danh mục thành công!");
        System.out.println();
        return true;
	}
	
	private boolean isValidParentId(String parentId) {
		//Danh mục gốc có parentId = 0
		if(parentId == null || parentId.trim().isEmpty() || parentId.equals("0")){
			return true;
		}
		//Check parentId có tồn tại ko
		Category parent = categoryDAO.findById(parentId);
		if(parent == null) {
			System.out.printf("\nDanh mục cha không tồn tại!");
			System.out.println();
			return false;
		}
		//Ko vượt quá 3 cấp
		int parentLevel = getLevel(parentId);
		if(parentLevel >= 3) {
			System.out.printf("\nKhông được vượt quá 3 cấp danh mục!");
			System.out.println();
			return false;
		}
		return true;
	}
	
	private int getLevel(String categoryId) {
		int level = 1;
		Category current = categoryDAO.findById(categoryId);
		while(current != null && current.getParentId() != null && !current.getParentId().equals("0")) {
			level++;
			current = categoryDAO.findById(current.getParentId());
		}
		return level;
	}
	
	public boolean deleteCategory(String id) {
		//Id phải > 0
		if(!ValidateUtil.isValidCategoryId(id)) {
			System.out.printf("\nMã danh mục không hợp lệ!");
			System.out.println();
            return false;
		}
		//Id phải tồn tại
		Category found = categoryDAO.findById(id);
		if(found == null) {
			System.out.printf("\nDanh mục không tồn tại!");
			System.out.println();
            return false;
		}
		//Ko thể xóa nếu có danh mục con
		if(categoryDAO.hasChildren(id)) {
			System.out.printf("\nKhông thể xóa danh mục đang có danh mục con!");
			System.out.println();
            return false;
		}
		
		categoryDAO.delete(id);
        System.out.printf("\nXóa danh mục thành công!");
        System.out.println();
        return true;
	}
	
	public List<Category> getAll(){
		return categoryDAO.findAll();
	}
	
	public Category getById(String id) {
		return categoryDAO.findById(id);
	}
	
	public List<Category> searchByName(String keyword) {
        return categoryDAO.searchByName(keyword == null ? "" : keyword.trim());
    }
}
