package com.bp.repository;

import java.util.List;

import com.bp.entity.Role;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

/**
 * Role db mapper.
 */
@Mapper
public interface RoleRepository {
	
	/**
	 * Find all roles.
	 * @return all roles
	 */
	@Select("SELECT * FROM role")
	List<Role> findAll();
	
	/**
	 * Find roles by id.
	 * @param id of role
	 * @return role
	 */
	@Select("SELECT * FROM role WHERE name=#{name}")
	Role findById(String id);
	
	/**
	 * True if record exist in database.
	 * @param id of record
	 * @return number 1, if record exist
	 */
	@Select("SELECT COUNT(1) FROM role WHERE name=#{name}")
	int existById(String id);
	
	/**
	 * Add role to db.
	 * @param role for add
	 */
	@Insert("INSERT INTO role(name, role) VALUES(#{name}, #{role})")
	@Options(keyProperty = "name")
	void insert(Role role);
	
	/**
	 * Delete role from db.
	 * @param id of role
	 */
	@Delete("DELETE FROM role WHERE name=#{name}")
	void delete(String id);
	
	/**
	 * Delete all roles from database.
	 */
	@Delete("DELETE FROM role")
	void deleteAll();
}
