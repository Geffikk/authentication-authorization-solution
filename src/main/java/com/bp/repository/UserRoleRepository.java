package com.bp.repository;

import java.util.List;
import com.bp.entity.Role;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserRoleRepository {
	
	/**
	 * Add role to user.
	 * @param userId for add role
	 * @param role for add to user
	 */
	@Insert(
		" INSERT INTO user_role(        " +
		"   userId, role, origin)       " +
		" VALUES(                       " +
		"   #{userId, jdbcType=BIGINT}, " +
		"   #{role, jdbcType=BIGINT},   " +
		"   #{origin, jdbcType=BIGINT}) ")
	void addRole(int userId, String role, String origin);
	
	/**
	 * Delete role from user.
	 * @param userId of user
	 * @param role of role
	 */
	@Delete(
		" DELETE FROM user_role " +
		" WHERE 1=1             " +
		" AND userId=#{userId}  " +
		" AND role=#{role}      ")
	void deleteRole(int userId, String role);
	
	/**
	 * Get current roles of user.
	 * @param userId of user
	 * @return current roles of user
	 */
	@Select("SELECT * FROM role AS r     " +
		"    INNER JOIN user_role AS ur  " +
		"    ON r.name=ur.role           " +
		"    INNER JOIN user_entity AS u " +
		"    ON ur.userId=u.userId       " +
		"    WHERE u.userId=#{userId}    ")
	List<Role> getRoles(int userId);
	
	/**
	 * Delete all user roles from database.
	 */
	@Delete("DELETE FROM user_role")
	void deleteAll();
	
	/**
	 * Check, if user have role.
	 * @param userId of user
	 * @param role of user
	 * @return number 1, if user has specific role
	 */
	@Select("SELECT COUNT(1) FROM user_role WHERE userId=#{userId} and role=#{role};")
	int existByRole(int userId, String role);
	
	/**
	 * Delete extra roles (USER_VISIT_1) from database.
	 * @param userId of user
	 * @param role of user
	 */
	@Delete(
		" DELETE FROM user_role " +
		" WHERE 1=1             " +
		" AND userId=#{userId}  " +
		" AND origin=#{role}      ")
	void deleteExtraRolesFromUser(int userId, String role);
	
	@Insert("CREATE INDEX user_role_userId ON user_role (userId)")
	void createIndexOnUserRoleIdx();
}
