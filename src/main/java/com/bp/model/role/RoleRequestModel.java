package com.bp.model.role;

import java.io.Serializable;

/**
 * Role request model.
 */
public class RoleRequestModel implements Serializable {
	
	private static final long serialVersionUID = -4469917722467120333L;
	
	/**
	 * Role name.
	 */
	private String role;
	
	public String getRole() {
		return role;
	}
	
	public void setRole(String role) {
		this.role = role;
	}
}
