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

    public static EquipmentResponse mapEquimentEntity(Equipment equipment) {
        return EquipmentResponse.builder()
                                .id(equipment.getId())
                                .name(equipment.getName())
                                .specialMessage(equipment.getSpecialMessage())
                                .status(equipment.getStatus())
                                .build();
    }
}
