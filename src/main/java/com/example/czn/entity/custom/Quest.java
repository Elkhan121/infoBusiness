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
public class Quest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String questAnswer;
    private int idSurvey;
    private int languageId;

    public Quest setId(int id) {
        this.id = id;
        return this;
    }

    public Quest setIdLanguage(int languageId) {
        this.languageId = languageId;
        return this;
    }

    public Quest setQuestAnswer(String questAnswer) {
        this.questAnswer = questAnswer;
        return this;
    }
}
