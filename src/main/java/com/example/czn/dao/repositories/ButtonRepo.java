package com.example.czn.dao.repositories;

import com.example.czn.entity.standart.Button;
import com.example.czn.entity.standart.Language;
import com.example.czn.entity.standart.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface ButtonRepo extends JpaRepository<Button, Long> {
   List<Button> findAllByLangIdAndIdBetween(int currentLanguage,int first,int second);
   Button findNameByIdAndLangId(int buttonId, int currentLanguage);
   Button findByIdAndLangId(int buttonId, int currentLanguage);

   Button findById(int id);
   int countButtonByNameAndLangId(String buttonName, int currentLanguage);
   List<Button> findAllByNameContainingAndLangIdOrderById(String name,int langId);
   Button findByNameContainingAndLangIdOrderById(String name,int langId);
   Optional<Button> findByName(String inputtedText);
   Button findByNameAndLangId(String name, int langId);

   @Transactional
   @Modifying
   @Query("update Button set name = ?1 where id = ?2 and langId = ?3")
   void update(String name, int id, int langId);

   @Transactional
   @Modifying
   @Query("update Button set name = ?1 where id = ?2 and langId = ?3")
   void updateFile(String name, int id, int langId);

   Button deleteByNameAndLangId(String name, int langId);


}

