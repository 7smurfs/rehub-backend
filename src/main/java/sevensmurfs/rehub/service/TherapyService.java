package sevensmurfs.rehub.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sevensmurfs.rehub.enums.TherapyStatus;
import sevensmurfs.rehub.model.entity.Patient;
import sevensmurfs.rehub.model.entity.RehubUser;
import sevensmurfs.rehub.model.entity.Therapy;
import sevensmurfs.rehub.model.message.request.TherapyRequest;
import sevensmurfs.rehub.repository.DoctorRepository;
import sevensmurfs.rehub.repository.PatientRepository;
import sevensmurfs.rehub.repository.RehubUserRepository;
import sevensmurfs.rehub.repository.TherapyRepository;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TherapyService {

    private final TherapyRepository therapyRepository;

    private final TherapyResultService therapyResultService;

    private final PatientRepository patientRepository;

    private final DoctorRepository doctorRepository;

    private final RehubUserRepository userRepository;

    @Transactional
    public Therapy createTherapy(TherapyRequest newTherapy, String username) {

        log.debug("Creating therapy entity.");
        RehubUser user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("User with given username does not exist."));
        Patient patient = patientRepository.findPatientByUserId(user.getId()).orElseThrow(
                () -> new IllegalArgumentException("Patient with given id does not exists."));

        doctorRepository.findByFirstNameAndLastName(newTherapy.getDoctorFirstName(), newTherapy.getDoctorLastName()).orElseThrow(
                () -> new IllegalArgumentException(
                        String.format("Doctor %s %s not found!", newTherapy.getDoctorFirstName(), newTherapy.getDoctorLastName())));

        Therapy therapy = Therapy.builder()
                                 .type(newTherapy.getType())
                                 .request(newTherapy.getRequest())
                                 .patient(patient)
                                 .status(TherapyStatus.PENDING_APPROVAL)
                                 .refId(newTherapy.getReferenceId())
                                 .build();

        log.debug("Saving therapy entity.");

        //Saving therapy to patients therapies
        List<Therapy> therapies = patient.getTherapies();
        therapies.add(therapy);
        patient.setTherapies(therapies);

        log.debug("Saving updated patient entity.");
        patientRepository.save(patient);

        return therapyRepository.save(therapy);
    }

    public List<Therapy> getAllTherapiesForPatient(String username) {
        log.debug("Fetching all therapies for patient {}", username);

        RehubUser user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("User with given username does not exist."));
        Patient patient = patientRepository.findPatientByUserId(user.getId()).orElseThrow(
                () -> new IllegalArgumentException("User with given user ID does not exist."));

        log.debug("Successfully fetched patient.");

        return patient.getTherapies().isEmpty() ? Collections.emptyList() : patient.getTherapies();
    }

    @Transactional
    public void invalidateAllTherapiesForPatient(Patient patient) {
        log.debug("Invalidating all therapies for employee with ID {}.", patient.getId());
        therapyResultService.deleteAllTherapyResultsForTherapies(patient.getTherapies());
        patient.getTherapies().forEach(therapy -> therapy.setStatus(TherapyStatus.INVALIDATED));
        therapyRepository.saveAll(patient.getTherapies());
        log.debug("Successfully deleted all therapies for employee with ID {}.", patient.getId());
    }

    public Long getNumberOfActiveTherapies() {
        return therapyRepository.count();
    }
}
