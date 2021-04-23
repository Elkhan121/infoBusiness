package com.example.czn.command.impl;

import com.example.czn.command.Command;
import com.example.czn.dao.enums.TableNames;
import com.example.czn.dao.repositories.QuestRepo;
import com.example.czn.dao.repositories.SurveyRepo;
import com.example.czn.dao.repositories.TelegramBorRepositoryProvider;
import com.example.czn.entity.custom.Quest;
import com.example.czn.entity.custom.Survey;
import com.example.czn.entity.standart.Language;
import com.example.czn.service.LanguageService;
import com.example.czn.util.Const;
import com.example.czn.util.type.WaitingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class id012_AddSurvey extends Command {
//    @Autowired
//    SurveyRepo surveyRepo;
//    @Autowired
//    QuestRepo questRepo;

    private boolean isCan = false;
//    private static SurveyDao surveyDao = factory.getSurveyDao();
    private static SurveyRepo surveyRepo1 = TelegramBorRepositoryProvider.getSurveyRepo();

    private Survey surveyRu;
    private Survey surveyKz;
    private List<Quest> questMessageListRu;
    private List<Quest> questMessageListKz;

    @Override
    public boolean execute() throws SQLException, TelegramApiException {

        if (!isAdmin()) {
            sendMessage(Const.NO_ACCESS);
            return EXIT;
        }
        if (isButton(Const.DONE_BUTTON)) {
            if (isCan) {
                insert();
                sendMessage(Const.SURVEY_CREATE_DONE);
                return EXIT;
            } else {
                sendMessage(Const.SURVEY_DO_NOT_DONE);
                return COMEBACK;
            }
        }
        switch (waitingType) {
            case START:
                sendMessage(Const.SET_NAME_SURVEY_RU_TEXT);
                waitingType = WaitingType.SET_NAME_RU;
                return COMEBACK;
            case SET_NAME_RU:
                if (hasMessageText()) {
                    surveyRu = new Survey();
                    surveyRu.setLanguageId(Language.ru.getId());
                    surveyRu.setSurveyName(updateMessageText);
                    sendMessage(Const.SET_NAME_SURVEY_KZ_TEXT);
                    waitingType = WaitingType.SET_NAME_KZ;
                }
                return COMEBACK;
            case SET_NAME_KZ:
                if (hasMessageText()) {
                    surveyKz = new Survey();
                    surveyKz.setLanguageId(Language.kz.getId());
                    surveyKz.setSurveyName(updateMessageText);
                    sendMessage(Const.SET_QUEST_TEXT_RU);
                    waitingType = WaitingType.SET_QUEST_RU;
                }
                return COMEBACK;
            case SET_QUEST_RU:
                if (hasMessageText()) {
                    surveyRu.setQuestText(updateMessageText);
                    sendMessage(Const.SET_QUEST_TEXT_KZ);
                    waitingType = WaitingType.SET_QUEST_KZ;
                }
                return COMEBACK;
            case SET_QUEST_KZ:
                if (hasMessageText()) {
                    surveyKz.setQuestText(updateMessageText);
                    sendMessage(String.format(getText(Const.SET_ANSWER_FOR_QUEST_RU_TEXT), Const.SPLIT_RANGE));
                    questMessageListRu = new ArrayList<>();
                    questMessageListKz = new ArrayList<>();
                    waitingType = WaitingType.SET_ANSWER_OF_QUEST_RU;
                }
                return COMEBACK;
            case SET_ANSWER_OF_QUEST_RU:
                if (hasMessageText()) {
                    questMessageListRu.add(new Quest().setQuestAnswer(updateMessageText).setIdLanguage(Language.ru.getId()));
                    sendMessage(String.format(getText(Const.SET_ANSWER_FOR_QUEST_KZ_TEXT), Const.SPLIT_RANGE));
                    isCan = false;
                    waitingType = WaitingType.SET_ANSWER_OF_QUEST_KZ;
                }
                return COMEBACK;
            case SET_ANSWER_OF_QUEST_KZ:
                if (hasMessageText()) {
                    if (!checkCount()) {
                        sendMessage(Const.WRONG_SIZE_QUEST_TEXT);
                        return COMEBACK;
                    }
                    questMessageListKz.add(new Quest().setQuestAnswer(updateMessageText).setIdLanguage(Language.kz.getId()));
                    sendMessage(String.format(getText(Const.DONE_TEXT_SURVEY_CREATE), Const.SPLIT_RANGE));
                    waitingType = WaitingType.SET_ANSWER_OF_QUEST_RU;
                    isCan = true;
                }
                return COMEBACK;
        }
        return COMEBACK;
    }

    private boolean checkCount() {
        int countVarRu = questMessageListRu.get(questMessageListRu.size() - 1).getQuestAnswer().split(Const.SPLIT_RANGE).length;
        int countVarKz = updateMessageText.split(Const.SPLIT_RANGE).length;
        return countVarKz == countVarRu;
    }

    private void insert() {

//        int surveyId = surveyRepo.findAllByLanguageIdOrderByIdDesc(LanguageService.getLanguage(chatId).getId()).get(0).getId(); //surveyDao.getNextId(TableNames.SURVEYS);
//        surveyRu.setId(surveyId);
//        surveyKz.setId(surveyId);
//        surveyDao.insert(surveyRu);
//        surveyDao.insert(surveyKz);
        int surveyIdRu = surveyRepo.saveAndFlush(surveyRu).getId();
        int surveyIdKz = surveyRepo.saveAndFlush(surveyKz).getId();
        for (int i = 0; i < questMessageListRu.size(); i++) {
            int questMessageId = questMessageListRu.get(i).getId();
            questMessageListRu.get(i).setId(questMessageId).setIdSurvey(surveyIdRu);
            questMessageListKz.get(i).setId(questMessageId).setIdSurvey(surveyIdKz);
//            questDao.insert(questMessageListRu.get(i));
//            questDao.insert(questMessageListKz.get(i));
            questRepo.save(questMessageListKz.get(i));
            questRepo.save(questMessageListRu.get(i));
        }
    }
}
