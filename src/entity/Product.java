package entity;

import java.sql.Date;

public class Product {
	private String id;
	private String name;
	private boolean status;
	private float price;
	private String description;
	private Date expiration;
	private String categoryId;
	
	public Product() {}
	
	public Product(String id, String name, boolean status, float price, String description, Date expiration, String categoryId) {
		this.id = id;
        this.name = name;
        this.status = status;
        this.price = price;
        this.description = description;
        this.expiration = expiration;
        this.categoryId = categoryId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getExpiration() {
		return expiration;
	}

	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
}
