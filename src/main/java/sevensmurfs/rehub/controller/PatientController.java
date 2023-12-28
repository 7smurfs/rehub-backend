package sevensmurfs.rehub.controller;

import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sevensmurfs.rehub.model.entity.Therapy;
import sevensmurfs.rehub.model.entity.Patient;
import sevensmurfs.rehub.model.message.request.TherapyRequest;
import sevensmurfs.rehub.model.message.request.UserRequest;
import sevensmurfs.rehub.model.message.request.validator.UserRequestValidator;
import sevensmurfs.rehub.model.message.response.PatientResponse;
import sevensmurfs.rehub.model.message.response.TherapyResponse;
import sevensmurfs.rehub.service.PatientService;
import sevensmurfs.rehub.service.TherapyService;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/v1/patient")
@RequiredArgsConstructor
@Slf4j
public class PatientController {

    private final PatientService patientService;

    private final TherapyService therapyService;

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
     * Invalidate patient request DELETE > /api/v1/patient/:id
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> invalidatePatient(@PathVariable(name = "id") Long id) {

        log.info(" > > > DELETE /api/v1/patient/{} (Invalidate patient request)", id);

        patientService.invalidatePatientWithId(id);

        log.info(" < < < DELETE /api/v1/patient/{} (Invalidate patient success)", id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/therapy")
    public ResponseEntity<Object> createTherapy(@Validated @RequestBody TherapyRequest therapyRequest) throws Exception {

        log.info(" > > > POST /api/v1/patient/therapy (Adding a new therapy)");

        Therapy therapy = therapyService.createTherapy(therapyRequest);

        log.info(" < < < POST /api/v1/therapy (New therapy added succesfully)");

        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
                                                                 .path("/{therapyId}")
                                                                 .buildAndExpand(therapy.getId())
                                                                 .toUri())
                             .build();
    }

    @GetMapping("/{id}/therapies")
    public ResponseEntity<Object> fetchAllTherapiesForPatient(@PathVariable(name = "id") Long id) throws Exception {

        log.info(" > > > GET /api/v1/patient/therapies (Fetching all therapies with id: {})", id);

        List<Therapy> therapies = therapyService.getAllTherapies(id);

        log.info(" < < < GET /api/v1/patient/therapies (Get all patients request success)");

        return ResponseEntity.ok(therapies.stream().map(TherapyResponse::mapTherapyEntity).toList());
    }

}
