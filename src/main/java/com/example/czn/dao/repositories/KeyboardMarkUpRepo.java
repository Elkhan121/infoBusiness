package com.example.czn.dao.repositories;

import com.example.czn.entity.standart.Keyboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.util.List;
@Repository
public interface KeyboardMarkUpRepo extends JpaRepository<Keyboard, Long> {
    Keyboard        findById(long key);
    int             countById(int id);
}
