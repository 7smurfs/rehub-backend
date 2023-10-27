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

    private DoctorResponse doctor;

    private TherapyResultResponse therapyResultResponse;

    public static TherapyResponse mapTherapyEntity(Therapy therapy) {
        return TherapyResponse.builder()
                              .id(therapy.getId())
                              .type(therapy.getType())
                              .request(therapy.getRequest())
                              .status(therapy.getStatus())
                              .referenceTherapyId(therapy.getRefId())
                              .roomLabel(therapy.getRoom().getLabel())
                              .startAt(therapy.getAppointment().getStartAt())
                              .endAt(therapy.getAppointment().getEndAt())
                              .doctor(DoctorResponse.mapDoctorEntity(therapy.getDoctor()))
                              .therapyResultResponse(TherapyResultResponse.mapTherapyResultEntity(therapy.getTherapyResult()))
                              .build();
    }
}