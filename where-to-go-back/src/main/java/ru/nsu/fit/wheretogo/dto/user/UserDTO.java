package ru.nsu.fit.wheretogo.dto.user;

import lombok.Getter;
import ru.nsu.fit.wheretogo.dto.BaseDTO;
import ru.nsu.fit.wheretogo.entity.user.User;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;

@Getter
public class UserDTO extends BaseDTO {

    @NotNull
    private String email;

    @NotNull
    @Size(max = 40, message = "Username have to considered 40 letters!")
    private String username;

    private Instant createdAt;

    public static UserDTO getFromEntity(User user) {
        if (user == null) {
            return null;
        }

        var dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        dto.setCreatedAt(user.getCreatedAt());

        return dto;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
