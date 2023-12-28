package sevensmurfs.rehub.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sevensmurfs.rehub.model.entity.Room;
import sevensmurfs.rehub.model.message.request.RoomRequest;
import sevensmurfs.rehub.repository.RoomRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomService {

    private final RoomRepository roomRepository;

    @Transactional
    public Room createRoom(RoomRequest roomRequest) {
        log.debug("Create room request.");
        if (roomRepository.findByLabel(roomRequest.getLabel()).isPresent()) {
            throw new IllegalArgumentException("Room with given label already exists.");
        }

        Room room = Room.builder()
                        .label(roomRequest.getLabel())
                        .capacity(roomRequest.getCapacity())
                        .status(roomRequest.getStatus())
                        .specialMessage(roomRequest.getSpecialMessage())
                        .build();

        log.debug("Create room request success.");
        return roomRepository.save(room);
    }

    @Transactional
    public void deleteRoom(Long id) {
        log.debug("Deleting room with id {}", id);

        roomRepository.deleteById(id);

        log.debug("Successfully deleted room with id {}", id);
    }

    @Transactional
    public Room getRoomWithId(Long id) {
        log.debug("Fetch room with id.");
        return roomRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Room with id does not exist."));
    }

    public List<Room> getAllRooms() {
        log.debug("Fetch all rooms.");
        return roomRepository.findAll();
    }

    public Long getNumberOfRooms() {
        return roomRepository.count();
    }
}
