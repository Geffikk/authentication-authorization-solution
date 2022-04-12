package com.bp.repository;

import com.bp.entity.DefaultCapability;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface DefaultCapabilityRepository {
	
	/**
	 * Find all capabilities.
	 * @return all capabilities
	 */
	@Select("SELECT * FROM default_capability")
	List<DefaultCapability> findAll();
	
	/**
	 * Find capability by id.
	 * @param name of visit
	 * @return visit
	 */
	@Select("SELECT * FROM default_capability WHERE name=#{name}")
	DefaultCapability findByName(String name);
	
	/**
	 * Find capability by id.
	 * @param role of capability
	 * @param entity of capability
	 * @return visit
	 */
	@Select("SELECT * FROM default_capability WHERE role=#{role} AND entity=#{entity} AND name!='OWNER'")
	DefaultCapability findByRoleAndByEntity(String role, String entity);
	
	/**
	 * Find default capability by name, where role and entity are null.
	 * @param name of position
	 * @return Default capability
	 */
	@Select("SELECT * FROM default_capability WHERE role IS NULL AND entity IS NULL AND name=#{name}")
	DefaultCapability findByRoleAndByEntityAndPosition(String name);
	
	/**
	 * Find default capability by role, entity and name.
	 * @param role of user
	 * @param entity type of entity
	 * @param name of position
	 * @return Default capability
	 */
	@Select("SELECT * FROM default_capability WHERE role=#{role} AND entity=#{entity} AND name=#{name}")
	DefaultCapability findByRoleAndByEntityAndPosition2(String role, String entity, String name);
	
	/**
	 * Add capability to database.
	 * @param capability for add
	 */
	@Insert(
		" INSERT INTO default_capability(    " +
			"   name, create,                " +
			"   update, delete, read)        " +
			" VALUES(                        " +
			"   #{name},#{create},           " +
			"   #{update},#{delete},#{read}) ")
	@Options(keyProperty = "capId")
	void insert(DefaultCapability capability);
	
	/**
	 * Update procedure in database.
	 * @param capability for update
	 */
	@Update(
		" UPDATE default_capability SET " +
			"   name=#{name},           " +
			"   read=#{read},           " +
			"   create=#{create},       " +
			"   update=#{update},       " +
			"   delete=#{delete}        " +
			" WHERE name=#{name};       ")
	void update(DefaultCapability capability);
	
	/**
	 * Delete visit from database.
	 * @param name of visit
	 */
	@Delete("DELETE FROM default_capability WHERE name=#{name}")
	void delete(String name);
	
	/**
	 * Delete all default capabilities from database.
	 */
	@Delete("DELETE FROM default_capability")
	void deleteAll();
}
