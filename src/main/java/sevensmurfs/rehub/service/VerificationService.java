package sevensmurfs.rehub.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sevensmurfs.rehub.enums.VerificationStatus;
import sevensmurfs.rehub.model.entity.RehubUser;
import sevensmurfs.rehub.model.entity.Verification;
import sevensmurfs.rehub.repository.VerificationRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class VerificationService {

    @Value("${user.verification.link}")
    private String userVerificationLink;

    private final VerificationRepository verificationRepository;

    public Verification createVerificationForUser(RehubUser user) {
        log.debug("Creating verification for user.");

        Verification verification = Verification.builder()
                                                .token(UUID.randomUUID().toString())
                                                .status(VerificationStatus.PENDING)
                                                .user(user)
                                                .build();

        log.debug("Verification created.");
        return verificationRepository.save(verification);
    }

    public String formatTokenForEmail(String token) {
        return userVerificationLink + token;
    }

    @Transactional
    public Verification verifyUserWithToken(String token) {
        log.debug("Verifying user with token.");

        Verification verification = verificationRepository.findByToken(token).orElseThrow(
                () -> new IllegalArgumentException("Cannot find verificaton with given token."));
        verification.setStatus(VerificationStatus.VALIDATED);
        return verificationRepository.save(verification);
    }
}
