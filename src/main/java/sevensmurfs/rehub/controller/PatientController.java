package sevensmurfs.rehub.controller;

import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sevensmurfs.rehub.model.entity.Patient;
import sevensmurfs.rehub.model.message.request.UserRequest;
import sevensmurfs.rehub.model.message.request.validator.UserRequestValidator;
import sevensmurfs.rehub.model.message.response.PatientResponse;
import sevensmurfs.rehub.service.PatientService;

import java.util.List;

@RestController
@RequestMapping("/v1/patient")
@RequiredArgsConstructor
@Slf4j
public class PatientController {

    private final PatientService patientService;

    /**
     * Patient registration request POST > /api/v1/patient
     */
    @PostMapping
    public ResponseEntity<Object> register(@Validated(UserRequestValidator.Register.class) @RequestBody UserRequest userRequest) {

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
    @RolesAllowed("ROLE_ADMIN")
    public ResponseEntity<Object> getAllPatients() {

        log.info(" > > > GET /api/v1/patient (Get all patients request)");

        List<Patient> patients = patientService.getAllPatients();

        log.info(" < < < GET /api/v1/patient (Get all patients request success)");

        return ResponseEntity.ok(patients.stream().map(PatientResponse::mapPatientEntity).toList());
    }
}
