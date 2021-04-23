package com.example.czn.command.impl;

import com.example.czn.command.Command;
import com.example.czn.dao.repositories.QuestRepo;
import com.example.czn.dao.repositories.SurveyRepo;
import com.example.czn.entity.custom.Quest;
import com.example.czn.entity.custom.Survey;
import com.example.czn.entity.standart.Language;
import com.example.czn.service.LanguageService;
import com.example.czn.util.ButtonUtil;
import com.example.czn.util.ButtonsLeaf;
import com.example.czn.util.Const;
import com.example.czn.util.UpdateUtil;
import com.example.czn.util.type.WaitingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class id013_EditSurvey extends Command {
    @Autowired
    SurveyRepo surveyRepo;
    @Autowired
    QuestRepo questRepo;

    private ButtonsLeaf buttonsLeaf;
    private List<Survey> all;
    private int surveyId;
    private int questId;
    private Survey survey;
    private Quest quest;
    private List<Quest> questMessageList;
//    private static SurveyDao surveyDao = factory.getSurveyDao();
    private Language currentLanguage;
    private WaitingType updateType = WaitingType.START;
    private int editionMessageId;
    private Quest addQuest;


    @Override
    public boolean execute() throws SQLException, TelegramApiException {
        if (!isAdmin()) {
            sendMessage(Const.NO_ACCESS);
            return EXIT;
        }
        switch (updateType) {
            case UPDATE_SURVEY:
                if (isCommand()) {
                    return COMEBACK;
                }
                break;
            case UPDATE_QUEST:
                if (isCommandQuest()) {
                    return COMEBACK;
                }
                break;
        }
        switch (waitingType) {
            case START:
                currentLanguage = LanguageService.getLanguage(chatId);
                all = surveyRepo.findAllByLanguageId(currentLanguage.getId());
                buttonsLeaf = new ButtonsLeaf(all.stream().map(Survey::getSurveyName).collect(Collectors.toList()));
                toDeleteKeyboard(sendMessageWithKeyboard(Const.CHOOSE_SURVEY_FOR_EDIT_TEXT, buttonsLeaf.getListButton()));
                waitingType = WaitingType.CHOOSE_SURVEY_FOR_EDIT;
                return COMEBACK;
            case CHOOSE_SURVEY_FOR_EDIT:
                if (hasCallbackQuery()) {
                    deleteMessage();
                    if (buttonsLeaf.isNext(updateMessageText)) {
                        toDeleteKeyboard(sendMessageWithKeyboard(Const.CHOOSE_SURVEY_FOR_EDIT_TEXT, buttonsLeaf.getListButton()));
                    }
                    surveyId = all.get(Integer.parseInt(updateMessageText)).getId();
                    waitingType = WaitingType.EDITION;
                    updateType = WaitingType.UPDATE_SURVEY;
                    sendMessage(Const.KEYBOARD_EDIT);
                    sendEditor();
                    return COMEBACK;
                }
                return COMEBACK;
            case EDITION:
                isCommand();
                return COMEBACK;
            case QUEST_EDITION:
                isCommandQuest();
                return COMEBACK;
            case SET_NEW_NAME:
                if (hasMessageText()) {
                    survey.setSurveyName(ButtonUtil.getButtonName(updateMessageText, 200));
//                    surveyDao.update(survey);
                    surveyRepo.save(survey);
                    sendEditor();
                }
                return COMEBACK;
            case SET_NEW_QUEST_TEXT:
                if (hasMessageText()) {
                    survey.setQuestText(updateMessageText);
//                    surveyDao.updateQuestText(survey);
                    surveyRepo.save(survey);
                    sendEditor();
                }
                return COMEBACK;
            case SET_RANGE:
                if (hasMessageText()) {
                    if (quest.getQuestAnswer().split(Const.SPLIT_RANGE).length == updateMessageText.split(Const.SPLIT_RANGE).length) {
                        quest.setQuestAnswer(updateMessageText);
//                        questDao.update(quest);
                        questRepo.save(quest);
                    } else {
                        for (Language language : Language.values()) {
//                            Quest byId = questDao.getById(questId, language);
                            Quest byId = (Quest) questRepo.findByIdAndLanguageIdOrderById(questId, currentLanguage.getId());
                            byId.setQuestAnswer(updateMessageText);
//                            questDao.update(byId);
                            questRepo.save(byId);
                        }
                        sendMessage(Const.WRONG_NUMBER_CHOICE);
                    }
                    sendEditorQuest();
                }
                return COMEBACK;
            case DELETE:
                if (hasMessageText() && updateMessageText.equals(Const.DELETE_MESSAGE)) {
//                    factory.getSurveyAnswerDao().deleteByQuestId(surveyId);
                    surveyRepo.deleteById((long) surveyId);
//                    questDao.deleteByQuestId(surveyId);
//                    surveyDao.delete(surveyId);
                    questRepo.deleteById((long) surveyId);
                    surveyRepo.deleteById((long) surveyId);
                    getLogger().info("Deleted question №{} - {}", questId, UpdateUtil.getUser(update).toString());
                    sendMessage(Const.SURVEY_DELETE_MESSAGE);
                    return EXIT;
                }
                return COMEBACK;
            case DELETE_QUEST:
                if (hasMessageText() && updateMessageText.equalsIgnoreCase(Const.DELETE_MESSAGE)) {
//                    questDao.delete(questId);
                    questRepo.deleteById((long) questId);
                    getLogger().info("Deleted message №{} for quest №{} - {}", questId, surveyId, UpdateUtil.getUser(update).toString());
                    sendMessage(Const.CHOICE_DELETE_MESSAGE);
                    sendEditor();
                }
                return COMEBACK;
            case GET_RANGE:
                if (hasMessageText()) {
                    addQuest = new Quest();
                    addQuest.setQuestAnswer(updateMessageText).setIdSurvey(surveyId);
                    for (Language language : Language.values()) {
//                        questDao.insert(addQuest.setIdLanguage(language.getId()));
                        questRepo.save(addQuest.setIdLanguage(language.getId()));
                    }
                    sendEditor();
                }
                return COMEBACK;
        }
        return EXIT;
    }

    private void sendEditor() throws TelegramApiException {
        deleteMessage(editionMessageId);
        loadQuest();
        String text = String.format(getText(Const.NAME_SURVEY_NAME_QUEST_TEXT), messageRepo.findByIdAndLangId(Const.Ru_KZ, currentLanguage.getId()), survey.getSurveyName(), survey.getQuestText());
        buttonsLeaf = new ButtonsLeaf(questMessageList.stream().map(Quest::getQuestAnswer).collect(Collectors.toList()));
        editionMessageId = sendMessageWithKeyboard(text, buttonsLeaf.getListButton());
        toDeleteKeyboard(editionMessageId);
        waitingType = WaitingType.EDITION;
    }

    private void sendEditorQuest() throws TelegramApiException {
        deleteMessage(editionMessageId);
        quest = questRepo.findByIdAndLanguageId(questId,currentLanguage.getId());
        String text = String.format(getText(Const.QUEST_EDIT_TEXT), messageRepo.findByIdAndLangId(Const.Ru_KZ, currentLanguage.getId()), quest.getQuestAnswer());
        sendMessageWithKeyboard(text, Const.EDIT_QUEST_KEYBOARD);
        waitingType = WaitingType.QUEST_EDITION;
    }

    private void loadQuest() {
        survey = surveyRepo.findByIdAndLanguageId(surveyId, currentLanguage.getId());
        questMessageList = questRepo.findAllByIdSurveyAndLanguageId(surveyId, currentLanguage.getId());
    }

    private boolean isCommand() throws TelegramApiException {
        if (hasCallbackQuery()) {
            questId = questMessageList.get(Integer.parseInt(updateMessageText)).getId();
            updateType = WaitingType.UPDATE_QUEST;
            sendEditorQuest();
        } else if (isButton(Const.CHANGE_SURVEY_NAME)) {
            sendMessage(Const.SET_NEW_NAME_SURVEY_TEXT);
            waitingType = WaitingType.SET_NEW_NAME;
        } else if (isButton(Const.CHANGE_QUEST_TEXT)) {
            sendMessage(Const.SET_NEW_QUEST_TEXT);
            waitingType = WaitingType.SET_NEW_QUEST_TEXT;
        } else if (isButton(Const.DELETE_BUTTON)) {
            sendMessage(Const.DELETE_TEXT);
            waitingType = WaitingType.DELETE;
        } else if (isButton(Const.CHANGE_LANGUAGE)) {
            changeLanguage();
            sendEditor();
        } else if (isButton(Const.ADD_CHOICE)) {
            sendMessage(Const.SET_ANSWER_FOR_QUEST_RU_TEXT);
            waitingType = WaitingType.GET_RANGE;
        } else {
            return false;
        }
        return true;
    }

    private void changeLanguage() {
        if (currentLanguage == Language.ru) {
            currentLanguage = Language.kz;
        } else {
            currentLanguage = Language.ru;
        }
    }

    public boolean isCommandQuest() throws TelegramApiException {
        if (isButton(Const.CHANGE_CHOICE)) {
            sendMessage(Const.SET_NEW_CHOICE_TEXT);
            waitingType = WaitingType.SET_RANGE;
        } else if (isButton(Const.DELETE_BUTTON)) {
            sendMessage(Const.QUEST_DELETE_TEXT);
            waitingType = WaitingType.DELETE_QUEST;
        } else if (isButton(Const.CHANGE_LANGUAGE)) {
            changeLanguage();
            sendEditorQuest();
        } else if (isButton(Const.BACK_BUTTON)) {
            updateType = WaitingType.UPDATE_SURVEY;
            sendMessage(Const.KEYBOARD_EDIT);
            sendEditor();
        } else {
            return false;
        }
        return true;
    }
}
