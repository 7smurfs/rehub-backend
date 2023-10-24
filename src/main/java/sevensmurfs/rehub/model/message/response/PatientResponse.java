package sevensmurfs.rehub.model.message.response;

import lombok.Builder;
import lombok.Data;
import sevensmurfs.rehub.model.entity.Patient;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PatientResponse {

    private Long id;

    private String firstName;

    private String lastName;

    private String pin;

    private String phoneNumber;

    private String gender;

    private LocalDateTime dateOfBirth;

}
