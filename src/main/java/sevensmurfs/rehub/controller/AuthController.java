package sevensmurfs.rehub.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sevensmurfs.rehub.model.entity.RehubUser;
import sevensmurfs.rehub.model.message.request.PasswordResetRequest;
import sevensmurfs.rehub.model.message.request.UserRequest;
import sevensmurfs.rehub.model.message.request.validator.PasswordResetValidator;
import sevensmurfs.rehub.model.message.request.validator.UserRequestValidator;
import sevensmurfs.rehub.model.message.response.UserResponse;
import sevensmurfs.rehub.security.JwtGenerator;
import sevensmurfs.rehub.service.PasswordResetService;
import sevensmurfs.rehub.service.UserService;
import sevensmurfs.rehub.util.SecurityUtil;

@RestController
@CrossOrigin
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtGenerator jwtGenerator;

    private final UserService userService;

    private final PasswordResetService passwordResetService;

    /**
     * User login request POST > /api/v1/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@Validated(UserRequestValidator.Login.class) @RequestBody UserRequest userRequest)
            throws Exception {

        log.info(" > > > POST /api/v1/auth/login (User login request)");

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(SecurityUtil.encryptUsername(userRequest.getUsername()),
                                                                      userRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String username = SecurityUtil.encryptUsername(userRequest.getUsername());
        RehubUser user = userService.findUserByUsername(username);
        String[] userInfo = userService.getUserInfo(user);
        if (userInfo == null)
            throw new IllegalArgumentException("No valid user data.");
        String jwtToken = jwtGenerator.generateToken(authentication, user.getRoles());

        log.info(" < < < POST /api/v1/auth/login (User login request successful)");

        return ResponseEntity.ok().body(UserResponse.mapAuthenticatedUserEntity(user, jwtToken, userInfo[0], userInfo[1]));
    }

    /**
     * Request password reset link POST > /api/v1/auth/reset
     */
    @PostMapping("/reset")
    public ResponseEntity<Object> PasswordResetLink(@Validated(PasswordResetValidator.PasswordResetLink.class)
                                                    @RequestBody PasswordResetRequest passwordResetRequest) throws Exception {

        log.info(" > > > POST /api/v1/auth/reset (Requesting password reset link)");

        passwordResetService.getPasswordResetLink(passwordResetRequest);

        log.info(" < < < POST /api/v1/auth/reset (Requesting password reset link successful)");

        return ResponseEntity.ok("Password reset link sent successfully");
    }

    /**
     * Save a new password request POST > /api/v1/auth/reset/password
     */
    @PostMapping("/reset/password")
    public ResponseEntity<Object> saveNewPassword(@Validated(PasswordResetValidator.SaveNewPassword.class)
                                                  @RequestBody PasswordResetRequest passwordResetRequest) throws Exception {

        log.info(" > > > POST /api/v1/auth/reset/password (Updating user's password)");

        passwordResetService.saveNewPassword(passwordResetRequest);

        log.info(" < < < POST /api/v1/auth/reset/password (Updating user's password successful)");

        return ResponseEntity.ok("Password has been updated successfully");
    }

    /**
     * Change user's password request POST > /api/v1/auth/change/password
     */
    @PostMapping("/change/password")
    public ResponseEntity<Object> changeUserPassword(@Validated(PasswordResetValidator.SaveNewPassword.class)
                                                     @RequestBody PasswordResetRequest passwordResetRequest,
                                                     @NotNull HttpServletRequest request) {

        log.info(" > > > POST /api/v1/auth/change/password (Change user's password)");

        String token = SecurityUtil.getJwtTokenFromRequest(request);
        String username = jwtGenerator.getUsernameFromToken(token);
        passwordResetService.changeUsersPassword(username, passwordResetRequest);

        log.info(" < < < POST /api/v1/auth/change/password (Change user's password successful)");

        return ResponseEntity.ok("Password has been changed successfully");
    }
}
