package main.utility;

import common.interaction.Request;
import common.interaction.User;

import java.util.Scanner;

public class AuthHandler {

    private final String loginCommand = "login";

    private final String registerCommand = "register";

    private Scanner userScanner;

    public AuthHandler(Scanner userScanner) {this.userScanner = userScanner;}


    /**
     * Handle user authentication.
     *
     * @return Request of user.
     */
    public Request handle() {
        AuthAsker authAsker = new AuthAsker(userScanner);
        String command = authAsker.askQuestion("У вас уже есть учетная запись?") ? loginCommand : registerCommand;
        User user = new User(authAsker.askLogin(), authAsker.askPassword());
        return new Request(command, "", user);
    }
}
