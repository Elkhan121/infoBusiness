package com.example.czn.entity.standart;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@ToString
@Entity
public class Responsible {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long   userId;
    private String comment;
}
