package com.example.czn.dao.repositories;

import com.example.czn.entity.standart.Language;
import com.example.czn.entity.standart.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface MessageRepo extends JpaRepository<Message, Long> {
    Message findByIdAndLangId(long id, int langId);
    Message findByIdAndLangId(String name, int lang);
    Message deleteByIdAndLangId(long id, int langId);
    List<Message> findAllByNameContainingAndLangIdOrderById(String name,int langId);

    @Transactional
    @Modifying
    @Query("update Message set name = ?1 where id = ?2 and langId = ?3")
    void update(String name, long id, int langId);
}
