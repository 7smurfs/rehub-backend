package sevensmurfs.rehub.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sevensmurfs.rehub.enums.UserStatus;
import sevensmurfs.rehub.model.entity.Employee;
import sevensmurfs.rehub.model.entity.RehubUser;
import sevensmurfs.rehub.model.message.request.UserRequest;
import sevensmurfs.rehub.repository.EmployeeRepository;
import sevensmurfs.rehub.util.SecurityUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final UserService userService;

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
        return employeeRepository.findAll();
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
}
