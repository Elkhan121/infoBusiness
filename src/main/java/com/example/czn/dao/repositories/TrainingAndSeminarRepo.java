package com.example.czn.dao.repositories;

import com.example.czn.entity.standart.TrainingAndSeminar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface TrainingAndSeminarRepo extends JpaRepository<TrainingAndSeminar,Integer> {

    TrainingAndSeminar findById(int id);

    @Transactional
    @Modifying
    @Query("update TrainingAndSeminar set nameInKz = ?1,nameInRus = ?2 where id = ?3")
    void update(String nameInKz, String nameInRus, int id);

    @Transactional
    @Modifying
    @Query("update TrainingAndSeminar set descriptionServiceInKz = ?1,descriptionServiceInRus = ?2 where id = ?3")
    void updateDescription(String descriptionServiceInKz, String descriptionServiceInRus, int id);

    @Transactional
    @Modifying
    @Query("update TrainingAndSeminar set price = ?1 where id = ?2")
    void updatePrice(int price, int id);

    @Transactional
    @Modifying
    @Query("delete from TrainingAndSeminar where id = ?1")
    void delete(int id);

    @Transactional
    @Modifying
    @Query("update TrainingAndSeminar set activity = ?1 where id = ?2")
    void updateActivity(boolean activity, int id);

    @Transactional
    @Modifying
    @Query("update TrainingAndSeminar set duration = ?1 where id = ?2")
    void updateDuration(String duration, int id);

}
