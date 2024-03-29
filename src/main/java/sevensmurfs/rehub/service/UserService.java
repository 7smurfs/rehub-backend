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
import sevensmurfs.rehub.model.entity.Verification;
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

    private final EmailService emailService;

    private final RehubUserRepository userRepository;

    private final UserRoleRepository userRoleRepository;

    private final PatientRepository patientRepository;

    private final EmployeeRepository employeeRepository;

    private final PersonalDataValidationService dataValidationService;

    private final PasswordEncoder encoder;

    @Transactional
    public RehubUser registerPatient(UserRequest userRequest) throws Exception {

        this.validateUserRequestRegistration(userRequest);
        Long personalDataId = dataValidationService.validatePatientFromHealthCareDatabase(userRequest);
        if (userRepository.findByPersonalDataId(personalDataId.toString()).isPresent()) {
            throw new IllegalArgumentException("Active user with given personal data already exists in the database.");
        }
        RehubUser user = RehubUser.builder()
                                  .username(SecurityUtil.encryptUsername(userRequest.getUsername()))
                                  .password(encoder.encode(userRequest.getPassword()))
                                  .roles(userRoleRepository.findAllByNameIn(List.of(Role.PATIENT)))
                                  .status(UserStatus.UNVERIFIED)
                                  .personalDataId(personalDataId.toString())
                                  .build();

        log.debug("Saving user entity.");
        return userRepository.save(user);
    }

    @Transactional
    public RehubUser registerEmployee(UserRequest userRequest) throws Exception {
        this.validateEmployeeUserRequestRegistration(userRequest);

        // This is mocked validation from our server database.
        // We are checking if user information is valid and legit
        Long personalDataId = dataValidationService.validateEmployeeFromMinistryDatabase(userRequest);

        String password = SecurityUtil.generateEmployeePassword(userRequest);

        emailService.sendAccountCreationInformationToEmployee(userRequest, password);

        RehubUser user = RehubUser.builder()
                                  .username(SecurityUtil.encryptUsername(userRequest.getUsername()))
                                  .password(encoder.encode(password))
                                  .roles(userRoleRepository.findAllByNameIn(List.of(Role.EMPLOYEE)))
                                  .status(UserStatus.ACTIVE)
                                  .personalDataId(personalDataId.toString())
                                  .build();

        log.debug("Saving user entity.");
        return userRepository.save(user);
    }

    private void validateEmployeeUserRequestRegistration(UserRequest userRequest) throws Exception {
        log.debug("Validating user registration request.");
        if (userRepository.findByUsername(SecurityUtil.encryptUsername(userRequest.getUsername())).isPresent())
            throw new IllegalArgumentException("User email already in use.");
        log.debug("User successfully validated.");
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
        user.setPersonalDataId(user.getPersonalDataId() + "_" + user.getId());
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
        if (userRoles.contains(Role.EMPLOYEE) && !userRoles.contains(Role.SUPERADMIN)) {
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

    public void giveAdminToUser(RehubUser user) {
        log.debug("Setting user with ID: {} as ADMIN.", user.getId());
        if (user.getRoles().stream().anyMatch(userRole -> userRole.getName().equals(Role.ADMIN))) {
            throw new IllegalArgumentException("User is already an ADMIN.");
        }
        UserRole adminRole = UserRole.builder()
                                     .name(Role.ADMIN)
                                     .build();
        List<UserRole> userRoles = user.getRoles();
        userRoles.add(adminRole);
        user.setRoles(userRoles);
        userRepository.save(user);
        log.debug("User saved successfully.");
    }

    @Transactional
    public void verifyUser(Verification verification) {
        RehubUser user = verification.getUser();
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);
    }
}
