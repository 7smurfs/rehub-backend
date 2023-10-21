package sevensmurfs.rehub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sevensmurfs.rehub.model.entity.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

}
