package sevensmurfs.rehub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sevensmurfs.rehub.model.entity.Faq;

@Repository
public interface FaqRepository extends JpaRepository<Faq, Long> {

}
