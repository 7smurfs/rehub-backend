package sevensmurfs.rehub.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sevensmurfs.rehub.model.entity.Patient;
import sevensmurfs.rehub.repository.TherapyRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class TherapyService {

    private final TherapyRepository therapyRepository;

    private final TherapyResultService therapyResultService;

    @Transactional
    public void deleteAllTherapiesForPatient(Patient patient) {
        therapyResultService.deleteAllTherapyResultsForTherapies(patient.getTherapies());
        log.debug("Deleting all therapies for employee with ID {}.", patient.getId());

        therapyRepository.deleteAll(patient.getTherapies());
        log.debug("Successfully deleted all therapies for employee with ID {}.", patient.getId());
    }

}
