package com.example.czn.command.impl;

import com.example.czn.command.Command;
import com.example.czn.dao.repositories.ComplaintRepo;
import com.example.czn.service.ComplaintReportService;
import com.example.czn.util.Const;
import com.example.czn.util.DateKeyboard;
import com.example.czn.util.type.WaitingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;
import java.util.Date;

public class id010_ReportComplaint extends Command {
    @Autowired
    ComplaintRepo complaintRepo;

    private DateKeyboard dateKeyboard;
    private Date start;
    private Date end;

    @Override
    public boolean execute() throws SQLException, TelegramApiException {
        if (!isAdmin()) {
            sendMessage(Const.NO_ACCESS);
            return EXIT;
        }
        switch (waitingType) {
            case START:
                dateKeyboard = new DateKeyboard();
                sendStartDate();
                waitingType = WaitingType.START_DATE;
                return COMEBACK;
            case START_DATE:
                if (hasCallbackQuery()) {
                    deleteMessage();
                    if (dateKeyboard.isNext(updateMessageText)) {
                        sendStartDate();
                        return COMEBACK;
                    }
                    start = dateKeyboard.getDateDate(updateMessageText);
                    sendEndDate();
                    waitingType = WaitingType.END_DATE;
                    return COMEBACK;
                }
                return COMEBACK;
            case END_DATE:
                if (hasCallbackQuery()) {
                    deleteMessage();
                    if (dateKeyboard.isNext(updateMessageText)) {
                        sendStartDate();
                        return COMEBACK;
                    }
                    end = dateKeyboard.getDateDate(updateMessageText);
                    sendReport();
                    waitingType = WaitingType.END_DATE;
                    return COMEBACK;
                }
                return COMEBACK;
        }
        sendLightReport();
        return EXIT;
    }

    private void sendReport() throws TelegramApiException {
        int preview = sendMessage(Const.REPORT_PREPARED);
        ComplaintReportService reportService = new ComplaintReportService();
        reportService.sendComplaintReport(chatId, bot, start, end, preview);
    }

    private void sendStartDate() throws TelegramApiException {
        toDeleteKeyboard(sendMessageWithKeyboard(sendLightReport() + getText(Const.SELECT_START_DATE), dateKeyboard.getCalendarKeyboard()));
    }

    private void sendEndDate() throws TelegramApiException {
        toDeleteKeyboard(sendMessageWithKeyboard(Const.SELECT_END_DATE, dateKeyboard.getCalendarKeyboard()));
    }

    private String sendLightReport() {
        String result = getText(Const.NUMBER_OF_COMPLAINT);
        result = String.format(result, complaintRepo.findAll().size());
        return result;
    }

}
