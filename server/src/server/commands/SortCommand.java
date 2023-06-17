package server.commands;

import common.exceptions.CollectionIsEmptyException;
import common.interaction.User;
import server.utility.CollectionManager;
import server.utility.ResponseOutputer;

public class SortCommand extends AbstractCommand{
    private CollectionManager collectionManager;

    public SortCommand(CollectionManager collectionManager) {
        super("sort","","Отображение коллекций, отсортированных по имени");
        this.collectionManager = collectionManager;
    }


    /**
     * Executes the command.
     * @return Command exit status.
     */

    @Override
    public boolean execute(String stringArgument, Object objectArgument, User user) {
        try {
            if (collectionManager.collectionSize() == 0) throw new CollectionIsEmptyException();
            collectionManager.sortByNameAscending();
            ResponseOutputer.appendln(collectionManager.showCollection());
            return true;
        } catch (CollectionIsEmptyException exception) {
            ResponseOutputer.appendln("Коллекция пуста!");
        }
        return false;
    }
}
