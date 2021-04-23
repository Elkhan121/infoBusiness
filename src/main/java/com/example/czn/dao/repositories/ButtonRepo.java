package com.example.czn.dao.repositories;

import com.example.czn.entity.standart.Button;
import com.example.czn.entity.standart.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface ButtonRepo extends JpaRepository<Button, Long> {
   Button findNameByIdAndLangId(int buttonId, int currentLanguage);
   Button findByIdAndLangId(int buttonId, int currentLanguage);
   Button findById(int id);
   int countButtonByNameAndLangId(String buttonName, int currentLanguage);

   Optional<Button> findByName(String inputtedText);
}
