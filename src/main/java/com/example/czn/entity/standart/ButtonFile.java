package com.example.czn.entity.standart;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class ButtonFile {
    @Id
    @GeneratedValue
    private long id;


    private int buttonId;
    private String file;

}
