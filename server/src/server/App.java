package server;

import common.exceptions.NotInDeclaredLimitsException;
import common.exceptions.WrongAmountOfElementsException;
import common.utility.Outputer;
import server.commands.*;
import server.utility.*;

public class App {

    private static final int MAX_CLIENTS = 1000;

//    private static String databaseUsername = "s345132";

    private static String databaseUsername = "s345132";

    private static int port = 4444;

    private static String databaseHost;

    private static String databasePassword = "3FHsKjvue3AvV9wU";

//    private static String databasePassword = "21052002";

    private static String databaseAddress = "jdbc:postgresql://127.0.0.1:5656/studs";

//    private static String databaseAddress = "jdbc:postgresql://localhost:5432/Lab7DB";

    public static void main(String[] args) {
//        if (!initialize(args)) return;
//        port = 2048;
        DatabaseHandler databaseHandler = new DatabaseHandler(databaseAddress, databaseUsername, databasePassword);
        DatabaseUserManager databaseUserManager = new DatabaseUserManager(databaseHandler);
        DatabaseCollectionManager databaseCollectionManager = new DatabaseCollectionManager(databaseHandler, databaseUserManager);
        CollectionManager collectionManager = new CollectionManager(databaseCollectionManager);
        CommandManager commandManager = new CommandManager(
                new HelpCommand(),
                new InfoCommand(collectionManager),
                new ShowCommand(collectionManager),
                new AddCommand(collectionManager, databaseCollectionManager),
                new UpdateCommand(collectionManager, databaseCollectionManager),
                new RemoveByIdCommand(collectionManager, databaseCollectionManager),
                new ClearCommand(collectionManager, databaseCollectionManager),
                new ExitCommand(),
                new ExecuteScriptCommand(),
                new RemoveAtIndexCommand(collectionManager, databaseCollectionManager),
                new SortCommand(collectionManager),
                new HistoryCommand(),
                new SumOfTransferredStudentsCommand(collectionManager),
                new MinBySemesterEnumCommand(collectionManager),
                new GroupCountingByCoordinatesCommand(collectionManager),
                new ServerExitCommand(),
                new LoginCommand(databaseUserManager),
                new RegisterCommand(databaseUserManager)
        );
        Server server = new Server(port, MAX_CLIENTS, commandManager);
        server.run();
        databaseHandler.closeConnection();
    }

    /**
     * Controls initialization.
     */
    private static boolean initialize(String[] args) {
        try {
            if (args.length != 1) throw new WrongAmountOfElementsException();
            port = Integer.parseInt(args[0]);
            if (port < 0) throw new NotInDeclaredLimitsException();
            return true;
        } catch (WrongAmountOfElementsException exception) {
            String jarName = new java.io.File(App.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .getPath())
                    .getName();
            Outputer.println("Использование: 'java -jar " + jarName + " <port>");
        } catch (NumberFormatException exception) {
            Outputer.printerror("Порт должен быть представлен числом!");
        } catch (NotInDeclaredLimitsException exception) {
            Outputer.printerror("Порт не может быть отрицательным!");
        }
        return false;
    }
}
