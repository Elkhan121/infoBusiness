package com.example.czn.command.impl;

import com.example.czn.command.Command;
import com.example.czn.entity.custom.Certificate;
import com.example.czn.service.CertificateService;
import com.example.czn.service.QuestionnaireService;
import com.example.czn.util.ButtonsLeaf;
import com.example.czn.util.Const;
import com.example.czn.util.DateKeyboard;
import com.example.czn.util.type.WaitingType;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class id038_Certificate_req extends Command {
    DateKeyboard dateKeyboard = new DateKeyboard();
    Date startDate;
    Date stopDate;
    private int delete;
    List<Certificate> certificates;
    private ButtonsLeaf buttonsLeaf;

    @Override
    public boolean execute() throws SQLException, TelegramApiException {
        switch (waitingType) {
            case START:
                dateKeyboard = new DateKeyboard();
                sendStartDate();
                waitingType = WaitingType.ONE;
                return COMEBACK;
            case ONE:
                if (update.hasCallbackQuery()) {
                    deleteMessage();
                    if (dateKeyboard.isNext(updateMessageText)) {
                        sendStartDate();
                        return COMEBACK;
                    }
                    startDate = dateKeyboard.getDateDate(updateMessageText);
                    sendEndDate();
//                    delete = sendMessageWithKeyboard(getText(1301), dateKeyboard.getCalendarKeyboard());
                    waitingType = WaitingType.TWO;
                }
                return COMEBACK;
            case TWO:
                if (hasCallbackQuery()) {
                    deleteMessage(delete);
                    if (dateKeyboard.isNext(updateMessageText)) {
                        sendStartDate();
                        return COMEBACK;
                    }
                    stopDate = dateKeyboard.getDateDate(updateMessageText);
                    sendCertReport();
                    waitingType = WaitingType.TWO;
                    return COMEBACK;
                }
                return COMEBACK;
        }
         sendLightReport();
        return EXIT;
//                waitingType = WaitingType.FOUR;
//            case FOUR:
//                sendCertReport();
//                certificates = certificateRepo.findAllByDateBetween(startDate, stopDate);
//                List<String> second = new ArrayList<>();
//                certificates.forEach(certificate -> {
//                    second.add(certificate.getFull_name());
//                });
//                buttonsLeaf = new ButtonsLeaf(second, certificates.size());
//                sendMessageWithKeyboard(getText(1305) + "\n", buttonsLeaf.getListButton(), chatId);
//                waitingType = WaitingType.FIVE;
//                return COMEBACK;
//        }
//        return EXIT;
        }
    private void sendCertReport() throws TelegramApiException {
        int preview = sendMessage(Const.REPORT_PREPARED);
        CertificateService certificateService = new CertificateService();
        certificateService.sendCertificateReport(chatId, bot, startDate, stopDate, preview);
    }

    private int sendStartDate() throws TelegramApiException {
        return toDeleteKeyboard(sendMessageWithKeyboard(sendLightReport() + getText(Const.SELECT_START_DATE), dateKeyboard.getCalendarKeyboard()));
    }

    private int sendEndDate() throws TelegramApiException {
        return toDeleteKeyboard(sendMessageWithKeyboard(Const.SELECT_END_DATE, dateKeyboard.getCalendarKeyboard()));
    }

    private String sendLightReport() {
        String result = getText(Const.NUMBER_OF_SUGGESTION);
        result = String.format(result, questionnaireRepo.findAll().size());
        return result;
    }
}

