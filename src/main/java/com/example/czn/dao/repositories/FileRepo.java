package com.example.czn.dao.repositories;

import com.example.czn.entity.standart.ButtonFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface FileRepo extends JpaRepository<ButtonFile, Long> {
    ButtonFile findByButtonId(int buttonId);
    ButtonFile deleteByButtonId(int ButtonId);
    @Transactional
    @Modifying
    @Query("delete from ButtonFile WHERE buttonId = ?1")
    void delete(int buttonId);

}
