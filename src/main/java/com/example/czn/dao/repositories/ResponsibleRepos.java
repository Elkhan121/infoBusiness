package com.example.czn.dao.repositories;

import com.example.czn.entity.standart.Admin;
import com.example.czn.entity.standart.Responsible;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResponsibleRepos extends JpaRepository<Responsible, Long> {
    //Responsible countByUserId();
//    @Override
//    List<Responsible> findAllById(Iterable<Long> longs);
    //void deleteByUserId(long userId);

    //@Override
    List<Responsible> findAllByUserId(long userId);
    //void save(Long chatId, String str);

    //void delete(Admin aLong);

   // int countByUserId(long newAdminChatId);
}
