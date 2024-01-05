package sevensmurfs.rehub.model.message.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import sevensmurfs.rehub.model.message.request.validator.PasswordResetValidator;

@Data
public class PasswordResetRequest {

    @Null(groups = {PasswordResetValidator.SaveNewPassword.class, PasswordResetValidator.ChangePassword.class})
    @NotBlank(groups = {PasswordResetValidator.PasswordResetLink.class})
    private String username;

    @Null(groups = {PasswordResetValidator.PasswordResetLink.class, PasswordResetValidator.SaveNewPassword.class})
    @NotBlank(groups = PasswordResetValidator.ChangePassword.class)
    private String oldPass;

    @Null(groups = {PasswordResetValidator.PasswordResetLink.class})
    @NotBlank(groups = {PasswordResetValidator.SaveNewPassword.class, PasswordResetValidator.ChangePassword.class})
    @Size(min = 8, groups = {PasswordResetValidator.SaveNewPassword.class})
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*[0-9]).{8,}$",
             groups = {PasswordResetValidator.SaveNewPassword.class, PasswordResetValidator.ChangePassword.class})
    private String newPass;

    @Null(groups = {PasswordResetValidator.PasswordResetLink.class})
    @NotBlank(groups = {PasswordResetValidator.SaveNewPassword.class, PasswordResetValidator.ChangePassword.class})
    private String confirmPass;

    @Null(groups = {PasswordResetValidator.PasswordResetLink.class, PasswordResetValidator.ChangePassword.class})
    @NotBlank(groups = {PasswordResetValidator.SaveNewPassword.class})
    private String token;
}
