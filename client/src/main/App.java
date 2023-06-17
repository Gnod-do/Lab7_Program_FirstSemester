package main;

import common.exceptions.NotInDeclaredLimitsException;
import common.exceptions.WrongAmountOfElementsException;
import main.utility.AuthHandler;
import common.utility.Outputer;
import main.utility.UserHandler;

import java.util.Scanner;

/**
 * Main client class. Creates all client instances.
 *
 * @author Do Van Dong - P3125.
 */


public class App {

    public static final String PS1 = "$ ";

    public static final String PS2 = "> ";

    private static final int RECONNECTION_TIMEOUT = 5 * 1000;

    private static final int MAX_RECONNECTION_ATTEMPTS = 5;

    private static String host;

    private static int port;

    public static void main(String[] args) {
//        if (!initialize(args)) return;
        host = "localhost";
        port = 4444;
        Scanner userScanner = new Scanner(System.in);
        AuthHandler authHandler = new AuthHandler(userScanner);
        UserHandler userHandler = new UserHandler(userScanner);
        Client client = new Client(host, port, RECONNECTION_TIMEOUT, MAX_RECONNECTION_ATTEMPTS,userHandler, authHandler);
        client.run();
        userScanner.close();
    }

    /**
     * Controls initialization.
     */
    private static boolean initialize(String[] args) {
        try {
            if (args.length != 2) throw new WrongAmountOfElementsException();
            host = args[0];
            port = Integer.parseInt(args[1]);
            if (port < 0) throw new NotInDeclaredLimitsException();
            return true;
        } catch (WrongAmountOfElementsException exception) {
            String jarName = new java.io.File(App.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .getPath())
                    .getName();
            Outputer.println("Использование: 'java -jar " + jarName + " <host> <port>'");
        } catch (NumberFormatException exception) {
            Outputer.printerror("Порт должен быть представлен числом!");
        } catch (NotInDeclaredLimitsException exception) {
            Outputer.printerror("Порт не может быть отрицательным!");
        }
        return false;
    }
}
