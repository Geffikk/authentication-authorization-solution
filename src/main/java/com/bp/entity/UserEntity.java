package com.bp.entity;

import com.bp.utility.UserUtility;

import java.util.Date;

/**
 * User model layer.
 */
public class UserEntity {
	
    /**
     * ID of user.
     */
    private int userId;
    
    /**
     * Email of user.
     */
    private String email;
    
    /**
     * Password of user.
     */
    private String password;
    
    /**
     * First name of user.
     */
    private String firstName;
    
    /**
     * Last name of user.
     */
    private String lastName;
    
    /**
     * Status active of user.
     */
    private int active;
    
    /**
     * Phone of a user.
     */
    private String phone;
    
    /**
     * Birthday of a user.
     */
    private Date birthdate;
    
    /**
     * Datetime user creation.
     */
    private Date createdAt;
    
	/**
	 * Role id of user.
	 */
	private int roleId;
	
	/**
	 * JWT token of user.
	 */
	private String jwtToken;
	
	/**
	 * Authentication provider of user.
	 */
	private String authProvider;
	
	public int getUserId() {
        return userId;
    }

    public void setUserId(final int id) {
        this.userId = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
 
    public void setPassword(final String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
  
    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public int getActive() {
        return active;
    }
 
    public void setActive(final int active) {
        this.active = active;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(final String phone) {
        this.phone = phone;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(final Date birthday) {
        this.birthdate = birthday;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final Date created_at) {
        this.createdAt = created_at;
    }

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(final int roleId) {
		this.roleId = roleId;
	}
	
	public String getAuthProvider() {
		return authProvider;
	}
	
	public void setAuthProvider(String authProvider) {
		this.authProvider = authProvider;
	}
	
	public String getJwtToken() {
		return jwtToken;
	}
	
	public void setJwtToken(String jwtToken) {
		this.jwtToken = jwtToken;
	}
	
	public boolean isPasswordValid(String password) {
    	return UserUtility.isPasswordValid(password);
	}
}
