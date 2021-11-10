package ru.nsk.wheretogo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name="place_reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//для автоинкрементации
    @Column(name="id")
    private Integer id;

    @Column(name = "written_at")
    private Instant written_at;

    @Column(name="review")
    private String text;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    @ManyToOne
    @JoinColumn(name="place_id", nullable = false)
    private Place place;

    public  Review(){}
}
