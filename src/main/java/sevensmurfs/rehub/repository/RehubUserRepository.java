package sevensmurfs.rehub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sevensmurfs.rehub.enums.UserStatus;
import sevensmurfs.rehub.model.entity.RehubUser;

import java.util.Optional;

@Repository
public interface RehubUserRepository extends JpaRepository<RehubUser, Long> {

    Optional<RehubUser> findByUsername(String username);

    Optional<RehubUser> findByUsernameAndStatus(String username, UserStatus status);

}
