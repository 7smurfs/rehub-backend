package sevensmurfs.rehub.enums;

public enum PasswordResetStatus {

    /**
     * Waiting for user to reset password
     */
    RESET_PENDING,

    /**
     * User has successfully reset his password
     */
    RESET_ACCEPTED,

    /**
     * Password reset token has expired
     */
    RESET_EXPIRED;
}
