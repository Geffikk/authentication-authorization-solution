package com.bp.service;

import com.bp.entity.Role;
import com.bp.repository.UserRepository;
import com.bp.repository.UserRoleRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.bp.entity.UserEntity;

/**
 * User details service implementation (Override spring security interface)
 */
@Service
public class MyUserDetailService implements UserDetailsService {
	
	/**
	 * User repository.
	 */
	private final UserRepository userRepository;
	
	/**
	 * User role repository.
	 */
	private final UserRoleRepository userRoleRepository;
	
	/**
	 * User service.
	 */
	@Inject
	private UserService userService;
	
	public MyUserDetailService(UserRepository userRepository, UserRoleRepository userRoleRepository) {
		this.userRepository = userRepository;
		this.userRoleRepository = userRoleRepository;
	}
	
	/**
	 * Load user from database by email.
	 * @param username email of user
	 * @return userDetails object
	 */
	@Override
	public UserDetails loadUserByUsername(String username) {
		UserEntity applicationUser = userRepository.findByEmail(username);
		if (applicationUser == null) {
			throw new UsernameNotFoundException(username);
		}
		if (applicationUser.getPassword() == null) {
			applicationUser.setPassword(generateRandomString());
			userService.saveUser(applicationUser, null);
		}
		List<Role> roles = userRoleRepository.getRoles(applicationUser.getUserId());
		return new User(applicationUser.getEmail(), applicationUser.getPassword(), getAuthority(roles));
	}
	
	/**
	 * Get user authority.
	 * @param roles of user
	 * @return user authorities
	 */
	private List<GrantedAuthority> getAuthority(List<Role> roles) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		
		for (Role role : roles) {
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		}
		return authorities;
	}
	
	/**
	 * Generate random password for users, which authenticated with
	 * oAuth (password cannot be null).
	 * @return generated string
	 */
	public String generateRandomString() {
		int leftLimit = 33;
		int rightLimit = 127;
		int targetStringLength = 100;
		Random random = new Random();
		
		return random.ints(leftLimit, rightLimit + 1)
			.limit(targetStringLength)
			.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
			.toString();
	}
}
