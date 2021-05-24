package com.example.czn.command.impl;

import com.example.czn.command.Command;
import com.example.czn.dao.repositories.ButtonRepo;
import com.example.czn.dao.repositories.KeyboardMarkUpRepo;
import com.example.czn.dao.repositories.MessageRepo;
import com.example.czn.dao.repositories.TelegramBorRepositoryProvider;
import com.example.czn.entity.standart.Button;
import com.example.czn.entity.standart.Language;
import com.example.czn.entity.standart.Message;
import com.example.czn.service.LanguageService;
import com.example.czn.service.ParserMessageEntity;
import com.example.czn.util.ButtonUtil;
import com.example.czn.util.Const;
import com.example.czn.util.FileType;
import com.example.czn.util.type.WaitingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;
import java.util.List;

import static com.google.common.net.HttpHeaders.LINK;

@Component
public class id005_EditMenu extends Command {

    private Language currentLanguage;
    private Button currentButton;
    private int textId;
    private int photoId;
    private String buttonName;
    private Message message;
    private int keyId;
    private static final String linkEdit = "/linkId";
    private int buttonLinkId;
    boolean buttonIds = false;
//    private static MessageRepo messageRepo = TelegramBorRepositoryProvider.getMessageRepo();
//    private final static String NAME = messageDao.getMessageText(Const.NAME_TEXT_FOR_LINK);
//    private final static String LINK = messageDao.getMessageText(Const.LINK_TEXT_FOR_EDIT);
    private static String NAME;

    @Override
    public boolean execute() throws SQLException, TelegramApiException {
        NAME = String.valueOf(messageRepo.findByIdAndLangId(Const.NAME_TEXT_FOR_LINK, LanguageService.getLanguage(chatId).getId()));
        if (!isAdmin()) {
            sendMessage(Const.NO_ACCESS);
            return EXIT;
        }
        switch (waitingType) {
            case START:
                currentLanguage = LanguageService.getLanguage(chatId);
                sendListMenu();
                waitingType = WaitingType.CHOOSE_OPTION;
                return COMEBACK;
            case CHOOSE_OPTION:
                deleteMessage();
                if (hasCallbackQuery()) {
                    int buttonId = Integer.parseInt(updateMessageText);
                    long keyboardMarkUpId = messageRepo.findByIdAndLangId(buttonRepo.findByIdAndLangId(buttonId, currentLanguage.getId()).getMessageId(), currentLanguage.getId()).getKeyboardId();
                    if (keyboardMarkUpId != 0) {
                        buttonIds = getButtonIds((int) keyboardMarkUpId);
                    }
                    if (keyboardMarkUpId == 2) {
                        currentButton = buttonRepo.findNameByIdAndLangId(buttonId, currentLanguage.getId());
                        sendEditor();
                        return COMEBACK;
                    } else if (keyboardMarkUpId > 0 & keyboardMarkUpId != 2) {
                        if (!buttonIds) {
                            toDeleteKeyboard(sendMessageWithKeyboard(Const.SELECT_BUTTON_FOR_EDIT, (ReplyKeyboard) keyboardMarkUpRepo.findById(keyboardMarkUpId)));
                            waitingType = WaitingType.CHOOSE_OPTION;
                        } else {
                            currentButton = buttonRepo.findNameByIdAndLangId(buttonId, currentLanguage.getId());
                            sendEditor();
                            return COMEBACK;
                        }
                    } else {
                        currentButton = buttonRepo.findNameByIdAndLangId(buttonId, currentLanguage.getId());
                        sendEditor();
                        return COMEBACK;
                    }
                } else {
                    sendListMenu();
                }
                return COMEBACK;
            case COMMAND_EDITOR:
                isCommand();
                return COMEBACK;
            case UPDATE_BUTTON:
                if (isCommand()) {
                    return COMEBACK;
                }
                if (hasMessageText()) {
                    String buttonName = (ButtonUtil.getButtonName(updateMessageText, 100));
                    if (buttonName.replaceAll("[0-9]", "").isEmpty()) {
                        sendMessage(Const.WRONG_NAME_BUTTON_TEXT);
                        return COMEBACK;
                    }
                        if (buttonRepo.countButtonByNameAndLangId(buttonName, currentLanguage.getId()) > 0){
                        sendMessage(Const.BUTTON_NAME_BUSY);
                        return COMEBACK;
                    }
                    currentButton.setName(buttonName);
                    buttonRepo.save(currentButton);
                    sendEditor();
                    return COMEBACK;
                }
                return COMEBACK;
            case UPDATE_TEXT:
                if (isCommand()) {
                    return COMEBACK;
                }
                if (hasMessageText()) {
                    message.setName(new ParserMessageEntity().getTextWithEntity(update.getMessage()));
                    messageRepo.save(message);
                    sendEditor();
                    return COMEBACK;
                }
                return COMEBACK;
            case UPDATE_BUTTON_LINK:
                if (isCommand()) {
                    return COMEBACK;
                }
                if (hasMessageText()) {
                    if (updateMessageText.startsWith(NAME)) {
                        String buttonName = ButtonUtil.getButtonName(updateMessageText.replace(NAME, ""));
                        if (buttonRepo.countButtonByNameAndLangId(buttonName, currentLanguage.getId()) > 0)
                            sendMessage(Const.BUTTON_NAME_BUSY);
                        return COMEBACK;
                    }
                    Button button = buttonRepo.findNameByIdAndLangId(buttonLinkId, currentLanguage.getId());
                    button.setName(buttonName);
                    buttonRepo.save(button);
                    sendEditor();
                    return COMEBACK;
                } else if (updateMessageText.startsWith(LINK)) {
                    Button button = buttonRepo.findByIdAndLangId(buttonLinkId, currentLanguage.getId());
                    button.setUrl(updateMessageText.replace(LINK, ""));
                    buttonRepo.save(button);
                    sendEditor();
                    return COMEBACK;
                } else {
                    sendMessage(Const.SET_CHANGE_LINKS);
                }
                sendMessage(Const.SET_CHANGE_LINKS);
                return COMEBACK;
            case UPDATE_FILE:
                if (hasDocument() || hasAudio() || hasVideo()) {
                    if (!isHasMessageForEdit()) {
                        return false;
                    }
                    updateFile();
                    sendMessage(Const.FILE_SUCCESS_ADDED_TEXT);
                    sendEditor();
                    return COMEBACK;
                }
        }
                return EXIT;
    }

    private void sendListMenu() throws TelegramApiException {
        toDeleteKeyboard(sendMessageWithKeyboard(Const.LIST_EDIT_MENU_MESSAGE,Const.MENU_KEYBOARD_ID));
    }

    private void sendEditor() throws TelegramApiException {
        StringBuilder urlList = null;
        clearOld();
        loadElements();
        String desc;
        if (message != null) {
            keyId = message.getKeyboardId().intValue();
            if (message.getPhoto() != null) {
                photoId = bot.execute(new SendPhoto()
                        .setPhoto(message.getPhoto())
                        .setChatId(chatId)
                ).getMessageId();
            }
             urlList = new StringBuilder();
              if (keyId != 0 && keyboardMarkUpRepo.countById( message.getKeyboardId().intValue()) > 0)
                urlList.append(getText(Const.BUTTON_LINKS)).append(next);//<b>Ссылки в виде кнопок:</b>
            List<Button> list = (List<Button>) buttonRepo.findById(keyId);
                for (Button button : list) {
                    if (button.getUrl() != null) {
                        urlList.append(linkEdit).append(button.getId()).append(" ").append(button.getName()).append(" - ").append(button.getUrl()).append(next);
                    }
                }
            }
            desc = String.format(getText(Const.TEXT_MENU_EDIT_BUTTON_LINKS), currentButton.getName(), message.getName(), urlList, currentLanguage.name());
            if (desc.length() > getMaxSizeMessage()) {            //максимальное сообщение
                String substring = message.getName().substring(0, desc.length() - getMaxSizeMessage() - 3) + "..."; //добавим многоточие что обрезано
                desc = String.format(getText(Const.TEXT_MENU_EDIT_BUTTON_LINKS), currentButton.getName(), substring, currentLanguage.name());
            } else {
            desc = String.format(getText(Const.TEXT_MENU_EDIT_BUTTON_LINKS), currentButton.getName(), getText(Const.DO_NOT_CHANGE_TEXT_THIS_BUTTON), currentLanguage.name());
        }
        textId = sendMessageWithKeyboard(desc, Const.KEYBOARD_EDIT_BUTTON_ID);
        toDeleteKeyboard(textId);
        waitingType = WaitingType.COMMAND_EDITOR;
    }

    private void loadElements() {
        currentButton = buttonRepo.findNameByIdAndLangId((int) currentButton.getId(), currentLanguage.getId());
        if (currentButton.getMessageId() == 0) {
            message = null;
        } else {
            message = messageRepo.findByIdAndLangId(currentButton.getMessageId(), currentLanguage.getId());
        }
    }

    private void clearOld() {
        deleteMessage(textId);
        deleteMessage(photoId);
    }

    private static int getMaxSizeMessage() {
        return Const.MAX_SIZE_MESSAGE;
    }

    private boolean isCommand() throws TelegramApiException {
        if (hasPhoto()) {
            if (!isHasMessageForEdit()) {
                return false;
            }
            updatePhoto();
        } else if (hasDocument() || hasAudio() || hasVideo()) {
            if (!isHasMessageForEdit()) {
                return false;
            }
            updateFile();
        } else if (isButton(Const.CHANGE_BUTTON_NAME)) {
            sendMessage(Const.SET_NAME_FOR_BUTTON);
            waitingType = WaitingType.UPDATE_BUTTON;
            return true;
        } else if (isButton(Const.CHANGE_BUTTON_TEXT)) {
            if (!isHasMessageForEdit()) {
                return false;
            }
            sendMessage(Const.SET_BUTTON_TEXT);
            waitingType = WaitingType.UPDATE_TEXT;
            return true;
        } else if (isButton(Const.ADD_NEW_FILE)) {
            sendMessage(Const.SEND_NEW_FILE_TEXT);
            waitingType = WaitingType.UPDATE_FILE;
            return true;
        } else if (isButton(Const.DELETE_FILE)) {
            if (!isHasMessageForEdit()) {
                return false;
            }
            deleteFile();
        } else if (isButton(Const.CHANGE_LANGUAGE)) {
            if (currentLanguage == Language.ru) {
                currentLanguage = Language.kz;
            } else {
                currentLanguage = Language.ru;
            }
        } else if (updateMessageText.startsWith(linkEdit)) {
            String buttId = updateMessageText.replace(linkEdit, "");
            //if (keyboardMarkUpDao.getButtonsString(keyId).contains(buttId)) {
            if (keyboardMarkUpRepo.findById(keyId).getButton_ids().contains(buttId)){
                sendMessage(Const.SET_CHANGE_LINKS);
                buttonLinkId = Integer.parseInt(buttId);
                waitingType = WaitingType.UPDATE_BUTTON_LINK;
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
        sendEditor();
        return true;
    }

    private boolean isHasMessageForEdit() throws TelegramApiException {
        if (message == null) {
            sendMessage(Const.DOSNT_FOR_THIS_BUTTON);
            return false;
        }
        return true;
    }

    private void updatePhoto() {
        message.setPhoto(updateMessagePhoto);
        update();
    }

    private void update() {
        //messageDao.update(message);
        messageRepo.save(message);
        getLogger().info("Update message {} for lang {} - chatId = ", message.getId(), currentLanguage.name(), chatId);
    }

    private void updateFile() {
        if (hasDocument()) {
            message.setFile(update.getMessage().getDocument().getFileId(), FileType.document);
        } else if (hasAudio()) {
            message.setFile(update.getMessage().getAudio().getFileId(), FileType.audio);
        } else if (hasVideo()) {
            message.setFile(update.getMessage().getVideo().getFileId(), FileType.video);
        }
        update();
    }

    private void deleteFile() {
        message.setFile(null);
        message.setFile(null);
        update();
    }

    private boolean getButtonIds(int keyboardMarkUpId) {
        //String buttonsString = keyboardMarkUpDao.getButtonsString(keyboardMarkUpId);
        String buttonsString = String.valueOf(keyboardMarkUpRepo.findById(keyboardMarkUpId));
        if (buttonsString == null) return false;
        String rows[] = buttonsString.split(";");
        for (String buttonIdString : rows) {
            String[] buttonIds = buttonIdString.split(",");
            for (String buttonId : buttonIds) {
                //Button buttonFromDb = buttonDao.getButton(Integer.parseInt(buttonId), currentLanguage);
                Button buttonFromDb = buttonRepo.findByIdAndLangId(Integer.parseInt(buttonId), currentLanguage.getId());
                String url = buttonFromDb.getUrl();
                if (url != null) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }
}
