package com.example.czn.entity.standart;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Data
@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long    id;
    private Long    chatId;
    private String  phone;
    private String  fullName;
    private String  email;
    private String  userName;
    private Long    IIN;
}
