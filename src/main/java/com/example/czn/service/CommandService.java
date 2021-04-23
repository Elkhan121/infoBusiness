package com.example.czn.service;

import com.example.czn.command.Command;
import com.example.czn.command.CommandFactory;
import com.example.czn.dao.repositories.ButtonRepo;
import com.example.czn.dao.repositories.MessageRepo;
import com.example.czn.dao.repositories.TelegramBorRepositoryProvider;
import com.example.czn.dao.repositories.UserRepo;
import com.example.czn.entity.standart.Button;
import com.example.czn.entity.standart.Language;
import com.example.czn.entity.standart.User;
import com.example.czn.exception.CommandNotFoundException;
import com.example.czn.util.Const;
import com.example.czn.util.UpdateUtil;
import org.hibernate.NonUniqueResultException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

@Service
public class CommandService {

    private long chatId;
    private ButtonRepo buttonRepo = TelegramBorRepositoryProvider.getButtonRepo();
    private MessageRepo messageRepo = TelegramBorRepositoryProvider.getMessageRepo();
    private UserRepo userRepo = TelegramBorRepositoryProvider.getUserRepo();

    public Optional<Command> getCommand(Update update) throws CommandNotFoundException{
        chatId = UpdateUtil.getChatId(update);
        Message updateMessage = update.getMessage();
        String inputtedText;
        if (update.hasCallbackQuery()){
            inputtedText = update.getCallbackQuery().getData().split(Const.SPLIT)[0];
            updateMessage = update.getCallbackQuery().getMessage();
            try {
                if (inputtedText != null && inputtedText.substring(0,6).equals(Const.ID_MARK)){
                    try {
                        return Optional.ofNullable(getCommandById(Integer.parseInt(inputtedText.split(Const.SPLIT)[0].replaceAll(Const.ID_MARK,""))));
                    } catch (Exception e){
                        inputtedText = updateMessage.getText();
                    }
                }
            }catch (Exception e){}
        } else {
            try {
                inputtedText = updateMessage.getText();
            } catch (Exception e){
                throw new CommandNotFoundException(e);
            }
        } try {
            return getCommand(buttonRepo.findByName(inputtedText));
        }catch (IncorrectResultSizeDataAccessException | NonUniqueResultException e){
            User user;
            if (userRepo.findByChatId(chatId) != null){
                user = userRepo.findByChatId(chatId);
            }else user = new User();
            return getCommand(buttonRepo.findByName(inputtedText));
        }
    }

    public Optional<Command> getCommand(Optional<Button> button) throws CommandNotFoundException{
        return button.map(Button::getCommandId).map(integer -> {
            return Optional.ofNullable(CommandFactory.getCommand(integer)).map(command -> {
                command.setId(integer);

                command.setMessageId(button.map(Button::getMessageId).orElse(0));
                return command;
            });
        }).orElseThrow(() -> new CommandNotFoundException());
    }

//
//    public Command getCommand(Update update) throws CommandNotFoundException {
//        Message updateMessage = update.getMessage();
//        String inputtedText;
//        if (update.hasCallbackQuery()) {
//            inputtedText = update.getCallbackQuery().getData().split(Const.SPLIT)[0];
//            updateMessage = update.getCallbackQuery().getMessage();
//            try {
//                if (inputtedText != null && inputtedText.substring(0, 6).equals(Const.ID_MARK)) {
//                    try {
//                        return getCommandById(Integer.parseInt(inputtedText.split(Const.SPLIT)[0].replaceAll(Const.ID_MARK, "")));
//                    } catch (Exception e) {
//                        inputtedText = updateMessage.getText();
//                    }
//                }
//            } catch (Exception e) {
//            }
//        } else {
//            try {
//                inputtedText = updateMessage.getText();
//            } catch (Exception e) {
//                throw new CommandNotFoundException(new Exception("No data is available"));
//            }
//        }
//        Button button;
//        button = buttonRepo.findByName(inputtedText);
//        try{
//            return getCommand(button);
//        }catch (Exception e){
//
//        }
//        return getCommand(button);
//    }

//    private Command getCommand(Button button) throws CommandNotFoundException {
//        if (button.getCommandId() == 0) throw new CommandNotFoundException(new Exception("No data is available"));
//        Command command = CommandFactory.getCommand(button.getCommandId());
//        command.setId(button.getCommandId());
//        command.setMessageId(button.getMessageId());
//        return command;
//    }

    private Command getCommandById(int id) {
        return CommandFactory.getCommand(id);
    }

    protected Language getLanguage(){
        if (chatId == 0) return Language.ru;
        return LanguageService.getLanguage(chatId);
    }
}
