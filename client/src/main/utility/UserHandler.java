package main.utility;

import common.data.Coordinates;
import common.data.FormOfEducation;
import common.data.Person;
import common.data.Semester;
import common.exceptions.CommandUsageException;
import common.exceptions.IncorrectInputInScriptException;
import common.exceptions.ScriptRecursionException;
import common.interaction.GroupRaw;
import common.interaction.Request;
import common.interaction.ResponseCode;
import common.interaction.User;
import main.App;
import common.utility.Outputer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Stack;

public class UserHandler {
    private final int maxRewriteAttempts = 1;

    private Scanner userScanner;

    private Stack<File> scriptStack = new Stack<>();

    private Stack<Scanner> scannerStack = new Stack<>();

    public UserHandler(Scanner userScanner) {
        this.userScanner = userScanner;
    }

    /**
     * Receives user input.
     *
     * @param serverResponseCode Last server's response code.
     * @return New request to server.
     */


    public Request handle(ResponseCode serverResponseCode, User user) {
        String userInput;
        String[] userCommand;
        ProcessingCode processingCode;
        int rewriteAttempts = 0;
        try {
            do {
                try {
                    if (fileMode() && (serverResponseCode == ResponseCode.ERROR ||
                            serverResponseCode == ResponseCode.SERVER_EXIT))
                        throw new IncorrectInputInScriptException();
                    while (fileMode() && !userScanner.hasNextLine()) {
                        userScanner.close();
                        userScanner = scannerStack.pop();
                        scriptStack.pop();
                    }
                    if (fileMode()) {
                        userInput = userScanner.nextLine();
                        if (!userInput.isEmpty()) {
                            Outputer.print(App.PS1);
                            Outputer.println(userInput);
                        }
                    } else {
                        Outputer.print(App.PS1);
                        userInput = userScanner.nextLine();
                    }
                    userCommand = (userInput.trim() + " ").split(" ", 2);
                    userCommand[1] = userCommand[1].trim();
                } catch (NoSuchElementException | IllegalStateException exception) {
                    Outputer.println();
                    Outputer.printerror("Произошла ошибка при вводе команды!");
                    userCommand = new String[]{"", ""};
                    rewriteAttempts++;
                    if (rewriteAttempts >= maxRewriteAttempts) {
                        Outputer.printerror("Превышено количество попыток ввода!");
                        System.exit(0);
                    }
                }
                processingCode = processCommand(userCommand[0], userCommand[1]);
            } while (processingCode == ProcessingCode.ERROR && !fileMode() || userCommand[0].isEmpty());
            try {
                if (fileMode() && (serverResponseCode == ResponseCode.ERROR || processingCode == ProcessingCode.ERROR))
                    throw new IncorrectInputInScriptException();
                switch (processingCode) {
                    case OBJECT:
                        GroupRaw groupAddRaw = generateGroupAdd();
                        return new Request(userCommand[0], userCommand[1], groupAddRaw, user);
                    case UPDATE_OBJECT:
                        GroupRaw groupUpdateRaw = generateGroupUpdate();
                        return new Request(userCommand[0], userCommand[1], groupUpdateRaw, user);
                    case SCRIPT:
                        File scriptFile = new File(userCommand[1]);
                        if (!scriptFile.exists()) throw new FileNotFoundException();
                        if (!scriptStack.isEmpty() && scriptStack.search(scriptFile) != -1)
                            throw new ScriptRecursionException();
                        scannerStack.push(userScanner);
                        scriptStack.push(scriptFile);
                        userScanner = new Scanner(scriptFile);
                        Outputer.println("Выполняю скрипт '" + scriptFile.getName() + "'...");
                        break;
                }
            } catch (FileNotFoundException exception) {
                Outputer.printerror("Файл со скриптом не найден!");
            } catch (ScriptRecursionException exception) {
                Outputer.printerror("Скрипты не могут вызываться рекурсивно!");
                throw new IncorrectInputInScriptException();
            }
        } catch (IncorrectInputInScriptException exception) {
            Outputer.printerror("Выполнение скрипта прервано!");
            while (!scannerStack.isEmpty()) {
                userScanner.close();
                userScanner = scannerStack.pop();
            }
            scriptStack.clear();
            return new Request(user);
        }
        return new Request(userCommand[0], userCommand[1], null, user);
    }



    /**
     * Processes the entered command.
     *
     * @return Status of code.
     */

    private ProcessingCode processCommand(String command, String commandArgument) {
        try {
            switch (command) {
                case "":
                    return ProcessingCode.ERROR;
                case "help":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "info":
                    if (!commandArgument.isEmpty())throw new CommandUsageException();
                    break;
                case "show":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "add":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException("{element}");
                    return ProcessingCode.OBJECT;
                case "update":
                    if (commandArgument.isEmpty()) throw new CommandUsageException("<ID> {element}");
                    return ProcessingCode.UPDATE_OBJECT;
                case "remove_by_id":
                    if (commandArgument.isEmpty()) throw new CommandUsageException("<ID>");
                    break;
                case "clear":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "save":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "execute_script":
                    if (commandArgument.isEmpty()) throw new CommandUsageException("<file_name>");
                    return ProcessingCode.SCRIPT;
                case "history":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "exit":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "server_exit":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "group_counting_by_coordinates":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "min_by_semester_enum":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "remove_at_index":
                    if (commandArgument.isEmpty()) throw new CommandUsageException("<index> {element}");
                    break;
                case "sort":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "sum_of_transferred_students":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                default:
                    Outputer.println("Команда '" + command + "' не найдена. Наберите 'help' для справки.");
                    return ProcessingCode.ERROR;
            }
        } catch (CommandUsageException exception) {
            if (exception.getMessage() != null) command += " " + exception.getMessage();
            Outputer.println("Использование: '" + command + "'");
            return ProcessingCode.ERROR;
        }
        return ProcessingCode.OK;
    }

    /**
     * Generates group to add.
     *
     * @return Group to add.
     * @throws IncorrectInputInScriptException When something went wrong in script.
     */

    private GroupRaw generateGroupAdd() throws IncorrectInputInScriptException{
        GroupAsker groupAsker = new GroupAsker(userScanner);
        if (fileMode()) groupAsker.setFileMode();
        return new GroupRaw(
                groupAsker.askName(),
                groupAsker.askCoordinates(),
                groupAsker.askStudentsCount(),
                groupAsker.askTransferredStudents(),
                groupAsker.askFormOfEducation(),
                groupAsker.askSemester(),
                groupAsker.askGroupAdmin()
        );
    }

    /**
     * Generates group to update.
     *
     * @return Group to update.
     * @throws IncorrectInputInScriptException When something went wrong in script.
     */

    private GroupRaw generateGroupUpdate() throws IncorrectInputInScriptException {
        GroupAsker groupAsker = new GroupAsker(userScanner);
        if(fileMode()) groupAsker.setFileMode();
        String name = groupAsker.askQuestion("Хотите изменить название группа?") ?
                groupAsker.askName() : null;
        Coordinates coordinates = groupAsker.askQuestion("Хотите изменить координаты группа?") ?
                groupAsker.askCoordinates(): null;
        Long studentsCount = groupAsker.askQuestion("Хотите изменить количество студентов в группе?") ?
                groupAsker.askStudentsCount(): -1;
        int transferredStudents = groupAsker.askQuestion("Хотите изменить количество переводных студентов в группе?") ?
                groupAsker.askTransferredStudents(): -1;
        FormOfEducation formOfEducation = groupAsker.askQuestion("Хотите изменить форму группового обучения?") ?
                groupAsker.askFormOfEducation(): null;
        Semester semester = groupAsker.askQuestion("Хотите изменить семестр группы?") ?
                groupAsker.askSemester(): null;
        Person groupAdmin = groupAsker.askQuestion("Хотите сменить администратора группы?") ?
                groupAsker.askGroupAdmin(): null;
        return new GroupRaw(
                name,
                coordinates,
                studentsCount,
                transferredStudents,
                formOfEducation,
                semester,
                groupAdmin
        );
    }


    /**
     * Checks if UserHandler is in file mode now.
     *
     * @return Is UserHandler in file mode now boolean.
     */
    private boolean fileMode() {
        return !scannerStack.isEmpty();
    }
}
