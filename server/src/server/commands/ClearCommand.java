package server.commands;

import common.data.StudyGroup;
import common.exceptions.DatabaseHandlingException;
import common.exceptions.ManualDatabaseEditException;
import common.exceptions.PermissionDeniedException;
import common.exceptions.WrongAmountOfElementsException;
import common.interaction.User;
import common.utility.Outputer;
import server.utility.CollectionManager;
import server.utility.DatabaseCollectionManager;
import server.utility.ResponseOutputer;

public class ClearCommand extends AbstractCommand{

    private CollectionManager collectionManager;

    private DatabaseCollectionManager databaseCollectionManager;

    public ClearCommand(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        super("clear", "", "очистить коллекцию");
        this.collectionManager = collectionManager;
        this.databaseCollectionManager = databaseCollectionManager;
    }

    /**
     * Executes the command.
     *
     * @return Command exit status.
     */

    @Override
    public boolean execute(String stringArgument, Object objectArgument, User user) {
        try {
            if (!stringArgument.isEmpty() || objectArgument != null) throw new WrongAmountOfElementsException();
            for (StudyGroup group : collectionManager.getCollection()) {
                if (!group.getOwner().equals(user)) throw new PermissionDeniedException();
                if (!databaseCollectionManager.checkGroupUserId(group.getId(), user)) throw new ManualDatabaseEditException();
            }
            collectionManager.clearCollection();
            databaseCollectionManager.clearCollection();
            ResponseOutputer.appendln("Коллекция очищена!");
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Использование: '" + getName() + " " + getUsage() + "'");
        } catch (DatabaseHandlingException exception) {
            Outputer.printerror("Phia server: " + exception.getMessage());
            ResponseOutputer.appenderror("Произошла ошибка при обращении к базе данных!");
        } catch (PermissionDeniedException exception) {
            Outputer.printerror("Phia server: " + exception.getMessage());
            ResponseOutputer.appenderror("Недостаточно прав для выполнения данной команды!");
            ResponseOutputer.appendln("Принадлежащие другим пользователям объекты доступны только для чтения.");
        } catch (ManualDatabaseEditException exception) {
            Outputer.printerror("Phia server: " + exception.getMessage());
            ResponseOutputer.appenderror("Произошло прямое изменение базы данных!");
            ResponseOutputer.appendln("Перезапустите клиент для избежания возможных ошибок.");
        }
        return false;
    }







}
