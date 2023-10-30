package sevensmurfs.rehub.model.message.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import sevensmurfs.rehub.enums.RoomStatus;

@Data
public class RoomRequest {

    @NotBlank
    private String label;

    @NotNull
    @Min(1)
    private Integer capacity;

    @NotNull
    private RoomStatus status;

    private String specialMessage;

}
