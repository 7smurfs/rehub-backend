package sevensmurfs.rehub.controller;

import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sevensmurfs.rehub.model.message.request.TherapyResultRequest;
import sevensmurfs.rehub.service.TherapyResultService;

@RestController
@CrossOrigin
@RequestMapping("/v1/therapy/result")
@RequiredArgsConstructor
@Slf4j
public class TherapyResultController {

    private final TherapyResultService therapyResultService;

    @PostMapping
    @RolesAllowed("ROLE_EMPLOYEE")
    public ResponseEntity<Object> writeTherapyResult(@Validated @RequestBody TherapyResultRequest request) {
        log.info(" > > > POST /api/v1/therapy/result (Write therapy result request)");

        // Call the service method to write the therapy result
        therapyResultService.writeTherapyResult(request);

        log.info(" < < < POST /api/v1/therapy/result (Write therapy result request successful)");

        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
                                                                 .path("/{therapyId}")
                                                                 .buildAndExpand(request.getTherapyId())
                                                                 .toUri())
                             .build();
    }
}

