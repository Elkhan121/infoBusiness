package com.example.czn.command.impl;

import com.example.czn.command.Command;
import com.example.czn.entity.standart.Button;
import com.example.czn.entity.standart.Language;
import com.example.czn.entity.standart.Message;
import com.example.czn.service.LanguageService;
import com.example.czn.util.type.WaitingType;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;

public class id041_ButtonNameEditor extends Command {
    private Language currentLanguage = LanguageService.getLanguage(1125002748);
    private int keyboardId;
    private Button currentButton;
    private Message currentMessage;
    private int delete;

    @Override
    public boolean execute() throws SQLException, TelegramApiException {
        switch (waitingType) {
            case START:
                sendListMenu(15, 202, currentLanguage);
                waitingType = WaitingType.TWO;
                return COMEBACK;
            case TWO:
                purpose(updateMessageText);
                getButtonInfo();
                waitingType = WaitingType.THREE;
                keyboardId = buttonNext(currentButton.getId(),keyboardId);
                return COMEBACK;
            case THREE:
                if (isButton(219)) {
                    delete = sendMessage(getText(1445));
                    waitingType = WaitingType.BUTTON_EDITION_NAME;
                    return COMEBACK;
                } else if (isButton(220)) {
                    sendMessageWithKeyboard(getText(1456), 211);
                    return COMEBACK;
                } else if (isButton(218)) {
                    delete = sendMessageWithKeyboard(getText(1451), 212);
                    waitingType = WaitingType.DELETE_FILE;
                    return COMEBACK;
                } else if (isButton(224)) {
                    delete = sendMessage(getText(1453));
                    waitingType = WaitingType.ADD_FILE;
                    return COMEBACK;
                } else if (isButton(225)) {
                    waitingType = WaitingType.INTERNAL_MENU;
                    return COMEBACK;
                }
                return COMEBACK;
            case BUTTON_EDITION_NAME:
                updateButtonName(updateMessageText, currentButton.getId(), currentLanguage.getId());
                waitingType = WaitingType.THREE;
                return COMEBACK;
            case ADD_FILE:
                addButtonFile(currentButton.getId(), update.getMessage().getDocument().getFileId());
                waitingType = WaitingType.THREE;
                return COMEBACK;
            case DELETE_FILE:
                if(cancel() == false){
                    waitingType = WaitingType.ONE;
                    return COMEBACK;
                }
                deleteButtonFile(currentButton.getId());
                waitingType = WaitingType.THREE;
                return COMEBACK;
            case INTERNAL_MENU:
                sendListMenu(15,keyboardId,currentLanguage);
                waitingType = WaitingType.THREE;
                return COMEBACK;
        }
        return EXIT;
    }

    private void purpose(String text) {
        currentButton = buttonRepo.findByIdAndLangId(Integer.parseInt(text), currentLanguage.getId());
    }

    private boolean cancel() throws TelegramApiException {
        if (isButton(222)) {
            sendListMenu(15,202,currentLanguage);
            waitingType = WaitingType.TWO;
            return true;
        } else {
            return false;
        }
    }

    private int buttonNext(int buttonId,int keyboardId){
        if(buttonId == 97){
            keyboardId = 204;
        } else if(buttonId == 98){
            keyboardId = 75;
        } else if(buttonId == 99){
            keyboardId = 3;
        } else if(buttonId == 100){
            keyboardId = 7;
        }
        return keyboardId;
    }

}
