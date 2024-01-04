package sevensmurfs.rehub.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sevensmurfs.rehub.enums.Role;
import sevensmurfs.rehub.model.entity.Employee;
import sevensmurfs.rehub.model.entity.Patient;
import sevensmurfs.rehub.model.entity.RehubUser;
import sevensmurfs.rehub.model.entity.UserRole;
import sevensmurfs.rehub.model.message.response.StatisticsResponse;
import sevensmurfs.rehub.model.message.response.UserResponse;
import sevensmurfs.rehub.util.SecurityUtil;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticsService {

    private final EmployeeService employeeService;

    private final PatientService patientService;

    private final RoomService roomService;

    private final EquipmentService equipmentService;

    private final TherapyService therapyService;

    public StatisticsResponse getMainStatistics() {

        log.debug("Fetching all statistics.");
        StatisticsResponse.StatisticsResponseBuilder statBuilder = StatisticsResponse.builder();

        statBuilder.noOfEmployees(employeeService.getNumberOfEmployees());
        statBuilder.noOfPatients(patientService.getNumberOfPatients());
        statBuilder.noOfRooms(roomService.getNumberOfRooms());
        statBuilder.noOfEquipment(equipmentService.getNumberOfEquipment());
        statBuilder.noOfActiveTherapies(therapyService.getNumberOfActiveTherapies());
        Long newPatientsForMonth = patientService.getNumberOfNewPatientsForMonth();
        Long newEmployeesForMonth = employeeService.getNumberOfNewEmployeesForMonth();
        statBuilder.noOfNewUsersForThisMonth(Long.sum(newEmployeesForMonth, newPatientsForMonth));

        log.debug("Statistics success.");
        return statBuilder.build();
    }

    public UserResponse getUserStatsAndInfo(RehubUser user) throws Exception {
        log.debug("Gathering user stats and info.");

        UserResponse.UserResponseBuilder responseBuilder = UserResponse.builder();
        responseBuilder.id(user.getId());
        responseBuilder.roles(user.getRoles().stream().map(UserRole::getName).collect(Collectors.toList()));
        responseBuilder.username(SecurityUtil.decryptUsername(user.getUsername()));

        if (user.getRoles().stream().anyMatch(userRole -> userRole.getName().equals(Role.EMPLOYEE))) {
            Employee employee = employeeService.findEmployeeByUserId(user.getId());
            responseBuilder.firstName(employee.getFirstName());
            responseBuilder.lastName(employee.getLastName());
            return responseBuilder.build();
        }
        if (user.getRoles().stream().anyMatch(userRole -> userRole.getName().equals(Role.PATIENT))) {
            Patient patient = patientService.findPatientByUserId(user.getId());
            responseBuilder.firstName(patient.getFirstName());
            responseBuilder.lastName(patient.getLastName());
            return responseBuilder.build();
        }
        if (user.getRoles().stream().anyMatch(userRole -> userRole.getName().equals(Role.SUPERADMIN))) {
            return responseBuilder.build();
        }
        throw new IllegalArgumentException("Unexpected error occured.");
    }
}
