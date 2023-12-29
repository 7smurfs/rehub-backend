package sevensmurfs.rehub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sevensmurfs.rehub.model.entity.Employee;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findEmployeeByUserId(Long  userId);

    long countAllByCreatedAtAfter(LocalDateTime localDateTime);
}
