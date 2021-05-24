package com.example.czn.command.impl;

import com.example.czn.command.Command;
import com.example.czn.entity.standart.Contest;
import com.example.czn.util.type.WaitingType;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;

import static com.example.czn.command.impl.id044_ContestEdit.currentContestId;

public class id046_RefactorProjectDescription extends Command {
    private Contest currentContest;
    private String kzText;
    private String ruText;
    private int delete;
    @Override
    public boolean execute() throws SQLException, TelegramApiException {
        switch(waitingType){
            case START:
                delete = sendMessage(getText(1465));
                waitingType = WaitingType.TWO;
                return COMEBACK;
            case TWO:
                kzText = updateMessageText;
                delete = sendMessage(getText(1466));
                waitingType = WaitingType.THREE;
                return COMEBACK;
            case THREE:
                ruText = updateMessageText;
                save(kzText,ruText);
                delete = sendMessage(getText(1461));
                return COMEBACK; }
        return EXIT;
    }

    private void save(String kzText, String ruText) throws TelegramApiException {
        currentContest = contestRepo.findById(currentContestId);
        contestRepo.updateDescription(kzText, ruText, currentContestId);
        sendMessageWithKeyboard(getText(1461), 215);
        waitingType = WaitingType.START;
    }
}
