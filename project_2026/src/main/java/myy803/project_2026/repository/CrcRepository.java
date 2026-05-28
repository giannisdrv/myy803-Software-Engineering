package myy803.project_2026.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import myy803.project_2026.domainmodel.Crc;


@Repository
public interface CrcRepository extends JpaRepository<Crc,Integer>{
	Crc findById(int id);
	List<Crc> findByProjectId(int projectId);
}
