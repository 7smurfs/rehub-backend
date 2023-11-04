package sevensmurfs.rehub.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sevensmurfs.rehub.enums.TherapyStatus;
import sevensmurfs.rehub.model.entity.Patient;
import sevensmurfs.rehub.model.entity.Therapy;
import sevensmurfs.rehub.model.message.request.TherapyRequest;
import sevensmurfs.rehub.repository.DoctorRepository;
import sevensmurfs.rehub.repository.PatientRepository;
import sevensmurfs.rehub.repository.TherapyRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TherapyService {

    private final TherapyRepository therapyRepository;

    private final TherapyResultService therapyResultService;

    private final PatientRepository patientRepository;

    private final DoctorRepository doctorRepository;

    @Transactional
    public Therapy createTherapy(TherapyRequest newTherapy) {

        log.debug("Creating therapy entity.");
        Patient patient = patientRepository.findById(newTherapy.getPatientId()).orElseThrow(
                () -> new IllegalArgumentException("Patient with given id does not exists."));

        doctorRepository.findByFirstNameAndLastName(newTherapy.getDoctorFirstName(), newTherapy.getDoctorLastName()).orElseThrow(
                () -> new IllegalArgumentException(
                        String.format("Doctor %s %s not found!", newTherapy.getDoctorFirstName(), newTherapy.getDoctorLastName())));

        Therapy therapy = Therapy.builder()
                                 .type(newTherapy.getType())
                                 .request(newTherapy.getRequest())
                                 .patient(patient)
                                 .status(newTherapy.getStatus())
                                 .build();

        log.debug("Saving therapy entity.");

        //Saving therapy to patients therapies
        List<Therapy> therapies = patient.getTherapies();
        therapies.add(therapy);
        patient.setTherapies(therapies);

        patientRepository.save(patient);

        return therapyRepository.save(therapy);
    }

    public List<Therapy> getAllTherapies(Long patientId) {
        log.debug("Fetching all therapies for patient {}", patientId);
        return therapyRepository.findByPatientId(patientId).orElseThrow(
                () -> new IllegalArgumentException("No therapies found for patient!"));
    }

    @Transactional
    public void invalidateAllTherapiesForPatient(Patient patient) {
        log.debug("Invalidating all therapies for employee with ID {}.", patient.getId());
        therapyResultService.deleteAllTherapyResultsForTherapies(patient.getTherapies());
        patient.getTherapies().forEach(therapy -> therapy.setStatus(TherapyStatus.INVALIDATED));
        therapyRepository.saveAll(patient.getTherapies());
        log.debug("Successfully deleted all therapies for employee with ID {}.", patient.getId());
    }
}
