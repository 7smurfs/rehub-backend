package sevensmurfs.rehub.model.message.response;

import lombok.Builder;
import lombok.Data;
import sevensmurfs.rehub.model.entity.TherapyResult;

@Data
@Builder
public class TherapyResultResponse {

    private Long id;

    private String result;

    private String status;

    public static TherapyResultResponse mapTherapyResultEntity(TherapyResult therapyResult) {
        return TherapyResultResponse.builder()
                                    .id(therapyResult.getId())
                                    .result(therapyResult.getResult())
                                    .status(therapyResult.getStatus())
                                    .build();
    }
}
