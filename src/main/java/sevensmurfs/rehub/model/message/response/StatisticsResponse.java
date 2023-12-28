package sevensmurfs.rehub.model.message.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatisticsResponse {

    private Long noOfEmployees;

    private Long noOfPatients;

    private Long noOfRooms;

    private Long noOfEquipment;

    private Long noOfActiveTherapies;

    private Long noOfNewUsersForThisMonth;

}
