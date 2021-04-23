package com.example.czn.entity.custom;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Suggestion {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int    id;
    private String FullName;
    private String phoneNumber;
    private String location;
    private String text;
    private Date   postDate;
}
