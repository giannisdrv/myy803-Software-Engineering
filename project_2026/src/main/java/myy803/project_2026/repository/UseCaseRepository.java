package myy803.project_2026.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import myy803.project_2026.domainmodel.UseCase;

@Repository
public interface UseCaseRepository extends JpaRepository<UseCase,Integer>{
	UseCase findById(int id);
    List<UseCase> findByProjectId(int projectId);
}
