package sevensmurfs.rehub.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sevensmurfs.rehub.model.entity.RehubUser;
import sevensmurfs.rehub.model.message.response.StatisticsResponse;
import sevensmurfs.rehub.model.message.response.UserResponse;
import sevensmurfs.rehub.security.JwtGenerator;
import sevensmurfs.rehub.service.StatisticsService;
import sevensmurfs.rehub.service.UserService;
import sevensmurfs.rehub.util.SecurityUtil;

@RestController
@CrossOrigin
@RequestMapping("/v1/stats")
@RequiredArgsConstructor
@Slf4j
public class UserStatsController {

    private final UserService userService;

    private final StatisticsService statisticsService;

    private final JwtGenerator jwtGenerator;

    @GetMapping
    @RolesAllowed("ROLE_SUPERADMIN")
    public ResponseEntity<StatisticsResponse> getStats() {

        log.info(" > > > GET /api/v1/stats (Get main statistics)");

        StatisticsResponse stats = statisticsService.getMainStatistics();

        log.info(" < < < GET /api/v1/stats (Get main statistics success)");

        return ResponseEntity.ok(stats);
    }

    @GetMapping("/user")
    public ResponseEntity<UserResponse> getUserInfo(@NotNull HttpServletRequest request) throws Exception {

        log.info(" > > > GET /api/v1/stats/user (Get user info)");

        String token = SecurityUtil.getJwtTokenFromRequest(request);
        String username = jwtGenerator.getUsernameFromToken(token);

        RehubUser user = userService.findUserByUsername(username);
        UserResponse response = statisticsService.getUserStatsAndInfo(user);

        log.info(" < < < GET /api/v1/stats/user (Get user info success)");

        return ResponseEntity.ok(response);
    }

}
