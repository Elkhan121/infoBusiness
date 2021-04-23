package com.example.czn.command.impl;

import com.example.czn.command.Command;
import com.example.czn.dao.repositories.AdminQuestionRepo;
import com.example.czn.dao.repositories.TelegramBorRepositoryProvider;
import com.example.czn.entity.custom.AdminQuest;
import com.example.czn.util.Const;
import com.example.czn.util.type.WaitingType;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;

public class id018_AdminQuest extends Command {
    AdminQuest adminQuest = new AdminQuest();

    @Override
    public boolean execute() throws SQLException, TelegramApiException {switch (waitingType) {
        case START:
            getFullName();
            waitingType = WaitingType.SET_FULL_NAME;
            return COMEBACK;
        case SET_FULL_NAME:
            if (update.hasMessage() && update.getMessage().hasText() && update.getMessage().getText().length() <= 50) {
                adminQuest.setName(update.getMessage().getText());
                getQuest();
                adminQuest.setQuestion(update.getMessage().getText());
                waitingType = WaitingType.SET_QUESTION;
                return COMEBACK;
            } else {
                wrongData();
                getFullName();
                return COMEBACK;
            }
        case SET_QUESTION:
            if(update.hasMessage() & update.getMessage().hasText()) {
//                AdminQuestionDao adminQuestionDao = new AdminQuestionDao();
                AdminQuestionRepo adminQuestionRepo = TelegramBorRepositoryProvider.getAdminQuestionRepo();
                adminQuest.setQuestion(update.getMessage().getText());
                adminQuestionRepo.save(adminQuest);
                sendMessage("Уважаемый(ая) " + adminQuest.getName() +
                        ", ваш вопрос №" + adminQuestionRepo.count()+1 + "отправлен!" );
                return EXIT;
            } else {
                wrongData();
                getSuggestion();
                return COMEBACK;
            }
    }
        return EXIT;
    }

    private int wrongData() throws TelegramApiException {
        return botUtils.sendMessage(Const.WRONG_DATA_TEXT, chatId);
    }

    private int getFullName() throws TelegramApiException {
        return botUtils.sendMessage(Const.SET_FULL_NAME, chatId);
    }

    private int getSuggestion() throws TelegramApiException {
        return botUtils.sendMessage(Const.SET_SUGGESTION, chatId);
    }

    private void getQuest() throws TelegramApiException{
        botUtils.sendMessage(Const.SET_QUESTION, chatId);
    }
}
