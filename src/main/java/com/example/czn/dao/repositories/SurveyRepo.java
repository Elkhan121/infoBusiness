package com.example.czn.dao.repositories;

import com.example.czn.dao.enums.TableNames;
import com.example.czn.entity.custom.Survey;
import com.example.czn.entity.standart.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SurveyRepo extends JpaRepository<Survey, Long> {
    //List<Survey> findAllByHideAndLanguageIdAndId(Language currentLanguage, int id);
    //List<Survey> findAllByLanguageId();
    Survey findByIdAndLanguageId(int surveyId, int currentLanguage);
    void deleteById(Survey entity);

    List<Survey> findAllByLanguageId(int currentLanguage);

    //Survey findById(int surveyId, Language currentLanguage);

    Survey findById(int surveys);


    List<Survey> findAllByLanguageIdOrderByIdDesc(int languageId);
}
