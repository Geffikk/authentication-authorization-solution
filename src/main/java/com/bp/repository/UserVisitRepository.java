package com.bp.repository;

import com.bp.entity.UserEntity;
import com.bp.entity.Visit;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserVisitRepository {
	
	/**
	 * Add role to user.
	 * @param userId for add role
	 * @param visitId for add to user
	 */
	@Insert(" INSERT INTO user_visit(         " +
			"   userId, visitId)              " +
			" VALUES(                         " +
			"   #{userId, jdbcType=BIGINT},   " +
			"   #{visitId, jdbcType=BIGINT}); ")
	void addUserVisit(int userId, int visitId);
	
	/**
	 * Delete role from user.
	 * @param userId of user
	 * @param visitId of role
	 */
	@Delete(" DELETE FROM user_visit  " +
			" WHERE 1=1               " +
			" AND userId=#{userId}    " +
			" AND visitId=#{visitId}  ")
	void deleteVisitFromUser(int userId, int visitId);
	
	/**
	 * Get current roles of user.
	 * @param userId of user
	 * @return current roles of user
	 */
	@Select(" SELECT * FROM visit AS v    " +
		    " INNER JOIN user_visit AS uv " +
		    " ON v.visitId=uv.visitId     " +
			" INNER JOIN user_entity AS u " +
			" ON uv.userId=u.userId       " +
			" WHERE u.userId=#{userId}    ")
	List<Visit> getVisits(int userId);
	
	/**
	 * Get users of role.
	 * @param visitId of role
	 * @return current role users
	 */
	@Select(" SELECT * FROM user_entity AS u " +
			" INNER JOIN user_visit AS uv    " +
			" ON u.userId=uv.userId          " +
			" INNER JOIN visit AS v          " +
			" ON uv.visitId=v.visitId        " +
			" WHERE v.visitId = #{visitId}   ")
	List<UserEntity> getUsers(int visitId);
	
	/**
	 * Get users of role.
	 * @param visitId of role
	 * @return current role users
	 */
	@Select(" SELECT * FROM user_entity AS u " +
		" INNER JOIN user_visit AS uv        " +
		" ON u.userId=uv.userId              " +
		" INNER JOIN visit AS v              " +
		" ON uv.visitId=v.visitId            " +
		" WHERE v.visitId = #{visitId}       " +
		" AND u.userId = #{userId}           ")
	UserEntity getUserInVisit(int userId, int visitId);
	
	/**
	 * Delete user roles from database.
	 */
	@Delete("DELETE FROM user_role")
	void deleteAll();
}
