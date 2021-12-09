package ru.nsu.fit.wheretogo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.Instant;
import ru.nsu.fit.wheretogo.entity.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @NotNull
    private Long id;

    @NotNull
    private String email;

    @NotNull
    @Size(max = 40, message = "Username have to considered 40 letters!")
    private String username;

    private Instant createdAt;
    private String avatarLink;
    private String avatarThumbnailLink;
    
    public static UserDto getFromEntity(User user) {
        if (user == null) {
            return null;
        }
        return new UserDto()
                .setId(user.getId())
                .setEmail(user.getEmail())
                .setUsername(user.getUsername())
                .setCreatedAt(user.getCreatedAt());
    }
}
