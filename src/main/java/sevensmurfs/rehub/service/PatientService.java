package sevensmurfs.rehub.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sevensmurfs.rehub.enums.UserStatus;
import sevensmurfs.rehub.model.entity.Patient;
import sevensmurfs.rehub.model.entity.RehubUser;
import sevensmurfs.rehub.model.entity.Verification;
import sevensmurfs.rehub.model.message.request.UserRequest;
import sevensmurfs.rehub.repository.PatientRepository;
import sevensmurfs.rehub.util.SecurityUtil;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatientService {

    private final PatientRepository patientRepository;

    private final UserService userService;

    private final VerificationService verificationService;

    private final TherapyService therapyService;

    private final EmailService emailService;

    @Transactional
    public Patient registerPatient(UserRequest userRequest) throws Exception {

        RehubUser user = userService.registerPatient(userRequest);
        log.debug("Creating patient entity.");

        Patient patient = Patient.builder()
                                 .firstName(userRequest.getFirstName())
                                 .lastName(userRequest.getLastName())
                                 .phoneNumber(userRequest.getPhoneNumber())
                                 .dateOfBirth(userRequest.getDateOfBirth())
                                 .gender(userRequest.getGender())
                                 .user(user)
                                 .build();

        log.debug("Saving patient entity.");
        Verification verification = verificationService.createVerificationForUser(user);
        String formattedToken = verificationService.formatTokenForEmail(verification.getToken());
        emailService.sendEmailForRegisteredUser(userRequest.getFirstName(), userRequest.getLastName(), userRequest.getUsername(),
                                                formattedToken);

        return patientRepository.save(patient);
    }

    public List<Patient> getAllPatients() {
        log.debug("Fetching all patients.");
        return patientRepository.findAll();
    }

    @Transactional
    public void invalidatePatientWithUsername(String username) {
        RehubUser user = userService.findUserByUsername(username);
        Patient patient = patientRepository.findPatientByUserId(user.getId()).orElseThrow(
                () -> new IllegalArgumentException("Patient with given ID does not exist."));
        if (patient.getUser().getStatus().equals(UserStatus.INVALIDATED))
            throw new IllegalArgumentException("User is already invalidated.");
        patient.setPhoneNumber(SecurityUtil.hashInput(patient.getPhoneNumber()) + "_" + patient.getId());
        log.debug("Invalidating patient with ID {}.", patient.getId());
        therapyService.invalidateAllTherapiesForPatient(patient);
        userService.invalidateUser(patient.getUser());
        patientRepository.save(patient);
        log.debug("Successfully invalidated patient with ID {}.", patient.getId());
    }

    public Long getNumberOfPatients() {
        return patientRepository.count();
    }

    public Long getNumberOfNewPatientsForMonth() {
        return patientRepository.countAllByCreatedAtAfter(LocalDateTime.now().minusMonths(1));
    }

    public Patient findPatientByUserId(Long id) {
        return patientRepository.findPatientByUserId(id).orElseThrow(
                () -> new IllegalArgumentException("Cannot find patient with user ID: " + id.toString()));
    }
}
