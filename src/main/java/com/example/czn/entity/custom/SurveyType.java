package com.example.czn.entity.custom;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@AllArgsConstructor //@NoArgsConstructor
@Getter @ToString

public enum SurveyType {

    button(1),
    mix(2),
    text(3);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    SurveyType() {

    }

//    public static SurveyType getById(int id) {
//        for (SurveyType surveyType : values()) {
//            if (surveyType.id == (id)) return surveyType;
//        }
//        return null;
//    }
}
