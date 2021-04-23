package com.example.czn.dao.repositories;

import com.example.czn.entity.standart.Operator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperatorRepo extends JpaRepository<Operator, Long> {
    long countByUserId(Long chatId);
}
