package com.georgeisaev.vietnam.enterprise.tin.storage.services.security;

import com.georgeisaev.vietnam.enterprise.tin.storage.security.User;
import com.georgeisaev.vietnam.enterprise.tin.storage.repositories.security.UserRepository;
import com.georgeisaev.vietnam.enterprise.tin.storage.security.UserCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserCredentialsService implements UserDetailsService {

	private final UserRepository userRepository;

	@Autowired
	public UserCredentialsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) {
		User user = userRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException(username);
		}
		return new UserCredentials(user);
	}

}
