package com.example.czn.dao.repositories;

import com.example.czn.entity.custom.Questionnaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
@Repository
public interface QuestionnaireRepo extends JpaRepository<Questionnaire,Long > {

    List<Questionnaire> findAllByDateBetween(Date startDate, Date stopDate);

    List<Questionnaire> findAllByPostDateBetween(Date dateBegin, Date dateEnd);
}

