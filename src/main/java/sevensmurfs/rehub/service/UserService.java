package sevensmurfs.rehub.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sevensmurfs.rehub.enums.Role;
import sevensmurfs.rehub.enums.UserStatus;
import sevensmurfs.rehub.model.entity.Employee;
import sevensmurfs.rehub.model.entity.Patient;
import sevensmurfs.rehub.model.entity.RehubUser;
import sevensmurfs.rehub.model.entity.UserRole;
import sevensmurfs.rehub.model.message.request.UserRequest;
import sevensmurfs.rehub.repository.EmployeeRepository;
import sevensmurfs.rehub.repository.PatientRepository;
import sevensmurfs.rehub.repository.RehubUserRepository;
import sevensmurfs.rehub.repository.UserRoleRepository;
import sevensmurfs.rehub.util.SecurityUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final RehubUserRepository userRepository;

    private final UserRoleRepository userRoleRepository;

    private final PatientRepository patientRepository;

    private final EmployeeRepository employeeRepository;

    private final PersonalDataValidationService dataValidationService;

    private final PasswordEncoder encoder;

    @Transactional
    public RehubUser registerPatient(UserRequest userRequest) throws Exception {
        this.validateUserRequestRegistration(userRequest);
        // This is mocked validation from our server database.
        // We are checking if user information is valid and legit
        dataValidationService.validatePatientFromHealthCareDatabase(userRequest);

        RehubUser user = RehubUser.builder()
                                  .username(SecurityUtil.encryptUsername(userRequest.getUsername()))
                                  .password(encoder.encode(userRequest.getPassword()))
                                  .roles(userRoleRepository.findAllByNameIn(List.of(Role.PATIENT)))
                                  .status(UserStatus.ACTIVE)
                                  .build();

        log.debug("Saving user entity.");
        return userRepository.save(user);
    }

    @Transactional
    public RehubUser registerEmployee(UserRequest userRequest) throws Exception {
        this.validateUserRequestRegistration(userRequest);
        // This is mocked validation from our server database.
        // We are checking if user information is valid and legit
        dataValidationService.validateEmployeeFromMinistryDatabase(userRequest);

        RehubUser user = RehubUser.builder()
                                  .username(SecurityUtil.encryptUsername(userRequest.getUsername()))
                                  .password(encoder.encode(userRequest.getPassword()))
                                  .roles(userRoleRepository.findAllByNameIn(List.of(Role.EMPLOYEE)))
                                  .status(UserStatus.ACTIVE)
                                  .build();

        log.debug("Saving user entity.");
        return userRepository.save(user);
    }

    private void validateUserRequestRegistration(UserRequest userRequest) throws Exception {
        log.debug("Validating user registration request.");

        if (userRepository.findByUsername(SecurityUtil.encryptUsername(userRequest.getUsername())).isPresent())
            throw new IllegalArgumentException("User email already in use.");

        if (!userRequest.getPassword().equals(userRequest.getConfirmPassword()))
            throw new IllegalArgumentException("Passwords do not match.");

        log.debug("User successfully validated.");
    }

    @Transactional
    public RehubUser findUserByUsername(String username) {
        log.debug("Fetching user by username.");
        return userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("User with given username does not exist."));
    }

    @Transactional
    public void saveUser(RehubUser user) {
        log.debug("Saving user.");
        userRepository.save(user);
        log.debug("User saved.");
    }

    @Transactional
    public void invalidateUser(RehubUser user) {
        log.debug("Invalidating user.");
        user.setStatus(UserStatus.INVALIDATED);
        user.setUsername(SecurityUtil.hashInput(user.getUsername()) + "_" + user.getId());
        userRepository.save(user);
        log.debug("User invalidated.");
    }

    @Transactional
    public String[] getUserInfo(RehubUser user) {
        String[] result = new String[2];
        List<Role> userRoles = user.getRoles().stream().map(UserRole::getName).toList();
        if (userRoles.contains(Role.PATIENT)) {
            Patient patient = patientRepository.findPatientByUserId(user.getId()).orElseThrow(
                    () -> new IllegalArgumentException("Invalid user data. Can't find patient with given user data."));
            result[0] = patient.getFirstName();
            result[1] = patient.getLastName();
            return result;
        }
        if (userRoles.contains(Role.EMPLOYEE)) {
            Employee employee = employeeRepository.findEmployeeByUserId(user.getId()).orElseThrow(
                    () -> new IllegalArgumentException("Invalid user data. Can't find employee with given user data."));
            result[0] = employee.getFirstName();
            result[1] = employee.getLastName();
            return result;
        }
        if (userRoles.contains(Role.SUPERADMIN)) {
            result[0] = "SuperAdmin";
            result[1] = "User";
            return result;
        }
        return null;
    }
}
