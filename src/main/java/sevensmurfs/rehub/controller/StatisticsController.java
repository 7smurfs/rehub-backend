package sevensmurfs.rehub.controller;

import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sevensmurfs.rehub.model.message.response.StatisticsResponse;
import sevensmurfs.rehub.service.StatisticsService;

@RestController
@CrossOrigin
@RequestMapping("/v1/stats")
@RequiredArgsConstructor
@Slf4j
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping
    @RolesAllowed("ROLE_SUPERADMIN")
    public ResponseEntity<StatisticsResponse> getStats() {

        log.info(" > > > GET /api/v1/stats (Get main statistics)");

        StatisticsResponse stats = statisticsService.getMainStatistics();

        log.info(" < < < GET /api/v1/stats (Get main statistics success)");

        return ResponseEntity.ok(stats);
    }
}
