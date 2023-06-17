package server.utility;

import common.interaction.Request;
import common.interaction.Response;
import common.interaction.ResponseCode;
import common.interaction.User;

import java.util.concurrent.RecursiveTask;

/**
 * A class for handle request task.
 */

public class HandleRequestTask extends RecursiveTask<Response> {
    private Request request;
    private CommandManager commandManager;

    public HandleRequestTask(Request request, CommandManager commandManager) {
        this.request = request;
        this.commandManager = commandManager;
    }

    @Override
    protected Response compute() {
        User hashedUser = new User(
                request.getUser().getUsername(),
                PasswordHasher.hashPassword(request.getUser().getPassword())
        );
        commandManager.addToHistory(request.getCommandName(), request.getUser());
        ResponseCode responseCode = executeCommand(request.getCommandName(), request.getCommandStringArgument(),
                request.getCommandObjectArgument(), hashedUser);
        return new Response(responseCode, ResponseOutputer.getAndClear());
    }

    /**
     * Executes a command from a request.
     *
     * @param command               Name of command.
     * @param commandStringArgument String argument for command.
     * @param commandObjectArgument Object argument for command.
     * @return Command execute status.
     */
    private synchronized ResponseCode executeCommand(String command, String commandStringArgument,
                                                     Object commandObjectArgument, User user) {
        switch (command) {
            case "":
                break;
            case "help":
                if (!commandManager.help(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "info":
                if (!commandManager.info(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "show":
                if (!commandManager.show(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "add":
                if (!commandManager.add(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "update":
                if (!commandManager.update(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "remove_by_id":
                if (!commandManager.removeById(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "clear":
                if (!commandManager.clear(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "execute_script":
                if (!commandManager.executeScript(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "exit":
                if (!commandManager.exit(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                return ResponseCode.CLIENT_EXIT;
            case "remove_at_index":
                if (!commandManager.removeAtIndex(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "sort":
                if (!commandManager.sort(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "history":
                if (!commandManager.history(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "sum_of_transferred_students":
                if (!commandManager.sumOfTransferredStudents(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "min_by_semester_enum":
                if (!commandManager.minBySemesterEnum(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "group_counting_by_coordinates":
                if (!commandManager.groupCountingByCoordinates(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "server_exit":
                if (!commandManager.serverExit(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                return ResponseCode.SERVER_EXIT;
            case "login":
                if (!commandManager.login(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "register":
                if (!commandManager.register(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            default:
                ResponseOutputer.appendln("Команда '" + command + "' не найдена. Наберите 'help' для справки.");
                return ResponseCode.ERROR;
        }
        return ResponseCode.OK;
    }
}
