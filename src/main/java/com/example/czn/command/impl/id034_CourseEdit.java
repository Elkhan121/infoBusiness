package com.example.czn.command.impl;

import com.example.czn.command.Command;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;

public class id034_CourseEdit extends Command {

    private int delete;

    @Override
    public boolean execute() throws SQLException, TelegramApiException {

        switch (waitingType) {
            case START:
                delete = sendMessageWithKeyboard(getText(1330) + "\n",206);
                if (isButton(211)) {
                    sendMessageWithKeyboard("Добавление курса" , 202);
                    deleteMessage(delete);
                }
        }

        return EXIT;
    }
}
