package com.bp.entity;

import com.bp.model.capability.AuthorizeModel;
import com.bp.utility.CS;

import java.io.Serializable;

/**
 * Capability entity, core entity for authorization.
 */
public class Capability implements Serializable {
	
	/**
	 * Auto generated capability ID.
	 */
	private int capId;
	
	/**
	 * Assigned role to capability.
	 */
	private String role;
	
	/**
	 * User email when capability is personal (null otherwise).
	 */
	private String user;
	
	/**
	 * Type of entity.
	 */
	private String entityType;
	
	/**
	 * ID of entity.
	 */
	private Integer entityId;
	
	/**
	 * Read operation.
	 */
	private Boolean read;
	
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
	
	private int priority;
	
	public int getCapId() {
		return capId;
	}
	
	public void setCapId(int capId) {
		this.capId = capId;
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
	
	public Integer getEntityId() {
		return entityId;
	}
	
	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}
	
	public String getUser() {
		return user;
	}
	
	public void setUser(String user) {
		this.user = user;
	}
	
	public Boolean isRead() {
		return read;
	}
	
	public void setRead(boolean read) {
		this.read = read;
	}
	
	public Boolean isCreate() {
		return create;
	}
	
	public void setCreate(boolean create) {
		this.create = create;
	}
	
	public Boolean isUpdate() {
		return update;
	}
	
	public void setUpdate(boolean update) {
		this.update = update;
	}
	
	public Boolean isDelete() {
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
	
	public int getPriority() {
		return priority;
	}
	
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	public static Capability prepareCapability(AuthorizeModel authorizeModel, int priority) {
		Capability capability = new Capability();
		capability.setCreate(authorizeModel.getDefaultCapability().isCreate());
		capability.setRead(authorizeModel.getDefaultCapability().isRead());
		capability.setUpdate(authorizeModel.getDefaultCapability().isUpdate());
		capability.setDelete(authorizeModel.getDefaultCapability().isDelete());
		capability.setCapability(authorizeModel.getDefaultCapability().isCapability());
		capability.setEntityType(authorizeModel.getEntityType());
		capability.setEntityId(authorizeModel.getEntityId());
		capability.setRole(authorizeModel.getGivenRole());
		if (authorizeModel.getAuthenticatedUser() != null) {
			capability.setUser(authorizeModel.getAuthenticatedUser().getEmail());
		}
		capability.setPriority(priority);
		return capability;
	}
	
	public boolean getOperation(String operation) {
		if (operation.equals(CS.sREAD)) {
			return isRead();
		} else if (operation.equals(CS.sUPDATE)) {
			return isUpdate();
		} else if (operation.equals(CS.sCREATE)) {
			return isCreate();
		} else if (operation.equals(CS.sDELETE)) {
			return isDelete();
		} else {
			return isCapability();
		}
	}
}