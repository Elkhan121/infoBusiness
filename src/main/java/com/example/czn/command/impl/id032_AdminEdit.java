package com.example.czn.command.impl;

import com.example.czn.command.Command;
import com.example.czn.entity.standart.Admin;
import com.example.czn.entity.standart.User;
import com.example.czn.util.ButtonsLeaf;
import com.example.czn.util.Const;
import com.example.czn.util.DateUtil;
import com.example.czn.util.type.WaitingType;
import org.apache.poi.ss.formula.functions.T;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.transaction.Transactional;
import java.io.BufferedWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class id032_AdminEdit extends Command {
    List<Admin> admins;
    private int mess;
    private StringBuilder text;
    private static String delete;
    private static String deleteIcon;
    private static String showIcon;

    @Override
    public boolean execute() throws SQLException, TelegramApiException {
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
                if (admins.size() > 1) {
                    int numberAdminList = Integer.parseInt(updateMessageText.replaceAll("[^0-9]", ""));
                    adminRepos.delete(admins.get(numberAdminList));
                }
            }
            sendEditorAdmin();
            return COMEBACK;
    }

    private boolean registerNewAdmin() throws TelegramApiException, SQLException {
        long newAdminChatId = update.getMessage().getContact().getUserID();
        if (userRepo.countByChatId(newAdminChatId) == 0) {
            sendMessage(Const.USER_DO_NOT_REGISTERED);
            return true;
        } else {
            if (adminRepos.countByUserId(newAdminChatId) > 0) {
                sendMessage(Const.USER_IS_ADMIN);
                return true;
            } else {
                User user = userRepo.findByChatId(newAdminChatId);
                String str = String.format("%s %s %s", user.getUserName(), user.getPhone(), DateUtil.getDbMmYyyyHhMmSs(new Date()));
                Admin admin = new Admin(newAdminChatId, str);
                admin.setUserId(newAdminChatId);
                admin.setComment(str);
                adminRepos.save(admin);
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
        admins = adminRepos.findAll();
        int count = 0;
        for (Admin admin : admins) {
            try {
                User user = userRepo.findByChatId(admin.getUserId());
                if (admins.size() == 1) {
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