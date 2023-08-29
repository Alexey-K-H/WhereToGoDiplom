package ru.nsu.fit.wheretogo.entity.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.nsu.fit.wheretogo.dto.user.UserDTO;
import ru.nsu.fit.wheretogo.entity.place.Place;
import ru.nsu.fit.wheretogo.entity.place.StayPoint;
import ru.nsu.fit.wheretogo.entity.score.Score;
import ru.nsu.fit.wheretogo.entity.user.coefficient.main.UserCoefficientMain;

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
@Table(name = "T_USER")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "CREATED_AT")
    private Instant createdAt;


    @ManyToMany
    @JoinTable(
            name = "T_USER_VISITED_PLACES",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "PLACE_ID")
    )
    private List<Place> visitedPlaces;

    @ManyToMany
    @JoinTable(
            name = "T_USER_FAVOURITES",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "PLACE_ID")
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
    private List<UserCoefficientMain> userVector;

    public User(String email, String password, String username, Instant createdAt) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.createdAt = createdAt;
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
