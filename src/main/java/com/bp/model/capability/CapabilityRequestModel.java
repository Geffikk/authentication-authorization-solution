package com.bp.model.capability;

import java.io.Serializable;

/**
 * Capability request model.
 */
public class CapabilityRequestModel implements Serializable {
	
	private static final long serialVersionUID = -7685515872285993853L;
	
	/**
	 * Entity type, where user want authorize access.
	 */
	private String entityType;
	
	/**
	 * Entity id, where user want authorize access.
	 */
	private int entityId;
	
	/**
	 * Role for capability;
	 */
	private String role;
	
	/**
	 * User email when capability is personal (null otherwise).
	 */
	private String user;
	
	/**
	 * Create operation.
	 */
	private Boolean create;
	
	/**
	 * Update operation.
	 */
	private Boolean update;
	
	/**
	 * Delete operation.
	 */
	private Boolean delete;
	
	/**
	 * Capability operation.
	 */
	private Boolean capability;
	
	private Boolean read;
	
	private int priority;
	
	public String getUser() {
		return user;
	}
	
	public void setUser(String user) {
		this.user = user;
	}
	
	public String getRole() {
		return role;
	}
	
	public void setRole(String role) {
		this.role = role;
	}
	
	public String getEntityType() {
		return entityType;
	}
	
	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}
	
	public int getEntityId() {
		return entityId;
	}
	
	public void setEntityId(int entityId) {
		this.entityId = entityId;
	}
	
	public Boolean isCreate() {
		return create;
	}
	
	public void setCreate(Boolean create) {
		this.create = create;
	}
	
	public Boolean isUpdate() {
		return update;
	}
	
	public void setUpdate(Boolean update) {
		this.update = update;
	}
	
	public Boolean isDelete() {
		return delete;
	}
	
	public void setDelete(Boolean delete) {
		this.delete = delete;
	}
	
	public Boolean isRead() {
		return read;
	}
	
	public void setRead(Boolean read) {
		this.read = read;
	}
	
	public Boolean isCapability() {
		return capability;
	}
	
	public void setCapability(Boolean capability) {
		this.capability = capability;
	}
	
	public int getPriority() {
		return priority;
	}
	
	public void setPriority(int priority) {
		this.priority = priority;
	}
}
