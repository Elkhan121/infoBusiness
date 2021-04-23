package com.example.czn.entity.custom;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class SurveyAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int surveyId;
    private long chatId;
    private String button;
}
