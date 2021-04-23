package com.example.czn.command.impl;

import com.example.czn.command.Command;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;

public class id017_EditResponsible extends Command {
    @Override
    public boolean execute() throws SQLException, TelegramApiException {
        sendMessage(111);
        return true;
    }
}
