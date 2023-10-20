package sevensmurfs.rehub.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sevensmurfs.rehub.model.entity.RehubUser;
import sevensmurfs.rehub.repository.RehubUserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final RehubUserRepository userRepository;

    @Transactional
    public RehubUser findUserByUsername(String username) {
        log.debug("Fetching user by username.");
        return userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("User with given username does not exist."));
    }
}
