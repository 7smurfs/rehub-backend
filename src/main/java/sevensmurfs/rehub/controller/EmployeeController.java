package sevensmurfs.rehub.controller;

import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sevensmurfs.rehub.model.entity.Employee;
import sevensmurfs.rehub.model.message.request.UserRequest;
import sevensmurfs.rehub.model.message.request.validator.UserRequestValidator;
import sevensmurfs.rehub.model.message.response.EmployeeResponse;
import sevensmurfs.rehub.service.EmployeeService;

import java.util.List;

@RestController
@RequestMapping("/v1/employee")
@RequiredArgsConstructor
@Slf4j
public class EmployeeController {

    public final EmployeeService employeeService;

    /**
     * Employee registration request POST > /api/v1/employee
     */
    @PostMapping
    @RolesAllowed("ROLE_ADMIN")
    public ResponseEntity<Object> register(@Validated({UserRequestValidator.Register.class,
                                                       UserRequestValidator.Employee.class}) @RequestBody UserRequest userRequest) {

        log.info(" > > > POST /api/v1/employee (Employee register request)");

        Employee employee = employeeService.registerEmployee(userRequest);

        log.info(" < < < POST /api/v1/employee (Employee register request successful)");

        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
                                                                 .path("/{employeeId}")
                                                                 .buildAndExpand(employee.getId())
                                                                 .toUri())
                             .build();
    }

    /**
     * Get all employees ADMIN request GET > /api/v1/employee
     */
    @GetMapping
    @RolesAllowed("ROLE_ADMIN")
    public ResponseEntity<Object> getAllEmployees() {

        log.info(" > > > GET /api/v1/employee (Get all employees ADMIN request)");

        List<Employee> employees = employeeService.getAllEmployees();

        log.info(" < < < GET /api/v1/employee (Get all employees ADMIN success)");

        return ResponseEntity.ok(employees.stream().map(EmployeeResponse::mapEmployeeEntity).toList());
    }

    /**
     * Delete employee ADMIN request DELETE > /api/v1/employee/:id
     */
    @DeleteMapping("/{id}")
    @RolesAllowed("ROLE_ADMIN")
    public ResponseEntity<Object> deleteEmployee(@PathVariable(name = "id") Long id) {

        log.info(" > > > DELETE /api/v1/employee/{} (Delete employee ADMIN request)", id);

        employeeService.deleteEmployeeWithId(id);

        log.info(" < < < DELETE /api/v1/employee/{} (Delete employee ADMIN success)", id);

        return ResponseEntity.noContent().build();
    }
}
