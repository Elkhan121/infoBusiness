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
public class Questionnaire{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String full_name;
    private String place_of_work;
    private String postDate;
    private long phone_number;
    private String education;
    private String specialization;
    private String preferred_subjects;
    private String address;
    private int course;
    private String completion_date;
    private long cost;
    private String certificate_receipt_status;
    private Date date;


       }
