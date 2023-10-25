package sevensmurfs.rehub.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sevensmurfs.rehub.model.entity.RehubUser;
import sevensmurfs.rehub.model.message.request.UserRequest;
import sevensmurfs.rehub.model.message.request.validator.UserRequestValidator;
import sevensmurfs.rehub.model.message.response.UserResponse;
import sevensmurfs.rehub.security.JwtGenerator;
import sevensmurfs.rehub.service.UserService;
import sevensmurfs.rehub.util.SecurityUtil;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtGenerator jwtGenerator;

    private final UserService userService;

    /**
     * User login request POST > /api/v1/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@Validated(UserRequestValidator.Login.class) @RequestBody UserRequest userRequest) {

        log.info(" > > > POST /api/v1/auth/login (User login request)");

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(SecurityUtil.hashInput(userRequest.getUsername()),
                                                                      userRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String username = SecurityUtil.hashInput(userRequest.getUsername());
        RehubUser user = userService.findUserByUsername(username);
        String jwtToken = jwtGenerator.generateToken(authentication, user.getRoles());

        log.info(" < < < POST /api/v1/auth/login (User login request successful)");

        return ResponseEntity.ok().body(UserResponse.mapAuthenticatedUserEntity(user, jwtToken));
    }
}
