package myy803.project_2026.services;
import org.springframework.security.core.userdetails.UserDetails;

import myy803.project_2026.domainmodel.User;


public interface UserService {
	public void saveUser(User user);
    public boolean isUserPresent(User user);
    public UserDetails loadUserByUsername(String username);
    public User findByUserName(String username);
    public void updateCredentials(User user, String name, String email, String password);
    public User getCurrentUser();
}