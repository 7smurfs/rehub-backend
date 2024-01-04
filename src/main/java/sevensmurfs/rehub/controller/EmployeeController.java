package sevensmurfs.rehub.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sevensmurfs.rehub.model.entity.Employee;
import sevensmurfs.rehub.model.entity.Equipment;
import sevensmurfs.rehub.model.entity.Room;
import sevensmurfs.rehub.model.entity.Therapy;
import sevensmurfs.rehub.model.message.request.AppointmentRequest;
import sevensmurfs.rehub.model.message.request.EquipmentRequest;
import sevensmurfs.rehub.model.message.request.RoomRequest;
import sevensmurfs.rehub.model.message.request.UserRequest;
import sevensmurfs.rehub.model.message.request.validator.UserRequestValidator;
import sevensmurfs.rehub.model.message.response.EmployeeResponse;
import sevensmurfs.rehub.model.message.response.EquipmentResponse;
import sevensmurfs.rehub.model.message.response.RoomResponse;
import sevensmurfs.rehub.model.message.response.TherapyResponse;
import sevensmurfs.rehub.security.JwtGenerator;
import sevensmurfs.rehub.service.EmployeeService;
import sevensmurfs.rehub.service.EquipmentService;
import sevensmurfs.rehub.service.RoomService;
import sevensmurfs.rehub.util.SecurityUtil;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/v1/employee")
@RequiredArgsConstructor
@Slf4j
public class EmployeeController {

    private final EmployeeService employeeService;

    private final RoomService roomService;

    private final EquipmentService equipmentService;

    private final JwtGenerator jwtGenerator;

    /**
     * Employee registration request POST > /api/v1/employee
     */
    @PostMapping
    @RolesAllowed("ROLE_SUPERADMIN")
    public ResponseEntity<Object> register(@Validated(UserRequestValidator.Employee.class) @RequestBody UserRequest userRequest)
            throws Exception {

        log.info(" > > > POST /api/v1/employee (Employee register request)");

        Employee employee = employeeService.registerEmployee(userRequest);

        log.info(" < < < POST /api/v1/employee (Employee register request successful)");

        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
                                                                 .path("/{employeeId}")
                                                                 .buildAndExpand(employee.getId())
                                                                 .toUri())
                             .build();
    }

    /**
     * Get all employees ADMIN request GET > /api/v1/employee
     */
    @GetMapping
    @RolesAllowed({"ROLE_ADMIN", "ROLE_SUPERADMIN"})
    public ResponseEntity<Object> getAllEmployees() {

        log.info(" > > > GET /api/v1/employee (Get all employees ADMIN request)");

        List<Employee> employees = employeeService.getAllEmployees();

        log.info(" < < < GET /api/v1/employee (Get all employees ADMIN success)");

        return ResponseEntity.ok(employees.stream().map(EmployeeResponse::mapEmployeeEntity).toList());
    }

    /**
     * Invalidate employee ADMIN request DELETE > /api/v1/employee/:id
     */
    @DeleteMapping("/{id}")
    @RolesAllowed("ROLE_SUPERADMIN")
    public ResponseEntity<Object> deleteEmployee(@PathVariable(name = "id") Long id) {

        log.info(" > > > DELETE /api/v1/employee/{} (Invalidate employee SUPERADMIN request)", id);

        employeeService.invalidateEmployeeWithId(id);

        log.info(" < < < DELETE /api/v1/employee/{} (Invalidate employee SUPERADMIN success)", id);

        return ResponseEntity.noContent().build();
    }

    /**
     * Set employee as ADMIN  SUPERADMIN request POST > /api/v1/employee/:id
     */
    @PostMapping("/{id}")
    @RolesAllowed("ROLE_SUPERADMIN")
    public ResponseEntity<Object> setEmployeeAsAdmin(@PathVariable(name = "id") Long id) {

        log.info(" > > > POST /api/v1/employee/{} (Set employee as ADMIN request)", id);

        employeeService.setEmployeeAsAdmin(id);

        log.info(" < < < POST /api/v1/employee/{} (Set employee as ADMIN success)", id);

        return ResponseEntity.ok().build();
    }

    /**
     * Get all therapies EMPLOYEE request GET > /api/v1/employee/therapies
     */
    @GetMapping("/accountable/therapies")
    @RolesAllowed("ROLE_EMPLOYEE")
    public ResponseEntity<Object> getEmployeeTherapies(@NotNull HttpServletRequest request) {

        log.info(" > > > GET /api/v1/employee/therapies (Get all therapies EMPLOYEE request)");

        String token = SecurityUtil.getJwtTokenFromRequest(request);
        String username = jwtGenerator.getUsernameFromToken(token);
        List<Therapy> therapies = employeeService.getEmployeesTherapies(username);

        log.info(" < < < GET /api/v1/employee/therapies (Get all therapies EMPLOYEE success)");

        return ResponseEntity.ok(therapies.stream().map(TherapyResponse::mapTherapyEntity).toList());
    }

    /**
     * Get all therapies EMPLOYEE request GET > /api/v1/employee/therapies
     */
    @GetMapping("/therapies")
    @RolesAllowed("ROLE_EMPLOYEE")
    public ResponseEntity<Object> getAllTherapies() {

        log.info(" > > > GET /api/v1/employee/therapies (Get all therapies EMPLOYEE request)");

        List<Therapy> therapies = employeeService.getAllTherapies();

        log.info(" < < < GET /api/v1/employee/therapies (Get all therapies EMPLOYEE success)");

        return ResponseEntity.ok(therapies.stream().map(TherapyResponse::mapTherapyEntity).toList());
    }

    /**
     * Set appointment for therapy request POST > /api/v1/employee/therapy
     */
    @PostMapping("/therapy")
    @RolesAllowed("ROLE_EMPLOYEE")
    public ResponseEntity<Object> setAppointmentForTherapy(@Validated @RequestBody AppointmentRequest appointmentRequest,
                                                           @NotNull HttpServletRequest request) throws Exception {

        log.info(" > > > POST /api/v1/employee/therapy (Set appointment for therapy EMPLOYEE request)");

        String token = SecurityUtil.getJwtTokenFromRequest(request);
        String username = jwtGenerator.getUsernameFromToken(token);
        employeeService.setAppointmentForTherapy(appointmentRequest, username);

        log.info(" < < < POST /api/v1/employee/therapy (Set appointment for therapy EMPLOYEE success)");

        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
                                                                 .path("/employee/therapy/{id}")
                                                                 .buildAndExpand(appointmentRequest.getTherapyId())
                                                                 .toUri())
                             .build();
    }

    /**
     * Employee create room request POST > /api/v1/employee/room
     */
    @PostMapping("/room")
    @RolesAllowed("ROLE_SUPERADMIN")
    public ResponseEntity<Object> createRoom(@Validated @RequestBody RoomRequest roomRequest) {

        log.info(" > > > POST /api/v1/employee/room (Employee room create request)");

        Room room = roomService.createRoom(roomRequest);

        log.info(" < < < POST /api/v1/employee/room (Employee room create request successful)");

        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
                                                                 .path("/room/{roomId}")
                                                                 .buildAndExpand(room.getId())
                                                                 .toUri())
                             .build();
    }

    /**
     * Employee get all rooms GET > /api/v1/employee/room
     */
    @GetMapping("/room")
    @RolesAllowed("ROLE_EMPLOYEE")
    public ResponseEntity<List<RoomResponse>> getAllRooms() {
        log.info(" > > > GET /api/v1/employee/room (Employee get all rooms)");

        List<Room> rooms = roomService.getAllRooms();

        log.info(" < < < GET /api/v1/employee/room (Employee get all rooms success)");

        return ResponseEntity.ok(rooms.stream().map(RoomResponse::mapRoomEntity).toList());
    }

    /**
     * Employee set room as operable request POST > /api/v1/employee/room/operable/:id
     */
    @PostMapping("/room/operable/{id}")
    @RolesAllowed("ROLE_SUPERADMIN")
    public ResponseEntity<Object> setRoomAsOperable(@PathVariable Long id) {

        log.info(" > > > POST /api/v1/employee/room/operable/{} (Employee set room as operable request)", id);

        roomService.setRoomAsOperable(id);

        log.info(" < < < POST /api/v1/employee/room/operable/{} (Employee set room as operable successful)", id);

        return ResponseEntity.ok().build();
    }

    /**
     * Employee set room as inoperable request POST > /api/v1/employee/room/inoperable/:id
     */
    @PostMapping("/room/inoperable/{id}")
    @RolesAllowed("ROLE_SUPERADMIN")
    public ResponseEntity<Object> setRoomAsInOperable(@PathVariable Long id) {

        log.info(" > > > POST /api/v1/employee/room/inoperable/{} (Employee set room as inoperable request)", id);

        roomService.setRoomAsInOperable(id);

        log.info(" < < < POST /api/v1/employee/room/inoperable/{} (Employee set room as inoperable successful)", id);

        return ResponseEntity.ok().build();
    }

    /**
     * Employee get room with id request GET > /api/v1/employee/room/{id}
     */
    @GetMapping("/room/{id}")
    @RolesAllowed("ROLE_EMPLOYEE")
    public ResponseEntity<RoomResponse> getRoomWithId(@PathVariable Long id) {
        log.info(" > > > GET /api/v1/employee/room/{} (Employee get room with id)", id);

        Room room = roomService.getRoomWithId(id);

        log.info(" < < < GET /api/v1/employee/room/{} (Employee get room with id successful)", id);

        return ResponseEntity.ok(RoomResponse.mapRoomEntity(room));
    }

    /**
     * Employee delete room request DELETE > /api/v1/employee/room/{id}
     */
    @DeleteMapping("/room/{id}")
    @RolesAllowed("ROLE_SUPERADMIN")
    public ResponseEntity<Object> deleteRoom(@PathVariable Long id) {

        log.info(" > > > DELETE /api/v1/employee/room/{} (Employee room delete request)", id);

        roomService.deleteRoom(id);

        log.info(" < < < DELETE /api/v1/employee/room/{} (Employee room delete request successful)", id);

        return ResponseEntity.noContent().build();
    }

    /**
     * Employee create equipment request POST > /api/v1/employee/equipment
     */
    @PostMapping("/equipment")
    @RolesAllowed({"ROLE_ADMIN", "ROLE_SUPERADMIN"})
    public ResponseEntity<Object> createEquipment(@Validated @RequestBody EquipmentRequest equipmentRequest) {

        log.info(" > > > POST /api/v1/employee/equipment (Employee equipment create request)");

        Equipment equipment = equipmentService.createEquipment(equipmentRequest);

        log.info(" < < < POST /api/v1/employee/equipment (Employee equipment create request successful)");

        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
                                                                 .path("/equipment/{roomId}")
                                                                 .buildAndExpand(equipment.getId())
                                                                 .toUri())
                             .build();
    }

    /**
     * Employee get all equipment GET > /api/v1/employee/equipment
     */
    @GetMapping("/equipment")
    @RolesAllowed("ROLE_EMPLOYEE")
    public ResponseEntity<List<EquipmentResponse>> getAllEquipment() {
        log.info(" > > > GET /api/v1/employee/equipment (Employee get all equipment)");

        List<Equipment> equipment = equipmentService.getAllEquipment();

        log.info(" < < < GET /api/v1/employee/equipment (Employee get all equipment success)");

        return ResponseEntity.ok(equipment.stream().map(EquipmentResponse::mapEquipmentEntity).toList());
    }

    /**
     * Employee get equipment with id request GET > /api/v1/employee/equipment/{id}
     */
    @GetMapping("/equipment/{id}")
    @RolesAllowed("ROLE_EMPLOYEE")
    public ResponseEntity<EquipmentResponse> getEquipmentWithId(@PathVariable Long id) {
        log.info(" > > > GET /api/v1/employee/equipment/{} (Employee get equipment with id)", id);

        Equipment equipment = equipmentService.getEquipmentWithId(id);

        log.info(" < < < GET /api/v1/employee/equipment/{} (Employee get equipment with id successful)", id);

        return ResponseEntity.ok(EquipmentResponse.mapEquipmentEntity(equipment));
    }

    /**
     * Employee set equipment as operable request POST > /api/v1/employee/equipment/operable/:id
     */
    @PostMapping("/equipment/operable/{id}")
    @RolesAllowed({"ROLE_ADMIN", "ROLE_SUPERADMIN"})
    public ResponseEntity<Object> setEquipmentAsOperable(@PathVariable Long id) {

        log.info(" > > > POST /api/v1/employee/equipment/operable/{} (Employee set equipment as operable request)", id);

        equipmentService.setEquipmentWithIdAsOperable(id);

        log.info(" < < < POST /api/v1/employee/equipment/operable/{} (Employee set equipment as operable request success)", id);

        return ResponseEntity.ok().build();
    }

    /**
     * Employee create equipment request POST > /api/v1/employee/equipment
     */
    @PostMapping("/equipment/inoperable/{id}")
    @RolesAllowed({"ROLE_ADMIN", "ROLE_SUPERADMIN"})
    public ResponseEntity<Object> setEquipmentAsOutOfService(@PathVariable Long id) {

        log.info(" > > > POST /api/v1/employee/equipment/inoperable/{} (Employee equipment set as out of service request)", id);

        equipmentService.setEquipmentWithIdAsOutOfService(id);

        log.info(" < < < POST /api/v1/employee/equipment/inoperable/{} (Employee equipment set as out of service request success)", id);

        return ResponseEntity.ok().build();
    }

    /**
     * Employee delete equipment request DELETE > /api/v1/employee/equipment/{id}
     */
    @DeleteMapping("/equipment/{id}")
    @RolesAllowed({"ROLE_ADMIN", "ROLE_SUPERADMIN"})
    public ResponseEntity<Object> deleteEquipment(@PathVariable Long id) {

        log.info(" > > > DELETE /api/v1/employee/equipment/{} (Employee equipment delete request)", id);

        equipmentService.deleteEquipment(id);

        log.info(" < < < DELETE /api/v1/employee/equipment/{} (Employee equipment delete request successful)", id);

        return ResponseEntity.noContent().build();
    }
}
