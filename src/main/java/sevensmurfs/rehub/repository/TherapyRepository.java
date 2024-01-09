package sevensmurfs.rehub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sevensmurfs.rehub.model.entity.Therapy;

import java.util.List;
import java.util.Optional;

@Repository
public interface TherapyRepository extends JpaRepository<Therapy, Long> {

    List<Therapy> findByEmployeeId(Long employeeId);

    List<Therapy> findAllByRoomId(Long roomId);
}
