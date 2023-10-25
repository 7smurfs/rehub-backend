package sevensmurfs.rehub.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sevensmurfs.rehub.model.message.request.UserRequest;
import sevensmurfs.rehub.repository.PersonalDataRepository;
import sevensmurfs.rehub.util.SecurityUtil;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonalDataValidationService {

    private final PersonalDataRepository personalDataRepository;

    public void validatePatientFromHealthCareDatabase(UserRequest userRequest) {
        log.debug("Validating patient from health care database.");
        personalDataRepository.findAllByFirstNameAndLastNameAndPinAndPhinAndDateOfBirth(userRequest.getFirstName(),
                                                                                        userRequest.getFirstName(),
                                                                                        SecurityUtil.hashInput(userRequest.getPin()),
                                                                                        SecurityUtil.hashInput(userRequest.getPhin()),
                                                                                        userRequest.getDateOfBirth()).orElseThrow(
                () -> new IllegalArgumentException("Invalid personal data."));
        log.debug("Successfully validated personal data.");
    }

    public void validateEmployeeFromMinistryDatabase(UserRequest userRequest) {
        log.debug("Validating employee from personal information database.");
        personalDataRepository.findAllByFirstNameAndLastNameAndPinAndDateOfBirth(userRequest.getFirstName(),
                                                                                 userRequest.getFirstName(),
                                                                                 SecurityUtil.hashInput(userRequest.getPin()),
                                                                                 userRequest.getDateOfBirth()).orElseThrow(
                () -> new IllegalArgumentException("Invalid personal data."));
        log.debug("Successfully validated personal data.");
    }
}
