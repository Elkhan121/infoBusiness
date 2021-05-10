package com.example.czn.command;


import com.example.czn.dao.repositories.*;
import com.example.czn.service.KeyboardService;
import com.example.czn.service.LanguageService;
import com.example.czn.util.BotUtil;
import com.example.czn.util.Const;
import com.example.czn.util.SetDeleteMessages;
import com.example.czn.util.UpdateUtil;
import com.example.czn.util.type.WaitingType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;


@NoArgsConstructor
@Component
public abstract class Command {


    @Getter
    @Setter
    protected long id;
    protected Long chatId;
    protected Update update;
    @Getter
    @Setter
    protected long messageId;
    protected String markChange;
    protected int updateMessageId;
    protected DefaultAbsSender bot;
    protected int lastSentMessageID;
    protected static BotUtil botUtils;
    protected String updateMessageText;
    protected String updateMessagePhoto;
    protected String updateMessagePhone;
    protected WaitingType waitingType = WaitingType.START;
    protected String editableTextOfMessage;
    protected static final String next = "\n";
    protected static final String space = " ";
    protected final static boolean EXIT = true;
    protected final static boolean COMEBACK = false;
    protected Message updateMessage;

    private KeyboardService keyboardService = new KeyboardService();

    protected  UserRepo userRepo = TelegramBorRepositoryProvider.getUserRepo();
    protected  AdminRepos adminRepos = TelegramBorRepositoryProvider.getAdminRepos();
    protected  ButtonRepo buttonRepo = TelegramBorRepositoryProvider.getButtonRepo();
    protected  MessageRepo messageRepo = TelegramBorRepositoryProvider.getMessageRepo();
    protected  KeyboardMarkUpRepo keyboardMarkUpRepo = TelegramBorRepositoryProvider.getKeyboardMarkUpRepo();
    protected  SuggestionRepo suggestionRepo = TelegramBorRepositoryProvider.getSuggestionRepo();
    protected  ComplaintRepo complaintRepo = TelegramBorRepositoryProvider.getComplaintRepo();
    protected  QuestionnaireRepo questionnaireRepo = TelegramBorRepositoryProvider.getQuestionnaireRepo();
    protected  QuestRepo questRepo = TelegramBorRepositoryProvider.getQuestRepo();
    protected  OperatorRepo operatorRepo = TelegramBorRepositoryProvider.getOperatorRepo();
    protected  SurveyRepo surveyRepo = TelegramBorRepositoryProvider.getSurveyRepo();
    protected  ResponsibleRepos responsibleRepos = TelegramBorRepositoryProvider.getResponsibleRepos();
    protected  ProjectRepo projectRepo = TelegramBorRepositoryProvider.getProjectRepo();
    protected  CertificateRepo certificateRepo = TelegramBorRepositoryProvider.getCertificateRepo();


    public abstract boolean execute() throws SQLException, TelegramApiException;

    protected int sendMessage(long messageId) throws TelegramApiException {
        return sendMessage(messageId, chatId);
    }

    protected int sendMessage(long messageId, long chatId) throws TelegramApiException {
        return sendMessage(messageId, chatId, null);
    }

    protected int sendMessage(long messageId, long chatId, Contact contact) throws TelegramApiException {
        return sendMessage(messageId, chatId, contact, null);
    }

    protected int sendMessage(long messageId, long chatId, Contact contact, String photo) throws TelegramApiException {
        lastSentMessageID = botUtils.sendMessage(messageId, chatId, contact, photo);
        return lastSentMessageID;
    }

    protected int sendMessage(String text) throws TelegramApiException {
        return sendMessage(text, chatId);
    }

    protected int sendMessage(String text, long chatId) throws TelegramApiException {
        return sendMessage(text, chatId, null);
    }

    protected int sendMessage(String text, long chatId, Contact contact) throws TelegramApiException {
        lastSentMessageID = botUtils.sendMessage(text, chatId);
        if (contact != null) {
            botUtils.sendContact(chatId, contact);
        }
        return lastSentMessageID;
    }

    protected void deleteMessage() {
        deleteMessage(chatId, lastSentMessageID);
    }

    protected void deleteMessage(int messageId) {
        deleteMessage(chatId, messageId);
    }

    protected void deleteMessage(long chatId, int messageId) {
        botUtils.deleteMessage(chatId, messageId);
    }

    protected String getText(int messageIdFromBD) {
        int lang = LanguageService.getLanguage(chatId).getId();
        com.example.czn.entity.standart.Message mes;
        try {
        mes = messageRepo.findByIdAndLangId(messageIdFromBD, lang);
        return mes.getName();
        }catch (Exception e){
            e.printStackTrace();
        }
        return messageRepo.findByIdAndLangId(messageIdFromBD, LanguageService.getLanguage(chatId).getId()).getName();
    }

    void clear() {
        clear();
        update = null;
        bot = null;
    }

    protected boolean isButton(int buttonId) {
        return updateMessageText.equals(buttonRepo.findByIdAndLangId(buttonId, LanguageService.getLanguage(chatId).getId()).getName());
    }

    public boolean isInitNotNormal(Update update, DefaultAbsSender bot) {
        if (botUtils == null) {
            botUtils = new BotUtil(bot);
        }
        this.update = update;
        this.bot = bot;
        chatId = UpdateUtil.getChatId(update);
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            updateMessage = callbackQuery.getMessage();
            updateMessageText = callbackQuery.getData();
            updateMessageId = updateMessage.getMessageId();
            editableTextOfMessage = callbackQuery.getMessage().getText();
        } else if (update.hasMessage()) {
            updateMessage = update.getMessage();
            updateMessageId = updateMessage.getMessageId();
            if (updateMessage.hasText()) {
                updateMessageText = updateMessage.getText();
            }
            if (updateMessage.hasPhoto()) {
                int size = update.getMessage().getPhoto().size();
                updateMessagePhoto = update.getMessage().getPhoto().get(size - 1).getFileId();
            } else {
                updateMessagePhoto = null;
            }
        }
        if (hasContact()) {
            updateMessagePhone = update.getMessage().getContact().getPhoneNumber();
        }
        if (markChange == null) {
            markChange = getText(Const.EDIT_BUTTON_ICON);
        }
        return false;
    }

    protected Logger getLogger() {
        return LoggerFactory.getLogger(this.getClass());
    }

    protected boolean hasContact() {
        return update.hasMessage() && update.getMessage().getContact() != null;
    }

    protected void sendFile(long chatId , String fileName,DefaultAbsSender bot ,String path) throws
            TelegramApiException {

        java.io.File file = new File(path);
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            bot.execute(new SendDocument().setChatId(chatId).setDocument(fileName, fileInputStream));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void sendMessageWithAddition() throws TelegramApiException {
        deleteMessage(updateMessageId);
        com.example.czn.entity.standart.Message message = messageRepo.findByIdAndLangId(messageId, LanguageService.getLanguage(chatId).getId());

        sendMessage(messageId, chatId, null, message.getPhoto());
        if (message.getKeyboardId() > 0 && message.getKeyboardId() != null){
            sendMessageWithKeyboard(getText((int)messageId),
                    keyboardService.getKeyboard(
                            keyboardMarkUpRepo.findById((long)message.getKeyboardId())
                    ,LanguageService.getLanguage(chatId)),
                    chatId
                    );
        }
        if (message.getFile() != null) {
            try {
                switch (message.getTypeFile()) {
                    case audio:
                        bot.execute(new SendAudio()
                                .setAudio(message.getFile())
                                .setChatId(chatId));
                    case video:
                        bot.execute(new SendVideo()
                                .setVideo(message.getFile())
                                .setChatId(chatId));
                    case document:
                        bot.execute(new SendDocument()
                                .setChatId(chatId)
                                .setDocument(message.getFile()));
                }
            } catch (TelegramApiException e) {
                getLogger().error("Exception by send file for message " + messageId, e);
            }
        }
    }

    protected boolean isAdmin() {
        return adminRepos.countByUserId(chatId) > 0;
//        return adminDao.isAdmin(chatId);
    }

    protected boolean isOperator(){
        return operatorRepo.countByUserId(chatId) > 0;
    }

    protected String getLinkForUser(long chatId, String userName) {
        return String.format("<a href = \"tg://user?id=%s\">%s</a>",(chatId), userName);
    }

    protected String getLinkForUseer(long chatId, String userName) {
        return String.format("<a href = \"https://t.me/username?id=%s\">%s</a>",(chatId), userName);
    }

    protected int toDeleteMessage(int messageDeleteId) {
        SetDeleteMessages.addMessage(chatId, messageDeleteId);
        return messageDeleteId;
    }

    protected int toDeleteKeyboard(int messageDeleteId) {
        SetDeleteMessages.addKeyboard(chatId, messageDeleteId);
        return messageDeleteId;
    }

    protected int sendMessageWithKeyboard(int messageId, ReplyKeyboard keyboard) throws TelegramApiException {
        return sendMessageWithKeyboard(getText(messageId), keyboard);
    }

    protected int sendMessageWithKeyboard(int messageId, long keyboardId) throws TelegramApiException{
        return sendMessageWithKeyboard(getText(messageId), keyboardService.getKeyboard(keyboardMarkUpRepo.findById(keyboardId), chatId));
    }

    protected int sendMessageWithKeyboard(String text, int keyboardId) throws TelegramApiException {
        return sendMessageWithKeyboard(text, (ReplyKeyboard) keyboardService.getKeyboard(keyboardMarkUpRepo.findById(keyboardId), chatId));
    }

    protected int sendMessageWithKeyboard(String text, ReplyKeyboard keyboard) throws TelegramApiException {
        lastSentMessageID = sendMessageWithKeyboard(text, keyboard, chatId);
        return lastSentMessageID;
    }

    protected int sendMessageWithKeyboard(String text, ReplyKeyboard keyboard, long chatId) throws TelegramApiException {
        return botUtils.sendMessageWithKeyboard(text, keyboard, chatId);
    }

    protected boolean hasCallbackQuery() {
        return update.hasCallbackQuery();
    }

    protected boolean hasPhoto() {
        return update.hasMessage() && update.getMessage().hasPhoto();
    }

    protected boolean hasDocument() {
        return update.hasMessage() && update.getMessage().hasDocument();
    }

    protected boolean hasAudio() {
        return update.hasMessage() && update.getMessage().getAudio() != null;
    }

    protected boolean hasVideo() {
        return update.hasMessage() && update.getMessage().getVideo() != null;
    }

    protected boolean hasMessageText() { return  update.hasMessage() && update.getMessage().hasText(); }
}
