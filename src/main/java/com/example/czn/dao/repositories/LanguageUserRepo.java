package com.example.czn.dao.repositories;

import com.example.czn.entity.standart.LanguageUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageUserRepo extends JpaRepository<LanguageUser, Long> {
    LanguageUser findAllByChatId(long chatId);
}
