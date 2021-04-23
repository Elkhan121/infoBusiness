package com.example.czn.command;

import com.example.czn.command.impl.*;
import com.example.czn.exception.NotRealizedMethodException;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

@Slf4j
public class CommandFactory {

    private static Map<Integer, Class<? extends Command>> mapCommand;

    public  static Command getCommand(int id) {
        return getFormMap(id).orElseThrow(() -> new NotRealizedMethodException("Not realized for type: " + id));
    }

    private static void addCommand(Class<? extends Command> commandClass) {
        int id = -1;
        try {
            id = getId(commandClass.getName());
        }
        catch (Exception e) { log.warn("Command {} no has id, id set {}", commandClass, id); }
        if (id > 0 && mapCommand.get(id) != null)
            log.warn("ID={} has duplicate command {} - {}", id, commandClass, mapCommand.get(id));
        mapCommand.put(id, commandClass);
    }

    private static int  getId(String commandName) { return Integer.parseInt(commandName.split("_")[0].replaceAll("[^0-9]", "")); }

    private static Optional<Command> getFormMap(int id) {
        if (mapCommand == null) {
            init();
        }
        try {
            return Optional.of(mapCommand.get(id).newInstance());
        }
        catch (Exception e) {
            log.error("Command caput: ", e);
        }
        return Optional.empty();
    }

    private static void  init() {
        mapCommand = new HashMap<>();
        addCommand(id002_SelectionLanguage.class);
        addCommand(id001_ShowInfo.class);
        addCommand(id003_ShowAdminInfo.class);
        addCommand(id004_EditAdmin.class);
        addCommand(id005_EditMenu.class);
        addCommand(id006_MapLocationSend.class);
        addCommand(id007_Suggestion.class);
        addCommand(id008_ReportSuggestion.class);
        addCommand(id009_Complaint.class);
        addCommand(id010_ReportComplaint.class);
        addCommand(id011_SurveyShow.class);
        addCommand(id012_AddSurvey.class);
        addCommand(id013_EditSurvey.class);
        addCommand(id014_ReportSurvey.class);
        addCommand(id015_ShowOperatorInfo.class);
        addCommand(id016_EditResponsible.class);
        addCommand(id017_EditResponsible.class);
        addCommand(id018_AdminQuest.class);
        addCommand(id019_QuestionList.class);
        addCommand(id020_Profile.class);
        addCommand(id021_UserList.class);
        printListCommand();
    }

    private static void                 printListCommand() {
        StringBuilder stringBuilder = new StringBuilder();
        new TreeMap<>(mapCommand).forEach((y, x) -> stringBuilder.append(x.getSimpleName()).append("\n"));
        log.info("List command:\n{}", stringBuilder.toString());
    }
}



//package com.example.czn.command;
//
//import com.example.czn.command.impl.*;
//import com.example.czn.exception.NotRealizedMethodException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.Optional;
//
////@Component
//public class CommandFactory {
//    public  static Command getCommand(int id) {
////        System.out.println(id);
//        return getFormMap(id).orElseThrow(() -> new NotRealizedMethodException("Not realized for type: " + id));
//    }
//    private Command getCommandWithoutReflection(int id) {
//        switch (id) {
//            case 1:
//                return new id001_ShowInfo();
//            case 2:
//                return new id002_SelectionLanguage();
//            case 3:
//                return new id003_ShowAdminInfo();
//            case 4:
//                return new id004_EditAdmin();
//            case 5:
//                return new id005_EditMenu();
//            case 6:
//                return new id006_MapLocationSend();
//            case 7:
//                return new id007_Suggestion();
//            case 8:
//                return new id008_ReportSuggestion();
//            case 9:
//                return new id009_Complaint();
//            case 10:
//                return new id010_ReportComplaint();
//            case 11:
//                return new id011_SurveyShow();
//            case 12:
//                return new id012_AddSurvey();
//            case 13:
//                return new id013_EditSurvey();
//            case 14:
//                return new id014_ReportSurvey();
//            case 16:
//                return new id016_EditResponsible();
//            case 17:
//                return new id017_EditResponsible();
//            case 18:
//                return new id018_AdminQuest();
//            case 19:
//                return new id019_QuestionList();
//            case 20:
//                return new id020_Profile();
//            case 21:
//                return new id021_UserList();
//        }
//        return null;
//    }
//    private static Optional<Command> getFormMap(int id) {
////        System.out.println(id);
//        if (mapCommand == null) {
//            init();
//        }
//        //System.out.println(mapCommand);
//        try {
//            return Optional.of(mapCommand.get(id).newInstance());
//        }
//        catch (Exception e) {
//            log.error("Command caput: ", e);
//        }
//        return Optional.empty();
//    }
//}
