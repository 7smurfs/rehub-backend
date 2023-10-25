package sevensmurfs.rehub.model.message.response;

import lombok.Builder;
import lombok.Data;
import sevensmurfs.rehub.model.entity.Doctor;

@Data
@Builder
public class DoctorResponse {

    private Long id;

    private String firstName;

    private String lastName;

    private String speciality;

    public static DoctorResponse mapDoctorEntity(Doctor doctor) {
        return DoctorResponse.builder()
                             .id(doctor.getId())
                             .firstName(doctor.getFirstName())
                             .lastName(doctor.getLastName())
                             .speciality(doctor.getSpeciality())
                             .build();
    }
}
