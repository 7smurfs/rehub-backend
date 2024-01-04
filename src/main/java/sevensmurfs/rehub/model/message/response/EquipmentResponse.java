package sevensmurfs.rehub.model.message.response;

import lombok.Builder;
import lombok.Data;
import sevensmurfs.rehub.enums.EquipmentStatus;
import sevensmurfs.rehub.model.entity.Equipment;

@Data
@Builder
public class EquipmentResponse {

    private Long id;

    private String name;

    private EquipmentStatus status;

    private String specialMessage;

    private Long roomId;

    private String roomLabel;

    public static EquipmentResponse mapEquipmentEntity(Equipment equipment) {
        return EquipmentResponse.builder()
                                .id(equipment.getId())
                                .name(equipment.getName())
                                .specialMessage(equipment.getSpecialMessage())
                                .status(equipment.getStatus())
                                .roomId(equipment.getRoom() != null ? equipment.getRoom().getId() : null)
                                .roomLabel(equipment.getRoom() != null ? equipment.getRoom().getLabel() : null)
                                .build();
    }

}
