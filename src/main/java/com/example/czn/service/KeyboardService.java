package com.example.czn.service;



import com.example.czn.dao.repositories.ButtonRepo;
import com.example.czn.dao.repositories.KeyboardMarkUpRepo;
import com.example.czn.dao.repositories.TelegramBorRepositoryProvider;
import com.example.czn.entity.standart.Button;
import com.example.czn.entity.standart.Keyboard;
import com.example.czn.entity.standart.Language;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Service
public class KeyboardService {

    KeyboardMarkUpRepo keyboardMarkUpRepo = TelegramBorRepositoryProvider.getKeyboardMarkUpRepo();
    ButtonRepo buttonRepo = TelegramBorRepositoryProvider.getButtonRepo();
    List<Button> list = new ArrayList<>();

    public List<Button> getList(long keyId) {
        Keyboard keyboard = keyboardMarkUpRepo.findById(keyId);
        String[] arr = keyboard.getButton_ids().split(";,");
        for (String x : arr) {
            list.add(buttonRepo.findById(Integer.parseInt(x)));
        }
        return list;
    }

    public List<Button> getListForEdit(int keyId) {
        //List<Button> list = new ArrayList<>();
        Keyboard keyboard = keyboardMarkUpRepo.findById(keyId);
        String[] arr = keyboard.getButton_ids().split(";,");
        for (String x : arr) {
            list.add(buttonRepo.findById(Integer.parseInt(x)));
        }
        return list;
    }

    private ReplyKeyboard getKeyboardForEdition(Keyboard keyboard, Language language) {
        String buttonIds = keyboard.getButton_ids();
        if (buttonIds == null) {
            return null;
        }
        String[] rows = buttonIds.split(";");
        return getInlineKeyboardForEdition(rows, language);
    }

    InlineKeyboardMarkup getInlineKeyboardForEdition(String[] rowIds, Language language) {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        for (String buttonIdsString : rowIds) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            String[] buttonIds = buttonIdsString.split(",");
            for (String buttonId : buttonIds) {
                Button buttonFromDb = buttonRepo.findByIdAndLangId(Integer.parseInt(buttonId), language.getId());
                InlineKeyboardButton button = new InlineKeyboardButton();
                String buttonText = buttonFromDb.getName();
                button.setText(buttonText);
                String url = buttonFromDb.getUrl();
                if (url != null) {
                    button.setUrl(url);
                } else {
                    button.setCallbackData(buttonId);
                }
                row.add(button);
            }
            rows.add(row);
        }
        keyboard.setKeyboard(rows);
        return keyboard;
    }

    ReplyKeyboard getReplyKeyboard(String[] rows, Language language) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        boolean isRequestContact = false;
        for (String buttonIdsString : rows) {
            KeyboardRow keyboardRow = new KeyboardRow();
            String[] buttonIds = buttonIdsString.split(",");
            for (String buttonId : buttonIds) {
                Button buttonFromDb = buttonRepo.findByIdAndLangId(Integer.parseInt(buttonId), language.getId());
                KeyboardButton button = new KeyboardButton();
                String buttonText = buttonFromDb.getName();
                button.setText(buttonText);
                if (buttonFromDb.getRequestContact() != null)
                    button.setRequestContact(buttonFromDb.getRequestContact());
                if (buttonFromDb.getRequestContact() != null) {
                    if (buttonFromDb.getName().equals("Направить контактный номер")
                    ||  buttonFromDb.getName().equals("Байланыс нөміріңізді жіберіңіз")) {
                        isRequestContact = true;
                        buttonFromDb.setRequestContact(true);
                    }else{
                        isRequestContact = false;
                        buttonFromDb.setRequestContact(false);
                    }
                }
                keyboardRow.add(button);
            }
            keyboardRowList.add(keyboardRow);
        }
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        replyKeyboardMarkup.setOneTimeKeyboard(isRequestContact);
        return replyKeyboardMarkup;
    }

    InlineKeyboardMarkup getInlineKeyboard(String[] rowIds, long chatId) {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        for (String buttonIdsString : rowIds) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            String[] buttonIds = buttonIdsString.split(",");
            for (String buttonId : buttonIds) {
                Button buttonFromDb = buttonRepo.findByIdAndLangId(Integer.parseInt(buttonId), LanguageService.getLanguage(chatId).getId());
                InlineKeyboardButton button = new InlineKeyboardButton();
                String buttonText = buttonFromDb.getName();
                button.setText(buttonText);
                String url = buttonFromDb.getUrl();
                if (url != null) {
                    button.setUrl(url);
                } else {
                    buttonText = buttonText.length() < 64 ? buttonText : buttonText.substring(0, 64);
                    button.setCallbackData(buttonText);
                }
                row.add(button);
            }
            rows.add(row);
        }
        keyboard.setKeyboard(rows);
        return keyboard;
    }

    InlineKeyboardMarkup getInlineKeyboard(String[] rowIds, Language language) {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        for (String buttonIdsString : rowIds) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            String[] buttonIds = buttonIdsString.split(",");
            for (String buttonId : buttonIds) {
                Button buttonFromDb = buttonRepo.findByIdAndLangId(Integer.parseInt(buttonId), language.getId());
                InlineKeyboardButton button = new InlineKeyboardButton();
                String buttonText = buttonFromDb.getName();
                button.setText(buttonText);
                String url = buttonFromDb.getUrl();
                if (url != null) {
                    button.setUrl(url);
                } else {
                    buttonText = buttonText.length() < 64 ? buttonText : buttonText.substring(0, 64);
                    button.setCallbackData(buttonText);
                }
                row.add(button);
            }
            rows.add(row);
        }
        keyboard.setKeyboard(rows);
        return keyboard;
    }

     public ReplyKeyboard getKeyboard(Keyboard keyboard, long chatId) {
        String buttonIds = keyboard.getButton_ids();
        if (buttonIds == null) {
            return null;
        }
        String[] rows = buttonIds.split(";");
        if (keyboard.isInline()) {
            return getInlineKeyboard(rows,chatId);
        } else {
            return getReplyKeyboard(rows,LanguageService.getLanguage(chatId));
        }
    }

    public ReplyKeyboard getKeyboard(Keyboard keyboard, Language language) {
        String buttonIds = keyboard.getButton_ids();
        if (buttonIds == null) {
            return null;
        }
        String[] rows = buttonIds.split(";");
        if (keyboard.isInline()) {
            return getInlineKeyboard(rows, language);
        } else {
            return getReplyKeyboard(rows, language);
        }
    }

    public ReplyKeyboard selectForEdition(long keyboardMarkUpId, Language language) {
        if (keyboardMarkUpId < 0) {
            return new ReplyKeyboardRemove();
        }
        if (keyboardMarkUpId == 0) return null;
        return getKeyboardForEdition(keyboardMarkUpRepo.findById(keyboardMarkUpId), language);
    }

//    ReplyKeyboard getReplyKeyboard(String[] rows) {
//        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
//        replyKeyboardMarkup.setResizeKeyboard(true);
//        List<KeyboardRow> keyboardRowList = new ArrayList<>();
//        boolean isRequestContact = false;
//        for (String buttonIdsString : rows) {
//            KeyboardRow keyboardRow = new KeyboardRow();
//            String[] buttonIds = buttonIdsString.split(",");
//            for (String buttonId : buttonIds) {
//                Button buttonFromDb = buttonRepo.findById(Integer.parseInt(buttonId));
//                KeyboardButton button = new KeyboardButton();
//                String buttonText = buttonFromDb.getName();
//                button.setText(buttonText);
//                button.setRequestContact(buttonFromDb.getRequestContact() != null);
//                if (buttonFromDb.getRequestContact() != null) {
//                    isRequestContact = true;
//                }
//                keyboardRow.add(button);
//            }
//            keyboardRowList.add(keyboardRow);
//        }
//        replyKeyboardMarkup.setKeyboard(keyboardRowList);
//        replyKeyboardMarkup.setOneTimeKeyboard(isRequestContact);
//        return replyKeyboardMarkup;
//    }

}
