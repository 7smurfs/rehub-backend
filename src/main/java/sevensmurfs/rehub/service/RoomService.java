package sevensmurfs.rehub.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sevensmurfs.rehub.enums.RoomStatus;
import sevensmurfs.rehub.model.entity.Room;
import sevensmurfs.rehub.model.message.request.RoomRequest;
import sevensmurfs.rehub.repository.EquipmentRepository;
import sevensmurfs.rehub.repository.RoomRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomService {

    private final RoomRepository roomRepository;

    private final EquipmentRepository equipmentRepository;

    @Transactional
    public Room createRoom(RoomRequest roomRequest) {
        log.debug("Create room request.");
        if (roomRepository.findByLabel(roomRequest.getLabel()).isPresent()) {
            throw new IllegalArgumentException("Room with given label already exists.");
        }

        Room room = Room.builder()
                        .label(roomRequest.getLabel())
                        .capacity(roomRequest.getCapacity())
                        .status(RoomStatus.OPERABLE)
                        .specialMessage(roomRequest.getSpecialMessage())
                        .build();

        log.debug("Create room request success.");
        return roomRepository.save(room);
    }

    @Transactional
    public void deleteRoom(Long id) {
        log.debug("Deleting room with id {}", id);

        Room room = roomRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Room with id does not exist."));
        if (room.getEquipment().isEmpty()) {
            roomRepository.delete(room);
        } else {
            room.getEquipment().forEach(equipment -> {
                equipment.setRoom(null);
                equipmentRepository.save(equipment);
            });
            roomRepository.delete(room);
        }
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

    public void setRoomAsOperable(Long id) {
        log.debug("Setting room as operable.");
        Room room = roomRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Room with id does not exist"));
        if (room.getStatus().equals(RoomStatus.OUT_OF_OPERATION))
            room.setStatus(RoomStatus.OPERABLE);
        else
            throw new IllegalArgumentException("Room might be in use at the moment.");
        roomRepository.save(room);
        log.debug("Room set as operable.");
    }

    public void setRoomAsInOperable(Long id) {
        log.debug("Setting room as inoperable.");
        Room room = roomRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Room with id does not exist"));
        if (room.getStatus().equals(RoomStatus.OPERABLE))
            room.setStatus(RoomStatus.OUT_OF_OPERATION);
        else
            throw new IllegalArgumentException("Room might be in use at the moment.");
        roomRepository.save(room);
        log.debug("Room set as operable.");
    }
}
