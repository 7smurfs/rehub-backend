package sevensmurfs.rehub.model.message.response;

import lombok.Builder;
import lombok.Data;
import sevensmurfs.rehub.model.entity.TherapyResult;

@Data
@Builder
public class TherapyResultResponse {

    private Long id;

    private Long therapyId;

    private String result;

    private String status;

    private String patientName;

    public static TherapyResultResponse mapTherapyResultEntity(TherapyResult therapyResult) {
        return TherapyResultResponse.builder()
                                    .id(therapyResult.getId())
                                    .therapyId(therapyResult.getTherapy().getId())
                                    .result(therapyResult.getResult())
                                    .patientName(therapyResult.getTherapy().getPatient().getFirstName() + " " +
                                                 therapyResult.getTherapy().getPatient().getLastName())
                                    .status(therapyResult.getStatus())
                                    .build();
    }
}
