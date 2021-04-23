package com.example.czn.command.impl;

import com.example.czn.command.Command;
import com.example.czn.dao.repositories.ComplaintRepo;
import com.example.czn.entity.custom.Complaint;
import com.example.czn.util.Const;
import com.example.czn.util.type.WaitingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;
import java.util.Date;

public class id009_Complaint extends Command {
    @Autowired
    ComplaintRepo complaintRepo;
    Complaint complaint;

    @Override
    public boolean execute() throws SQLException, TelegramApiException {
        switch (waitingType) {
            case START:
                getFullName();
                waitingType = WaitingType.SET_FULL_NAME;
                return COMEBACK;
            case SET_FULL_NAME:
                if (update.hasMessage() && update.getMessage().hasText() && update.getMessage().getText().length() <= 50) {
                    complaint = new Complaint();
                    complaint.setFullName(update.getMessage().getText());
                    complaint.setPostDate(new Date());
                    getPhone();
                    waitingType = WaitingType.SET_PHONE_NUMBER;
                    return COMEBACK;
                } else {
                    wrongData();
                    getFullName();
                    return COMEBACK;
                }
            case SET_PHONE_NUMBER:
                if (botUtils.hasContactOwner(update)) {
                    complaint.setPhoneNumber(update.getMessage().getContact().getPhoneNumber());
                    getLocation();
                    waitingType = WaitingType.SET_LOCATION;
                    return COMEBACK;
                } else {
                    wrongData();
                    getPhone();
                    return COMEBACK;
                }
            case SET_LOCATION:
                if (update.hasMessage() && update.getMessage().hasText()) {
                    complaint.setLocation(update.getMessage().getText());
                    getComplaint();
                    waitingType = WaitingType.COMPLAINT;
                    return COMEBACK;
                } else {
                    wrongData();
                    getLocation();
                    return COMEBACK;
                }
            case COMPLAINT:
                if (update.hasMessage() & update.getMessage().hasText()) {
                    complaint.setText(update.getMessage().getText());
                    //complaintDao.insert(complaint);
                    complaintRepo.save(complaint);
                    sendMessage(Const.COMPLAINT_DONE);
                    return EXIT;
                } else {
                    wrongData();
                    getComplaint();
                    return COMEBACK;
                }
        }
        return EXIT;
    }

    private int wrongData() throws TelegramApiException {
        return botUtils.sendMessage(Const.WRONG_DATA_TEXT, chatId);
    }

    private int getFullName() throws TelegramApiException {
        return botUtils.sendMessage(Const.SET_FULL_NAME, chatId);
    }

    private int getPhone() throws TelegramApiException {
        return botUtils.sendMessage(Const.SET_MOBILE_PHONE_NUMBER, chatId);
    }

    private int getLocation() throws TelegramApiException {
        return botUtils.sendMessage(Const.SET_LOCATION, chatId);
    }

    private int getComplaint() throws TelegramApiException {
        return botUtils.sendMessage(Const.SET_COMPLAINT, chatId);
    }
}

