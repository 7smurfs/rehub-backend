package sevensmurfs.rehub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sevensmurfs.rehub.model.entity.PersonalData;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface PersonalDataRepository extends JpaRepository<PersonalData, Long> {

    Optional<PersonalData> findAllByFirstNameAndLastNameAndPinAndPhinAndDateOfBirth(String firstName,
                                                                                    String lastName,
                                                                                    String pin,
                                                                                    String phin,
                                                                                    LocalDate dateOfBirth);

    Optional<PersonalData> findAllByFirstNameAndLastNameAndPinAndDateOfBirth(String firstName,
                                                                             String lastName,
                                                                             String pin,
                                                                             LocalDate dateOfBirth);
}
