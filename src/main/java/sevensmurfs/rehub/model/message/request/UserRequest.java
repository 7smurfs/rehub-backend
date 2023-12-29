package sevensmurfs.rehub.model.message.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import sevensmurfs.rehub.model.message.request.validator.UserRequestValidator;

import java.time.LocalDate;

@Data
public class UserRequest {

    @NotNull(groups = {UserRequestValidator.Delete.class})
    @Null(groups = {UserRequestValidator.Login.class, UserRequestValidator.Register.class})
    private Long id;

    @NotBlank(groups = {UserRequestValidator.Register.class, UserRequestValidator.Employee.class})
    @Null(groups = {UserRequestValidator.Login.class})
    private String firstName;

    @NotBlank(groups = {UserRequestValidator.Register.class, UserRequestValidator.Employee.class})
    @Null(groups = {UserRequestValidator.Login.class})
    private String lastName;

    @NotBlank(groups = {UserRequestValidator.Register.class, UserRequestValidator.Login.class, UserRequestValidator.Employee.class})
    @Email(groups = {UserRequestValidator.Register.class, UserRequestValidator.Login.class, UserRequestValidator.Employee.class})
    private String username;

    @Null(groups = UserRequestValidator.Employee.class)
    @NotBlank(groups = {UserRequestValidator.Register.class, UserRequestValidator.Login.class, UserRequestValidator.Delete.class})
    @Size(min = 8, groups = {UserRequestValidator.Register.class})
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*[0-9]).{8,}$", groups = {UserRequestValidator.Register.class, UserRequestValidator.Update.class})
    private String password;

    @Null(groups = UserRequestValidator.Employee.class)
    @NotNull(groups = {UserRequestValidator.Register.class})
    private String confirmPassword;

    @NotNull(groups = {UserRequestValidator.Register.class, UserRequestValidator.Employee.class})
    @Null(groups = {UserRequestValidator.Update.class})
    @Pattern(regexp = "^[0-9]{11}$", groups = {UserRequestValidator.Register.class, UserRequestValidator.Employee.class})
    private String pin;

    @NotNull(groups = {UserRequestValidator.Patient.class})
    @Null(groups = {UserRequestValidator.Update.class, UserRequestValidator.Employee.class})
    @Pattern(regexp = "^[0-9]{9}$")
    private String phin;

    @NotNull(groups = {UserRequestValidator.Register.class, UserRequestValidator.Employee.class})
    @Pattern(regexp = "^\\+3859(1|2|5|8|9|76|77)\\d{6,7}$", groups = {UserRequestValidator.Register.class, UserRequestValidator.Employee.class})
    private String phoneNumber;

    @NotBlank(groups = {UserRequestValidator.Employee.class})
    private String profession;

    private String gender;

    @NotNull(groups = {UserRequestValidator.Register.class, UserRequestValidator.Employee.class})
    private LocalDate dateOfBirth;
}
