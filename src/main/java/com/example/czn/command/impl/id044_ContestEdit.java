package com.example.czn.command.impl;

import com.example.czn.command.Command;
import com.example.czn.entity.standart.Contest;
import com.example.czn.service.LanguageService;
import com.example.czn.util.ButtonsLeaf;
import com.example.czn.util.type.WaitingType;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.example.czn.command.impl.id043_ProjectEditMenu.buttonName;

public class id044_ContestEdit extends Command {
    private List<Contest> contents;
    private ButtonsLeaf buttonsLeaf;
    private Contest currentContest;
    public static int currentContestId;
    private int delete;

    @Override
    public boolean execute() throws SQLException, TelegramApiException {
        buttonName = updateMessageText;
        switch (waitingType) {
            case START:
                getContests();
                return COMEBACK;
            case GET_INFO:
                deleteMessage(delete);
                getContestInfo(Integer.parseInt(updateMessageText));
                return COMEBACK;
            case SELECTING:
                selectingCommand();
                return COMEBACK;
            case PRICE_EDIT:
                deleteMessage(delete);
                editPrice(Integer.parseInt(updateMessageText));
                return COMEBACK;
            case DURATION_EDIT:
                editDuration(updateMessageText);
                return COMEBACK;
        }
        return EXIT;
    }

    private void getContests() throws TelegramApiException {
        currentLanguage = LanguageService.getLanguage(chatId);
        List<String> listOfContents = new ArrayList<>();
        List<String> ids = new ArrayList<>();
        contents = contestRepo.findAll();
        if (currentLanguage.getId() == 1) {
            contents.forEach(content -> {
                listOfContents.add(content.getNameInRus());
                ids.add(String.valueOf(content.getId()));
            });
            listOfContents.add(buttonRepo.findByIdAndLangId(234, LanguageService.getLanguage(chatId).getId()).getName());
            ids.add(buttonRepo.findByIdAndLangId(234, LanguageService.getLanguage(chatId).getId()).getName());
        } else {
            contents.forEach(content -> {
                listOfContents.add(content.getNameInKz());
                ids.add(String.valueOf(content.getId()));
            });
            listOfContents.add(buttonRepo.findByIdAndLangId(234, LanguageService.getLanguage(chatId).getId()).getName());
            ids.add(buttonRepo.findByIdAndLangId(234, LanguageService.getLanguage(chatId).getId()).getName());
        }
            buttonsLeaf = new ButtonsLeaf(listOfContents, ids, listOfContents.size());
            delete = sendMessageWithKeyboard(getText(1459), buttonsLeaf.getListButton2());
            waitingType = WaitingType.GET_INFO;
        }

    private void getContestInfo(int contestId) throws TelegramApiException {
        try {
            currentLanguage = LanguageService.getLanguage(chatId);
            currentContest = contestRepo.findById(contestId);
            sendMessageWithKeyboard(
            String.format(getText(1460)
                    , currentContest.getNameInRus()
                    , currentContest.getPrice()
                    , currentContest.getDuration()
                    , isActive(currentContest.isActivity())
                    , currentLanguage.name()
                    , currentContest.getDescriptionServiceInRus()),216);
            currentContestId = contestId;
            waitingType = WaitingType.SELECTING;
        } catch (Exception e) {}
    }

    private void selectingCommand() throws TelegramApiException {
        if (isButton(231)) {
            delete(currentContestId);
        } else if (isButton(232)) {
            turnOffOnn(currentContest.isActivity());
        } else if (isButton(233)) {
            delete = sendMessage(getText(1469));
            waitingType = WaitingType.PRICE_EDIT;
        } else if(isButton(235)){
            sendMessage(getText(1474));
            waitingType = WaitingType.DURATION_EDIT;
        }
    }

    private void editPrice(int price) throws TelegramApiException {
        currentContest = contestRepo.findById(currentContestId);
        contestRepo.updatePrice(price,currentContestId);
        sendMessageWithKeyboard(getText(1461),215);
    }

    private void delete(int id) throws TelegramApiException {
        currentContest = contestRepo.findById(id);
        contestRepo.delete(id);
        sendMessageWithKeyboard(getText(1467),215);
        waitingType = WaitingType.TURN_OFF_ON;
    }

    private String isActive(boolean activity){
        String active = "";
        if(activity){
            active = getText(1470);
        } else {
            active = getText(1471);
        }
        return active;
    }

    private void turnOffOnn(boolean active) throws TelegramApiException {
        if(active) {
            contestRepo.updateActivity(false,currentContestId);
            active = false;
            sendMessage(getText(1473));
        } else {
            contestRepo.updateActivity(true,currentContestId);
            active = true;
            sendMessage(getText(1472));

        }
    }

    private void editDuration (String duration) throws TelegramApiException {
        currentContest = contestRepo.findById(currentContestId);
        trainingAndSeminarRepo.updateDuration(duration, currentContestId);
        sendMessage(getText(1461));
    }

}
