package com.bp.entity;

import java.util.Date;

/**
 * Role model layer.
 */
public class Role {
    
    /**
     * Role name.
     */
    private String name;
	
	/**
	 * Role type.
	 */
    private String role;
    
    /**
     * Datetime of role creation.
     */
    private Date createdAt;
    
	/**
	 * User id of role.
	 */
	private int userId;
	
	public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final Date created_at) {
        this.createdAt = created_at;
    }

	public int getUserId() {
		return userId;
	}

	public void setUserId(final int userId) {
		this.userId = userId;
	}
	
	public String getRole() {
		return role;
	}
	
	public void setRole(String role) {
		this.role = role;
	}
}
