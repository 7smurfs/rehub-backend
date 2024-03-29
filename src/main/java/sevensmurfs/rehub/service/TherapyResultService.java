package sevensmurfs.rehub.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sevensmurfs.rehub.model.entity.Employee;
import sevensmurfs.rehub.model.entity.RehubUser;
import sevensmurfs.rehub.model.entity.Therapy;
import sevensmurfs.rehub.model.entity.TherapyResult;
import sevensmurfs.rehub.model.message.request.TherapyResultRequest;
import sevensmurfs.rehub.repository.EmployeeRepository;
import sevensmurfs.rehub.repository.TherapyRepository;
import sevensmurfs.rehub.repository.TherapyResultRepository;
import sevensmurfs.rehub.util.SecurityUtil;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class TherapyResultService {

    private final TherapyResultRepository therapyResultRepository;

    private final TherapyRepository therapyRepository;

    private final EmployeeRepository employeeRepository;

    private final UserService userService;

    private final EmailService emailService;

    @Transactional
    public void deleteAllTherapyResultsForTherapies(List<Therapy> therapies) {
        log.debug("Deleting all therapy results.");
        List<TherapyResult> therapyResults = therapies.stream().map(Therapy::getTherapyResult).filter(Objects::nonNull).toList();

        if (!therapyResults.isEmpty())
            therapyResultRepository.deleteAll(therapyResults);

        log.debug("Successfully deleted all therapy results.");
    }

    @Transactional
    public void writeTherapyResult(TherapyResultRequest request) throws Exception {

        Therapy therapy = therapyRepository.findById(request.getTherapyId())
                                           .orElseThrow(
                                                   () -> new IllegalArgumentException("Invalid therapy ID: " + request.getTherapyId()));

        // Create a new TherapyResult entity
        TherapyResult therapyResult = TherapyResult.builder()
                                                   .therapy(therapy)
                                                   .result(request.getResult())
                                                   .status(request.getStatus())
                                                   .build();

        therapy.setTherapyResult(therapyResult);

        TherapyResult savedTherapyResult = therapyResultRepository.save(therapyResult);
        Therapy savedTherapy = therapyRepository.save(therapy);
        log.debug("Therapy result saved successfully.");
        String username = SecurityUtil.decryptUsername(therapy.getPatient().getUser().getUsername());
        emailService.sendTherapyResultEmail(username, therapy.getPatient(), savedTherapy, savedTherapyResult);
    }

    public List<TherapyResult> getTherapyResultsForEmployee(String username) {
        log.debug("Get therapy results for employee.");

        RehubUser user = userService.findUserByUsername(username);
        Employee employee = employeeRepository.findEmployeeByUserId(user.getId()).orElseThrow(
                () -> new IllegalArgumentException("Cannot find employee with user id."));

        List<Therapy> employeeTherapies = therapyRepository.findByEmployeeId(employee.getId());
        List<TherapyResult> therapyResults = employeeTherapies.stream().filter(therapy -> therapy.getTherapyResult() != null).map(
                Therapy::getTherapyResult).toList();

        log.debug("Fetched therapy results successfully");
        return therapyResults;
    }
}
