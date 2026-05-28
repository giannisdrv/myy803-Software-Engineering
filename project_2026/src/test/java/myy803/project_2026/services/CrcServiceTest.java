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

import jakarta.persistence.EntityManager;
import myy803.project_2026.domainmodel.Crc;
import myy803.project_2026.domainmodel.Project;
import myy803.project_2026.domainmodel.UseCase;
import myy803.project_2026.domainmodel.User;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
public class CrcServiceTest {

    @Autowired
    private CrcService crcService;
    @Autowired
    private UseCaseService useCaseService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    @Qualifier("userServiceImpl")
    private UserService userService;
    @Autowired
    private EntityManager entityManager;

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
    public void testCreateCrc() {
        Crc crc = new Crc();
        crc.setName("Giannis");
        crc.setResponsibilities("Whatever");
        crc.setCollaborations("Another whatever");
        crcService.createCrc(projectId, crc);

        List<Crc> crcs = crcService.findByProjectId(projectId);
        assertEquals(1, crcs.size());
        assertEquals("Giannis", crcs.get(0).getName());
    }

    @Test
    public void testCreateCrc_duplicateName_throwsException() {
        Crc crc1 = new Crc();
        crc1.setName("Giannis");
        crcService.createCrc(projectId, crc1);

        Crc crc2 = new Crc();
        crc2.setName("Giannis");
        assertThrows(IllegalArgumentException.class, () -> {
            crcService.createCrc(projectId, crc2);
        });
    }

    @Test
    public void testUpdateField() {
        Crc crc = new Crc();
        crc.setName("Giannis");
        crc.setResponsibilities("Old responsibilities");
        crcService.createCrc(projectId, crc);

        int crcId = crcService.findByProjectId(projectId).get(0).getId();
        crcService.updateField(crcId, "responsibilities", "New responsibilities");

        Crc updated = crcService.findById(crcId);
        assertEquals("New responsibilities", updated.getResponsibilities());
    }

    @Test
    public void testDeleteCrc_returnsProjectId() {
        Crc crc = new Crc();
        crc.setName("Giannis");
        crcService.createCrc(projectId, crc);

        int crcId = crcService.findByProjectId(projectId).get(0).getId();
        int returnedProjectId = crcService.deleteById(crcId);

        assertEquals(projectId, returnedProjectId);
        assertTrue(crcService.findByProjectId(projectId).isEmpty());
    }

    @Test
    public void testLinkUseCaseToCrc() {
        Crc crc = new Crc();
        crc.setName("Giannis");
        crcService.createCrc(projectId, crc);

        UseCase uc = new UseCase();
        uc.setName("Ioannis");
        useCaseService.createUseCase(projectId, uc);

        int crcId = crcService.findByProjectId(projectId).get(0).getId();
        int ucId = useCaseService.findByProjectId(projectId).get(0).getId();

        crcService.linkUseCase(crcId, ucId);

        List<UseCase> linked = crcService.getLinkedUseCases(crcId);
        assertEquals(1, linked.size());
        assertEquals("Ioannis", linked.get(0).getName());
    }

    @Test
    public void testUnlinkUseCaseFromCrc() {
        Crc crc = new Crc();
        crc.setName("Giannis");
        crcService.createCrc(projectId, crc);

        UseCase uc = new UseCase();
        uc.setName("Ioannis");
        useCaseService.createUseCase(projectId, uc);

        int crcId = crcService.findByProjectId(projectId).get(0).getId();
        int ucId = useCaseService.findByProjectId(projectId).get(0).getId();

        crcService.linkUseCase(crcId, ucId);
        assertEquals(1, crcService.getLinkedUseCases(crcId).size());

        crcService.unlinkUseCase(crcId, ucId);
        assertTrue(crcService.getLinkedUseCases(crcId).isEmpty());
    }

    @Test
    public void testGetLinkedCrcs_fromUseCaseSide() {
        Crc crc = new Crc();
        crc.setName("Giannis");
        crcService.createCrc(projectId, crc);

        UseCase uc = new UseCase();
        uc.setName("Ioannis");
        useCaseService.createUseCase(projectId, uc);

        int crcId = crcService.findByProjectId(projectId).get(0).getId();
        int ucId = useCaseService.findByProjectId(projectId).get(0).getId();

        crcService.linkUseCase(crcId, ucId);
        entityManager.flush();
        entityManager.clear();

        List<Crc> linkedCrcs = crcService.getLinkedCrcs(ucId);
        assertEquals(1, linkedCrcs.size());
        assertEquals("Giannis", linkedCrcs.get(0).getName());
    }
}