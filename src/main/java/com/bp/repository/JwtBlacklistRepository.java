package com.bp.repository;

import com.bp.entity.JwtBlacklist;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface JwtBlacklistRepository {
	
	/**
	 * Find token by body.
	 * @param token body
	 * @return token
	 */
	@Select("SELECT * FROM jwtblacklist WHERE token=#{token}")
	String findByTokenEquals(String token);
	
	/**
	 * Insert token to database
	 * @param jwtBlacklist jwt token.
	 */
	@Insert("INSERT INTO jwtblacklist(token) VALUES(#{token})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	void insert(JwtBlacklist jwtBlacklist);
	
	/**
	 * Find all tokens.
	 * @return all tokens from db.
	 */
	@Select("SELECT * FROM jwtblacklist")
	List<JwtBlacklist> findAll();
}
