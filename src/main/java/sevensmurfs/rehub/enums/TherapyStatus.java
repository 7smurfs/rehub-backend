package sevensmurfs.rehub.enums;

public enum TherapyStatus {

    /**
     * Therapy has been approved by employee
     */
    APPROVED,

    /**
     * Therapy is pending approval from employee
     */
    PENDING_APPROVAL,

    /**
     * Therapy has been canceled by employee or patient
     */
    CANCELED,

    /**
     * There is no available appointment for this therapy
     */
    NO_AVAILABLE_APPOINTMENT
    ;
}
