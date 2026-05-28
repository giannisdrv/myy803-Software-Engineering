package myy803.project_2026.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import myy803.project_2026.domainmodel.Project;
import myy803.project_2026.domainmodel.UseCase;
import myy803.project_2026.domainmodel.User;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
public class UseCaseServiceTest {

    @Autowired
    private UseCaseService useCaseService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    @Qualifier("userServiceImpl")
    private UserService userService;

    private int projectId;

    @BeforeEach
    public void setUp() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setName("Test User");
        user.setEmail("test@test.com");
        userService.saveUser(user);

        loginAs("testuser");

        Project project = new Project();
        project.setName("Test Project");
        projectService.createProject(project);
        projectId = projectService.getProjectsForCurrentUser().get(0).getId();
    }

    private void loginAs(String username) {
        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken(username, "password")
        );
    }

    @Test
    public void testCreateUseCase() {
        UseCase uc = new UseCase();
        uc.setName("Giannis");
        uc.setActor("User");
        uc.setPreconditions("User is not logged in");
        uc.setMain_flow("User enters credentials");
        uc.setPostconditions("User is logged in");
        useCaseService.createUseCase(projectId, uc);

        List<UseCase> useCases = useCaseService.findByProjectId(projectId);
        assertEquals(1, useCases.size());
        assertEquals("Giannis", useCases.get(0).getName());
    }

    @Test
    public void testCreateUseCase_duplicateName_throwsException() {
        UseCase uc1 = new UseCase();
        uc1.setName("Giannis");
        useCaseService.createUseCase(projectId, uc1);

        UseCase uc2 = new UseCase();
        uc2.setName("Giannis");
        assertThrows(IllegalArgumentException.class, () -> {
            useCaseService.createUseCase(projectId, uc2);
        });
    }

    @Test
    public void testUpdateField() {
        UseCase uc = new UseCase();
        uc.setName("Giannis");
        uc.setActor("User");
        useCaseService.createUseCase(projectId, uc);

        int ucId = useCaseService.findByProjectId(projectId).get(0).getId();
        useCaseService.updateField(ucId, "actor", "Admin");

        UseCase updated = useCaseService.findById(ucId);
        assertEquals("Admin", updated.getActor());
    }

    @Test
    public void testDeleteUseCase_returnsProjectId() {
        UseCase uc = new UseCase();
        uc.setName("Giannis");
        useCaseService.createUseCase(projectId, uc);

        int ucId = useCaseService.findByProjectId(projectId).get(0).getId();
        int returnedProjectId = useCaseService.deleteById(ucId);

        assertEquals(projectId, returnedProjectId);
        assertTrue(useCaseService.findByProjectId(projectId).isEmpty());
    }

    @Test
    public void testAccessUseCaseFromOtherUsersProject_throwsAccessDenied() {
        UseCase uc = new UseCase();
        uc.setName("Giannis");
        useCaseService.createUseCase(projectId, uc);
        int ucId = useCaseService.findByProjectId(projectId).get(0).getId();

        User otherUser = new User();
        otherUser.setUsername("otheruser");
        otherUser.setPassword("password");
        otherUser.setName("Ioannis");
        otherUser.setEmail("other@test.com");
        userService.saveUser(otherUser);

        loginAs("otheruser");
        assertThrows(AccessDeniedException.class, () -> {
            useCaseService.findById(ucId);
        });
    }
}