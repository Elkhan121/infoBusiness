package com.example.czn.dao.repositories;

import com.example.czn.dao.enums.TableNames;
import com.example.czn.entity.custom.Quest;
import com.example.czn.entity.standart.Language;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestRepo extends JpaRepository<Quest, Long> {
    List<Quest> findByIdAndLanguageIdOrderById(int id, int languageId);
    Quest findByIdAndLanguageId(int id, int languageId);
    Quest deleteByIdSurvey(int idSurvey);
    Quest deleteById(int id);

    Quest findById(int questId);

    //Quest findByIdAndLanguageId(int id, int languageId);

    List<Quest> findAllByIdSurveyAndLanguageId(int idSurvey, int languageId);

    //int findById(int id);
}
