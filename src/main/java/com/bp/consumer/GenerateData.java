package com.bp.consumer;

import com.bp.beans.Authentication;
import com.bp.beans.IAuthentication;
import com.bp.entity.Role;
import com.bp.entity.UserEntity;
import com.bp.repository.UserRoleRepository;
import com.bp.utility.CS;
import com.bp.utility.generator.DataGenerator;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Data generator.
 */
@RestController
@RequestMapping(value = "/api/v1/generate")
public class GenerateData {
	
	/**
	 * Data generator implementation.
	 */
	private final DataGenerator dataGenerator;
	
	/**
	 * User authentication.
	 */
	private final IAuthentication authentication;
	
	private final UserRoleRepository userRoleRepository;
	
	public GenerateData(DataGenerator dataGenerator, Authentication authentication, UserRoleRepository userRoleRepository) {
		this.dataGenerator = dataGenerator;
		this.authentication = authentication;
		this.userRoleRepository = userRoleRepository;
	}
	
	@GetMapping("/small/data")
	public String generateData(HttpServletRequest httpRequest) {
		UserEntity authenticatedUser = authentication.authorizeRequest(httpRequest);
		List<Role> roleList = userRoleRepository.getRoles(authenticatedUser.getUserId());
		for (Role role : roleList) {
			if (role.getName().equals(CS.ADMIN)) {
				dataGenerator.generateDataXS();
				return "Data generated !";
			}
		}
		
		throw new AccessDeniedException("You have not a permission to generate data");
	}
}
