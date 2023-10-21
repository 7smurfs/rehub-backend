package sevensmurfs.rehub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sevensmurfs.rehub.model.entity.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

}
