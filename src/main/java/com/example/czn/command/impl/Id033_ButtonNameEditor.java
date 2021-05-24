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
import com.example.czn.util.ButtonsLeaf;
import com.example.czn.util.Const;
import com.example.czn.util.FileType;
import com.example.czn.util.type.WaitingType;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.security.Key;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.net.HttpHeaders.LINK;

@Component
public class Id033_ButtonNameEditor extends Command {
//
//    private Language currentLanguage;
//    private ButtonsLeaf buttonsLeaf;
//    private List<Button> buttonList;
//    private Button currentButton;
//    private int textId;
//    private int photoId;
//    private String buttonName;
//    private Message message;
//    private int keyId;
//    private static final String linkEdit = "/linkId";
//    private int buttonLinkId;
//    boolean buttonIds = false;
//    private static String NAME;
//
//    @Override
//    public boolean execute() throws SQLException, TelegramApiException {
//        NAME = String.valueOf(messageRepo.findByIdAndLangId(Const.NAME_TEXT_FOR_LINK, LanguageService.getLanguage(chatId).getId()));
//        switch (waitingType) {
//            case START:
//                currentLanguage = LanguageService.getLanguage(chatId);
//                sendListMenu();
//                waitingType = WaitingType.ONE;
//                return COMEBACK;
//            case ONE:
//                deleteMessage();
//                if (hasCallbackQuery()) {
//                    int buttonIdd = buttonList.get(Integer.parseInt(updateMessageText)).getId();
//                    int buttonId = buttonIdd;
//                    System.out.println(buttonId);
//
//                    long keyboardMarkUpId = messageRepo.findByIdAndLangId(buttonRepo.findByIdAndLangId(buttonId, currentLanguage.getId()).getMessageId(), currentLanguage.getId()).getKeyboardId();
//                    if (keyboardMarkUpId != 0) {
//                        buttonIds = getButtonIds((int) keyboardMarkUpId);
//                    }
//                    if (keyboardMarkUpId == 2) {
//                        currentButton = buttonRepo.findNameByIdAndLangId(buttonId, currentLanguage.getId());
//                        sendEditor();
//                        return COMEBACK;
//                    } else if (keyboardMarkUpId > 0 & keyboardMarkUpId != 2) {
//                        if (!buttonIds) {
//                            toDeleteKeyboard(sendMessageWithKeyboard(Const.SELECT_BUTTON_FOR_EDIT, (ReplyKeyboard) keyboardMarkUpRepo.findById(keyboardMarkUpId)));
//                            waitingType = WaitingType.CHOOSE_OPTION;
//                        } else {
//                            currentButton = buttonRepo.findNameByIdAndLangId(buttonId, currentLanguage.getId());
//                            sendEditor();
//                            return COMEBACK;
//                        }
//                    } else {
//                        currentButton = buttonRepo.findNameByIdAndLangId(buttonId, currentLanguage.getId());
//                        sendEditor();
//                        return COMEBACK;
//                    }
//                } else {
//                    sendListMenu();
//                }
//                return COMEBACK;
//            case COMMAND_EDITOR:
//                isCommand();
//                return COMEBACK;
//            case UPDATE_BUTTON:
//                if (isCommand()) {
//                    return COMEBACK;
//                }
//                if (hasMessageText()) {
//                    String buttonName = (ButtonUtil.getButtonName(updateMessageText, 100));
//                    if (buttonName.replaceAll("[0-9]", "").isEmpty()) {
//                        sendMessage(Const.WRONG_NAME_BUTTON_TEXT);
//                        return COMEBACK;
//                    }
//                    if (buttonRepo.countButtonByNameAndLangId(buttonName, currentLanguage.getId()) > 0) {
//                        sendMessage(Const.BUTTON_NAME_BUSY);
//                        return COMEBACK;
//                    }
//                    currentButton.setName(buttonName);
//                    buttonRepo.save(currentButton);
//                    sendEditor();
//                    return COMEBACK;
//                }
//                return COMEBACK;
//            case UPDATE_TEXT:
//                if (isCommand()) {
//                    return COMEBACK;
//                }
//                if (hasMessageText()) {
//                    message.setName(new ParserMessageEntity().getTextWithEntity(update.getMessage()));
//                    messageRepo.save(message);
//                    sendEditor();
//                    return COMEBACK;
//                }
//                return COMEBACK;
//            case UPDATE_BUTTON_LINK:
//                if (isCommand()) {
//                    return COMEBACK;
//                }
//                if (hasMessageText()) {
//                    if (updateMessageText.startsWith(NAME)) {
//                        String buttonName = ButtonUtil.getButtonName(updateMessageText.replace(NAME, ""));
//                        if (buttonRepo.countButtonByNameAndLangId(buttonName, currentLanguage.getId()) > 0)
//                            sendMessage(Const.BUTTON_NAME_BUSY);
//                        return COMEBACK;
//                    }
//                    Button button = buttonRepo.findNameByIdAndLangId(buttonLinkId, currentLanguage.getId());
//                    button.setName(buttonName);
//                    buttonRepo.save(button);
//                    sendEditor();
//                    return COMEBACK;
//                } else if (updateMessageText.startsWith(LINK)) {
//                    Button button = buttonRepo.findByIdAndLangId(buttonLinkId, currentLanguage.getId());
//                    button.setUrl(updateMessageText.replace(LINK, ""));
//                    buttonRepo.save(button);
//                    sendEditor();
//                    return COMEBACK;
//                } else {
//                    sendMessage(Const.SET_CHANGE_LINKS);
//                }
//                sendMessage(Const.SET_CHANGE_LINKS);
//                return COMEBACK;
//            case UPDATE_FILE:
//                if (hasDocument() || hasAudio() || hasVideo()) {
//                    if (!isHasMessageForEdit()) {
//                        return false;
//                    }
//                    updateFile();
//                    sendMessage(Const.FILE_SUCCESS_ADDED_TEXT);
//                    sendEditor();
//                    return COMEBACK;
//                }
//        }
//        return EXIT;
//    }
//
//    private void sendListMenu() throws TelegramApiException {
//        buttonList = buttonRepo.findAllByLangIdAndIdBetween(currentLanguage.getId(),Const.MENU_BUTTONS_START_ID,Const.MENU_BUTTONS_STOP_ID);
//        List<String> list = new ArrayList<>();
//        buttonList.forEach(button -> {
//            list.add(button.getName());
//        });
//        buttonsLeaf = new ButtonsLeaf(list);
//        sendMessageWithKeyboard(getText(Const.LIST_EDIT_MENU_MESSAGE),buttonsLeaf.getListButton(),chatId);
////        toDeleteKeyboard(sendMessageWithKeyboard(Const.LIST_EDIT_MENU_MESSAGE, Const.MENU_KEYBOARD_ID));
//        waitingType = WaitingType.ONE;
//    }
//
//    private void sendEditor() throws TelegramApiException {
//        StringBuilder urlList = null;
//        clearOld();
//        loadElements();
//        String desc;
//        if (message != null) {
//            keyId = message.getKeyboardId().intValue();
//            if (message.getPhoto() != null) {
//                photoId = bot.execute(new SendPhoto()
//                        .setPhoto(message.getPhoto())
//                        .setChatId(chatId)
//                ).getMessageId();
//            }
//            urlList = new StringBuilder();
//
//            if (keyId != 0 && keyboardMarkUpRepo.countById(message.getKeyboardId().intValue()) > 0)
//                urlList.append(getText(Const.BUTTON_LINKS)).append(next);//<b>Ссылки в виде кнопок:</b>
//            List<Button> list = (List<Button>) buttonRepo.findById(keyId);
//            for (Button button : list) {
//                if (button.getUrl() != null) {
//                    urlList.append(linkEdit).append(button.getId()).append(" ").append(button.getName()).append(" - ").append(button.getUrl()).append(next);
//                }
//            }
//        }
//        desc = String.format(getText(Const.TEXT_MENU_EDIT_BUTTON_LINKS), currentButton.getName(), message.getName(), urlList, currentLanguage.name());
//        if (desc.length() > getMaxSizeMessage()) {            //максимальное сообщение
//            String substring = message.getName().substring(0, desc.length() - getMaxSizeMessage() - 3) + "..."; //добавим многоточие что обрезано
//            desc = String.format(getText(Const.TEXT_MENU_EDIT_BUTTON_LINKS), currentButton.getName(), substring, currentLanguage.name());
//        } else {
//            desc = String.format(getText(Const.TEXT_MENU_EDIT_BUTTON_LINKS), currentButton.getName(), getText(Const.DO_NOT_CHANGE_TEXT_THIS_BUTTON), currentLanguage.name());
//        }
//        textId = sendMessageWithKeyboard(desc, Const.KEYBOARD_EDIT_BUTTON_ID);
//        toDeleteKeyboard(textId);
//        waitingType = WaitingType.COMMAND_EDITOR;
//    }
//
//    private void loadElements() {
//        currentButton = buttonRepo.findNameByIdAndLangId((int) currentButton.getId(), currentLanguage.getId());
//        if (currentButton.getMessageId() == 0) {
//            message = null;
//        } else {
//            message = messageRepo.findByIdAndLangId(currentButton.getMessageId(), currentLanguage.getId());
//        }
//    }
//
//    private void clearOld() {
//        deleteMessage(textId);
//        deleteMessage(photoId);
//    }
//
//    private static int getMaxSizeMessage() {
//        return Const.MAX_SIZE_MESSAGE;
//    }
//
//    private boolean isCommand() throws TelegramApiException {
//        if (hasPhoto()) {
//            if (!isHasMessageForEdit()) {
//                return false;
//            }
//            updatePhoto();
//        } else if (hasDocument() || hasAudio() || hasVideo()) {
//            if (!isHasMessageForEdit()) {
//                return false;
//            }
//            updateFile();
//        } else if (isButton(Const.CHANGE_BUTTON_NAME)) {
//            sendMessage(Const.SET_NAME_FOR_BUTTON);
//            waitingType = WaitingType.UPDATE_BUTTON;
//            return true;
//        } else if (isButton(Const.CHANGE_BUTTON_TEXT)) {
//            if (!isHasMessageForEdit()) {
//                return false;
//            }
//            sendMessage(Const.SET_BUTTON_TEXT);
//            waitingType = WaitingType.UPDATE_TEXT;
//            return true;
//        } else if (isButton(Const.ADD_NEW_FILE)) {
//            sendMessage(Const.SEND_NEW_FILE_TEXT);
//            waitingType = WaitingType.UPDATE_FILE;
//            return true;
//        } else if (isButton(Const.DELETE_FILE)) {
//            if (!isHasMessageForEdit()) {
//                return false;
//            }
//            deleteFile();
//        } else if (isButton(Const.CHANGE_LANGUAGE)) {
//            if (currentLanguage == Language.ru) {
//                currentLanguage = Language.kz;
//            } else {
//                currentLanguage = Language.ru;
//            }
//        } else if (updateMessageText.startsWith(linkEdit)) {
//            String buttId = updateMessageText.replace(linkEdit, "");
//            if (keyboardMarkUpRepo.findById(keyId).getButton_ids().contains(buttId)) {
//                sendMessage(Const.SET_CHANGE_LINKS);
//                buttonLinkId = Integer.parseInt(buttId);
//                waitingType = WaitingType.UPDATE_BUTTON_LINK;
//                return true;
//            } else {
//                return false;
//            }
//        } else {
//            return false;
//        }
//        sendEditor();
//        return true;
//    }
//
//    private boolean isHasMessageForEdit() throws TelegramApiException {
//        if (message == null) {
//            sendMessage(Const.DOSNT_FOR_THIS_BUTTON);
//            return false;
//        }
//        return true;
//    }
//
//    private void updatePhoto() {
//        message.setPhoto(updateMessagePhoto);
//        update();
//    }
//
//    private void update() {
//        messageRepo.save(message);
//        getLogger().info("Update message {} for lang {} - chatId = ", message.getId(), currentLanguage.name(), chatId);
//    }
//
//    private void updateFile() {
//        if (hasDocument()) {
//            message.setFile(update.getMessage().getDocument().getFileId(), FileType.document);
//        } else if (hasAudio()) {
//            message.setFile(update.getMessage().getAudio().getFileId(), FileType.audio);
//        } else if (hasVideo()) {
//            message.setFile(update.getMessage().getVideo().getFileId(), FileType.video);
//        }
//        update();
//    }
//
//    private void deleteFile() {
//        message.setFile(null);
//        message.setFile(null);
//        update();
//    }
//
//    private boolean getButtonIds(int keyboardMarkUpId) {
////        String buttonsString = String.valueOf(keyboardMarkUpRepo.findById(keyboardMarkUpId));
//        String buttonsString = String.valueOf(keyboardMarkUpRepo.findById(keyboardMarkUpId));
//        if (buttonsString == null) return false;
//        String rows[] = buttonsString.split(";");
//        for (String buttonIdString : rows) {
//            String[] buttonIds = buttonIdString.split(",");
//            for (String buttonId : buttonIds) {
//                //Button buttonFromDb = buttonDao.getButton(Integer.parseInt(buttonId), currentLanguage);
//                Button buttonFromDb = buttonRepo.findByIdAndLangId(Integer.parseInt(buttonId), currentLanguage.getId());
//                String url = buttonFromDb.getUrl();
//                if (url != null) {
//                    return true;
//                } else {
//                    return false;
//                }
//            }
//        }
//        return false;

    private int inlineMessId;
    private int wrongMessId;
    private int infoMessId;
    private int notFoundMess;
    private Button currentButton;
    private Language currentLang;
    private List<Button> searchResultButtons;

    @Override
    public boolean execute() throws TelegramApiException {
        if (!isAdmin()) {
            sendMessage(Const.NO_ACCESS);
            return EXIT;
        }
        switch (waitingType) {
            case START:
                deleteUpdateMess();
                if (isButton(219)) { // editing button
                    infoMessId = sendMessage(getText(1445));
                    waitingType = WaitingType.ONE;
                } else {
                    sendMessageWithKeyboard(getText(59), 18);
                }
                return COMEBACK;
            case ONE:
//                deleteUpdateMess();
//                if (hasCallbackQuery()) {
//                    currentLang = LanguageService.getLanguage(chatId);
//                    searchResultButtons = buttonRepo.findAllByNameContainingAndLangIdOrderById(updateMessageText, currentLang.getId());
//                    if (searchResultButtons.size() != 0) {
//                        deleteMessage(notFoundMess);
//                        deleteMessage(infoMessId);
//                        inlineMessId = sendMessage(getInfoButtons(searchResultButtons));
//                        waitingType = WaitingType.CHOOSE_OPTION;
//                    } else {
//                        sendNotFound();
//                    }
//                } else {
//                    deleteUpdateMess();
//                    sendWrongData();
//                }
//                return COMEBACK;
//            case CHOOSE_OPTION:
                deleteUpdateMess();
//                if (updateMessageText.contains("/editName")) { //edit name
//                    currentButton = buttonRepo.findByIdAndLangId(Integer.parseInt((updateMessageText.substring(9))), currentLang.getId());
                System.out.println(updateMessageText + " " + currentLang);
                    currentButton = buttonRepo.findByNameContainingAndLangIdOrderById(updateMessageText, currentLang.getId());
                    System.out.println(Integer.parseInt((updateMessageText.substring(9))));
//                    currentButton = buttonRepo.findByNameAndLangId(updateMessageText, currentLang.getId());
                    if (currentButton == null) {
                        sendWrongData();
                        return COMEBACK;
                    }
                    deleteMessage(inlineMessId);
                    inlineMessId = sendMessage(getInfoForEdit(currentButton));
//                        editMessage(getInfoForEdit(currentButton), inlineMessId);
//                        infoMessId = sendMessage(57);
                    waitingType = WaitingType.TWO;
//                } else if (updateMessageText.contains("/back")) { // back
                 if (updateMessageText.contains("/back")) { // back
                    deleteMessage(infoMessId);
                    deleteMessage(inlineMessId);
                    infoMessId = sendMessage(60);
                    waitingType = WaitingType.ONE;
                } else if (updateMessageText.contains("/swapLanguage")) { //swap lang
                    if (currentLang.getId() == 1)
                        currentLang = Language.kz;
                    else
                        currentLang = Language.ru;
                    List<Button> newSearchRes = new ArrayList<>();
                    for (Button button : searchResultButtons) {
                        newSearchRes.add(buttonRepo.findByIdAndLangId(button.getId(), currentLang.getId()));
                    }
                    searchResultButtons = newSearchRes;
                    if (currentButton != null)
                        currentButton = buttonRepo.findByIdAndLangId(currentButton.getId(), currentLang.getId());
                    newSearchRes = null;
//                    editMessage(getInfoButtons(searchResultButtons), inlineMessId);
                    sendMessage(getInfoButtons(searchResultButtons), inlineMessId);
                }
                return COMEBACK;
            case TWO:
                deleteUpdateMess();
                if (hasMessageText() && updateMessageText.length() < 100) {
                    if (updateMessageText.equals("/cancel")) {
                        deleteMessage(infoMessId);
                        deleteMessage(inlineMessId);
                        inlineMessId = sendMessage(getInfoButtons(searchResultButtons));
                        waitingType = WaitingType.CHOOSE_OPTION;
                        return COMEBACK;
                    } else if (buttonRepo.findByNameAndLangId(updateMessageText, 1) != null || buttonRepo.findByNameAndLangId(updateMessageText, 2) != null || updateMessageText.equals("/swapLanguage") || updateMessageText.equals("/back") || updateMessageText.contains("/editName")) {
                        deleteMessage(infoMessId);
                        infoMessId = sendMessage(63);
                        return COMEBACK;
                    } else {
                        deleteWrongMess();
//                        buttonRepo.update(updateMessageText, currentButton.getId(), currentLang.getId());
                        deleteMessage(inlineMessId);
                        deleteMessage(infoMessId);

                        searchResultButtons = updateButtons(searchResultButtons);
//                        searchResultButtons = buttonRepository.findAllByNameContainingAndLangIdOrderById(currentSearchValue, currentLang.getId());
                        currentButton = buttonRepo.findByIdAndLangId(currentButton.getId(), currentLang.getId());
                        inlineMessId = sendMessage(getInfoButtons(searchResultButtons));
                        waitingType = WaitingType.CHOOSE_OPTION;
                    }
                } else {
                    sendWrongData();
                }
                return COMEBACK;
        }
        return EXIT;
    }

    private List<Button> updateButtons(List<Button> searchResultButtons) {
        List<Button> newSearchRes = new ArrayList<>();
        for (Button button : searchResultButtons) {
            newSearchRes.add(buttonRepo.findByIdAndLangId(button.getId(), currentLang.getId()));
        }
        return newSearchRes;
    }

    private String getInfoForEdit(Button currentButton) {
        return getText(64) + currentButton.getName() + next +
                getText(57);
    }

    private String getInfoButtons(List<Button> searchResultButtons) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Button button : searchResultButtons) {
            stringBuilder.append(button.getName()).append(" \uD83D\uDD8A /editName").append(button.getId()).append(next).append(next);
        }
        return String.format(getText(1445), stringBuilder.toString(), currentLang.name());
    }

    private void sendNotFound() throws TelegramApiException {
        deleteMessage(updateMessageId);
        deleteMessage(notFoundMess);
        notFoundMess = sendMessage(61, chatId);
    }

//    private String getInfoButton(Button button) {
//        String s = getText(55) + next + button.getName() + " \uD83D\uDD8A /editName" + button.getId();
//        return String.format(getText(62), s, currentLang.name());
//    }

    private Long getLong(String updateMessageText) {
        try {
            return Long.parseLong(updateMessageText);
        } catch (Exception e) {
            return -1L;
        }
    }

    private void deleteUpdateMess() {
        deleteMessage(updateMessageId);
    }

    private void deleteWrongMess() {
        deleteMessage(wrongMessId);
    }

    private void sendWrongData() throws TelegramApiException {
        deleteMessage(updateMessageId);
        deleteMessage(wrongMessId);
        wrongMessId = sendMessage(4, chatId);
    }
}


