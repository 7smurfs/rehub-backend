package sevensmurfs.rehub.model.message.response;

import lombok.Builder;
import lombok.Data;
import sevensmurfs.rehub.model.entity.Employee;

import java.time.LocalDate;

@Data
@Builder
public class EmployeeResponse {

    private Long id;

    private UserResponse user;

    private String firstName;

    private String lastName;

    private String pin;

    private String phoneNumber;

    private String gender;

    private String profession;

    private LocalDate dateOfBirth;

    public static EmployeeResponse mapEmployeeEntity(Employee employee) {
        return EmployeeResponse.builder()
                               .id(employee.getId())
                               .user(UserResponse.mapUserEntity(employee.getUser()))
                               .firstName(employee.getFirstName())
                               .lastName(employee.getLastName())
                               .pin(employee.getPin())
                               .phoneNumber(employee.getPhoneNumber())
                               .gender(employee.getGender())
                               .profession(employee.getProfession())
                               .dateOfBirth(employee.getDateOfBirth())
                               .build();
    }
}
