package server.commands;

import common.data.StudyGroup;
import common.exceptions.*;
import common.interaction.User;
import server.utility.CollectionManager;
import server.utility.DatabaseCollectionManager;
import server.utility.ResponseOutputer;

/**
 * Command 'remove_at_index'. Removes the element by index.
 */

public class RemoveAtIndexCommand extends  AbstractCommand{

    private CollectionManager collectionManager;

    private DatabaseCollectionManager databaseCollectionManager;

    public RemoveAtIndexCommand(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        super("remove_at_index <index>","","удалить элемент из коллекции по index");
        this.collectionManager = collectionManager;
        this.databaseCollectionManager = databaseCollectionManager;
    }

    /**
     * Executes the command.
     * @return Command exit status.
     */

    @Override
    public boolean execute(String stringArgument, Object objectArgument, User user) {
        try {
            if (stringArgument.isEmpty() || objectArgument != null) throw new WrongAmountOfElementsException();
            if (collectionManager.collectionSize() == 0) throw new CollectionIsEmptyException();
            int index = Integer.parseInt(stringArgument);
            StudyGroup groupToRemove = collectionManager.getByIndex(index);
            long id = collectionManager.getIdByIndex(index);
            if (groupToRemove == null) throw new GroupNotFoundException();
            if (!groupToRemove.getOwner().equals(user)) throw new PermissionDeniedException();
            if (!databaseCollectionManager.checkGroupUserId(groupToRemove.getId(), user)) throw new ManualDatabaseEditException();
            databaseCollectionManager.deleteGroupById(id);
            collectionManager.removeFromCollection(groupToRemove);
            ResponseOutputer.appendln("группа успешно удалена!");
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Использование: '" + getName() + "'");
        } catch (CollectionIsEmptyException exception) {
            ResponseOutputer.appenderror("Коллекция пуста!");
        } catch (NumberFormatException exception) {
            ResponseOutputer.appenderror("Index должен быть представлен числом!");
        } catch (GroupNotFoundException exception) {
            ResponseOutputer.appenderror("группа с таким ID в коллекции нет!");
        } catch (IndexOutOfBoundsException exception) {
            ResponseOutputer.appenderror("этот индекс не существует в коллекции");
        } catch (ManualDatabaseEditException exception) {
            ResponseOutputer.appenderror("Произошло прямое изменение базы данных!");
            ResponseOutputer.appendln("Перезапустите клиент для избежания возможных ошибок.");
        } catch (PermissionDeniedException exception) {
            ResponseOutputer.appenderror("Недостаточно прав для выполнения данной команды!");
            ResponseOutputer.appendln("Принадлежащие другим пользователям объекты доступны только для чтения.");
        } catch (DatabaseHandlingException exception) {
            ResponseOutputer.appenderror("Произошла ошибка при обращении к базе данных!");
        }
        return false;
    }
}
