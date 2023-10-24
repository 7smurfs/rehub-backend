package sevensmurfs.rehub.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sevensmurfs.rehub.model.entity.Patient;
import sevensmurfs.rehub.model.entity.RehubUser;
import sevensmurfs.rehub.model.message.request.UserRequest;
import sevensmurfs.rehub.repository.PatientRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatientService {

    private final PatientRepository patientRepository;

    private final UserService userService;

    @Transactional
    public Patient registerPatient(UserRequest userRequest) {

        RehubUser user = userService.registerPatient(userRequest);
        log.debug("Creating patient entity.");

        Patient patient = Patient.builder()
                                 .firstName(userRequest.getFirstName())
                                 .lastName(userRequest.getLastName())
                                 .pin(userRequest.getPin())
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
}
