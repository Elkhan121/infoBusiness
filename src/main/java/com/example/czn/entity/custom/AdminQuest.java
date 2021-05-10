package com.example.czn.entity.custom;

import lombok.*;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import javax.persistence.Entity;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class   AdminQuest {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    private String question;
}
