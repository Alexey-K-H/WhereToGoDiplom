package ru.nsu.fit.wheretogo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Entity
@Table(name = "place_pictures")
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class PlacePicture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "url", nullable = false)
    private String url;

    @ManyToOne
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;
}
