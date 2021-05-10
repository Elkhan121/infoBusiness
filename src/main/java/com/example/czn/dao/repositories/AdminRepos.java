package com.example.czn.dao.repositories;

import com.example.czn.entity.standart.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepos extends JpaRepository<Admin, Long> {
    Admin findByUserId(long chatId);
    Admin deleteByComment(String comment);

    long countByUserId(long newAdminChatId);

    void deleteByUserId(long userId);

}