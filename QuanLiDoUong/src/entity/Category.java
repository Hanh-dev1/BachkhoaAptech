package entity;

public class Category {
	private String id;
	private String name;
	private boolean status;
	private String parentId;
	
	public Category(){}
	
	public Category(String id, String name, boolean status, String parentId) {
		this.id = id;
		this.name = name;
		this.status = status;
		this.parentId = parentId;
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

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
}
