package sevensmurfs.rehub.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sevensmurfs.rehub.model.entity.Faq;
import sevensmurfs.rehub.model.message.request.FaqRequest;
import sevensmurfs.rehub.repository.FaqRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FaqService {

    private final FaqRepository faqRepository;

    @Transactional
    public Faq addFaq(FaqRequest newFaq) {

        log.debug("Creating faq entity.");

        Faq faq = Faq.builder()
                     .question(newFaq.getQuestion())
                     .answer(newFaq.getAnswer())
                     .build();

        log.debug("Saving faq entity.");

        return faqRepository.save(faq);
    }

    public List<Faq> getAllFaq() {
        log.debug("Fetching all faq.");
        return faqRepository.findAll();
    }

    @Transactional
    public void deleteFaqWithId(Long id) {
        Faq faq = faqRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Faq with given ID does not exist."));
        log.debug("Deleting faq with ID {}.", id);

        faqRepository.delete(faq);

        log.debug("Successfully delete faq with ID {}.", id);
    }
}
