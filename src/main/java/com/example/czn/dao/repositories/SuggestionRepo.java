package com.example.czn.dao.repositories;

import com.example.czn.entity.custom.Suggestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SuggestionRepo extends JpaRepository<Suggestion, Long> {
   // long countById();
//    List<Suggestion> findAllByPostDateBetween(Date postDate, Date postDate2);

    List<Suggestion> findAllByPostDateBetween(Date dateBegin, Date dateEnd);
}
