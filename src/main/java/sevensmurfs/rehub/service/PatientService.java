package sevensmurfs.rehub.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sevensmurfs.rehub.enums.UserStatus;
import sevensmurfs.rehub.model.entity.Patient;
import sevensmurfs.rehub.model.entity.RehubUser;
import sevensmurfs.rehub.model.message.request.UserRequest;
import sevensmurfs.rehub.repository.PatientRepository;
import sevensmurfs.rehub.util.SecurityUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatientService {

    private final PatientRepository patientRepository;

    private final UserService userService;

    private final TherapyService therapyService;

    @Transactional
    public Patient registerPatient(UserRequest userRequest) throws Exception {

        RehubUser user = userService.registerPatient(userRequest);
        log.debug("Creating patient entity.");

        Patient patient = Patient.builder()
                                 .firstName(userRequest.getFirstName())
                                 .lastName(userRequest.getLastName())
                                 .pin(SecurityUtil.hashInput(userRequest.getPin()))
                                 .phin(SecurityUtil.hashInput(userRequest.getPhin()))
                                 .phoneNumber(userRequest.getPhoneNumber())
                                 .dateOfBirth(userRequest.getDateOfBirth())
                                 .gender(userRequest.getGender())
                                 .user(user)
                                 .build();

        log.debug("Saving patient entity.");
        return patientRepository.save(patient);
    }

    public List<Patient> getAllPatients() {
        log.debug("Fetching all patients.");
        return patientRepository.findAll();
    }

    @Transactional
    public void invalidatePatientWithId(Long id) {
        Patient patient = patientRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Patient with given ID does not exist."));
        if (patient.getUser().getStatus().equals(UserStatus.INVALIDATED))
            throw new IllegalArgumentException("User is already invalidated.");

        log.debug("Invalidating patient with ID {}.", patient.getId());
        therapyService.invalidateAllTherapiesForPatient(patient);
        patient.getUser().setStatus(UserStatus.INVALIDATED);
        userService.saveUser(patient.getUser());
        log.debug("Successfully invalidated patient with ID {}.", patient.getId());
    }
}
