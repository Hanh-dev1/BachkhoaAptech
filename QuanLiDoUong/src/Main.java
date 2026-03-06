import dao.CategoryDao;
import dao.CategoryDaoImpl;
import dao.ProductDao;
import dao.ProductDaoImpl;
import entity.Category;
import entity.Product;
import service.CategoryService;
import service.ProductService;
import utility.ValidateUtil;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;

public class Main {
	private static final Scanner sc = new Scanner(System.in);

//MENU GỐC
	public static void main(String[] args) {
		CategoryDao categoryDAO = new CategoryDaoImpl();
		ProductDao productDAO = new ProductDaoImpl();

		CategoryService categoryService = new CategoryService(categoryDAO);
		ProductService productService = new ProductService(productDAO, categoryDAO);

		while (true) {
			System.out.printf("\n===================== MENU =====================");
			System.out.println();
			System.out.println("1. Quản lý danh mục");
			System.out.println("2. Quản lý đồ uống");
			System.out.println("3. Thoát");
			System.out.print("Sự lựa chọn của bạn: ");
			String choice = sc.nextLine();

			switch (choice) {
			case "1":
				categoryMenu(categoryService);
				break;
			case "2":
				productMenu(categoryService, productService);
				break;
			case "3":
				System.out.printf("\nThoát chương trình.");
				return;
			default:
				System.out.println("Lựa chọn không hợp lệ!");
			}
		}
	}

//CATEGORY
	private static void categoryMenu(CategoryService categoryService) {
		while (true) {
			System.out.printf("\n===================== QUẢN LÝ DANH MỤC =====================");
			System.out.println();
			System.out.println("1. Danh sách danh mục");
			System.out.println("2. Thêm danh mục");
			System.out.println("3. Xóa danh mục");
			System.out.println("4. Tìm kiếm danh mục");
			System.out.println("5. Quay lại");
			System.out.print("Sự lựa chọn của bạn: ");
			String choice = sc.nextLine();

			switch (choice) {
			case "1":
				showCategoryListMenu(categoryService);
				break;
			case "2":
				addCategoryFlow(categoryService);
				break;
			case "3":
				System.out.print("Nhập mã danh mục cần xóa: ");
				String id = sc.nextLine().trim();
				categoryService.deleteCategory(id);
				break;
			case "4":
				System.out.print("Nhập tên danh mục cần tìm: ");
				String name = sc.nextLine().trim();
				List<Category> list = categoryService.searchByName(name);
				printCategories(list);
				break;
			case "5":
				return;
			default:
				System.out.println("Lựa chọn không hợp lệ!");
			}
		}
	}
	
	//Danh sách danh mục
	private static void showCategoryListMenu(CategoryService categoryService) {
		while (true) {
			System.out.printf("\n===================== DANH SÁCH DANH MỤC =====================");
			System.out.println();
			System.out.println("1. Danh sách cây danh mục");
			System.out.println("2. Thông tin chi tiết danh mục");
			System.out.println("3. Quay lại");
			System.out.print("Sự lựa chọn của bạn: ");
			String choice = sc.nextLine();

			switch (choice) {
			case "1":
				printCategoryTree(categoryService.getAll(), "0", 0);
				break;
			case "2":
				System.out.print("Nhập mã danh mục: ");
				String id = sc.nextLine().trim();
				Category c = categoryService.getById(id);

				if (c == null) {
					System.out.printf("\nKhông tìm thấy danh mục.");
					System.out.println();
				} else {
					System.out.println(c.getId()+ ". " 
				                      + c.getName() 
					                  + " | status: " + categoryStatusText(c.isStatus()) 
					                  + " | parent ID: " + c.getParentId());
				}
				break;
			case "3":
				return;
			default:
				System.out.println("Lựa chọn không hợp lệ!");
			}
		}
	}
	
	//Thêm danh mục
	private static void addCategoryFlow(CategoryService categoryService) {
		while (true) {
			Category c = new Category();

			c.setId(promptCategoryId(categoryService));
			c.setName(promptCategoryName(categoryService));
			c.setStatus(promptCategoryStatus());
			c.setParentId(promptParentId(categoryService));

			categoryService.addCategory(c);

			System.out.printf("\nTiếp tục thêm danh mục? (y/n): ");
			if (!"y".equalsIgnoreCase(sc.nextLine().trim())) {
				return;
			}
		}
	}

	private static String promptCategoryId(CategoryService categoryService) {
		while (true) {
			System.out.print("Nhập id danh mục: ");
			String id = sc.nextLine().trim();

			if (!ValidateUtil.isValidCategoryId(id)) {
				System.out.printf("\nMã danh mục không hợp lệ!");
				System.out.println();
				continue;
			}
			if (categoryService.getById(id) != null) {
				System.out.printf("\nMã danh mục đã tồn tại!");
				System.out.println();
				continue;
			}
			return id;
		}
	}

	private static String promptCategoryName(CategoryService categoryService) {
		while (true) {
			System.out.print("Nhập tên danh mục: ");
			String name = sc.nextLine().trim();

			if (!ValidateUtil.isValidCategoryName(name)) {
				System.out.printf("\nTên danh mục phải từ 6-30 ký tự!");
				System.out.println();
				continue;
			}
			boolean exists = false;
			for (Category category : categoryService.getAll()) {
				if (category.getName() != null && category.getName().trim().equalsIgnoreCase(name)) {
					exists = true;
					break;
				}
			}
			if (exists) {
				System.out.printf("\nTên danh mục đã tồn tại!");
				System.out.println();
				continue;
			}
			return name;
		}
	}

	private static boolean promptCategoryStatus() {
		while (true) {
			System.out.print("Nhập trạng thái (true/false): ");
			String statusStr = sc.nextLine().trim();

			if (!ValidateUtil.isValidStatus(statusStr)) {
				System.out.printf("\nTrạng thái không hợp lệ!");
				System.out.println();
				continue;
			}
			return Boolean.parseBoolean(statusStr);
		}
	}

	private static String promptParentId(CategoryService categoryService) {
		while (true) {
			System.out.print("Nhập parent_id (0 nếu là danh mục gốc): ");
			String parentId = sc.nextLine().trim();

			if (parentId.isEmpty() || "0".equals(parentId)) {
				return "0";
			}
			if (!ValidateUtil.isValidCategoryId(parentId)) {
				System.out.printf("\nparent_id không hợp lệ!");
				System.out.println();
				continue;
			}
			if (categoryService.getById(parentId) == null) {
				System.out.printf("\nDanh mục cha không tồn tại!");
				continue;
			}
			return parentId;
		}
	}
	
	private static void printCategories(List<Category> list) {
		if (list == null || list.isEmpty()) {
			System.out.printf("\nKhông có dữ liệu.");
			System.out.println();
			return;
		}
		for (Category c : list) {
			System.out.println(c.getId() + ". "
					          + c.getName()
					          + " | trạng thái: " + categoryStatusText(c.isStatus())
					          + " | parent ID: " + c.getParentId());
		}
	}
	
	//Hiển thị danh sách cây
	private static void printCategoryTree(List<Category> all, String parentId, int level) {
		for (Category c : all) {
			if ((parentId == null && c.getParentId() == null)
					|| (parentId != null && parentId.equals(c.getParentId()))) {
				System.out.println("  ".repeat(Math.max(0, level)) + c.getId() + ". " + c.getName());
				printCategoryTree(all, c.getId(), level + 1);
			}
		}
	}
//CATEGORY

//PRODUCT
	private static void productMenu(CategoryService categoryService, ProductService productService) {
		while (true) {
			System.out.printf("\n===================== QUẢN LÝ ĐỒ UỐNG =====================");
			System.out.println();
			System.out.println("1. Thêm mới");
			System.out.println("2. Hiển thị thông tin");
			System.out.println("3. Sắp xếp sản phẩm");
			System.out.println("4. Cập nhật thông tin sản phẩm");
			System.out.println("5. Xóa đồ uống");
			System.out.println("6. Quay lại");
			System.out.print("Sự lựa chọn của bạn: ");
			String choice = sc.nextLine();

			switch (choice) {
			case "1":
				addProductFlow(categoryService, productService);
				break;
			case "2":
				productInfoMenu(categoryService, productService);
				break;
			case "3":
				sortMenu(productService);
				break;
			case "4":
				updateProductFlow(categoryService, productService);
				break;
			case "5":
				System.out.print("Nhập mã đồ uống cần xóa: ");
				String id = sc.nextLine().trim();
				productService.deleteProduct(id);
				break;
			case "6":
				return;
			default:
				System.out.println("Lựa chọn không hợp lệ!");
			}
		}
	}
	
	//Cập nhật đồ uống
	private static void updateProductFlow(CategoryService categoryService, ProductService productService) {
		while(true) {
			Product p = readProductInput(categoryService, productService, true);
			
			productService.updateProduct(p);
			
			System.out.printf("\nTiếp tục cập nhật đồ uống? (y/n): ");
			if (!"y".equalsIgnoreCase(sc.nextLine().trim())) {
				return;
			}
		}
	}
	
	//Thêm đồ uống
	private static void addProductFlow(CategoryService categoryService, ProductService productService) {
		while(true) {
			Product p = readProductInput(categoryService, productService, false);
			
			productService.addProduct(p);
			
			System.out.printf("\nTiếp tục thêm đồ uống? (y/n): ");
			if (!"y".equalsIgnoreCase(sc.nextLine().trim())) {
				return;
			}
		}
	}

	private static Product readProductInput(CategoryService categoryService, ProductService productService, boolean isUpdate) {
		Product p = new Product();
		if(isUpdate) {
			p.setId(promptExistingProductId(productService));
			p.setName(promptProductNameForUpdate(productService, p.getId()));
		} else {
			p.setId(promptProductId(productService));
			p.setName(promptProductName(productService));
		}
		p.setStatus(promptProductStatus());
		p.setPrice(promptProductPrice());
		p.setDescription(promptDescription());
		p.setExpiration(promptProductExpiration());
		p.setCategoryId(promptProductCategoryId(categoryService));
		
		return p;
	}

	private static String promptExistingProductId(ProductService productService) {
		while(true) {
			System.out.print("Nhập mã đồ uống cần cập nhật: ");
			String id = sc.nextLine().trim().toUpperCase();
			
			if(!ValidateUtil.isValidProductId(id)) {
				System.out.printf("\nMã đồ uống không hợp lệ! (gồm 4 kí tự và bắt đầu bằng chữ C)");
				System.out.println();
				continue;
			}
			if(productService.getById(id) == null) {
				System.out.printf("\nKhông tìm thấy đồ uống để cập nhật!");
				System.out.println();
				continue;
			}
			return id;
		}
	}
	
	private static String promptProductNameForUpdate(ProductService productService, String currentProductId) {
		while(true) {
			System.out.print("Nhập tên đồ uống: ");
			String name = sc.nextLine().trim();
			
			if(!ValidateUtil.isValidProductName(name)) {
				System.out.printf("\nTên đồ uống phải từ 6-50 ký tự!");
				System.out.println();
				continue;
			}
			boolean exists = false;
			for(Product p: productService.getAll()) {
				if(p.getId() != null && !p.getId().equals(currentProductId) && p.getName() != null && p.getName().trim().equalsIgnoreCase(name)) {
					exists = true;
					break;
				}
			}
			if(exists) {
				System.out.printf("\nTên đồ uống đã tồn tại!");
				System.out.println();
				continue;
			}
			return name;
		}
	}

	private static String promptProductId(ProductService productService) {
		while (true) {
			System.out.print("Nhập mã đồ uống: ");
			String id = sc.nextLine().trim().toUpperCase();

			if (!ValidateUtil.isValidProductId(id)) {
				System.out.printf("\nMã đồ uống không hợp lệ! (gồm 4 kí tự và bắt đầu bằng chữ C)");
				System.out.println();
				continue;
			}
			if (productService.getById(id) != null) {
				System.out.printf("\nMã đồ uống đã tồn tại!");
				System.out.println();
				continue;
			}
			return id;
		}
	}

	private static String promptProductName(ProductService productService) {
		while (true) {
			System.out.print("Nhập tên đồ uống: ");
			String name = sc.nextLine().trim();

			if (!ValidateUtil.isValidCategoryName(name)) {
				System.out.printf("\nTên đồ uống phải từ 6-50 ký tự!");
				System.out.println();
				continue;
			}
			boolean exists = false;
			for (Product product : productService.getAll()) {
				if (product.getName() != null && product.getName().trim().equalsIgnoreCase(name)) {
					exists = true;
					break;
				}
			}
			if (exists) {
				System.out.printf("\nTên đồ uống đã tồn tại!");
				System.out.println();
				continue;
			}
			return name;
		}
	}

	private static boolean promptProductStatus() {
		while (true) {
			System.out.print("Nhập trạng thái (true/false): ");
			String statusStr = sc.nextLine().trim();

			if (!ValidateUtil.isValidStatus(statusStr)) {
				System.out.printf("\nTrạng thái không hợp lệ!");
				System.out.println();
				continue;
			}
			return Boolean.parseBoolean(statusStr);
		}
	}

	private static float promptProductPrice() {
		while (true) {
			System.out.print("Nhập giá đồ uống: ");
			String inputPrice = sc.nextLine().trim();

			try {
				float price = Float.parseFloat(inputPrice);

				if (!ValidateUtil.isValidProductPrice(price)) {
					System.out.printf("\nGiá phải là số thực > 0!");
					System.out.println();
					continue;
				}
				return price;
			} catch (NumberFormatException e) {
				System.out.printf("Giá không hợp lệ! Vui lòng nhập số.");
				System.out.println();
			}
		}
	}

	private static String promptDescription() {
		System.out.print("Nhập mô tả: ");
		return sc.nextLine().trim();
	}

	private static Date promptProductExpiration() {
		while (true) {
			System.out.print("Nhập hạn sử dụng (yyyy-mm-dd): ");
			String inputDate = sc.nextLine().trim();

			try {
				Date expiration = Date.valueOf(inputDate);

				if (!ValidateUtil.isValidProductExpiration(expiration)) {
					System.out.printf("Ngày hết hạn phải lớn hơn ngày hiện tại!");
					System.out.println();
					continue;
				}
				return expiration;
			} catch (IllegalArgumentException e) {
				System.out.printf("Định dạng ngày không hợp lệ! Vui lòng nhập lại.");
				System.out.println();
			}
		}
	}

	private static String promptProductCategoryId(CategoryService categoryService) {
		while(true) {
			System.out.print("Nhập mã danh mục: ");
			String categoryId = sc.nextLine().trim();
			
			if(!ValidateUtil.isValidCategoryId(categoryId)) {
				System.out.printf("\nMã danh mục không hợp lệ!");
				System.out.println();
                continue;
			}
			if(categoryService.getById(categoryId) == null) {
				System.out.printf("\nDanh mục không tồn tại!");
				System.out.println();
                continue;
			}
		    return categoryId;
		}
	}

	//Thông tin đồ uống
	private static void productInfoMenu(CategoryService categoryService, ProductService productService) {
		while (true) {
			System.out.printf("\n===================== THÔNG TIN ĐỒ UỐNG =====================");
			System.out.println();
			System.out.println("1. Hiển thị theo danh mục");
			System.out.println("2. Hiển thị chi tiết theo mã đồ uống");
			System.out.println("3. Đồ uống gần hết hạn (3 ngày)");
			System.out.println("4. Quay lại");
			System.out.print("Sự lựa chọn của bạn: ");
			String choice = sc.nextLine();

			switch (choice) {
			case "1":
				System.out.print("Nhập mã danh mục: ");
				String categoryId = sc.nextLine();

				if (categoryService.getById(categoryId) == null) {
					System.out.printf("\nDanh mục không tồn tại!");
					System.out.println();
					continue;
				}
				printProducts(productService.getByCategory(categoryId));
				break;
			case "2":
				System.out.print("Nhập mã đồ uống: ");
				String id = sc.nextLine();
				Product p = productService.getById(id);

				if (p == null) {
					System.out.printf("\nKhông tìm thấy đồ uống.");
					System.out.println();
					continue;
				} else {
					printProduct(p);
				}
				break;
			case "3":
				List<Product> list = productService.getExpiringSoon();
				printProducts(list);
				break;
			case "4":
				return;
			default:
				System.out.println("Lựa chọn không hợp lệ!");
			}
		}
	}

	private static void printProduct(Product p) {
		System.out.println(p.getId() + ". " 
	                      + p.getName() 
	                      + " | trạng thái: " + productStatusText(p.isStatus())
	                      + " | " + p.getPrice() 
	                      + " | HSD: " + p.getExpiration() 
	                      + " | category ID: " + p.getCategoryId());
	}

	public static void printProducts(List<Product> list) {
		if (list == null || list.isEmpty()) {
			System.out.println("Không có dữ liệu.");
			System.out.println();
			return;
		}
		for (Product p : list) {
			printProduct(p);
		}
	}

	//Sắp xếp đồ uống
	private static void sortMenu(ProductService productService) {
		while (true) {
			System.out.printf("\n===================== SẮP XẾP =====================");
			System.out.println();
			System.out.println("1. Theo giá giảm dần");
			System.out.println("2. Theo tên tăng dần");
			System.out.println("3. Quay lại");
			System.out.print("Sự lựa chọn của bạn: ");
			String choice = sc.nextLine();

			switch (choice) {
			case "1":
				printProducts(productService.sortByPriceDesc());
				break;
			case "2":
				printProducts(productService.sortByNameAsc());
				break;
			case "3":
				return;
			default:
				System.out.println("Lựa chọn không hợp lệ!");
			}
		}
	}
	
	private static String categoryStatusText(boolean status) {
		return status ? "Đang kinh doanh" : "Ngừng kinh doanh";
	}
	
	private static String productStatusText(boolean status) {
		return status ? "Còn hàng" : "Hết hàng";
	}
}
