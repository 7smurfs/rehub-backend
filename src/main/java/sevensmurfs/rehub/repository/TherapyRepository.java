package sevensmurfs.rehub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sevensmurfs.rehub.model.entity.Therapy;


@Repository
public interface TherapyRepository extends JpaRepository<Therapy, Long> {


}
