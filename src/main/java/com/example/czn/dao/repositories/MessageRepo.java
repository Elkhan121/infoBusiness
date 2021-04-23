package com.example.czn.dao.repositories;

import com.example.czn.entity.standart.Language;
import com.example.czn.entity.standart.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepo extends JpaRepository<Message, Long> {
    Message findByIdAndLangId(long id, int lang);
}
