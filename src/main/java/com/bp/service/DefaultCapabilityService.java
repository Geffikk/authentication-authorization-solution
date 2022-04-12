package com.bp.service;

import com.bp.entity.DefaultCapability;
import com.bp.entity.Role;
import com.bp.model.capability.AuthorizeModel;
import com.bp.repository.DefaultCapabilityRepository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Service implementation of default capability.
 */
public class DefaultCapabilityService implements IDefaultCapabilityService {
	
	/**
	 * Default capability repository
	 */
	private final DefaultCapabilityRepository defaultCapabilityRepository;
	
	/**
	 * User role service.
	 */
	private final IUserRoleService userRoleService;
	
	public DefaultCapabilityService(DefaultCapabilityRepository defaultCapabilityRepository, IUserRoleService userRoleService) {
		this.defaultCapabilityRepository = defaultCapabilityRepository;
		this.userRoleService = userRoleService;
	}
	
	@Override
	public DefaultCapability findByName(String name) {
		DefaultCapability defaultCapability = defaultCapabilityRepository.findByName(name);
		
		if (defaultCapability == null) {
			throw new NullPointerException("Default capability does not exist !");
		}
		return defaultCapability;
	}
	
	@Override
	public DefaultCapability findByRoleAndByEntity(AuthorizeModel authorizeModel) {
		String entity = authorizeModel.getEntityType();
		String role = authorizeModel.getGivenRole();
		
		return defaultCapabilityRepository.findByRoleAndByEntity(role, entity);
	}
	
	@Override
	public DefaultCapability findByRoleAndByEntityAndPosition(AuthorizeModel authorizeModel, String position) {
		DefaultCapability capability;
		capability = findThisBest(authorizeModel, position);
		return capability;
	}
	
	@Override
	public DefaultCapability findByRoleAndByEntityAndPositionStranger(AuthorizeModel authorizeModel, String position) {
		DefaultCapability capability;
		AuthorizeModel a = new AuthorizeModel();
		a.setEntityType(authorizeModel.getEntityType());
		a.setRoles(userRoleService.getRoles(authorizeModel.getHostUserId(), null));
		capability = findThisBest(a, position);
		return capability;
	}
	
	/**
	 * Return best default capability for user.
	 * @param a authorization model
	 * @param p position in entity
	 * @return default capability
	 */
	private DefaultCapability findThisBest(AuthorizeModel a, String p) {
		URL resource = getClass().getClassLoader().getResource("role.definition");
		DefaultCapability defaultCapability;
		File file;
		List<String> hierarchy = new ArrayList<>();
		List<String> roles = new ArrayList<>();
		
		for (Role r : a.getRoles()) {
			roles.add(r.getName());
		}
		try {
			assert resource != null;
			file = new File(resource.toURI());
			BufferedReader br = new BufferedReader(new FileReader(file));
			hierarchy = Arrays.asList(br.readLine().replace(" ", "").split(">"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (hierarchy.size() > 0) {
			for (String s : hierarchy) {
				if (roles.contains(s)) {
					defaultCapability = defaultCapabilityRepository.findByRoleAndByEntityAndPosition2(s, a.getEntityType(), p);
					if (defaultCapability != null) {
						return defaultCapability;
					}
				}
			}
		}
		return defaultCapabilityRepository.findByRoleAndByEntityAndPosition(p);
	}
}
