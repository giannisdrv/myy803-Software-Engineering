package myy803.project_2026.services;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import myy803.project_2026.domainmodel.User;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
public class UserServiceTest {

    @Autowired
    @Qualifier("userServiceImpl")
    private UserService userService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    public void testSaveUser() {
        User user = new User();
        user.setUsername("newuser");
        user.setPassword("password");
        user.setName("New User");
        user.setEmail("new@test.com");
        userService.saveUser(user);

        User found = userService.findByUserName("newuser");
        assertNotNull(found);
        assertEquals("newuser", found.getUsername());
    }

    @Test
    public void testSaveUser_passwordIsEncoded() {
        User user = new User();
        user.setUsername("newuser");
        user.setPassword("mypassword");
        user.setName("Giannis");
        user.setEmail("new@test.com");
        userService.saveUser(user);

        User found = userService.findByUserName("newuser");
        assertNotEquals("mypassword", found.getPassword());
        assertTrue(passwordEncoder.matches("mypassword", found.getPassword()));
    }

    @Test
    public void testIsUserPresent_existingUser() {
        User user = new User();
        user.setUsername("Giannis");
        user.setPassword("password");
        userService.saveUser(user);

        User check = new User();
        check.setUsername("Giannis");
        assertTrue(userService.isUserPresent(check));
    }

    @Test
    public void testIsUserPresent_nonExistingUser() {
        User check = new User();
        check.setUsername("nonexistent");
        assertFalse(userService.isUserPresent(check));
    }

    @Test
    public void testGetCurrentUser() {
        User user = new User();
        user.setUsername("currentuser");
        user.setPassword("password");
        user.setName("Giannis");
        user.setEmail("current@test.com");
        userService.saveUser(user);

        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken("currentuser", "password")
        );

        User current = userService.getCurrentUser();
        assertNotNull(current);
        assertEquals("currentuser", current.getUsername());
    }

    @Test
    public void testUpdateCredentials_updatesNameAndEmail() {
        User user = new User();
        user.setUsername("updateuser");
        user.setPassword("password");
        user.setName("Giannis");
        user.setEmail("old@test.com");
        userService.saveUser(user);

        User saved = userService.findByUserName("updateuser");
        userService.updateCredentials(saved, "Ioannis", "new@test.com", "");

        User updated = userService.findByUserName("updateuser");
        assertEquals("Ioannis", updated.getName());
        assertEquals("new@test.com", updated.getEmail());
    }

    @Test
    public void testUpdateCredentials_emptyPassword_keepsOldPassword() {
        User user = new User();
        user.setUsername("updateuser");
        user.setPassword("originalpass");
        user.setName("Giannis");
        user.setEmail("user@test.com");
        userService.saveUser(user);

        User saved = userService.findByUserName("updateuser");
        String oldPasswordHash = saved.getPassword();
        userService.updateCredentials(saved, "Giannis", "user@test.com", "");

        User updated = userService.findByUserName("updateuser");
        assertEquals(oldPasswordHash, updated.getPassword());
    }

    @Test
    public void testUpdateCredentials_newPassword_encodesIt() {
        User user = new User();
        user.setUsername("updateuser");
        user.setPassword("oldpass");
        user.setName("Giannis");
        user.setEmail("user@test.com");
        userService.saveUser(user);

        User saved = userService.findByUserName("updateuser");
        userService.updateCredentials(saved, "Giannis", "user@test.com", "newpass");

        User updated = userService.findByUserName("updateuser");
        assertTrue(passwordEncoder.matches("newpass", updated.getPassword()));
    }
}