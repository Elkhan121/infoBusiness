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
public class Survey {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String surveyName;
    private String questsId;
    private SurveyType surveyType;
    private int languageId;
    private boolean isHide;
    private String questText;

    public void setQuestText(String questText) {
        this.questText = questText;
    }
}
