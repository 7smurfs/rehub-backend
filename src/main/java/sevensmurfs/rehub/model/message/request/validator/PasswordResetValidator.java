package sevensmurfs.rehub.model.message.request.validator;

import jakarta.validation.groups.Default;

public interface PasswordResetValidator {

    interface PasswordResetLink extends Default {}

    interface ChangePassword extends Default {}

    interface SaveNewPassword extends Default {}
}
