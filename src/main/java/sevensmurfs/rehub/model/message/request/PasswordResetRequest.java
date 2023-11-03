package sevensmurfs.rehub.model.message.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import sevensmurfs.rehub.model.message.request.validator.PasswordResetValidator;

@Data
public class PasswordResetRequest {

    @Null(groups = {PasswordResetValidator.saveNewPassword.class})
    @NotBlank(groups = {PasswordResetValidator.getPasswordResetLink.class})
    private String username;

    @Null(groups = {PasswordResetValidator.getPasswordResetLink.class})
    @NotBlank(groups = {PasswordResetValidator.saveNewPassword.class})
    @Size(min = 8, groups = {PasswordResetValidator.saveNewPassword.class})
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*[0-9]).{8,}$", groups = {PasswordResetValidator.saveNewPassword.class})
    private String newPass;

    @Null(groups = {PasswordResetValidator.getPasswordResetLink.class})
    @NotBlank(groups = {PasswordResetValidator.saveNewPassword.class})
    private String confirmPass;

    @Null(groups = {PasswordResetValidator.getPasswordResetLink.class})
    @NotBlank(groups = {PasswordResetValidator.saveNewPassword.class})
    private String token;

}
