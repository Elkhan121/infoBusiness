package com.example.czn.entity.standart;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Admin {
    @Id
    @GeneratedValue
    private Long    id;
    private long   userId;
    private String comment;

    public Admin(long userId, String comment) {
        this.userId = userId;
        this.comment = comment;
    }
}
