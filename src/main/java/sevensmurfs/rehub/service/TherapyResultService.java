package sevensmurfs.rehub.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sevensmurfs.rehub.model.entity.Therapy;
import sevensmurfs.rehub.model.entity.TherapyResult;
import sevensmurfs.rehub.repository.TherapyResultRepository;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class TherapyResultService {

    private final TherapyResultRepository therapyResultRepository;

    @Transactional
    public void deleteAllTherapyResultsForTherapies(List<Therapy> therapies) {
        log.debug("Deleting all therapy results.");
        List<TherapyResult> therapyResults = therapies.stream().map(Therapy::getTherapyResult).filter(Objects::nonNull).toList();

        if (!therapyResults.isEmpty())
            therapyResultRepository.deleteAll(therapyResults);

        log.debug("Successfully deleted all therapy results.");
    }
}
