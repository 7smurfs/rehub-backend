package sevensmurfs.rehub.model.message.request.validator;

import jakarta.validation.groups.Default;

public interface PasswordResetValidator {

    interface getPasswordResetLink extends Default {}

    interface saveNewPassword extends Default {}
}
