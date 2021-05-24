package com.example.czn.entity.standart;

import lombok.Data;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Contest {
    @Id
    @GeneratedValue
    private int id;
    private String nameInKz;
    private String nameInRus;
    private int price;
    private String descriptionServiceInKz;
    private String descriptionServiceInRus;
    private boolean activity;
    private String duration;

}
