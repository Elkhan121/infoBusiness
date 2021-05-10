package com.example.czn.command.impl;

import com.example.czn.command.Command;
import com.example.czn.dao.repositories.SubscribersRepos;
import com.example.czn.dao.repositories.TelegramBorRepositoryProvider;
import com.example.czn.dao.repositories.UserRepo;
import com.example.czn.entity.custom.Subscribes;
import com.example.czn.entity.standart.User;
import com.example.czn.service.KeyboardService;
import com.example.czn.service.LanguageService;
import com.example.czn.util.ButtonsLeaf;
import com.example.czn.util.Const;
import com.example.czn.util.type.WaitingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class id021_UserList extends Command {
//    @Autowired
    UserRepo userRepo = TelegramBorRepositoryProvider.getUserRepo();
    KeyboardService keyboardService = new KeyboardService();

    private ButtonsLeaf buttonsLeaf;
    private int delete;
    private List<User> userList = new ArrayList<>();
    @Override
    public boolean execute() throws SQLException, TelegramApiException {
            SubscribersRepos subscribersRepos = TelegramBorRepositoryProvider.getSubscribersRepos();
        switch (waitingType){
            case START:
                userList = userRepo.findAll();
                List<String> users = new ArrayList<>();
                userList.forEach(user -> {
                    users.add(user.getFullName());
                });
                buttonsLeaf = new ButtonsLeaf(users, userList.size()/90, "<<", ">>");

                try {
                    delete = sendMessageWithKeyboard("List of users", buttonsLeaf.getListButton(), chatId);
                    waitingType = WaitingType.CHOOSE_OPTION;
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                return false;

            case CHOOSE_OPTION:
                long userChatId = 0;
              //  deleteMessage(delete);
                if (hasCallbackQuery()){
                    if (buttonsLeaf.isNext(updateMessageText)){
                        try {
                            delete = sendMessageWithKeyboard("List of users", buttonsLeaf.getListButton(), chatId);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    }else{
                      //      System.out.println(updateMessageText);
                        sendMessageWithKeyboard("Информация о контакте: \n Имя пользователя:  " +
                                userList.get(Integer.parseInt(updateMessageText)).getFullName() +
                                "\n Username:  " + userList.get(Integer.parseInt(updateMessageText)).getUserName() +
                                "\n Email: " + userList.get(Integer.parseInt(updateMessageText)).getEmail(),205);

                        userChatId = userList.get(Integer.parseInt(updateMessageText)).getChatId();

                        boolean isExist = subscribersRepos.findBySubscribersAndSubscriptions(userChatId, chatId) != null;

                        if (isExist == true){
                            delete = sendMessageWithKeyboard("отписаться", keyboardService
                                    .getKeyboard(keyboardMarkUpRepo.findById(73), LanguageService.getLanguage(chatId)));
                            waitingType = WaitingType.UNFOLLOW;
                            return COMEBACK;
                        }else{
                            delete = sendMessageWithKeyboard("подписаться", keyboardService
                                    .getKeyboard(keyboardMarkUpRepo.findById(72), LanguageService.getLanguage(chatId)));
                            waitingType = WaitingType.FOLLOW;
                            return false;
                        } }
                    return false;
                }
                return false;
            case UNFOLLOW:
                userChatId = userList.get(Integer.parseInt(updateMessageText)).getChatId();
                if (isButton(77)){
                    subscribersRepos.deleteSubscribesBySubscribersAndSubscriptions(userChatId, chatId);
                    sendMessage("Вы отписались!", chatId);
                }
                return false;
            case FOLLOW:
                userChatId = userList.get(Integer.parseInt(updateMessageText)).getChatId();
                if (updateMessageText.equalsIgnoreCase("подписаться")) delete = sendMessage("Вы подписались!",
                        chatId);
                Subscribes subscribes = new Subscribes();
                subscribes.setSubscribers(userChatId);
                subscribes.setSubscriptions(chatId);
                subscribersRepos.save(subscribes);
                return false;
            }
           return true;
    }
}
