package ru.nsk.wheretogo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
@Table(name="categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//для автоинкрементации
    @Column(name="id")
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    public Category(String name) {this.name = name;}

    public Category() {

    }
}
