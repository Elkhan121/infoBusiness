package com.example.czn.command.impl;

import com.example.czn.command.Command;
import com.example.czn.dao.repositories.TelegramBorRepositoryProvider;
import com.example.czn.entity.custom.Quest;
import com.example.czn.entity.custom.Survey;
import com.example.czn.entity.custom.SurveyAnswer;
import com.example.czn.entity.standart.Language;
import com.example.czn.service.LanguageService;
import com.example.czn.util.ButtonsLeaf;
import com.example.czn.util.Const;
import com.example.czn.util.type.WaitingType;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class id011_SurveyShow extends Command {

    private Language currentLanguage;
    private List<Survey> allSurveys;
    private List<Survey> otherSurveys;
    private ButtonsLeaf buttonsLeaf;
    private Survey survey;
    private List<Quest> allQuest;
    private List<String> listAnswers;
    private SurveyAnswer surveyAnswer;

    @Override
    public boolean execute() throws SQLException, TelegramApiException {
        switch (waitingType) {
            case START:
                currentLanguage = LanguageService.getLanguage(chatId);
               // allSurveys = factory.getSurveyDao().getAllActive(currentLanguage, chatId);
                allSurveys = TelegramBorRepositoryProvider.getSurveyRepo().findAllByLanguageId(currentLanguage.getId());
                if (allSurveys == null || allSurveys.size() == 0) {
                    sendMessage(Const.SURVEY_END);
                    return EXIT;
                }
                sendMessage(Const.KEYBOARD_FROM_SURVEY);
                List<String> list = new ArrayList<>();
                allSurveys.forEach((e) -> list.add(e.getSurveyName()));
                buttonsLeaf = new ButtonsLeaf(list);
                toDeleteKeyboard(sendMessageWithKeyboard(Const.CHOOSE_SURVEY_TEXT, buttonsLeaf.getListButton()));
                waitingType = WaitingType.CHOOSE_SURVEY;
                return COMEBACK;
            case CHOOSE_SURVEY:
                if (hasCallbackQuery()) {
                    deleteMessage();
                    if (buttonsLeaf.isNext(updateMessageText)) {
                        deleteMessage();
                        toDeleteKeyboard(sendMessageWithKeyboard(Const.CHOOSE_SURVEY_TEXT, buttonsLeaf.getListButton()));
                    } else {
                        survey = allSurveys.get(Integer.parseInt(updateMessageText));
//                        allQuest = factory.getQuestDao().getAll(survey.getId(), currentLanguage);
                        allQuest = TelegramBorRepositoryProvider.getQuestRepo().findByIdAndLanguageIdOrderById(survey.getId(), currentLanguage.getId());
                        listAnswers = new ArrayList<>();
                        allQuest.forEach((e) -> Collections.addAll(listAnswers, e.getQuestAnswer().split(",")));
                        buttonsLeaf = new ButtonsLeaf(listAnswers);
                        toDeleteKeyboard(sendMessageWithKeyboard(survey.getQuestText(), buttonsLeaf.getListButton()));
                        waitingType = WaitingType.CHOOSE_OPTION_SURVEY;
                    }
                }
                return COMEBACK;
            case CHOOSE_OPTION_SURVEY:
                if (hasCallbackQuery()) {
                    deleteMessage();
                    String button = listAnswers.get(Integer.parseInt(updateMessageText));
                    surveyAnswer = new SurveyAnswer();
                    surveyAnswer.setButton(button);
                    surveyAnswer.setChatId(chatId);
                    surveyAnswer.setSurveyId(survey.getId());
//                    factory.getSurveyAnswerDao().insert(surveyAnswer);
                    TelegramBorRepositoryProvider.getSurveyAnswerRepo().save(surveyAnswer);

//                    allSurveys = factory.getSurveyDao().getAllActive(currentLanguage, chatId);
                    allSurveys = TelegramBorRepositoryProvider.getSurveyRepo().findAllByLanguageId(currentLanguage.getId());
                    if (allSurveys == null || allSurveys.size() == 0) {
                        sendMessage(Const.SURVEY_END);
                        return EXIT;
                    }
                    sendMessage(Const.KEYBOARD_FROM_SURVEY);
                    List<String> listSurveyName = new ArrayList<>();
                    allSurveys.forEach((e) -> listSurveyName.add(e.getSurveyName()));
                    buttonsLeaf = new ButtonsLeaf(listSurveyName);
                    toDeleteKeyboard(sendMessageWithKeyboard(Const.CHOOSE_SURVEY_TEXT, buttonsLeaf.getListButton()));
                    waitingType = WaitingType.CHOOSE_SURVEY;
                    return COMEBACK;
                }
                return COMEBACK;
        }
        return EXIT;
    }

}
