package ru.nsk.wheretogo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.nsk.wheretogo.entity.User;

import javax.validation.constraints.NotNull;
import java.time.Instant;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserDto extends BaseDTO{
    private Integer id;
    @NotNull(message = "user must have name")
    private String username;
    @NotNull(message = "user must have email")
    private String email;
    @NotNull(message = "user must have password")
    private String password;
    private Instant created_at;
    private String avatar_link;
    private String avatar_thumbnail_link;

    public static UserDto getFromEntity(User user) {
        if (user==null) {
            return null;
        }
        return new UserDto()
                .setId(user.getId())
                .setUsername(user.getUsername())
                .setEmail(user.getEmail())
                .setPassword(user.getPassword())
                .setCreated_at(user.getCreated_at())
                .setAvatar_link(user.getAvatar_link())
                .setAvatar_thumbnail_link(user.getAvatar_thumbnail_link());

    }


}
