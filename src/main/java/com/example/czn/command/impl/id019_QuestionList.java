package com.example.czn.command.impl;

import com.example.czn.command.Command;
import com.example.czn.dao.repositories.AdminQuestionRepo;
import com.example.czn.dao.repositories.TelegramBorRepositoryProvider;
import com.example.czn.entity.custom.AdminQuest;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class id019_QuestionList extends Command {
    @Override
    public boolean execute() throws SQLException, TelegramApiException {
//        AdminQuestionDao adminQuestionDao = new AdminQuestionDao();
        AdminQuestionRepo adminQuestionRepo = TelegramBorRepositoryProvider.getAdminQuestionRepo();

        List<AdminQuest> adminQuests = adminQuestionRepo.findAll();
        StringBuilder stringBuilder = new StringBuilder();
        AtomicInteger i = new AtomicInteger();
        adminQuests.forEach(adminQuest -> {
            i.getAndIncrement();
            stringBuilder.append(i).append(")").append(adminQuest.getQuestion()).append(" от ").append(adminQuest.getName()).append("\n");
        });
        sendMessage(stringBuilder.toString());
         return true;

    }
}
