package com.example.czn.dao.repositories;


import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TelegramBorRepositoryProvider {
    @Getter
    @Setter
    private static MessageRepo messageRepo;

    @Getter
    @Setter
    private static ButtonRepo buttonRepo;

    @Getter
    @Setter
    private static AdminQuestionRepo adminQuestionRepo;

    @Getter
    @Setter
    private static AdminRepos adminRepos;

    @Getter
    @Setter
    private static ComplaintRepo complaintRepo;

    @Getter
    @Setter
    private static KeyboardMarkUpRepo keyboardMarkUpRepo;

    @Getter
    @Setter
    private static LanguageUserRepo languageUserRepo;

    @Getter
    @Setter
    private static OperatorRepo operatorRepo;

    @Getter
    @Setter
    private static QuestRepo questRepo;

    @Getter
    @Setter
    private static ResponsibleRepos responsibleRepos;

    @Getter
    @Setter
    private static SubscribersRepos subscribersRepos;

    @Getter
    @Setter
    private static SuggestionRepo suggestionRepo;

    @Getter
    @Setter
    private static SurveyAnswerRepo surveyAnswerRepo;

    @Getter
    @Setter
    private static SurveyRepo surveyRepo;

    @Getter
    @Setter
    private static UserRepo userRepo;


    @Autowired
    public TelegramBorRepositoryProvider(UserRepo userRepo,
                                         SurveyRepo surveyRepo,
                                         SurveyAnswerRepo surveyAnswerRepo,
                                         SuggestionRepo suggestionRepo,
                                         SubscribersRepos subscribersRepos,
                                         ResponsibleRepos responsibleRepos,
                                         QuestRepo questRepo,
                                         OperatorRepo operatorRepo,
                                         MessageRepo messageRepo,
                                         ButtonRepo buttonRepo,
                                         AdminQuestionRepo adminQuestionRepo,
                                         AdminRepos adminRepos,
                                         ComplaintRepo complaintRepo,
                                         KeyboardMarkUpRepo keyboardMarkUpRepo,
                                         LanguageUserRepo languageUserRepo){
        setUserRepo(userRepo);
        setSurveyRepo(surveyRepo);
        setSurveyAnswerRepo(surveyAnswerRepo);
        setSuggestionRepo(suggestionRepo);
        setSubscribersRepos(subscribersRepos);
        setResponsibleRepos(responsibleRepos);
        setQuestRepo(questRepo);
        setOperatorRepo(operatorRepo);
        setLanguageUserRepo(languageUserRepo);
        setMessageRepo(messageRepo);
        setButtonRepo(buttonRepo);
        setAdminQuestionRepo(adminQuestionRepo);
        setAdminRepos(adminRepos);
        setComplaintRepo(complaintRepo);
        setKeyboardMarkUpRepo(keyboardMarkUpRepo);
    }
}
