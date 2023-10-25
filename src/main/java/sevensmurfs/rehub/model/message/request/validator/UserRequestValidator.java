package sevensmurfs.rehub.model.message.request.validator;

import jakarta.validation.groups.Default;

public interface UserRequestValidator {

    interface Login extends Default {}

    interface Register extends Default {}

    interface Employee extends Default {}

    interface Patient extends Default {}

    interface Update extends Default {}

    interface Delete extends Default {}

}
