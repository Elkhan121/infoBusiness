package com.example.czn.dao.repositories;

import com.example.czn.entity.standart.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepos extends JpaRepository<Admin, Long> {
    Admin findByUserId(long chatId);

    long countByUserId(long newAdminChatId);

    void deleteByUserId(long userId);

}