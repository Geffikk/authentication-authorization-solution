package com.bp.repository;

import com.bp.entity.VerificationToken;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface VerificationTokenRepository {
	
	/**
	 * Find verification token by email.
	 * @param email of user
	 * @return verification token
	 */
	@Select("SELECT * FROM verification_token WHERE email=#{email}")
	VerificationToken findByUserEmail(String email);
	
	/**
	 * Find verification token by token.
	 * @param token body of token
	 * @return token
	 */
	@Select("SELECT * FROM verification_token WHERE token=#{token}")
	VerificationToken findByToken(String token);
	
	/**
	 * Add procedure to database.
	 * @param verificationToken for add
	 */
	@Insert(
		" INSERT INTO verification_token(                          " +
			"   email, token, status, expiredDateTime,             " +
			"   issuedDateTime, confirmedDateTime, userId)         " +
			" VALUES(                                              " +
			"   #{email}, #{token},#{status}, #{expiredDateTime},  " +
			"   #{issuedDateTime},#{confirmedDateTime}, #{userId}) ")
	@Options(useGeneratedKeys = true, keyProperty = "verificationId")
	void insert(VerificationToken verificationToken);
	
	/**
	 *  Delete all tokens from database.
	 */
	@Delete("DELETE FROM verification_token")
	void deleteAll();
}
