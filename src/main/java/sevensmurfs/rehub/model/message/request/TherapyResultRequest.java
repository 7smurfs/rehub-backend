package sevensmurfs.rehub.model.message.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TherapyResultRequest {

    @NotNull
    private Long therapyId;

    @NotBlank
    private String result;

    @NotBlank
    private String status;
}
