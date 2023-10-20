package sevensmurfs.rehub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sevensmurfs.rehub.model.entity.RehubUser;


@Repository
public interface RehubUserRepository extends JpaRepository<RehubUser, Long> {

}
