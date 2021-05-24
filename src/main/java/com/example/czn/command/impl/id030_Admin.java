package com.example.czn.command.impl;

import com.example.czn.command.Command;
import com.example.czn.util.Const;
import com.example.czn.util.type.WaitingType;
//import org.apache.poi.ss.formula.functions.T;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;

public class id030_Admin extends Command {
    @Override
    public boolean execute() throws SQLException, TelegramApiException {
        if (!isAdmin()) {
            sendMessage(Const.NO_ACCESS);
            return EXIT;
        }

        switch (waitingType) {
            case START:
                    try {
                        sendMessageWithKeyboard(getText(1322), 200);
                        waitingType = WaitingType.ONE;
                        return COMEBACK;
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
        }
        return true;
    }
}
