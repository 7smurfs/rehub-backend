package sevensmurfs.rehub.model.message.response;

import lombok.Builder;
import lombok.Data;
import sevensmurfs.rehub.model.entity.Faq;

@Data
@Builder
public class FaqResponse {

    private Long id;

    private String question;

    private String answer;

    public static FaqResponse mapFaqEntity(Faq faq) {
        return FaqResponse.builder()
                          .id(faq.getId())
                          .question(faq.getQuestion())
                          .answer(faq.getAnswer())
                          .build();
    }
}
