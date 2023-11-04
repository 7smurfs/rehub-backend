package sevensmurfs.rehub.model.message.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TherapyResultRequest {

    @NotBlank
    private Long therapyId;

    @NotBlank
    private String result;

    @NotNull
    private String status;
}
