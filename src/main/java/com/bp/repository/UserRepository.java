package com.bp.repository;

import java.util.List;
import com.bp.entity.UserEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserRepository {
	
	/**
	 * Find all users.
	 * @return all users
	 */
	@Select("SELECT * FROM user_entity")
	List<UserEntity> findAll();
	
	/**
	 * Find user by id.
	 * @param id of user
	 * @return user
	 */
	@Select("SELECT * FROM user_entity WHERE userId=#{userId}")
	UserEntity findById(int id);
	
	/**
	 * Find token by token body.
	 * @param jwtToken token body.
	 * @return token
	 */
	@Select("SELECT * FROM user_entity WHERE jwtToken=#{jwtToken}")
	UserEntity findByToken(String jwtToken);
	
	/**
	 * Find user by email.
	 * @param email of user
	 * @return user
	 */
	@Select("SELECT * FROM user_entity WHERE email=#{email}")
	UserEntity findByEmail(String email);
	
	/**
	 * Add user to database.
	 * @param userEntity for add
	 */
	@Insert(
		" INSERT INTO user_entity(                        " +
		"    email, password, firstName,                  " +
		"    lastName, active, phone, birthdate,          " +
		"    authProvider, jwtToken)                      " +
		" VALUES(                                         " +
		"    #{email},#{password},#{firstName},           " +
		"    #{lastName},#{active},#{phone},#{birthdate}, " +
		"    #{authProvider}, #{jwtToken})                ")
	@Options(useGeneratedKeys = true, keyProperty = "userId")
	void insert(UserEntity userEntity);
	
	/**
	 * Update user in database.
	 * @param userEntity for update
	 */
	@Update(
		" UPDATE user_entity SET          " +
		"   email=#{email},               " +
		"   password=#{password},         " +
		"   firstName=#{firstName},       " +
		"   lastName=#{lastName},         " +
		"   active=#{active},             " +
		"   phone=#{phone},               " +
		"   birthdate=#{birthdate},       " +
		"   authProvider=#{authProvider}, " +
		"   jwtToken=#{jwtToken}          " +
		" WHERE userId=#{userId};         ")
	void update(UserEntity userEntity);
	
	/**
	 * Delete user from database.
	 * @param id of user
	 */
	@Delete("DELETE FROM user_entity WHERE userId=#{userId}")
	void delete(int id);
	
	/**
	 * True if record exist in database.
	 * @param email of user
	 * @return number 1, if record exist
	 */
	@Select("SELECT COUNT(1) FROM user_entity WHERE email=#{email};")
	int existByEmail(String email);
	
	/**
	 * True if record exist in database.
	 * @param id of record
	 * @return number 1, if record exist
	 */
	@Select("SELECT COUNT(1) FROM user_entity WHERE userId=#{userId};")
	int existById(int id);
	
	/**
	 * Create index on user table.
	 */
	@Insert("CREATE INDEX user_userId_idx ON user_entity (userId)")
	void createUserIdIndexOnUser();
	
	/**
	 *  Create index on user table.
	 */
	@Insert("CREATE INDEX user_jwtToken_idx ON user_entity (jwtToken)")
	void createTokenIndexOnUser();
	
	/**
	 * Create index on user table.
	 */
	@Insert("CREATE INDEX user_email_idx ON user_entity (email)")
	void createEmailIndexOnUser();
}
