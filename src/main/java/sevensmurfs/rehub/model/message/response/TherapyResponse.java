package sevensmurfs.rehub.model.message.response;

import lombok.Builder;
import lombok.Data;
import sevensmurfs.rehub.enums.TherapyStatus;
import sevensmurfs.rehub.model.entity.Therapy;

import java.time.LocalDateTime;

@Data
@Builder
public class TherapyResponse {

    private Long id;

    private String type;

    private String request;

    private TherapyStatus status;

    private Long referenceTherapyId;

    private String roomLabel;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    private PatientResponse patientResponse;

    private TherapyResultResponse therapyResultResponse;

    public static TherapyResponse mapTherapyEntity(Therapy therapy) {
        return TherapyResponse.builder()
                              .id(therapy.getId())
                              .type(therapy.getType())
                              .request(therapy.getRequest())
                              .status(therapy.getStatus())
                              .referenceTherapyId(therapy.getRefId())
                              .roomLabel(therapy.getRoom() != null ? therapy.getRoom().getLabel() : null)
                              .startAt(therapy.getAppointment() != null ? therapy.getAppointment().getStartAt() : null)
                              .endAt(therapy.getAppointment() != null ? therapy.getAppointment().getEndAt() : null)
                              .patientResponse(PatientResponse.mapPatientEntityForTherapy(therapy.getPatient()))
                              .therapyResultResponse(therapy.getTherapyResult() != null ?
                                                     TherapyResultResponse.mapTherapyResultEntity(therapy.getTherapyResult()) :
                                                     null)
                              .build();
    }
}
