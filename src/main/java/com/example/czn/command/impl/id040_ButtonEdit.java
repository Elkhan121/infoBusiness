package com.example.czn.command.impl;

import com.example.czn.command.Command;
import com.example.czn.entity.standart.Button;
import com.example.czn.entity.standart.ButtonFile;
import com.example.czn.entity.standart.Language;
import com.example.czn.entity.standart.Message;
import com.example.czn.service.KeyboardService;
import com.example.czn.service.LanguageService;
import com.example.czn.util.type.WaitingType;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;

public class id040_ButtonEdit extends Command {
    private Message currentMessage;
    private Language currentLanguage;
    private Button currentButton;
    private int delete;
    private int keyboardId;

    @Override
    public boolean execute() throws SQLException, TelegramApiException {
        switch (waitingType) {
            case START:
                currentLanguage = LanguageService.getLanguage(chatId);
                sendListMenu(15, 210, currentLanguage);
                waitingType = WaitingType.ONE;
                return COMEBACK;
            case ONE:
                deleteMessage();
                if (hasCallbackQuery()) {
                    purpose(updateMessageText);
                    getButtonInfo();
                    keyboardId = buttonNext(currentButton.getId(),keyboardId);
                    return COMEBACK;
                }
            case TWO:
                if (isButton(219)) {
                    delete = sendMessage(getText(1445));
                    waitingType = WaitingType.BUTTON_EDITION_NAME;
                    return COMEBACK;
                } else if (isButton(220)) {
                    delete = sendMessage(getText(1446));
                    waitingType = WaitingType.BUTTON_EDITION_MESSAGE;
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
                    internalMenu();
                    return COMEBACK;
                } else if (isButton(221)){
                    deleteMessage();
                    cancel(221);
                    return COMEBACK;
                }
                return COMEBACK;
            case BUTTON_EDITION_NAME:
                updateButtonName(updateMessageText
                        , currentButton.getId()
                        , currentButton.getLangId());
                waitingType = WaitingType.TWO;
                return COMEBACK;
            case BUTTON_EDITION_MESSAGE:
                currentMessage = messageRepo.findByIdAndLangId(currentButton.getMessageId(), currentButton.getLangId());
                updateButtonText(updateMessageText
                        , currentMessage.getId()
                        , currentMessage.getLangId());
                waitingType = WaitingType.TWO;
                return COMEBACK;
            case DELETE_FILE:
                if(cancel(222) == false){
                    currentMessage = messageRepo.findByIdAndLangId(currentButton.getMessageId(), currentButton.getLangId());
                    waitingType = WaitingType.ONE;
                    return COMEBACK;
                }

                deleteButtonFile(currentButton.getId());
                waitingType = WaitingType.TWO;
                return COMEBACK;
            case ADD_FILE:
                addButtonFile(currentButton.getId()
                        , update.getMessage().getDocument().getFileId());
                waitingType = WaitingType.TWO;
                return COMEBACK;
        }
        return EXIT;
    }

    private void purpose(String text) {
        currentButton = buttonRepo.findByIdAndLangId(Integer.parseInt(text), currentLanguage.getId());
    }

    private boolean cancel(int buttonId) throws TelegramApiException {
        if (isButton(buttonId)) {
            sendListMenu(15,210,currentLanguage);
            waitingType = WaitingType.ONE;
            return COMEBACK;
        } else {
            return true;
        }
    }

    private int buttonNext(int buttonId,int keyboardId){
        if(buttonId == 97){
            keyboardId = 212;
        } else if(buttonId == 98){
            keyboardId = 200;
        } else if(buttonId == 99){
            keyboardId = 211;
        } else if(buttonId == 100){
            keyboardId = 210;
        }
        return keyboardId;
    }

    private void internalMenu() throws TelegramApiException {
        sendListMenu(15,keyboardId,currentLanguage);
        waitingType = WaitingType.ONE;
    }

}