package com.example.czn.entity.standart;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class LanguageUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private long     chatId;
    private Language language;

    public LanguageUser(long chatId, Language language) {
        this.chatId = chatId;
        this.language = language;
    }

}

