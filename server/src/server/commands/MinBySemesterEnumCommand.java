package server.commands;

import common.exceptions.CollectionIsEmptyException;
import common.exceptions.WrongAmountOfElementsException;
import common.interaction.User;
import server.utility.CollectionManager;
import server.utility.ResponseOutputer;

/**
 * Command 'min_by_semester_enum'. Prints the element of the collection with minimum semester.
 */

public class MinBySemesterEnumCommand extends AbstractCommand{

    private CollectionManager collectionManager;

    public MinBySemesterEnumCommand(CollectionManager collectionManager) {
        super("min_by_semester_enum","", "вывести элемент, значение поля Semester которого минимально");
        this.collectionManager = collectionManager;
    }

    @Override
    public String getDescription() {
        return "вывести элемент, значение поля Semester которого минимально";
    }

    /**
     * Executes the command.
     * @return Command exit status.
     */

    @Override
    public boolean execute(String stringArgument, Object objectArgument, User user) {
        try {
            if (!stringArgument.isEmpty() || objectArgument != null) throw new WrongAmountOfElementsException();
            ResponseOutputer.appendln(collectionManager.minBySemester());
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Использование: '" + getName() + " " + getUsage() + "'");
        } catch (CollectionIsEmptyException exception) {
            ResponseOutputer.appenderror("Коллекция пуста!");
        }
        return true;
    }
}
