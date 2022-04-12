package com.bp.repository;

import java.util.List;
import com.bp.entity.Visit;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface VisitRepository {
	
	/**
	 * Find all visits.
	 * @return all visits
	 */
	@Select("SELECT * FROM visit")
	List<Visit> findAll();
	
	/**
	 * Find visit by id.
	 * @param id of visit
	 * @return visit
	 */
	@Select("SELECT * FROM visit WHERE visitId=#{visitId}")
	Visit findById(int id);
	
	/**
	 * Add visit to db.
	 * @param visit for add
	 */
	@Insert(
		" INSERT INTO visit(                             " +
		"   identification, firstName, lastName,         " +
		"   reason, symptoms, email, visitTime,          " +
		"   city, address, birthdate, phone,             " +
		"   patientId)                                   " +
		" VALUES(                                        " +
		"   #{identification},#{firstName},#{lastName},  " +
		"   #{reason},#{symptoms},#{email},#{visitTime}, " +
		"   #{city},#{address},#{birthdate}, #{phone},   " +
		"   #{patientId})                                ")
	@Options(useGeneratedKeys = true, keyProperty = "visitId")
	void insert(Visit visit);
	
	/**
	 * Update procedure in database.
	 * @param visit for update
	 */
	@Update(
		" UPDATE visit SET                    " +
		"   identification=#{identification}, " +
		"   firstName=#{firstName},           " +
		"   lastName=#{lastName},             " +
		"   reason=#{reason},                 " +
		"   symptoms=#{symptoms},             " +
		"   email=#{email},                   " +
		"   city=#{city},                     " +
		"   birthdate=#{birthdate},           " +
		"   phone=#{phone},                   " +
		"   address=#{address},               " +
		"   visitTime=#{visitTime}            " +
		" WHERE visitId=#{visitId};           ")
	void update(Visit visit);
	
	/**
	 * Delete visit from db.
	 * @param id of visit
	 */
	@Delete("DELETE FROM visit WHERE visitId=#{visitId}")
	void delete(int id);
	
	/**
	 * Find visits by patient id.
	 * @param patient_id of visits
	 * @return visits
	 */
	@Select("SELECT * FROM visit WHERE patientId=#{patientId}")
	List<Visit> findVisitsByPatientId(long patient_id);
	
	/**
	 * Delete all visit from database.
	 */
	@Delete("DELETE FROM visit")
	void deleteAll();
	
	/**
	 * Create index on table visit.
	 */
	@Insert("CREATE INDEX visit_visitId_idx ON visit (visitId)")
	void createVisitIdIndexOnVisit();
}
