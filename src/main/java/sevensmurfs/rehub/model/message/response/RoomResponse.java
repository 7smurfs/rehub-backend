package sevensmurfs.rehub.model.message.response;

import lombok.Builder;
import lombok.Data;
import sevensmurfs.rehub.enums.RoomStatus;
import sevensmurfs.rehub.model.entity.Room;

import java.util.List;

@Data
@Builder
public class RoomResponse {

    private Long id;

    private String label;

    private Integer capacity;

    private RoomStatus status;

    private String specialMessage;

    private List<EquipmentResponse> equipment;

    public static RoomResponse mapRoomEntity(Room room) {
        return RoomResponse.builder()
                           .id(room.getId())
                           .label(room.getLabel())
                           .capacity(room.getCapacity())
                           .status(room.getStatus())
                           .specialMessage(room.getSpecialMessage())
                           .equipment(room.getEquipment().stream().map(EquipmentResponse::mapEquimentEntity).toList())
                           .build();
    }
}
