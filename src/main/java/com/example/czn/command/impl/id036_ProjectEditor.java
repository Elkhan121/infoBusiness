package com.example.czn.command.impl;

import com.example.czn.command.Command;
import com.example.czn.dao.repositories.ProjectRepo;
import com.example.czn.entity.standart.Projects;
import com.example.czn.util.ButtonsLeaf;
import com.example.czn.util.type.WaitingType;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class id036_ProjectEditor extends Command {
    private List<Projects> projects;
    private ButtonsLeaf buttonsLeaf;
    Projects project = new Projects();
    private long id;

    @Override
    public boolean execute() throws SQLException, TelegramApiException {
        switch (waitingType) {
            case START:
                sendMessageWithKeyboard(getText(1332), 208);
                projects = projectRepo.findAll();
                List<String> projectsNames = new ArrayList<>();
                projects.forEach(project -> {
                    projectsNames.add(project.getName());
                });
                buttonsLeaf = new ButtonsLeaf(projectsNames, projects.size());
                sendMessageWithKeyboard(getText(1333) + "\n", buttonsLeaf.getListButton(), chatId);
                waitingType = WaitingType.ONE;
                return COMEBACK;
            case ONE:
                if (hasCallbackQuery()) {
                    if (isButton(216)) {
                        waitingType = WaitingType.TWO;
                        return COMEBACK;
                    }
                    id = projects.get(Integer.parseInt(updateMessageText)).getId();
                    sendMessageWithKeyboard(
                             "\n" + "№: " + projects.get(Integer.parseInt(updateMessageText)).getId()
                               + "\n" + getText(1337) + projects.get(Integer.parseInt(updateMessageText)).getName()
                               + "\n" + getText(1338) + projects.get(Integer.parseInt(updateMessageText)).getCategory()
                            , 209);
                    waitingType = WaitingType.FIVE;
                    return COMEBACK;
                }
            case TWO:
                try {
                    sendMessage(getText(1334));
                    waitingType = WaitingType.THREE;
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                return COMEBACK;
            case THREE:
                project.setName(updateMessage.getText());
                sendMessage(getText(1335));
                waitingType = WaitingType.FOUR;
                return COMEBACK;
            case FOUR:
                project.setCategory(updateMessage.getText());
                projectRepo.save(project);
                sendMessageWithKeyboard(getText(1336), 202);
                return COMEBACK;
            case FIVE:
                if (isButton(217)) {
                    projectRepo.deleteById(id);
                    sendMessageWithKeyboard(getText(1339), 202);
                    waitingType = WaitingType.TWO;
                    return COMEBACK;
                }
        }
        return true;
    }
}