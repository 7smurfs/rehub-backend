package sevensmurfs.rehub.model.message.response;

import lombok.Builder;
import lombok.Data;
import sevensmurfs.rehub.model.entity.RehubUser;

import java.time.LocalDateTime;

@Data
@Builder
public class UserResponse {

    private Long id;

    private String username;

    private String accessToken;

    private String firstName;

    private String lastName;

    private String password;

    private String pin;

    private String gender;

    private LocalDateTime dateOfBirth;

    public static UserResponse mapAuthenticatedUser(RehubUser user, String jwtToken) {
        return UserResponse.builder()
                           .id(user.getId())
                           .username(user.getUsername())
                           .accessToken(jwtToken)
                           .build();
    }
}
