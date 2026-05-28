package myy803.project_2026.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import myy803.project_2026.domainmodel.User;
import myy803.project_2026.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private UserRepository UserRepository;
	
	
	@Override
	public void saveUser(User user) {
		String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        UserRepository.save(user);	
		
	}

	@Override
	public boolean isUserPresent(User user) {
		User potentialUser = UserRepository.findByUsername(user.getUsername());
		if (potentialUser != null) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		 return UserRepository.findByUsername(username);
	}
	
	@Override
	public User findByUserName(String username) {
		return UserRepository.findByUsername(username);
	}
	
	
	@Override 
	public void updateCredentials(User user, String name, String email, String password) {
		if (password != null && !password.trim().isEmpty()) {
	        String encodedPassword = bCryptPasswordEncoder.encode(password);
	        user.setPassword(encodedPassword);
	    }
	    if (name != null ) {
	        user.setName(name);
	    }
	    if (email != null ) {
	        user.setEmail(email);
	    }
		UserRepository.save(user);
	}
	
	@Override
	public User getCurrentUser() {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    return UserRepository.findByUsername(auth.getName());
	}

}