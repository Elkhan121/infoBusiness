package com.example.czn.command.impl;

import com.example.czn.command.Command;
import com.example.czn.entity.standart.Contest;
import com.example.czn.entity.standart.Language;
import com.example.czn.entity.standart.TrainingAndSeminar;
import com.example.czn.service.LanguageService;
import com.example.czn.util.type.WaitingType;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;

import static com.example.czn.command.impl.id043_ProjectEditMenu.buttonName;

public class id042_AddingCourseSeminar extends Command {
    private Contest currentContest;
    private TrainingAndSeminar trainingAndSeminar;
    private String nameInKz;
    private String nameInRus;
    private int price;
    private String descriptionInKz;
    private String descriptionInRus;


    @Override
    public boolean execute() throws SQLException, TelegramApiException {
        switch (waitingType) {
            case START:
                sendMessage(getText(1462));
                waitingType = WaitingType.TWO;
                return COMEBACK;
            case TWO:
                nameInKz = updateMessageText;
                sendMessage(getText(1463));
                waitingType = WaitingType.THREE;
                return COMEBACK;
            case THREE:
                nameInRus = updateMessageText;
                sendMessage(getText(1468));
                waitingType = WaitingType.FOUR;
                return COMEBACK;
            case FOUR:
                price = Integer.parseInt(updateMessageText);
                sendMessage(getText(1465));
                waitingType = WaitingType.FIVE;
                return COMEBACK;
            case FIVE:
                descriptionInKz = updateMessageText;
                sendMessage(getText(1466));
                waitingType = WaitingType.SIX;
                return COMEBACK;
            case SIX:
                descriptionInRus = updateMessageText;
                saving(nameInKz
                        , nameInRus
                        , price
                        , descriptionInKz
                        , descriptionInRus);
                return COMEBACK;
        }
        return EXIT;
    }

    private void saveContest(String nameInKz, String nameInRus, int price, String descriptionInKz, String descriptionInRus) throws TelegramApiException {
        currentContest = new Contest();
        currentContest.setNameInRus(nameInRus);
        currentContest.setNameInKz(nameInKz);
        currentContest.setPrice(price);
        currentContest.setDescriptionServiceInKz(descriptionInKz);
        currentContest.setDescriptionServiceInKz(descriptionInRus);

        contestRepo.save(currentContest);
        sendMessageWithKeyboard(getText(1461), 215);
    }

    private void saveTrainingAndSeminar(String nameInKz, String nameInRus, int price, String descriptionInKz, String descriptionInRus) throws TelegramApiException {
        trainingAndSeminar = new TrainingAndSeminar();
        trainingAndSeminar.setNameInRus(nameInRus);
        trainingAndSeminar.setNameInKz(nameInKz);
        trainingAndSeminar.setPrice(price);
        trainingAndSeminar.setDescriptionServiceInKz(descriptionInKz);
        trainingAndSeminar.setDescriptionServiceInKz(descriptionInRus);

        trainingAndSeminarRepo.save(trainingAndSeminar);
        sendMessageWithKeyboard(getText(1461), 215);
    }

    private void saving(String nameInKz, String nameInRus, int price, String descriptionInKz, String descriptionInRus) throws TelegramApiException {
        currentLanguage = LanguageService.getLanguage(chatId);
        if (buttonName.equals(buttonRepo.findByIdAndLangId(227, currentLanguage.getId()).getName())) {
            saveTrainingAndSeminar(nameInKz, nameInRus, price, descriptionInKz, descriptionInRus);
        } else if (buttonName.equals(buttonRepo.findByIdAndLangId(228, currentLanguage.getId()).getName())) {
            saveContest(nameInKz, nameInRus, price, descriptionInKz, descriptionInRus);
        } else {}
    }
}
