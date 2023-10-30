package sevensmurfs.rehub.model.message.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import sevensmurfs.rehub.enums.EquipmentStatus;

@Data
public class EquipmentRequest {

    @NotBlank
    private String name;

    @NotNull
    private EquipmentStatus status;

    private String specialMessage;

    private Long roomId;

}
