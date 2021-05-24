package com.example.czn.entity.standart;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class TrainingAndSeminar {

    @Id
    @GeneratedValue
    private int id;
    private String nameInKz;
    private String nameInRus;
    private int price;
    private String descriptionServiceInKz;
    private String descriptionServiceInRus;
    private String duration;
    private boolean activity;

}
