package sevensmurfs.rehub.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sevensmurfs.rehub.model.entity.Equipment;
import sevensmurfs.rehub.model.entity.Room;
import sevensmurfs.rehub.model.message.request.EquipmentRequest;
import sevensmurfs.rehub.repository.EquipmentRepository;
import sevensmurfs.rehub.repository.RoomRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;

    private final RoomRepository roomRepository;

    @Transactional
    public Equipment createEquipment(EquipmentRequest equipmentRequest) {
        log.debug("Create equipment request.");
        Room room = roomRepository.findById(equipmentRequest.getRoomId()).orElseThrow(
                () -> new IllegalArgumentException("Room with given id does not exists."));

        Equipment equipment = Equipment.builder()
                                       .name(equipmentRequest.getName())
                                       .status(equipmentRequest.getStatus())
                                       .specialMessage(equipmentRequest.getSpecialMessage())
                                       .room(room)
                                       .build();

        log.debug("Create equipment request success.");
        return equipmentRepository.save(equipment);
    }

    @Transactional
    public void deleteEquipment(Long id) {

        log.debug("Deleting equipment with id {}", id);

        equipmentRepository.deleteById(id);

        log.debug("Successfully deleted equipment with id {}", id);
    }

    public List<Equipment> getAllEquipment() {
        log.debug("Fethc all equipment.");
        return equipmentRepository.findAll();
    }

    @Transactional
    public Equipment getEquipmentWithId(Long id) {
        log.debug("Fetching equipment with id.");
        return equipmentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Equipment with id does not exist."));
    }
}
