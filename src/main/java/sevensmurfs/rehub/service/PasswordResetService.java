package sevensmurfs.rehub.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sevensmurfs.rehub.enums.PasswordResetStatus;
import sevensmurfs.rehub.model.entity.PasswordReset;
import sevensmurfs.rehub.model.entity.RehubUser;
import sevensmurfs.rehub.model.message.request.PasswordResetRequest;
import sevensmurfs.rehub.repository.PasswordResetRepository;
import sevensmurfs.rehub.repository.RehubUserRepository;
import sevensmurfs.rehub.util.SecurityUtil;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordResetService {

    private final PasswordResetRepository passwordResetRepository;

    private final RehubUserRepository rehubUserRepository;

    private final PasswordEncoder encoder;

    private final EmailService emailService;

    @Transactional
    public void getPasswordResetLink(PasswordResetRequest passwordResetRequest) throws Exception {

        //Encoding given username and fetching it from database

        String username = SecurityUtil.encryptUsername(passwordResetRequest.getUsername());

        RehubUser rehubUser = rehubUserRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("User with given E-mail address does not exist"));

        log.debug("Creating PasswordReset entity.");

        //Creating a reset token
        String token = UUID.randomUUID().toString();

        PasswordReset passwordReset = PasswordReset.builder()
                                                   .token(token)
                                                   .status(PasswordResetStatus.RESET_PENDING)
                                                   .rehubUser(rehubUser)
                                                   .build();

        log.debug("Saving PasswordReset entity.");

        passwordResetRepository.save(passwordReset);

        emailService.sendPasswordReset(passwordResetRequest.getUsername(), token);
    }

    @Transactional
    public void saveNewPassword(PasswordResetRequest passwordResetRequest) throws Exception {

        int tokenTimeToLiveInHours = 12;

        PasswordReset passwordReset = passwordResetRepository.findByToken(passwordResetRequest.getToken()).orElseThrow(
                () -> new IllegalArgumentException("Given token does not exist"));

        //Checking if token is still valid

        if (!passwordReset.getCreatedAt().isAfter(LocalDateTime.now().minusHours(tokenTimeToLiveInHours))) {

            throw new IllegalArgumentException("Token alive time exceeded");

        }

        //Checking if token was already used to reset password

        if (passwordReset.getStatus().equals(PasswordResetStatus.RESET_ACCEPTED)){

            throw new IllegalArgumentException("Token was already used to reset password");
        }

        //Checking if newPass and confirmPass are equal

        if (!passwordResetRequest.getNewPass().equals(passwordResetRequest.getConfirmPass())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        RehubUser rehubUser = passwordReset.getRehubUser();

        //Changing PasswordReset status to ACCEPTED and saving changes
        passwordReset.setStatus(PasswordResetStatus.RESET_ACCEPTED);
        passwordResetRepository.save(passwordReset);

        //Encoding new password before saving it
        rehubUser.setPassword(encoder.encode(passwordResetRequest.getNewPass()));

        log.debug("Saving new password");

        rehubUserRepository.save(rehubUser);

        log.debug("Saving new password successful");

    }
}
