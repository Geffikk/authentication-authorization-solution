package com.bp.model.capability;

import com.bp.entity.Capability;
import com.bp.entity.DefaultCapability;
import com.bp.entity.Role;
import com.bp.entity.UserEntity;

import java.io.Serializable;
import java.util.List;

/**
 * Authorization control model. Entity which control whole access control in application.
 */
public class AuthorizeModel implements Serializable {
	
	private static final long serialVersionUID = 7803128499441613947L;
	
	/**
	 * Authenticated user.
	 */
	private UserEntity authenticatedUser;
	
	/**
	 * Roles of user.
	 */
	private List<Role> roles;
	
	/**
	 * One specific given role (Authorize specially one defined role, otherwise authorize "roles");
	 */
	private String givenRole;
	
	/**
	 * Entity type, where user want authorize access.
	 */
	private String entityType;
	
	/**
	 * Entity id, where user want authorize access.
	 */
	private Integer entityId;
	
	/**
	 * CRUD operation, where user want authorize access.
	 */
	private String operation;
	
	/**
	 * ID of host user, this option is used when strange user want create capability for another user
	 */
	private int hostUserId;
	
	/**
	 * Capability for create.
	 */
	private Capability capability;
	
	/**
	 * Predefined capability.
	 */
	private DefaultCapability defaultCapability;
	
	public UserEntity getAuthenticatedUser() {
		return authenticatedUser;
	}
	
	public void setAuthenticatedUser(UserEntity authenticatedUser) {
		this.authenticatedUser = authenticatedUser;
	}
	
	public List<Role> getRoles() {
		return roles;
	}
	
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	
	public String getEntityType() {
		return entityType;
	}
	
	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}
	
	public int getHostUserId() {
		return hostUserId;
	}
	
	public void setHostUserId(int hostUserId) {
		this.hostUserId = hostUserId;
	}
	
	public Integer getEntityId() {
		return entityId;
	}
	
	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}
	
	public String getOperation() {
		return operation;
	}
	
	public void setOperation(String operation) {
		this.operation = operation;
	}
	
	public String getGivenRole() {
		return givenRole;
	}
	
	public void setGivenRole(String givenRole) {
		this.givenRole = givenRole;
	}
	
	public Capability getCapability() {
		return capability;
	}
	
	public void setCapability(Capability capability) {
		this.capability = capability;
	}
	
	public DefaultCapability getDefaultCapability() {
		return defaultCapability;
	}
	
	public void setDefaultCapability(DefaultCapability defaultCapability) {
		this.defaultCapability = defaultCapability;
	}
	
	public static AuthorizeModel copyAuthorizeModel(AuthorizeModel authorizeModel) {
		AuthorizeModel am = new AuthorizeModel();
		am.setEntityId(authorizeModel.getEntityId());
		am.setEntityType(authorizeModel.getEntityType());
		am.setRoles(authorizeModel.getRoles());
		am.setAuthenticatedUser(authorizeModel.getAuthenticatedUser());
		am.setOperation(authorizeModel.getOperation());
		am.setDefaultCapability(authorizeModel.getDefaultCapability());
		am.setGivenRole(authorizeModel.getGivenRole());
		am.setHostUserId(authorizeModel.getHostUserId());
		am.setCapability(authorizeModel.getCapability());
		return am;
	}
}
