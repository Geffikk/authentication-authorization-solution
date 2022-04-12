package com.bp.repository;

import com.bp.entity.Procedure;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ProcedureRepository {
	
	/**
	 * Find all procedures.
	 * @return all procedures
	 */
	@Select("SELECT * FROM procedure")
	List<Procedure> findAll();
	
	/**
	 * Find procedure by id.
	 * @param id of procedure
	 * @return procedure
	 */
	@Select("SELECT * FROM procedure WHERE procedureId=#{procedureId}")
	Procedure findById(int id);
	
	/**
	 * Add procedure to database.
	 * @param procedure for add
	 */
	@Insert(
		" INSERT INTO procedure(                    " +
		"   procedureType, result,                  " +
		"   billing, procedureTime, visitId)        " +
		" VALUES(                                   " +
		"   #{procedureType},#{result},             " +
		"   #{billing},#{procedureTime},#{visitId}) ")
	@Options(useGeneratedKeys = true, keyProperty = "procedureId")
	void insert(Procedure procedure);
	
	/**
	 * Update procedure in database.
	 * @param procedure for update
	 */
	@Update(
		" UPDATE procedure SET                " +
		"   procedureType=#{procedureType},   " +
		"   result=#{result},                 " +
		"   billing=#{billing},               " +
		"   procedureTime=#{procedureTime}    " +
		" WHERE procedureId=#{procedureId};   ")
	void update(Procedure procedure);
	
	/**
	 * Delete procedure from database.
	 * @param id of procedure
	 */
	@Delete("DELETE FROM procedure WHERE procedureId=#{procedureId}")
	void delete(int id);
	
	/**
	 * Find procedures by visit.
	 * @param visit_id of visit
	 * @return procedures of visit
	 */
	@Select("SELECT * FROM procedure WHERE visitId=#{visitId}")
	List<Procedure> findProceduresByVisitId(int visit_id);
	
	/**
	 * Delete all procedure from database.
	 */
	@Delete("DELETE FROM procedure")
	void deleteAll();
	
	/**
	 * Create index on procedure table.
	 */
	@Insert("CREATE INDEX procedure_procedureId_idx ON procedure (procedureId)")
	void createProcedureIdIndexOnProcedure();
}
