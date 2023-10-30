package sevensmurfs.rehub.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sevensmurfs.rehub.model.entity.Therapy;
import sevensmurfs.rehub.model.entity.TherapyResult;
import sevensmurfs.rehub.model.message.request.TherapyResultRequest;
import sevensmurfs.rehub.repository.TherapyRepository;
import sevensmurfs.rehub.repository.TherapyResultRepository;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class TherapyResultService {

    private final TherapyResultRepository therapyResultRepository;
    private final TherapyRepository therapyRepository;

    @Transactional
    public void deleteAllTherapyResultsForTherapies(List<Therapy> therapies) {
        log.debug("Deleting all therapy results.");
        List<TherapyResult> therapyResults = therapies.stream().map(Therapy::getTherapyResult).filter(Objects::nonNull).toList();

        if (!therapyResults.isEmpty())
            therapyResultRepository.deleteAll(therapyResults);

        log.debug("Successfully deleted all therapy results.");
    }

    @Transactional
    public void writeTherapyResult(TherapyResultRequest request) {
        // Find the therapy by ID
        Therapy therapy = therapyRepository.findById(request.getTherapyId())
                                           .orElseThrow(() -> new EntityNotFoundException("Therapy not found"));

        // Create a new TherapyResult entity
        TherapyResult therapyResult = new TherapyResult();
        therapyResult.setResult(request.getResult());

        // Set the therapy result for the therapy
        therapy.setTherapyResult(therapyResult);
        therapy.setStatus(request.getStatus());

        // Save the therapy result and update the therapy
        therapyResultRepository.save(therapyResult);
        therapyRepository.save(therapy);

    }
}
