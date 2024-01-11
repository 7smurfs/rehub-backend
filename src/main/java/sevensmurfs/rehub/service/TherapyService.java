package sevensmurfs.rehub.service;

import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sevensmurfs.rehub.enums.TherapyStatus;
import sevensmurfs.rehub.model.entity.Appointment;
import sevensmurfs.rehub.model.entity.Employee;
import sevensmurfs.rehub.model.entity.Patient;
import sevensmurfs.rehub.model.entity.RehubUser;
import sevensmurfs.rehub.model.entity.Therapy;
import sevensmurfs.rehub.repository.AppointmentRepository;
import sevensmurfs.rehub.repository.DoctorRepository;
import sevensmurfs.rehub.repository.PatientRepository;
import sevensmurfs.rehub.repository.RehubUserRepository;
import sevensmurfs.rehub.repository.TherapyRepository;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TherapyService {

    private static final String PDF_EXTENSION = ".pdf";

    private final TherapyRepository therapyRepository;

    private final AppointmentRepository appointmentRepository;

    private final TherapyResultService therapyResultService;

    private final PatientRepository patientRepository;

    private final DoctorRepository doctorRepository;

    private final RehubUserRepository userRepository;

    @Getter
    @Value("${therapy.scan.save.dir}")
    private String therapyScanDir;

    @Transactional
    public Therapy createTherapy(String type,
                                 String request,
                                 String doctorFullName,
                                 String referenceId,
                                 MultipartFile therapyScan,
                                 String username) throws IOException {

        log.debug("Creating therapy entity.");

        this.validateTherapyScan(therapyScan);

        RehubUser user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("User with given username does not exist."));
        Patient patient = patientRepository.findPatientByUserId(user.getId()).orElseThrow(
                () -> new IllegalArgumentException("Patient with given id does not exists."));

        String[] doctor = doctorFullName.trim().split(" ");
        if (doctor.length < 2)
            throw new IllegalArgumentException("Invalid doctor name.");

        doctorRepository.findByFirstNameAndLastName(doctor[0], doctor[1]).orElseThrow(
                () -> new IllegalArgumentException(
                        String.format("Doctor %s %s not found!", doctor[0], doctor[1])));

        String therapyScanUrl = this.saveTherapyScanForPatient(therapyScan, patient);

        Therapy therapy = Therapy.builder()
                                 .type(type)
                                 .request(request)
                                 .patient(patient)
                                 .doctorFullName(doctorFullName)
                                 .therapyScan(therapyScanUrl)
                                 .status(TherapyStatus.PENDING_APPROVAL)
                                 .refId(referenceId == null ? null : Long.parseLong(referenceId))
                                 .build();

        log.debug("Saving therapy entity.");

        //Saving therapy to patients therapies
        List<Therapy> therapies = patient.getTherapies();
        therapies.add(therapy);
        patient.setTherapies(therapies);

        log.debug("Saving updated patient entity.");
        patientRepository.save(patient);

        return therapyRepository.save(therapy);
    }

    private String saveTherapyScanForPatient(MultipartFile therapyScan, Patient patient) throws IOException {
        String newFileName = "T_SCAN_" + UUID.randomUUID() + "_" + patient.getId() + PDF_EXTENSION;
        Path filePath = Path.of(therapyScanDir + newFileName);
        Files.copy(therapyScan.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        log.debug("Therapy scan save successfully.");
        return newFileName;
    }

    private void validateTherapyScan(MultipartFile therapyScan) {
        log.debug("Validating therapy scan.");
        if (therapyScan.isEmpty()) {
            throw new IllegalArgumentException("Valid therapy scan needs to be uploaded.");
        }
        if (!Objects.equals(therapyScan.getContentType(), MediaType.APPLICATION_PDF_VALUE)) {
            throw new IllegalArgumentException("Therapy scan needs to be PDF.");
        }
        if ((therapyScan.getSize() / (1024.0 * 1024.0)) > 2.0) {
            throw new IllegalArgumentException("Therapy scan needs to be less than 2 MB in size.");
        }
        log.debug("Therapy scan successfully validated.");
    }

    public List<Therapy> getAllTherapiesForPatient(String username) {
        log.debug("Fetching all therapies for patient {}", username);

        RehubUser user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("User with given username does not exist."));
        Patient patient = patientRepository.findPatientByUserId(user.getId()).orElseThrow(
                () -> new IllegalArgumentException("User with given user ID does not exist."));
        List<Therapy> therapies = patient.getTherapies()
                                         .stream()
                                         .sorted(Comparator.comparing(therapy -> therapy.getCreatedAt().compareTo(LocalDateTime.now())))
                                         .filter(therapy -> !therapy.getStatus().equals(TherapyStatus.CANCELED))
                                         .toList();

        log.debug("Successfully fetched patient.");

        return patient.getTherapies().isEmpty() ? Collections.emptyList() : therapies;
    }

    @Transactional
    public void invalidateAllTherapiesForPatient(Patient patient) {
        log.debug("Invalidating all therapies for employee with ID {}.", patient.getId());
        therapyResultService.deleteAllTherapyResultsForTherapies(patient.getTherapies());
        patient.getTherapies().forEach(therapy -> therapy.setStatus(TherapyStatus.INVALIDATED));
        therapyRepository.saveAll(patient.getTherapies());
        log.debug("Successfully deleted all therapies for employee with ID {}.", patient.getId());
    }

    public Long getNumberOfActiveTherapies() {
        return therapyRepository.count();
    }

    public List<Therapy> getAllAvailableTherapies() {
        log.debug("Fetching all available therapies.");
        return therapyRepository.findAll().stream().filter(therapy -> therapy.getStatus().equals(TherapyStatus.PENDING_APPROVAL))
                                .toList();
    }

    public Therapy findTherapyById(Long therapyId) {
        return therapyRepository.findById(therapyId).orElseThrow(() -> new IllegalArgumentException("Cannot find therapy with given ID."));
    }

    public void saveTherapy(Therapy therapy) {
        log.debug("Saving therapy.");
        therapyRepository.save(therapy);
    }

    public void saveAppointment(Appointment appointment) {
        log.debug("Saving appointment.");
        appointmentRepository.save(appointment);
    }

    public List<Therapy> getAllEmployeeTherapies(Employee employee) {
        log.debug("Fetching all employees therapies.");
        List<Therapy> therapies = therapyRepository.findByEmployeeId(employee.getId());
        return therapies.isEmpty() ? Collections.emptyList() : therapies;
    }

    public void cancelTherapyWithId(Long id) throws IOException {
        log.debug("Canceling therapy.");
        Therapy therapy = findTherapyById(id);
        if (therapy.getStatus().equals(TherapyStatus.CANCELED) || therapy.getStatus().equals(TherapyStatus.INVALIDATED)) {
            throw new IllegalArgumentException("Therapy is already invalidated.");
        }
        String fullPath = therapyScanDir + therapy.getTherapyScan();
        Path filePath = Paths.get(fullPath);

        if (Files.exists(filePath)) {
            Files.delete(filePath);
            log.debug("Successfully deleted PDF scan.");
        }

        therapy.setStatus(TherapyStatus.CANCELED);
        therapyRepository.save(therapy);

        log.debug("Therapy has been canceled.");
    }

    @Transactional
    public List<Therapy> getAllTherapiesForRoomId(Long id) {
        return therapyRepository.findAllByRoomId(id).stream().filter(
                therapy -> therapy.getAppointment().getStartAt().isAfter(LocalDateTime.now())).toList();
    }

    public Resource getTherapyScanForPatientWithTherapyWithId(String username, Long id) throws MalformedURLException {
        log.debug("Fetching PDF for therapy with id: {}", id);

        RehubUser user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("User with given username does not exist."));
        patientRepository.findPatientByUserId(user.getId()).orElseThrow(
                () -> new IllegalArgumentException("User with given user ID does not exist."));
        Therapy therapy = therapyRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Cannot find therapy with given ID"));
        Path pdfPath = Path.of(therapyScanDir + therapy.getTherapyScan());
        log.debug("Successfully fetched PDF from URL. {}", therapyScanDir + therapy.getTherapyScan());
        return new UrlResource(pdfPath.toUri());
    }

    public List<LocalDate> getListOfNonWorkingDaysInCroatia() {
        LocalDate currentDate = LocalDate.now();
        int year = currentDate.getYear();
        List<LocalDate> nonWorkingDates = new ArrayList<>();

        nonWorkingDates.add(LocalDate.of(year, 1, 1));  // New Year's Day
        nonWorkingDates.add(LocalDate.of(year, 5, 1));  // International Workers' Day
        nonWorkingDates.add(LocalDate.of(year, 6, 22)); // Anti-Fascist Struggle Day
        nonWorkingDates.add(LocalDate.of(year, 8, 5));  // Victory and Homeland Thanksgiving Day
        nonWorkingDates.add(LocalDate.of(year, 8, 15)); // Assumption of Mary
        nonWorkingDates.add(LocalDate.of(year, 10, 8)); // Independence Day
        nonWorkingDates.add(LocalDate.of(year, 11, 1)); // All Saints' Day
        nonWorkingDates.add(LocalDate.of(year, 12, 25)); // Christmas Day
        nonWorkingDates.add(LocalDate.of(year, 12, 26)); // St. Stephen's Day

        LocalDate easterDate = calculateEasterDate(year);
        nonWorkingDates.add(easterDate);  // Easter Sunday
        nonWorkingDates.add(easterDate.plusDays(1));  // Easter Monday

        LocalDate corpusChristi = easterDate.plusDays(60);  // Corpus Christi is 60 days after Easter
        nonWorkingDates.add(corpusChristi);

        return nonWorkingDates;
    }

    private static LocalDate calculateEasterDate(int year) {
        int a = year % 19;
        int b = year / 100;
        int c = year % 100;
        int d = b / 4;
        int e = b % 4;
        int f = (b + 8) / 25;
        int g = (b - f + 1) / 3;
        int h = (19 * a + b - d - g + 15) % 30;
        int i = c / 4;
        int k = c % 4;
        int l = (32 + 2 * e + 2 * i - h - k) % 7;
        int m = (a + 11 * h + 22 * l) / 451;
        int month = (h + l - 7 * m + 114) / 31;
        int day = ((h + l - 7 * m + 114) % 31) + 1;

        return LocalDate.of(year, month, day);
    }

}
