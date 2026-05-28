package myy803.project_2026.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import myy803.project_2026.domainmodel.User;

@Repository
public interface UserRepository extends JpaRepository<User,Integer>{
	User findById(int id);
	User findByUsername(String username);
}
