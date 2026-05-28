package myy803.project_2026.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import myy803.project_2026.domainmodel.Project;
import myy803.project_2026.domainmodel.User;

@Repository
public interface ProjectRepository extends JpaRepository<Project,Integer>{
	Project findById(int id);
	List<Project> findByUser(User user);
	
}
