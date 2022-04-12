package com.bp.entity;

import java.util.Date;

/**
 * Patient model layer.
 */
public class Patient {
	
    /**
     * ID of patient.
     */
    private int patientId;
    
	/**
	 * identification of patient.
	 */
	private long identification;
	
    /**
     * First name of patient.
     */
    private String firstName;
    
    /**
     * Last name of patient.
     */
    private String lastName;
    
    /**
     * Age of patient.
     */
    private Date birthdate;
    
    /**
     * Phone of patient.
     */
    private String phone;
    
    /**
     * Mail of patient.
     */
    private String email;
    
    /**
     * City of patient.
     */
    private String city;
    
    /**
     * Residence of patient.
     */
    private String address;
    
    /**
     * Datetime of patient creation.
     */
    private Date createdAt;
    
	/**
	 * Visit id, where procedure has relationship.
	 */
	private int visitId;

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(final int id) {
        this.patientId = id;
    }
	
	public long getIdentification() {
		return identification;
	}
	
	public void setIdentification(long identification) {
		this.identification = identification;
	}
	
	public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String first_name) {
        this.firstName = first_name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String last_name) {
        this.lastName = last_name;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(final Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(final String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }
 
    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }
 
    public void setAddress(final String address) {
        this.address = address;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final Date created_at) {
        this.createdAt = created_at;
    }
	
	public int getVisitId() {
		return visitId;
	}
	
	public void setVisitId(int visit_id) {
		this.visitId = visit_id;
	}
}
