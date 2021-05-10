package com.example.czn.command.impl;

import com.example.czn.command.Command;
import com.example.czn.entity.standart.User;
import com.example.czn.util.type.WaitingType;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class id035_Mailing extends Command {
    @Override
    public boolean execute() throws SQLException, TelegramApiException {
        switch (waitingType) {
            case START:
                sendMessage(getText(1302));
                waitingType = WaitingType.EDITION;
                return COMEBACK;

            case EDITION:
                sendMessageWithKeyboard(getText(1303), 207);
                waitingType = WaitingType.CHOOSE_SURVEY;
                return COMEBACK;
            case CHOOSE_SURVEY:
                sendMessageWithKeyboard(getText(1331), 200);
                return false;
            }
            return true;
    }
}

