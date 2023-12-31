package sevensmurfs.rehub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sevensmurfs.rehub.model.entity.TherapyResult;

@Repository
public interface TherapyResultRepository extends JpaRepository<TherapyResult, Long> {

}
