package com.bp.entity;

/**
 * DefaultCapability entity, predefined capabilities for authorization.
 */
public class DefaultCapability {
	
	/**
	 * Name of default capability.
	 */
	private String name;
	
	/**
	 * Assigned role to capability.
	 */
	private String role;
	
	/**
	 * Type of entity.
	 */
	private String entity;
	
	/**
	 * Create operation.
	 */
	private boolean create;
	
	/**
	 * Read operation.
	 */
	private boolean read;
	
	/**
	 * Update operation.
	 */
	private boolean update;
	
	/**
	 * Delete operation.
	 */
	private boolean delete;
	
	/**
	 * Capability operation.
	 */
	private boolean capability;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getRole() {
		return role;
	}
	
	public void setRole(String role) {
		this.role = role;
	}
	
	public String getEntity() {
		return entity;
	}
	
	public void setEntity(String entity) {
		this.entity = entity;
	}
	
	public boolean isCreate() {
		return create;
	}
	
	public void setCreate(boolean create) {
		this.create = create;
	}
	
	public boolean isRead() {
		return read;
	}
	
	public void setRead(boolean read) {
		this.read = read;
	}
	
	public boolean isUpdate() {
		return update;
	}
	
	public void setUpdate(boolean update) {
		this.update = update;
	}
	
	public boolean isDelete() {
		return delete;
	}
	
	public void setDelete(boolean delete) {
		this.delete = delete;
	}
	
	public Boolean isCapability() {
		return capability;
	}
	
	public void setCapability(Boolean capability) {
		this.capability = capability;
	}
}
