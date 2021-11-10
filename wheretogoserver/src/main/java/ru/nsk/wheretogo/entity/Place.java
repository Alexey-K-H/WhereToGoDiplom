package ru.nsk.wheretogo.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.Instant;


@Entity
@Table(name="places")
@Setter
@Getter
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="description")
    private String description;

    @Column(name="thumbnail", nullable = false )
    private String thumbnail;

    @Column(name="upload_at", nullable = false )
    private Instant upload_at;

    @Column(name="average_score", nullable = false )
    private double average_score;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "lat", column = @Column(name = "lat", nullable = false)),
            @AttributeOverride(name = "longg", column = @Column(name = "longg", nullable = false))
    })
    private Coordinates coordinates ;

    @PrePersist
    public void setUploadDate() {upload_at = Instant.now();}


    public Place(){

    }

}
