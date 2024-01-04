package sevensmurfs.rehub.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sevensmurfs.rehub.enums.TherapyStatus;
import sevensmurfs.rehub.enums.UserStatus;
import sevensmurfs.rehub.model.entity.Appointment;
import sevensmurfs.rehub.model.entity.Employee;
import sevensmurfs.rehub.model.entity.RehubUser;
import sevensmurfs.rehub.model.entity.Therapy;
import sevensmurfs.rehub.model.message.request.AppointmentRequest;
import sevensmurfs.rehub.model.message.request.UserRequest;
import sevensmurfs.rehub.repository.EmployeeRepository;
import sevensmurfs.rehub.util.SecurityUtil;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final TherapyService therapyService;

    private final UserService userService;

    private final RoomService roomService;

    private final EmailService emailService;

    @Transactional
    public Employee registerEmployee(UserRequest userRequest) throws Exception {

        RehubUser user = userService.registerEmployee(userRequest);

        log.debug("Creating employee entity.");
        Employee employee = Employee.builder()
                                    .firstName(userRequest.getFirstName())
                                    .lastName(userRequest.getLastName())
                                    .phoneNumber(userRequest.getPhoneNumber())
                                    .profession(userRequest.getProfession())
                                    .gender(userRequest.getGender())
                                    .user(user)
                                    .build();
        log.debug("Saving employee entity.");

        return employeeRepository.save(employee);
    }

    public List<Employee> getAllEmployees() {
        log.debug("Fetching all employees.");
        List<Employee> allEmployees = employeeRepository.findAll();
        return allEmployees.stream().filter(employee -> employee.getUser().getStatus().equals(UserStatus.ACTIVE)).toList();
    }

    public long getNumberOfEmployees() {
        return employeeRepository.count();
    }

    @Transactional
    public void invalidateEmployeeWithId(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Employee with given ID does not exist."));
        if (employee.getUser().getStatus().equals(UserStatus.INVALIDATED))
            throw new IllegalArgumentException("User is already invalidate.");
        log.debug("Invalidating employee with ID {}.", employee.getId());
        employee.getUser().setStatus(UserStatus.INVALIDATED);
        userService.saveUser(employee.getUser());

        log.debug("Successfully invalidate employee with ID {}.", employee.getId());
    }

    public Long getNumberOfNewEmployeesForMonth() {
        return employeeRepository.countAllByCreatedAtAfter(LocalDateTime.now().minusMonths(1));
    }

    public void setEmployeeAsAdmin(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Employee with given ID does not exist."));
        if (employee.getUser().getStatus().equals(UserStatus.INVALIDATED))
            throw new IllegalArgumentException("User is already invalidate.");
        log.debug("Setting employee  with ID {} as ADMIN.", employee.getId());
        userService.giveAdminToUser(employee.getUser());
        log.debug("ADMIN successfully given to employee.");
    }

    public List<Therapy> getAllTherapies() {
        log.debug("Fetching all therapies.");

        return therapyService.getAllAvailableTherapies();
    }

    @Transactional
    public void setAppointmentForTherapy(AppointmentRequest appointmentRequest, String username) throws Exception {

        log.debug("Setting new appointment for therapy with ID: {}", appointmentRequest.getTherapyId());
        RehubUser user = userService.findUserByUsername(username);
        Employee employee = employeeRepository.findEmployeeByUserId(user.getId()).orElseThrow(
                () -> new IllegalArgumentException("Cannot find employee with user ID: " + user.getId()));

        Therapy therapy = therapyService.findTherapyById(appointmentRequest.getTherapyId());
        this.validateAppointmentTime(appointmentRequest);
        Appointment appointment = Appointment.builder()
                                             .startAt(appointmentRequest.getStartAt())
                                             .endAt(appointmentRequest.getEndAt())
                                             .therapy(therapy)
                                             .build();
        therapy.setAppointment(appointment);
        therapy.setStatus(TherapyStatus.APPROVED);
        therapy.setRoom(roomService.getRoomWithId(appointmentRequest.getRoomId()));
        therapy.setEmployee(employee);

        therapyService.saveTherapy(therapy);
        therapyService.saveAppointment(appointment);

        emailService.sendTherapyConfirmedNotification(SecurityUtil.decryptUsername(therapy.getPatient().getUser().getUsername()),
                                                      therapy.getPatient(),
                                                      therapy,
                                                      appointment.getStartAt().toLocalDate(),
                                                      appointment.getStartAt(),
                                                      appointment.getEndAt());

        log.debug("Saved appointment.");
    }

    private void validateAppointmentTime(AppointmentRequest appointmentRequest) {
        log.debug("Validating appointment start and end time.");

        if (appointmentRequest.getStartAt().isAfter(appointmentRequest.getEndAt()))
            throw new IllegalArgumentException("Invalid appointment time. Start date needs to be before end date.");

        if (appointmentRequest.getStartAt().getHour() < 8)
            throw new IllegalArgumentException("Invalid appointment time. Start date needs to be after opening hours.");

        if ((appointmentRequest.getEndAt().getHour() == 20 && appointmentRequest.getEndAt().getMinute() > 0)
            || (appointmentRequest.getEndAt().getHour() > 20))
            throw new IllegalArgumentException("Invalid appointment time. End date needs to be before closing hours.");

        if (!appointmentRequest.getStartAt().toLocalDate().equals(appointmentRequest.getEndAt().toLocalDate()))
            throw new IllegalArgumentException("Invalid appointment time. Start and end dates needs to be on the same date.");
    }

    public Employee findEmployeeByUserId(Long id) {
        return employeeRepository.findEmployeeByUserId(id).orElseThrow(
                () -> new IllegalArgumentException("Cannot find employee with user ID: " + id.toString()));
    }

    public List<Therapy> getEmployeesTherapies(String username) {
        log.debug("Fetching employees therapies.");

        RehubUser user = userService.findUserByUsername(username);
        Employee employee = employeeRepository.findEmployeeByUserId(user.getId()).orElseThrow(
                () -> new IllegalArgumentException("Cannot find employee with user ID: " + user.getId()));

        return therapyService.getAllEmployeeTherapies(employee);
    }
}
