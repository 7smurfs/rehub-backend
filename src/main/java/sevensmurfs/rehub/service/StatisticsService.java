package sevensmurfs.rehub.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sevensmurfs.rehub.model.message.response.StatisticsResponse;

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
}
