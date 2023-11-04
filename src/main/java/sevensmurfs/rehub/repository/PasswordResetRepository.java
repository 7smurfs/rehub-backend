package sevensmurfs.rehub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sevensmurfs.rehub.model.entity.PasswordReset;

import java.util.Optional;

public interface PasswordResetRepository extends JpaRepository<PasswordReset, Long> {

    Optional<PasswordReset> findByToken(String token);
}
