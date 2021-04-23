package com.example.czn.entity.standart;

import com.example.czn.dao.repositories.KeyboardMarkUpRepo;
import com.example.czn.dao.repositories.TelegramBorRepositoryProvider;
import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiValidationException;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter @Setter
@Entity
public class Keyboard implements ReplyKeyboard {
    //KeyboardMarkUpRepo keyboardMarkUpRepo = TelegramBorRepositoryProvider.getKeyboardMarkUpRepo();
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long     id;
    private String  button_ids;
    private boolean inline;
    private String  comment;

    @Override
    public void validate() throws TelegramApiValidationException {

    }
}
