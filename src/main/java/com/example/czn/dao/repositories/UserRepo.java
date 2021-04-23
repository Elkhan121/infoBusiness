package com.example.czn.dao.repositories;

import com.example.czn.entity.standart.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    long countByChatId(long newAdminChatId);
    User findByChatId(Long chatId);

}
