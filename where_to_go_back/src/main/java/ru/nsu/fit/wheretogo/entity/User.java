package ru.nsu.fit.wheretogo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.nsu.fit.wheretogo.dto.UserDTO;
import ru.nsu.fit.wheretogo.entity.score.Score;
import ru.nsu.fit.wheretogo.entity.user_coeff.UserCoefficient;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "username")
    private String username;

    @Column(name = "created_at")
    private Instant createdAt;


    @ManyToMany
    @JoinTable(
            name = "user_visited_places",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "place_id")
    )
    private List<Place> visitedPlaces;

    @ManyToMany
    @JoinTable(
            name = "user_favourites",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "place_id")
    )
    private List<Place> favouritePlaces;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Score> userScores;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<StayPoint> stayPoints;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<UserCoefficient> userVector;

    public User(String email, String password, String username, Instant createdAt) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.createdAt = createdAt;
        ;
    }

    public static User getFromDTO(UserDTO dto) {
        if (dto == null) {
            return null;
        }
        return new User()
                .setId(dto.getId())
                .setEmail(dto.getEmail())
                .setUsername(dto.getUsername())
                .setCreatedAt(dto.getCreatedAt());
    }
}