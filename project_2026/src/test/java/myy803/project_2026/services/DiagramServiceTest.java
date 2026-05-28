package myy803.project_2026.services;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import myy803.project_2026.domainmodel.Crc;
import myy803.project_2026.domainmodel.Project;
import myy803.project_2026.domainmodel.UseCase;
import myy803.project_2026.domainmodel.User;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
public class DiagramServiceTest {

    @Autowired
    private DiagramService diagramService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private UseCaseService useCaseService;
    @Autowired
    private CrcService crcService;
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
        project.setName("My Project");
        projectService.createProject(project);
        projectId = projectService.getProjectsForCurrentUser().get(0).getId();
    }

    private void loginAs(String username) {
        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken(username, "password")
        );
    }

    @Test
    public void testGenerateUseCaseDiagram_containsTitle() {
        String script = diagramService.generateUseCaseDiagram(projectId);
        assertTrue(script.contains("#title:"));
        assertTrue(script.contains("My Project"));
    }

    @Test
    public void testGenerateUseCaseDiagram_containsActorsAndUseCases() {
        UseCase uc = new UseCase();
        uc.setName("Giannis");
        uc.setActor("Developer");
        useCaseService.createUseCase(projectId, uc);

        String script = diagramService.generateUseCaseDiagram(projectId);
        assertTrue(script.contains("[<actor> Developer]"));
        assertTrue(script.contains("[<usecase> Giannis]"));
        assertTrue(script.contains("->"));
    }

    @Test
    public void testGenerateUseCaseDiagram_useCaseWithoutActor() {
        UseCase uc = new UseCase();
        uc.setName("Giannis");
        useCaseService.createUseCase(projectId, uc);

        String script = diagramService.generateUseCaseDiagram(projectId);
        assertTrue(script.contains("[<usecase> Giannis]"));
        assertFalse(script.contains("<actor>"));
    }

    @Test
    public void testGenerateUseCaseDiagram_emptyProject() {
        String script = diagramService.generateUseCaseDiagram(projectId);
        assertTrue(script.contains("#title:"));
        assertNotNull(script);
    }

    @Test
    public void testGenerateCrcDiagram_containsClassInfo() {
        Crc crc = new Crc();
        crc.setName("Giannis");
        crc.setResponsibilities("Whatever");
        crc.setCollaborations("Another whatever");
        crcService.createCrc(projectId, crc);

        String script = diagramService.generateCrcDiagram(projectId);
        assertTrue(script.contains("[Giannis|"));
        assertTrue(script.contains("Whatever"));
    }

    @Test
    public void testGenerateCrcDiagram_collaborationRelationship() {
        Crc crc1 = new Crc();
        crc1.setName("Giannis");
        crc1.setResponsibilities("Whatever");
        crc1.setCollaborations("Ioannis");
        crcService.createCrc(projectId, crc1);

        Crc crc2 = new Crc();
        crc2.setName("Ioannis");
        crc2.setResponsibilities("test");
        crc2.setCollaborations("");
        crcService.createCrc(projectId, crc2);

        String script = diagramService.generateCrcDiagram(projectId);
        assertTrue(script.contains("[Giannis] -> [Ioannis]"));
    }

    @Test
    public void testGenerateCrcDiagram_noSelfCollaboration() {
        Crc crc = new Crc();
        crc.setName("Giannis");
        crc.setResponsibilities("Whatever");
        crc.setCollaborations("Another whatever");
        crcService.createCrc(projectId, crc);

        String script = diagramService.generateCrcDiagram(projectId);
        assertFalse(script.contains("[Giannis] -> [Giannis]"));
    }
}