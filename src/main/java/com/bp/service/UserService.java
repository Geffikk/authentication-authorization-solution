package com.bp.service;

import com.bp.entity.UserEntity;
import com.bp.entity.Visit;
import com.bp.model.capability.AuthorizeModel;
import com.bp.repository.UserRepository;
import com.bp.repository.UserRoleRepository;
import com.bp.entity.Role;
import com.bp.repository.UserVisitRepository;
import com.bp.usecase.capability.ICapabilityInteractor;
import com.bp.utility.CS;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

public class UserService implements IUserService {
	
	/**
	 * User repository.
	 */
	private final UserRepository userRepository;
	
	/**
	 * User role repository.
	 */
	private final UserRoleRepository userRoleRepository;
	
	/**
	 * User visit repository.
	 */
	private final UserVisitRepository userVisitRepository;
	
	/**
	 * Capability application logic.
	 * (Annotation inject due to circular dependency)
	 */
	@Inject
	private ICapabilityInteractor capabilityInteractor;
	
	public UserService(UserRepository userRepository, UserRoleRepository userRoleRepository, UserVisitRepository userVisitRepository) {
		this.userRepository = userRepository;
		this.userRoleRepository = userRoleRepository;
		this.userVisitRepository = userVisitRepository;
	}
	
	@Override
	public UserEntity findById(final int id, AuthorizeModel authorizeModel) {
		if (authorizeModel != null) {
			authorizeModel.setEntityType(CS.USER.toLowerCase());
			authorizeModel.setOperation(CS.sREAD);
			authorizeModel.setEntityId(id);
			capabilityInteractor.isPermitted(authorizeModel);
		}
		return userRepository.findById(id);
	}
	
	@Override
	public List<UserEntity> findAll(AuthorizeModel authorizeModel) {
		if (authorizeModel != null) {
			authorizeModel.setEntityType(CS.USER);
			authorizeModel.setOperation(CS.sREAD);
			capabilityInteractor.isPermitted(authorizeModel);
		}
		return userRepository.findAll();
	}

	@Override
	public UserEntity findUserByEmail(final String mail) {
		return userRepository.findByEmail(mail);
	}

	@Override
	public void createUser(final UserEntity userEntity, AuthorizeModel authorizeModel) {
		if (authorizeModel != null) {
			authorizeModel.setEntityType(CS.USER);
			authorizeModel.setOperation(CS.sCREATE);
			capabilityInteractor.isPermitted(authorizeModel);
		}
		userRepository.insert(userEntity);
	}

	@Override
	public void saveUser(UserEntity userEntity, AuthorizeModel authorizeModel) {
		if (authorizeModel != null) {
			authorizeModel.setEntityType(CS.USER);
			authorizeModel.setOperation(CS.sUPDATE);
			authorizeModel.setEntityId(userEntity.getUserId());
			capabilityInteractor.isPermitted(authorizeModel);
		}
		userRepository.update(userEntity);
	}

	@Override
	@Transactional
	public void deleteUser(final int id, AuthorizeModel authorizeModel) {
		if (authorizeModel != null) {
			authorizeModel.setEntityType(CS.USER);
			authorizeModel.setOperation(CS.sDELETE);
			authorizeModel.setEntityId(id);
			capabilityInteractor.isPermitted(authorizeModel);
		}
		List<Role> roles = userRoleRepository.getRoles(id);
		for (Role role : roles) {
			userRoleRepository.deleteRole(id, role.getName());
		}
		List<Visit> visits = userVisitRepository.getVisits(id);
		for (Visit visit : visits) {
			userVisitRepository.deleteVisitFromUser(id, visit.getVisitId());
		}
		userRepository.delete(id);
	}

	@Override
	public List<Role> getRoles(final int userId) {
		return userRoleRepository.getRoles(userId);
	}
	
	@Override
	public boolean existByEmail(String email) {
		int isExist = userRepository.existByEmail(email);
		return isExist == 1;
	}
	
	@Override
	public boolean existById(int id) {
		int isExist = userRepository.existById(id);
		return isExist == 1;
	}
	
	@Override
	public boolean hasRole(int userId, String role) {
		int isExist = userRoleRepository.existByRole(userId, role);
		return isExist == 1;
	}
	
	@Override
	public UserEntity findByToken(String substring, AuthorizeModel authorizeModel) {
		if (authorizeModel != null) {
			authorizeModel.setEntityType(CS.USER);
			authorizeModel.setOperation(CS.sREAD);
			capabilityInteractor.isPermitted(authorizeModel);
		}
		return userRepository.findByToken(substring);
	}
}
