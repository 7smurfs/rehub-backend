package sevensmurfs.rehub.model.message.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import sevensmurfs.rehub.enums.TherapyStatus;

import java.time.LocalDateTime;

@Data
public class TherapyRequest {

    @NotBlank
    private String type;

    @NotBlank
    private String request;

    @NotNull
    private Long patientId;

    @NotNull
    private TherapyStatus status;

    @NotBlank
    private String doctorFirstName;

    @NotBlank
    private String doctorLastName;
}
