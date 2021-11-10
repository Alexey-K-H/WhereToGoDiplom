
package ru.nsk.wheretogo.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name="users")
@Setter
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//для автоинкрементации
    @Column(name="id")
    private Integer id;

    @Column(name = "username",nullable = false)
    private String username;

    @Column(name="email",nullable = false)
    private String email;

    @Column(name="password",nullable = false)
    private String password;

    @Column(name="date",nullable = false)
    private Instant created_at;

    @Column(name="avatar_link")
    private String avatar_link;

    @Column(name = "avatar_thumbnail_link")
    private String avatar_thumbnail_link;

    public User(String email, String password, String username, Instant created_at, String avatar_link, String avatar_thumbnail_link) {
        this.email=email;
        this.password=password;
        this.username=username;
        this.created_at=created_at;
        this.avatar_link=avatar_link;
        this.avatar_thumbnail_link=avatar_thumbnail_link;}

    protected User() {}
}
