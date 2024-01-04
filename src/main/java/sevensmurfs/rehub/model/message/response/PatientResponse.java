package sevensmurfs.rehub.model.message.response;

import lombok.Builder;
import lombok.Data;
import sevensmurfs.rehub.model.entity.Patient;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class PatientResponse {

    private Long id;

    private UserResponse user;

    private String firstName;

    private String lastName;

    private String pin;

    private String phoneNumber;

    private String gender;

    private LocalDate dateOfBirth;

    private List<TherapyResponse> therapies;

    public static PatientResponse mapPatientEntity(Patient patient) {
        return PatientResponse.builder()
                              .id(patient.getId())
                              .user(UserResponse.mapUserEntity(patient.getUser()))
                              .firstName(patient.getFirstName())
                              .lastName(patient.getLastName())
                              .phoneNumber(patient.getPhoneNumber())
                              .gender(patient.getGender())
                              .dateOfBirth(patient.getDateOfBirth())
                              .therapies(patient.getTherapies().stream().map(TherapyResponse::mapTherapyEntity).toList())
                              .build();
    }

    public static PatientResponse mapPatientEntityForTherapy(Patient patient) {
        return PatientResponse.builder()
                              .id(patient.getId())
                              .firstName(patient.getFirstName())
                              .lastName(patient.getLastName())
                              .gender(patient.getGender())
                              .dateOfBirth(patient.getDateOfBirth())
                              .build();
    }
}
