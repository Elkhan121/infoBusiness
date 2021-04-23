package com.example.czn.service;

import com.example.czn.dao.repositories.LanguageUserRepo;
import com.example.czn.dao.repositories.TelegramBorRepositoryProvider;
import com.example.czn.entity.standart.Language;
import com.example.czn.entity.standart.LanguageUser;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
//@AllArgsConstructor
public class LanguageService {

    private static Map<Long, Language> langMap = new HashMap<>();

    public static Language getLanguage(long chatId) {
        Language language = langMap.get(chatId);
        if (language == null) {
             //langUser = DaoFactory.getFactory().getLanguageUserDao().getByChatId(chatId);
            LanguageUserRepo languageUserRepo = TelegramBorRepositoryProvider.getLanguageUserRepo();
            LanguageUser langUser = languageUserRepo.findAllByChatId(chatId);
            if (langUser != null) {
                language = langUser.getLanguage();
                langMap.put(chatId, language);
            }
        }
        return language;
    }

    public static void setLanguage(long chatId, Language language) {
        langMap.put(chatId, language);
        LanguageUserRepo languageUserRepo = TelegramBorRepositoryProvider.getLanguageUserRepo();

//        DaoFactory.getFactory().getLanguageUserDao().insertOrUpdate(new LanguageUser(chatId, language));
        TelegramBorRepositoryProvider.getLanguageUserRepo().save(languageUserRepo.findAllByChatId(chatId) != null?languageUserRepo.findAllByChatId(chatId):new LanguageUser(chatId,language));
    }
}
