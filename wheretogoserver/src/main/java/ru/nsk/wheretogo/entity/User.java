package ru.nsk.wheretogo.entity;


import lombok.*;
import lombok.experimental.Accessors;
import ru.nsk.wheretogo.dto.UserDto;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@Setter
@Getter
@Accessors(chain = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="users")
public class User {
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "username",nullable = false)
    private String username;

    @Column(name="password", nullable = false)
    private String password;

    @Column(name="email", nullable = false)
    private String email;

    @Column(name = "created_at")
    private Instant created_at;

    @Column(name="avatar_link")
    private String avatar_link;

    @Column(name = "avatar_thumbnail_link")
    private String avatar_thumbnail_link;

    @ManyToMany()
    @JoinTable(
            name="user_favourites",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "place_id")
    )
    private List<Place> favourite_places;
    @ManyToMany()
    @JoinTable(
            name="user_visited_places",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "place_id")
    )
    private List<Place> visited_place;

    public static User getFromDTO(UserDto dto) {
        if (dto==null) {
            return null;
        }
        return new User()
                .setId(dto.getId())
                .setUsername(dto.getUsername())
                .setEmail(dto.getEmail())
                .setPassword(dto.getPassword())
                .setCreated_at(dto.getCreated_at())
                .setAvatar_link(dto.getAvatar_link())
                .setAvatar_thumbnail_link(dto.getAvatar_thumbnail_link());

    }
}
