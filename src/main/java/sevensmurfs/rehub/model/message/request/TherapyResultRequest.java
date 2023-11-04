package sevensmurfs.rehub.model.message.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TherapyResultRequest {

    @NotBlank
    private Long therapyId;

    @NotBlank
    private String result;

    @NotBlank
    private String status;
}
