package myy803.project_2026.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import myy803.project_2026.domainmodel.AlternativeFlow;

@Repository
public interface AlternativeFlowRepository extends JpaRepository<AlternativeFlow,Integer>{
	AlternativeFlow findById(int id);
	List<AlternativeFlow> findByUsecase_Id(int usecaseId);
}
