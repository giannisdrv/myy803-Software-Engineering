package myy803.project_2026.services;
import java.util.List;


import myy803.project_2026.domainmodel.Project;

public interface ProjectService{
	public void createProject(Project project);
    public List<Project> getProjectsForCurrentUser();
    public void deleteById(int id);
    public Project findById(int id);
}