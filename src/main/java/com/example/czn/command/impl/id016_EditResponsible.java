package com.example.czn.command.impl;

import com.example.czn.command.Command;
import com.example.czn.dao.repositories.AdminRepos;
import com.example.czn.dao.repositories.ResponsibleRepos;
import com.example.czn.dao.repositories.UserRepo;
import com.example.czn.entity.standart.Admin;
import com.example.czn.entity.standart.Responsible;
import com.example.czn.entity.standart.User;
import com.example.czn.util.Const;
import com.example.czn.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class id016_EditResponsible extends Command {
//    @Autowired
//    ResponsibleRepos responsibleRepos;
//    @Autowired
//    UserRepo userRepo;
//    @Autowired
//    AdminRepos adminRepos;

//    ResponsibleDao responsibleDao = new ResponsibleDao();
    private int mess;
    private static String delete;
    private static String deleteIcon;
    private static String showIcon;
    private StringBuilder text;
    private List<Admin> allAdmins;
    private List<Responsible> allRes;

    @Override
    public boolean execute() throws SQLException, TelegramApiException {
        if (!isAdmin()) {
            sendMessage(Const.NO_ACCESS);
            return EXIT;
        }
        if (deleteIcon == null) {
            deleteIcon = getText(Const.ICON_CROSS);
            showIcon = getText(Const.ICON_LOUPE);
            delete = getText(Const.DELETE_BUTTON_SLASH);
        }
        if (mess != 0) {
            deleteMessage(mess);
        }
        if (hasContact()) {
            registerNewAdmin();
            return COMEBACK;
        }
        if (updateMessageText.contains(delete)) {
            if (allRes.size() > 1) {
                int numberAdminList = Integer.parseInt(updateMessageText.replaceAll("[^0-9]",""));
//                responsibleDao.delete(allAdmins.get(numberAdminList));
                responsibleRepos.delete(allRes.get(numberAdminList));
            }
        }
        sendEditorAdmin();
        return COMEBACK;
    }

    private boolean registerNewAdmin() throws TelegramApiException, SQLException {
        long newAdminChatId = update.getMessage().getContact().getUserID();
//        if (!usersDao.isRegistered(newAdminChatId)) {
            if (userRepo.countByChatId(newAdminChatId) == 0){
            sendMessage(Const.USER_DO_NOT_REGISTERED);
            return true;
        } else {
//            if (responsibleDao.isAdmin(newAdminChatId)) {
                if (responsibleRepos.findAllByUserId(newAdminChatId).size() > 0){
                sendMessage(Const.USER_IS_ADMIN);
                return true;
            } else {
                User user = userRepo.findByChatId(newAdminChatId);
                    Responsible responsible = new Responsible();
              //  responsibleDao.addAssistant(newAdminChatId,String.format("%s %s %s", user.getUserName(), user.getPhone(), DateUtil.getDbMmYyyyHhMmSs(new Date())));
                String str = String.format("%s %s %s", user.getUserName(), user.getPhone(), DateUtil.getDbMmYyyyHhMmSs(new Date()));
                responsible.setComment(str);
                responsible.setUserId(newAdminChatId);
                responsibleRepos.save(responsible);
                User userAdmin = userRepo.findByChatId(chatId);
                getLogger().info("{} added new admin - {} ", getInfoByUser(userAdmin), getInfoByUser(user));
                sendEditorAdmin();
            }
        }
        return false;
    }

    private void sendEditorAdmin() throws SQLException, TelegramApiException {
        deleteMessage();
        try {
            getText(true);
            mess = sendMessage(String.format(getText(Const.ADMIN_SHOW_LIST), text.toString()));
        } catch (TelegramApiException e) {
            getText(false);
            mess = sendMessage(String.format(getText(Const.ADMIN_SHOW_LIST), text.toString()));
        }
        toDeleteMessage(mess);
    }

    private String getInfoByUser(User user) {
        return String.format("%s %s %s", user.getFullName(), user.getPhone(), user.getChatId());
    }

    private void getText(boolean withLink) throws SQLException {
        text = new StringBuilder("");
        allAdmins = adminRepos.findAll();
        int count = 0;
        for (Admin admin : allAdmins) {
            try {
                User user = userRepo.findByChatId(admin.getUserId());
                if (allAdmins.size() == 1) {
                    if (withLink) {
                        text.append(getLinkForUser(user.getChatId(), user.getUserName())).append(space).append(next);
                    } else {
                        text.append(getInfoByUser(user)).append(space).append(next);
                    }
                    text.append(getText(Const.WARNING_INFO_ABOUT_ADMIN)).append(next);
                    count++;
                } else {
                    if (withLink) {
                        text.append(delete).append(count).append(deleteIcon).append(" - ").append(showIcon).append(getLinkForUser(user.getChatId(), user.getUserName())).append(space).append(next);
                    } else {
                        text.append(delete).append(count).append(deleteIcon).append(" - ").append(getInfoByUser(user)).append(space).append(next);
                    }
                    count++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
