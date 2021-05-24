package com.example.czn.command.impl;

import com.example.czn.command.Command;
import com.example.czn.entity.standart.Contest;
import com.example.czn.util.type.WaitingType;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.sql.SQLException;
import static com.example.czn.command.impl.id044_ContestEdit.currentContestId;

public class id045_RefactorProjectName extends Command {

    private Contest currentContest;
    private String kzText;
    private String ruText;
    private int delete;

    @Override
    public boolean execute() throws SQLException, TelegramApiException {
        chatId = (long)1125002748;
        switch (waitingType){
            case START:
                delete = sendMessage(getText(1462));
                waitingType = WaitingType.TWO;
                return COMEBACK;
            case TWO:
                kzText = updateMessageText;
                deleteMessage(updateMessageId);
                deleteMessage(delete);
                delete = sendMessage(getText(1463));
                waitingType = WaitingType.THREE;
                return COMEBACK;
            case THREE:
                ruText = updateMessageText;
                deleteMessage(delete);
                deleteMessage(updateMessageId);
                save(kzText,ruText);
                delete = sendMessageWithKeyboard(getText(1461), 215);
                return COMEBACK;
        }
        return EXIT;
    }

    private void save(String kzText, String ruText) throws TelegramApiException {
        currentContest = contestRepo.findById(currentContestId);
        contestRepo.update(kzText, ruText, currentContestId);
        sendMessageWithKeyboard(getText(1461), 215);
        waitingType = WaitingType.START;
    }
}
