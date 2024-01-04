package sevensmurfs.rehub.model.message.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import sevensmurfs.rehub.enums.TherapyStatus;

@Data
public class TherapyRequest {

    @NotBlank
    private String type;

    @NotBlank
    private String request;

    @NotBlank
    private String doctorFirstName;

    @NotBlank
    private String doctorLastName;

    private Long referenceId;
}
