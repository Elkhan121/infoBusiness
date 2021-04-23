package com.example.czn.dao.repositories;

import com.example.czn.entity.custom.SurveyAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SurveyAnswerRepo extends JpaRepository<SurveyAnswer, Long> {
    void deleteBySurveyId(SurveyAnswer entity);
    List<SurveyAnswer> findAllBySurveyId(int id);
}
