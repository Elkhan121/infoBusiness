package com.example.czn.entity.standart;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@AllArgsConstructor
@Getter
@ToString

public enum Language {
    ru(1),
    kz(2);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    public static Language getById(int id) {
        for (Language language : values()) {
            if (language.id == (id)) return language;
        }
        return ru;
    }
}
