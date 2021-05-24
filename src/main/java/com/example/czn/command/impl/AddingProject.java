package com.example.czn.command.impl;

import com.example.czn.command.Command;
import com.example.czn.util.type.WaitingType;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;

public class AddingProject extends Command {
    private int delete;
    private String ruName;
    private String kzName;
    private String ruDescription;
    private String kzDescription;
    private int salary;

    @Override
    public boolean execute() throws SQLException, TelegramApiException {
        switch (waitingType) {
            case START:
                delete = sendMessage(getText(1462));
                waitingType = WaitingType.TWO;
                return COMEBACK;
            case TWO:
                deleteMessage(delete);
                deleteMessage(updateMessageId);
                kzName = updateMessageText;
                delete = sendMessage(getText(1463));
                waitingType = WaitingType.THREE;
                return COMEBACK;
            case THREE:
                deleteMessage(delete);
                deleteMessage(updateMessageId);
                ruName = updateMessageText;
                delete = sendMessage(getText(1465));
                waitingType = WaitingType.FOUR;
                return COMEBACK;
            case FOUR:
                deleteMessage(delete);
                deleteMessage(updateMessageId);
                kzDescription = updateMessageText;
                delete = sendMessage(getText(1466));
                waitingType = WaitingType.FIVE;
                return COMEBACK;
            case FIVE:
                deleteMessage(delete);
                deleteMessage(updateMessageId);
                ruDescription = updateMessageText;
                delete = sendMessage(getText(1467));
                waitingType = WaitingType.SIX;
                return COMEBACK;
            case SIX:
                deleteMessage(delete);
                deleteMessage(updateMessageId);
                salary = Integer.parseInt(updateMessageText);
                sendMessage(getText(1461));
                waitingType = WaitingType.TWO;
                return COMEBACK;
        }
        return EXIT;
    }
}
