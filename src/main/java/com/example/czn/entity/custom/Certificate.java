package com.example.czn.entity.custom;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Getter
@Setter
public class Certificate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String full_name;
    private long phone_number;
    private int course;
    private String completion_date;
    private int cost;
    private Date course_end_date;
    private String full_name_as_certificate;
    private String certificate_request;
    private Date date;

}
