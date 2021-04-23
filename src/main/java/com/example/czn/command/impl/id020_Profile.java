package com.example.czn.command.impl;

import com.example.czn.command.Command;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;

public class id020_Profile extends Command {
    @Override
    public boolean execute() throws SQLException, TelegramApiException {
        botUtils.sendMessage("Ваш username: " + update.getMessage().getFrom().getUserName(), chatId);
        botUtils.sendMessage("Ваш chatId: " + chatId, chatId);
        return true;
    }
}
