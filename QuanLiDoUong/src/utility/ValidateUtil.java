package utility;

import java.sql.Date;

public class ValidateUtil {
	/*CATEGORY*/
	//1. Validate ID theo dạng phân cấp: số nguyên dương hoặc chuỗi dạng 1.2.3
	public static boolean isValidCategoryId(String id) {
		if(id == null) {
			return false;
		}
		String normalizedId = id.trim();
		if(normalizedId.isEmpty()) {
			return false;
		}
		return normalizedId.matches("[1-9]\\d*(\\.[1-9]\\d*)*");
	}
	//2. Validate name từ 6-30 kí tự
	public static boolean isValidCategoryName(String name) {
		return name != null 
				&& name.trim().length() >= 6
				&& name.trim().length() <= 30;
	}
	//3. Validate status chỉ có true/false
	public static boolean isValidStatus(String status) {
		return status.equalsIgnoreCase("true")
				|| status.equalsIgnoreCase("false");
	}
	
	
	/*PRODUCT*/
	//1. Validate ID gồm 4 kí tự & bắt đầu bằng "C"
	public static boolean isValidProductId(String id) {
		return id != null
				&& id.length() == 4
				&& id.startsWith("C");
	}
	//2. Validate name từ 6-50 kí tự
	public static boolean isValidProductName(String name) {
		return name != null
				&& name.trim().length() >= 6
				&& name.trim().length() <= 50;
	}
	//3. Validate price là float > 0
	public static boolean isValidProductPrice(float price) {
		return price > 0;
	}
	//4. Validate ngày hết hạn phải sau ngày hiện tại
	public static boolean isValidProductExpiration(Date expiration) {
		if(expiration == null) {
			return false;
		}
		Date today = new Date(System.currentTimeMillis());
		return expiration.after(today);
	}
}
