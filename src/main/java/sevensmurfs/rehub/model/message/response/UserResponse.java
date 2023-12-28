package sevensmurfs.rehub.model.message.response;

import lombok.Builder;
import lombok.Data;
import sevensmurfs.rehub.enums.Role;
import sevensmurfs.rehub.model.entity.RehubUser;
import sevensmurfs.rehub.model.entity.UserRole;

import java.util.List;

@Data
@Builder
public class UserResponse {

    private Long id;

    private String username;

    private String accessToken;

    private String firstName;

    private String lastName;

    private List<Role> roles;

    public static UserResponse mapAuthenticatedUserEntity(RehubUser user, String jwtToken, String firstName, String lastName) {
        return UserResponse.builder()
                           .id(user.getId())
                           .accessToken(jwtToken)
                           .firstName(firstName)
                           .lastName(lastName)
                           .roles(user.getRoles().stream().map(UserRole::getName).toList())
                           .build();
    }

    public static UserResponse mapUserEntity(RehubUser user) {
        return UserResponse.builder()
                           .id(user.getId())
                           .roles(user.getRoles().stream().map(UserRole::getName).toList())
                           .build();
    }
}
