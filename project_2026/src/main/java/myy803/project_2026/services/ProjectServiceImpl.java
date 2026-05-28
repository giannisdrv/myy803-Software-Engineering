package myy803.project_2026.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;

import myy803.project_2026.domainmodel.Project;
import myy803.project_2026.domainmodel.User;
import myy803.project_2026.repository.ProjectRepository;

@Service
public class ProjectServiceImpl implements ProjectService{
	
	@Autowired
	private ProjectRepository projectRepository;
	@Autowired
    private UserService userService;
	
	@Override
    public void createProject(Project project) {
        User currentUser = userService.getCurrentUser();
        project.setUser(currentUser);
        projectRepository.save(project);
    }
	
	@Override
    public List<Project> getProjectsForCurrentUser() {
        User currentUser = userService.getCurrentUser();
        return projectRepository.findByUser(currentUser);
    }
	
	@Override
    public void deleteById(int id) {
        Project project = projectRepository.findById(id);
        validateOwnership(project);
        projectRepository.deleteById(id);
    }
	
	@Override
    public Project findById(int id) {
        Project project = projectRepository.findById(id);
        validateOwnership(project);
        return project;
    }
	
	private void validateOwnership(Project project) {
        User currentUser = userService.getCurrentUser();
        if (project.getUser().getId() != currentUser.getId()) {
            throw new AccessDeniedException("Access denied");
        }
    }
}