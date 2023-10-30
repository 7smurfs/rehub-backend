package sevensmurfs.rehub.model.message.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;

@Data
public class FaqRequest {

    @NotNull
    private String question;

    @NotNull
    private String answer;
}
