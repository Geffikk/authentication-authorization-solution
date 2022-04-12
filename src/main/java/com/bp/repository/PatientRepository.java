package com.bp.repository;

import java.util.List;
import com.bp.entity.Patient;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface PatientRepository {
	
	/**
	 * Find all patients.
	 * @return all patients.
	 */
	@Select("SELECT * FROM patient")
	List<Patient> findAll();
	
	/**
	 * Find patient by id.
	 * @param id of patient
	 * @return patient
	 */
	@Select("SELECT * FROM patient WHERE patientId=#{patientId}")
	Patient findById(int id);
	
	/**
	 * Add patient to database.
	 * @param patient for add
	 */
	@Insert(
		" INSERT INTO patient(                                     " +
		"   identification, firstName, lastName,                   " +
		"   email, phone, city, address, birthdate)                " +
		" VALUES(                                                  " +
		"   #{identification}, #{firstName},#{lastName},           " +
		"   #{email}, #{phone}, #{city}, #{address}, #{birthdate}) ")
	@Options(useGeneratedKeys = true, keyProperty = "patientId")
	void insert(Patient patient);
	
	/**
	 * Update patient to database.
	 * @param patient for update
	 */
	@Update(
		" UPDATE patient SET                  " +
		"   identification=#{identification}, " +
		"   firstName=#{firstName},          " +
		"   lastName=#{lastName},            " +
		"   email=#{email},                   " +
		"   phone=#{phone},                   " +
		"   city=#{city},                     " +
		"   address=#{address},               " +
		"   birthdate=#{birthdate}            " +
		" WHERE patientId=#{patientId};      ")
	void update(Patient patient);
	
	/**
	 * Delete patient from database.
	 * @param id for patient
	 */
	@Delete("DELETE FROM patient WHERE patientId=#{patientId}")
	void delete(int id);
	
	/**
	 * Find patient by id.
	 * @param identification of patient
	 * @return patient
	 */
	@Select("SELECT * FROM patient WHERE identification=#{identification}")
	Patient findByIdentification(long identification);
	
	/**
	 * Delete all patient from database.
	 */
	@Delete("DELETE FROM patient")
	void deleteAll();
}
