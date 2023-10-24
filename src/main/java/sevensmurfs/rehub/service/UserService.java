package sevensmurfs.rehub.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sevensmurfs.rehub.enums.Role;
import sevensmurfs.rehub.model.entity.RehubUser;
import sevensmurfs.rehub.model.message.request.UserRequest;
import sevensmurfs.rehub.repository.RehubUserRepository;
import sevensmurfs.rehub.repository.UserRoleRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final RehubUserRepository userRepository;

    private final UserRoleRepository userRoleRepository;

    private final PasswordEncoder encoder;

    @Transactional
    public RehubUser registerPatient(UserRequest userRequest) {
        this.validateUserRequestRegistration(userRequest);
        RehubUser user = RehubUser.builder()
                                  .username(userRequest.getUsername())
                                  .password(encoder.encode(userRequest.getPassword()))
                                  .roles(userRoleRepository.findAllByNameIn(List.of(Role.PATIENT)))
                                  .build();

        log.debug("Saving user entity.");
        return userRepository.save(user);
    }

    @Transactional
    public RehubUser registerEmployee(UserRequest userRequest) {
        this.validateUserRequestRegistration(userRequest);
        RehubUser user = RehubUser.builder()
                                  .username(userRequest.getUsername())
                                  .password(encoder.encode(userRequest.getPassword()))
                                  .roles(userRoleRepository.findAllByNameIn(List.of(Role.EMPLOYEE)))
                                  .build();

        log.debug("Saving user entity.");
        return userRepository.save(user);
    }

    private void validateUserRequestRegistration(UserRequest userRequest) {
        log.debug("Validating user registration request.");

        if (userRepository.findByUsername(userRequest.getUsername()).isPresent())
            throw new IllegalArgumentException("User email already in use.");

        if (!userRequest.getPassword().equals(userRequest.getConfirmPassword()))
            throw new IllegalArgumentException("Passwords do not match.");

        if (userRequest.getDateOfBirth().plusYears(18L).isAfter(LocalDate.now()))
            throw new IllegalArgumentException("User needs to be legal age to register.");

        log.debug("User successfully validated.");
    }

    @Transactional
    public RehubUser findUserByUsername(String username) {
        log.debug("Fetching user by username.");
        return userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("User with given username does not exist."));
    }

}
