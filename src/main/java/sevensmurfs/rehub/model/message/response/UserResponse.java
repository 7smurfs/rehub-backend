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

    private List<Role> roles;

    public static UserResponse mapAuthenticatedUserEntity(RehubUser user, String jwtToken) {
        return UserResponse.builder()
                           .id(user.getId())
                           .username(user.getUsername())
                           .accessToken(jwtToken)
                           .roles(user.getRoles().stream().map(UserRole::getName).toList())
                           .build();
    }

    public static UserResponse mapUserEntity(RehubUser user) {
        return UserResponse.builder()
                           .id(user.getId())
                           .username(user.getUsername())
                           .roles(user.getRoles().stream().map(UserRole::getName).toList())
                           .build();
    }
}
