package sevensmurfs.rehub.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sevensmurfs.rehub.model.entity.PasswordReset;
import sevensmurfs.rehub.model.message.request.PasswordResetRequest;
import sevensmurfs.rehub.model.message.request.validator.PasswordResetValidator;
import sevensmurfs.rehub.model.message.request.validator.UserRequestValidator;
import sevensmurfs.rehub.service.PasswordResetService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    /**
     * Request password reset link POST > /api/v1/auth/reset
     */
    @PostMapping("/v1/auth/reset")
    public ResponseEntity<Object> getPasswordResetLink(@Validated(PasswordResetValidator.getPasswordResetLink.class)
                                                       @RequestBody PasswordResetRequest passwordResetRequest) throws Exception {

        log.info(" > > > POST /api/v1/auth/reset (Requesting password reset link)");

        passwordResetService.getPasswordResetLink(passwordResetRequest);

        log.info(" < < < POST /api/v1/auth/reset (Requesting password reset link successful)");

        return ResponseEntity.ok("Password reset link sent successfully");
    }

    /**
     * Save a new password request POST > /api/v1/auth/reset/password
     */
    @PostMapping("/v1/auth/reset/password")
    public ResponseEntity<Object> saveNewPassword(@Validated(PasswordResetValidator.saveNewPassword.class)
                                                  @RequestBody PasswordResetRequest passwordResetRequest) throws Exception {

        log.info(" > > > POST /api/v1/auth/reset/password (Updating user's password)");

        passwordResetService.saveNewPassword(passwordResetRequest);

        log.info(" < < < POST /api/v1/auth/reset/password (Updating user's password successful)");

        return ResponseEntity.ok("Password has been updated successfully");
    }
}
