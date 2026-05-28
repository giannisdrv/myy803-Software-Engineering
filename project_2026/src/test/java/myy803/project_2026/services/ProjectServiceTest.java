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
import myy803.project_2026.domainmodel.User;
import myy803.project_2026.repository.UserRepository;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
public class ProjectServiceTest {

    @Autowired
    private ProjectService projectService;
    @Autowired
    @Qualifier("userServiceImpl")
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    private User userA;
    private User userB;

    @BeforeEach
    public void setUp() {
        userA = new User();
        userA.setUsername("userA");
        userA.setPassword("password");
        userA.setName("User A");
        userA.setEmail("a@test.com");
        userService.saveUser(userA);
        userA = userRepository.findByUsername("userA");

        userB = new User();
        userB.setUsername("userB");
        userB.setPassword("password");
        userB.setName("User B");
        userB.setEmail("b@test.com");
        userService.saveUser(userB);
        userB = userRepository.findByUsername("userB");
    }

    private void loginAs(String username) {
        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken(username, "password")
        );
    }

    @Test
    public void testCreateProject() {
        loginAs("userA");
        Project project = new Project();
        project.setName("Test Project");
        projectService.createProject(project);

        List<Project> projects = projectService.getProjectsForCurrentUser();
        assertEquals(1, projects.size());
        assertEquals("Test Project", projects.get(0).getName());
    }

    @Test
    public void testFindOwnProject() {
        loginAs("userA");
        Project project = new Project();
        project.setName("My Project");
        projectService.createProject(project);

        int projectId = projectService.getProjectsForCurrentUser().get(0).getId();
        Project found = projectService.findById(projectId);
        assertEquals("My Project", found.getName());
    }

    @Test
    public void testFindOtherUsersProject_throwsAccessDenied() {
        loginAs("userA");
        Project project = new Project();
        project.setName("A Project");
        projectService.createProject(project);
        int projectId = projectService.getProjectsForCurrentUser().get(0).getId();

        loginAs("userB");
        assertThrows(AccessDeniedException.class, () -> {
            projectService.findById(projectId);
        });
    }

    @Test
    public void testDeleteOtherUsersProject_throwsAccessDenied() {
        loginAs("userA");
        Project project = new Project();
        project.setName("A Project");
        projectService.createProject(project);
        int projectId = projectService.getProjectsForCurrentUser().get(0).getId();

        loginAs("userB");
        assertThrows(AccessDeniedException.class, () -> {
            projectService.deleteById(projectId);
        });
    }

    @Test
    public void testGetProjectsForCurrentUser_returnsOnlyOwnProjects() {
        loginAs("userA");
        Project pA = new Project();
        pA.setName("A Project");
        projectService.createProject(pA);

        loginAs("userB");
        Project pB = new Project();
        pB.setName("B Project");
        projectService.createProject(pB);

        List<Project> bProjects = projectService.getProjectsForCurrentUser();
        assertEquals(1, bProjects.size());
        assertEquals("B Project", bProjects.get(0).getName());
    }

    @Test
    public void testDeleteOwnProject() {
        loginAs("userA");
        Project project = new Project();
        project.setName("Delete");
        projectService.createProject(project);
        int projectId = projectService.getProjectsForCurrentUser().get(0).getId();

        projectService.deleteById(projectId);

        List<Project> projects = projectService.getProjectsForCurrentUser();
        assertTrue(projects.isEmpty());
    }
}