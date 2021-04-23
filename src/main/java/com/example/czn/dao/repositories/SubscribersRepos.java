package com.example.czn.dao.repositories;

import com.example.czn.entity.custom.Subscribes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscribersRepos extends JpaRepository<Subscribes, Long> {
    //long countBy();


    //int countBySubscribersAndSubscriptions(long userChatId, Long chatId);
//    SubscribersRepos findBySubscribersAndSubscriptions(long userChatId, long chatId);
    void deleteSubscribesBySubscribersAndSubscriptions(long subscribers, long subscriptions);
    Subscribes findBySubscribersAndSubscriptions(long subscribers, long subscriptions);
//    @Override
//    List<Subscribes> findAll();

   // void delete(long userChatId, Long chatId);
}
