package sevensmurfs.rehub.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sevensmurfs.rehub.model.entity.Employee;
import sevensmurfs.rehub.model.entity.RehubUser;
import sevensmurfs.rehub.model.message.request.UserRequest;
import sevensmurfs.rehub.repository.EmployeeRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final UserService userService;

    @Transactional
    public Employee registerEmployee(UserRequest userRequest) {

        RehubUser user = userService.registerEmployee(userRequest);

        log.debug("Creating employee entity.");
        Employee employee = Employee.builder()
                                    .firstName(userRequest.getFirstName())
                                    .lastName(userRequest.getLastName())
                                    .pin(userRequest.getPin())
                                    .phoneNumber(userRequest.getPhoneNumber())
                                    .dateOfBirth(userRequest.getDateOfBirth())
                                    .profession(userRequest.getProfession())
                                    .gender(userRequest.getGender())
                                    .user(user)
                                    .build();
        log.debug("Saving employee entity.");

        return employeeRepository.save(employee);
    }
}
