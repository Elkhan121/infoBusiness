package com.example.czn.dao.repositories;

import com.example.czn.entity.custom.AdminQuest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminQuestionRepo extends JpaRepository<AdminQuest, Long> {
//    int countAdminQuestById(long id);
    AdminQuest findAllById(int id);

    //List<AdminQuest> findAll();

}
