package com.example.czn.command.impl;

import com.example.czn.command.Command;
import com.example.czn.entity.standart.Contest;
import com.example.czn.entity.standart.TrainingAndSeminar;
import com.example.czn.service.KeyboardService;
import com.example.czn.service.LanguageService;
import com.example.czn.util.ButtonsLeaf;
import com.example.czn.util.type.WaitingType;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.example.czn.command.impl.id043_ProjectEditMenu.buttonName;

public class id036_TrainingAndSeminarEditor extends Command {
    private List<TrainingAndSeminar> trainingAndSeminars;
    private ButtonsLeaf buttonsLeaf;
    private TrainingAndSeminar trainingAndSeminar;
    private int trainingId;
    private int delete;

    @Override
    public boolean execute() throws SQLException, TelegramApiException {
        buttonName = updateMessageText;
        switch (waitingType) {
            case START:
                getSeminarsAndTrainings();
                return COMEBACK;
            case GET_INFO:
                getTrainingInfo(Integer.parseInt(updateMessageText));
                return COMEBACK;
            case SELECTING:
                selectingCommand();
                return COMEBACK;
            case PRICE_EDIT:
              //  deleteMessage(delete);
                editPrice(Integer.parseInt(updateMessageText));
                return COMEBACK;
            case DURATION_EDIT:
                editDuration(updateMessageText);
                return COMEBACK;
        }
        return EXIT;
    }

    private void getSeminarsAndTrainings() throws TelegramApiException {
        currentLanguage = LanguageService.getLanguage(chatId);
        List<String> listOfTraining = new ArrayList<>();
        List<String> ids = new ArrayList<>();
        trainingAndSeminars = trainingAndSeminarRepo.findAll();
        if (currentLanguage.getId() == 1) {
            trainingAndSeminars.forEach(trainingAndSeminar -> {
                listOfTraining.add(trainingAndSeminar.getNameInRus());
                ids.add(String.valueOf(trainingAndSeminar.getId()));
            });
            listOfTraining.add(buttonRepo.findByIdAndLangId(234, LanguageService.getLanguage(chatId).getId()).getName());
            ids.add(buttonRepo.findByIdAndLangId(234, LanguageService.getLanguage(chatId).getId()).getName());
        } else {
            trainingAndSeminars.forEach(trainingAndSeminar -> {
                listOfTraining.add(trainingAndSeminar.getNameInKz());
                ids.add(String.valueOf(trainingAndSeminar.getId()));
            });
            listOfTraining.add(buttonRepo.findByIdAndLangId(234, LanguageService.getLanguage(chatId).getId()).getName());
            ids.add(buttonRepo.findByIdAndLangId(234, LanguageService.getLanguage(chatId).getId()).getName());

        }
            buttonsLeaf = new ButtonsLeaf(listOfTraining, ids, listOfTraining.size());
            sendMessageWithKeyboard(getText(1459), buttonsLeaf.getListButton2());
            waitingType = WaitingType.GET_INFO;
        }

        private void getTrainingInfo ( int contestId){
            deleteMessage();
            currentLanguage = LanguageService.getLanguage(chatId);
            trainingAndSeminar = trainingAndSeminarRepo.findById(contestId);
            this.trainingId = contestId;
            print();
            waitingType = WaitingType.SELECTING;
        }

        private void print () {
            if (currentLanguage.getId() == 1) {
                printRus(trainingId);
            } else {
                printKz(trainingId);
            }
        }

        private void printRus ( int trainingId){
            try {
                trainingAndSeminar = trainingAndSeminarRepo.findById(trainingId);
                currentLanguage = LanguageService.getLanguage(chatId);
                sendMessageWithKeyboard(String.format(getText(1460)
                        , trainingAndSeminar.getNameInRus()
                        , trainingAndSeminar.getPrice()
                        , trainingAndSeminar.getDuration()
                        , isActive(trainingAndSeminar.isActivity())
                        , currentLanguage.name()
                        , trainingAndSeminar.getDescriptionServiceInRus()), 216);
                this.trainingId = trainingId;
                waitingType = WaitingType.SELECTING;
            } catch (Exception e) {
            }
        }

        private void printKz ( int trainingId){
            try {
                trainingAndSeminar = trainingAndSeminarRepo.findById(trainingId);
                currentLanguage = LanguageService.getLanguage(chatId);
                sendMessageWithKeyboard(String.format(getText(1460)
                        , trainingAndSeminar.getNameInKz()
                        , trainingAndSeminar.getPrice()
                        , isActive(trainingAndSeminar.isActivity())
                        , currentLanguage.name()
                        , trainingAndSeminar.getDescriptionServiceInKz()), 216);
                this.trainingId = trainingId;
                waitingType = WaitingType.SELECTING;
            } catch (Exception e) {
            }
        }

        private void selectingCommand () throws TelegramApiException {
            if (isButton(231)) {
               // delete(trainingId);
            } else if (isButton(232)) {
                turnOffOnn(trainingAndSeminar.isActivity());
            } else if (isButton(233)) {
                delete = sendMessage(getText(1469));
                waitingType = WaitingType.PRICE_EDIT;
            } else if (isButton(235)) {
                sendMessage(getText(1474));
                waitingType = WaitingType.DURATION_EDIT;
            }
        }

        private void delete ( int id) throws TelegramApiException {
            trainingAndSeminar = trainingAndSeminarRepo.findById(id);
            trainingAndSeminarRepo.delete(id);
            sendMessageWithKeyboard(getText(1467), 215);
            waitingType = WaitingType.TURN_OFF_ON;
        }

        private String isActive ( boolean activity){
            String active = "";
            if (activity) {
                active = getText(1470);
            } else {
                active = getText(1471);
            }
            return active;
        }

        private void editPrice ( int price) throws TelegramApiException {
            trainingAndSeminar = trainingAndSeminarRepo.findById(trainingId);
            trainingAndSeminarRepo.updatePrice(price, trainingId);
            sendMessageWithKeyboard(getText(1461), 215);
        }

        private void turnOffOnn ( boolean active) throws TelegramApiException {
            if (active) {
                trainingAndSeminarRepo.updateActivity(false, trainingId);
                sendMessage(getText(1473));
                active = false;
            } else {
                trainingAndSeminarRepo.updateActivity(true, trainingId);
                sendMessage(getText(1472));
                active = true;
            }
        }

        private void editDuration (String duration) throws TelegramApiException {
            trainingAndSeminar = trainingAndSeminarRepo.findById(trainingId);
            trainingAndSeminarRepo.updateDuration(duration, trainingId);
            sendMessage(getText(1461));
        }

    }