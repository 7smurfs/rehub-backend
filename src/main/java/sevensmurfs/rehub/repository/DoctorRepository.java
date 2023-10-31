package sevensmurfs.rehub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sevensmurfs.rehub.enums.Role;
import sevensmurfs.rehub.model.entity.Doctor;
import sevensmurfs.rehub.model.entity.Room;
import sevensmurfs.rehub.model.entity.UserRole;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    @Query("SELECT d FROM Doctor d WHERE d.firstName = :firstName AND d.lastName = :lastName")
    Doctor findByFirstAndLastName(@Param("firstName") String firstName, @Param("lastName") String lastName);
}
