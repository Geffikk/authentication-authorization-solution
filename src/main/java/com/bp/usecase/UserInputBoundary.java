package com.bp.usecase;

import com.bp.entity.UserEntity;
import com.bp.model.capability.AuthorizeModel;
import com.bp.model.user.UserRequestModel;
import com.bp.model.user.UserResponseModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * User/Login application logic.
 */
public interface UserInputBoundary {
	
	/**
	 * Login with mobile otp code.
	 * @param requestModel user request data
	 */
	void otpLogin(UserRequestModel requestModel);
	
	/**
	 * Login with email otp code.
	 * @param requestModel user request data
	 */
	void emailOtpLogin(UserRequestModel requestModel);
	
	/**
	 * Classic jwt login.
	 * @param requestModel user request data
	 * @return user response model
	 */
	UserResponseModel login(UserRequestModel requestModel);
	
	/**
	 * Logout from application.
	 * @param httpServletRequest http request
	 */
	void logout(HttpServletRequest httpServletRequest);
	
	/**
	 * Find all users of application.
	 * @param authorizeModel authorization model
	 * @return list of users
	 */
	List<UserEntity> findAll(AuthorizeModel authorizeModel);
	
	/**
	 * Find user by id.
	 * @param userId id of user
	 * @param authorizeModel authorization model
	 * @return user
	 */
	UserEntity findById(int userId, AuthorizeModel authorizeModel);
	
	/**
	 * Fill user with request data.
	 * @param userEntity user entity
	 * @param userRequestModel request data
	 * @return user
	 */
	UserEntity fillUser(UserEntity userEntity, UserRequestModel userRequestModel);
	
	/**
	 * Create user.
	 * @param userEntity user entity
	 * @param authorizeModel authorization model
	 */
	void createUser(UserEntity userEntity, AuthorizeModel authorizeModel);
	
	/**
	 * Save user.
	 * @param userEntity user entity
	 * @param authorizeModel authorization model
	 */
	void saveUser(UserEntity userEntity, AuthorizeModel authorizeModel);
	
	/**
	 * Delete user.
	 * @param userId id of user
	 * @param authorizeModel authorization model
	 */
	void deleteUser(int userId, AuthorizeModel authorizeModel);
}
