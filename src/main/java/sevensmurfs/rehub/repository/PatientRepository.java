package sevensmurfs.rehub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sevensmurfs.rehub.model.entity.Patient;
import sevensmurfs.rehub.model.entity.RehubUser;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findPatientByUserId(Long userId);

}
