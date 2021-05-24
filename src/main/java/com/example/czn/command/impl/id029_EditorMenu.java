package com.example.czn.command.impl;

import com.example.czn.command.Command;
import com.example.czn.entity.standart.Button;
import com.example.czn.entity.standart.Language;
import com.example.czn.service.LanguageService;
import com.example.czn.util.ButtonsLeaf;
import com.example.czn.util.Const;
import com.example.czn.util.type.WaitingType;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class id029_EditorMenu extends Command {
    private List<Button> buttonList;
    private Language currentLanguage;
    private ButtonsLeaf buttonsLeaf;

    @Override
    public boolean execute() throws SQLException, TelegramApiException {
        if (!isAdmin()) {
            sendMessage(Const.NO_ACCESS);
            return EXIT;
        }
        switch (waitingType) {
            case START:
                sendMessageWithKeyboard(getText(1324), 202);
                waitingType = WaitingType.TWO;
                return COMEBACK;
        }
        return true;
    }
}
