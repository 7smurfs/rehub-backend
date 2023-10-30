package sevensmurfs.rehub.model.message.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import sevensmurfs.rehub.enums.TherapyStatus;

@Data
public class TherapyResultRequest {

    @NotNull
    private Long therapyId;

    @NotNull
    private String result;

    private TherapyStatus status;
}
