package sevensmurfs.rehub.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sevensmurfs.rehub.model.entity.Patient;
import sevensmurfs.rehub.model.entity.Therapy;
import sevensmurfs.rehub.model.message.request.UserRequest;
import sevensmurfs.rehub.model.message.request.validator.UserRequestValidator;
import sevensmurfs.rehub.model.message.response.PatientResponse;
import sevensmurfs.rehub.model.message.response.TherapyResponse;
import sevensmurfs.rehub.security.JwtGenerator;
import sevensmurfs.rehub.service.PatientService;
import sevensmurfs.rehub.service.TherapyService;
import sevensmurfs.rehub.util.SecurityUtil;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/v1/patient")
@RequiredArgsConstructor
@Slf4j
public class PatientController {

    private final PatientService patientService;

    private final TherapyService therapyService;

    private final JwtGenerator jwtGenerator;

    /**
     * Patient registration request POST > /api/v1/patient
     */
    @PostMapping("/register")
    public ResponseEntity<Object> register(@Validated({UserRequestValidator.Register.class,
                                                       UserRequestValidator.Patient.class}) @RequestBody UserRequest userRequest)
            throws Exception {

        log.info(" > > > POST /api/v1/patient (Patient register request)");

        Patient patient = patientService.registerPatient(userRequest);

        log.info(" < < < POST /api/v1/patient (Patient register request successful)");

        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
                                                                 .path("/{patientId}")
                                                                 .buildAndExpand(patient.getId())
                                                                 .toUri())
                             .build();
    }

    /**
     * Get all patients request GET > /api/v1/patient
     */
    @GetMapping
    @RolesAllowed({"ROLE_ADMIN", "ROLE_SUPERADMIN"})
    public ResponseEntity<Object> getAllPatients() {

        log.info(" > > > GET /api/v1/patient (Get all patients request)");

        List<Patient> patients = patientService.getAllPatients();

        log.info(" < < < GET /api/v1/patient (Get all patients request success)");

        return ResponseEntity.ok(patients.stream().map(PatientResponse::mapPatientEntity).toList());
    }

    /**
     * Invalidate patient request DELETE > /api/v1/patient
     */
    @DeleteMapping
    public ResponseEntity<Object> invalidatePatient(@NotNull HttpServletRequest request) {

        log.info(" > > > DELETE /api/v1/patient (Invalidate patient request)");

        String token = SecurityUtil.getJwtTokenFromRequest(request);
        String username = jwtGenerator.getUsernameFromToken(token);
        patientService.invalidatePatientWithUsername(username);

        log.info(" < < < DELETE /api/v1/patient (Invalidate patient success)");

        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/therapy", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @RolesAllowed("ROLE_PATIENT")
    public ResponseEntity<Object> createTherapy(@RequestPart @NotBlank String type,
                                                @RequestPart @NotBlank String request,
                                                @RequestPart @NotBlank String doctorFullName,
                                                @RequestPart(required = false) String referenceId,
                                                @RequestPart("therapyScan") MultipartFile therapyScan,
                                                @NotNull HttpServletRequest httpServletRequest) throws IOException {

        log.info(" > > > POST /api/v1/patient/therapy (Adding a new therapy)");

        String token = SecurityUtil.getJwtTokenFromRequest(httpServletRequest);
        String username = jwtGenerator.getUsernameFromToken(token);
        Therapy therapy = therapyService.createTherapy(type, request, doctorFullName, referenceId, therapyScan, username);

        log.info(" < < < POST /api/v1/patient/therapy (New therapy added succesfully)");

        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
                                                                 .path("/{therapyId}")
                                                                 .buildAndExpand(therapy.getId())
                                                                 .toUri())
                             .build();
    }

    /**
     *
     * Patient request to fetch PDF scan > GET /api/v1/patient/therapy/scan/{} (Requesting therapy scan PDF)
     */
    @GetMapping(value = "/therapy/scan/{id}", produces = MediaType.APPLICATION_PDF_VALUE)
    @RolesAllowed("ROLE_PATIENT")
    public ResponseEntity<Resource> getTherapyScanForTherapyById(@PathVariable(name = "id") Long id,
                                                                 @NotNull HttpServletRequest httpServletRequest) throws IOException {

        log.debug(" > > > GET /api/v1/patient/therapy/scan/{} (Requesting therapy scan PDF)", id);

        String token = SecurityUtil.getJwtTokenFromRequest(httpServletRequest);
        String username = jwtGenerator.getUsernameFromToken(token);
        Resource therapyPdf = therapyService.getTherapyScanForPatientWithTherapyWithId(username, id);

        log.debug(" < < < GET /api/v1/patient/therapy/scan/{} (Requesting therapy scan PDF success)", id);

        return ResponseEntity.ok().body(therapyPdf);
    }

    @DeleteMapping("/therapy/{id}")
    public ResponseEntity<Object> cancelTherapy(@PathVariable Long id) throws IOException {

        log.info(" > > > DELETE /api/v1/patient/therapy/{} (Canceling therapy)", id);

        therapyService.cancelTherapyWithId(id);

        log.info(" < < < DELETE /api/v1/patient/therapy/{} (Canceling therapy success)", id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/therapies")
    public ResponseEntity<Object> fetchAllTherapiesForPatient(@NotNull HttpServletRequest request) {

        log.info(" > > > GET /api/v1/patient/therapies (Fetching all therapies for patient)");

        String token = SecurityUtil.getJwtTokenFromRequest(request);
        String username = jwtGenerator.getUsernameFromToken(token);

        List<Therapy> therapies = therapyService.getAllTherapiesForPatient(username);

        log.info(" < < < GET /api/v1/patient/therapies (Get all patients request success)");

        return ResponseEntity.ok(therapies.stream().map(TherapyResponse::mapTherapyEntity).toList());
    }
}
