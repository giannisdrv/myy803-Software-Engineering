package myy803.project_2026.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import myy803.project_2026.domainmodel.AlternativeFlow;
import myy803.project_2026.domainmodel.Project;
import myy803.project_2026.domainmodel.UseCase;
import myy803.project_2026.domainmodel.User;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
public class AlternativeFlowServiceTest {

    @Autowired
    private AlternativeFlowService alternativeFlowService;
    @Autowired
    private UseCaseService useCaseService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    @Qualifier("userServiceImpl")
    private UserService userService;

    private int useCaseId;

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
        int projectId = projectService.getProjectsForCurrentUser().get(0).getId();

        UseCase uc = new UseCase();
        uc.setName("Login");
        useCaseService.createUseCase(projectId, uc);
        useCaseId = useCaseService.findByProjectId(projectId).get(0).getId();
    }

    private void loginAs(String username) {
        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken(username, "password")
        );
    }

    @Test
    public void testAddAlternativeFlow() {
        alternativeFlowService.addAlternativeFlow(useCaseId, "Test");

        List<AlternativeFlow> flows = alternativeFlowService.findByUseCaseId(useCaseId);
        assertEquals(1, flows.size());
        assertEquals("Test", flows.get(0).getDescription());
    }

    @Test
    public void testAddMultipleAlternativeFlows() {
        alternativeFlowService.addAlternativeFlow(useCaseId, "Test1");
        alternativeFlowService.addAlternativeFlow(useCaseId, "Test2");

        List<AlternativeFlow> flows = alternativeFlowService.findByUseCaseId(useCaseId);
        assertEquals(2, flows.size());
    }

    @Test
    public void testDeleteAlternativeFlow_returnsUseCaseId() {
        alternativeFlowService.addAlternativeFlow(useCaseId, "Test");
        int flowId = alternativeFlowService.findByUseCaseId(useCaseId).get(0).getId();

        int returnedUseCaseId = alternativeFlowService.deleteById(flowId);

        assertEquals(useCaseId, returnedUseCaseId);
        assertTrue(alternativeFlowService.findByUseCaseId(useCaseId).isEmpty());
    }
}