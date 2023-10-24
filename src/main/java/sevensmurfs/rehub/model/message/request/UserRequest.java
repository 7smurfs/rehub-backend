package sevensmurfs.rehub.model.message.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import sevensmurfs.rehub.model.message.request.validator.UserRequestValidator;

import java.time.LocalDateTime;

@Data
public class UserRequest {

    @NotNull(groups = {UserRequestValidator.Delete.class})
    @Null(groups = {UserRequestValidator.Login.class, UserRequestValidator.Register.class})
    private Long id;

    @NotNull(groups = {UserRequestValidator.Register.class})
    @Null(groups = {UserRequestValidator.Login.class})
    private String firstName;

    @NotNull(groups = {UserRequestValidator.Register.class})
    @Null(groups = {UserRequestValidator.Login.class})
    private String lastName;

    @NotNull(groups = {UserRequestValidator.Register.class, UserRequestValidator.Login.class})
    @Email(groups = {UserRequestValidator.Register.class, UserRequestValidator.Login.class})
    private String username;

    @NotNull(groups = {UserRequestValidator.Register.class, UserRequestValidator.Login.class, UserRequestValidator.Delete.class})
    @Size(min = 8, groups = {UserRequestValidator.Register.class})
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*[0-9]).{8,}$", groups = {UserRequestValidator.Register.class, UserRequestValidator.Update.class})
    private String password;

    @NotNull(groups = {UserRequestValidator.Register.class})
    private String confirmPassword;

    @NotNull(groups = {UserRequestValidator.Register.class})
    @Null(groups = {UserRequestValidator.Update.class})
    @Pattern(regexp = "^[0-9]{11}$", groups = {UserRequestValidator.Register.class})
    private String pin;

    @NotNull(groups = {UserRequestValidator.Register.class})
    @Pattern(regexp = "^\\+3859(1|2|5|8|9|76|77)\\d{6,7}$")
    private String phoneNumber;

    @NotNull(groups = {UserRequestValidator.Employee.class})
    private String profession;

    private String gender;

    @NotNull(groups = {UserRequestValidator.Register.class})
    private LocalDateTime dateOfBirth;
}
