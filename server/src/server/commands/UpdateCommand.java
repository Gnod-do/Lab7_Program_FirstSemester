package server.commands;

import common.data.*;
import common.exceptions.*;
import common.interaction.GroupRaw;
import common.interaction.User;
import server.utility.CollectionManager;
import server.utility.DatabaseCollectionManager;
import server.utility.ResponseOutputer;

import java.time.LocalDateTime;

/**
 * Command 'update'. Updates the information about selected group.
 */

public class UpdateCommand extends AbstractCommand{

    private CollectionManager collectionManager;

    private DatabaseCollectionManager databaseCollectionManager;

    public UpdateCommand(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        super("update", "<ID> {element}", "обновить значение элемента коллекции по ID");
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
            if (stringArgument.isEmpty() || objectArgument == null) throw new WrongAmountOfElementsException();
            if (collectionManager.collectionSize() == 0) throw new CollectionIsEmptyException();

            long id = Long.parseLong(stringArgument);
            if (id <= 0) throw new NumberFormatException();
            StudyGroup oldGroup = collectionManager.getById(id);
            if (oldGroup == null) throw new GroupNotFoundException();
            if (!oldGroup.getOwner().equals(user)) throw new PermissionDeniedException();
            if (!databaseCollectionManager.checkGroupUserId(oldGroup.getId(), user)) throw new ManualDatabaseEditException();
            GroupRaw groupRaw = (GroupRaw) objectArgument;

            databaseCollectionManager.updateGroupById(id, groupRaw);

            String name = groupRaw.getName() == null ? oldGroup.getName() : groupRaw.getName();
            Coordinates coordinates = groupRaw.getCoordinates() == null ? oldGroup.getCoordinates() : groupRaw.getCoordinates();
            LocalDateTime creationDate = oldGroup.getCreationDate();
            Long studentsCount = groupRaw.getStudentsCount() < 0 ? oldGroup.getStudentsCount() : groupRaw.getStudentsCount();
            int transferredStudents = groupRaw.getTransferredStudents() < 0 ? oldGroup.getTransferredStudents() : groupRaw.getTransferredStudents();
            FormOfEducation formOfEducation = groupRaw.getFormOfEducation() == null ? oldGroup.getFormOfEducation() : groupRaw.getFormOfEducation();
            Semester semester = groupRaw.getSemester() == null ? oldGroup.getSemesterEnum() : groupRaw.getSemester();
            Person groupAdmin = groupRaw.getGroupAdmin() == null ? oldGroup.getGroupAdmin() : groupRaw.getGroupAdmin();

            collectionManager.removeFromCollection(oldGroup);
            collectionManager.addToCollection(new StudyGroup(
                    id,
                    name,
                    coordinates,
                    creationDate,
                    studentsCount,
                    transferredStudents,
                    formOfEducation,
                    semester,
                    groupAdmin,
                    user
            ));
            ResponseOutputer.appendln("Группа успешно изменена!");
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Использование: '" + getName() + " " + getUsage() + "'");
        } catch (CollectionIsEmptyException exception) {
            ResponseOutputer.appenderror("Коллекция пуста!");
        } catch (NumberFormatException exception) {
            ResponseOutputer.appenderror("ID должен быть представлен положительным числом!");
        } catch (GroupNotFoundException exception) {
            ResponseOutputer.appenderror("Группа с таким ID в коллекции нет!");
        } catch (ClassCastException exception) {
            ResponseOutputer.appenderror("Переданный клиентом объект неверен!");
        } catch (DatabaseHandlingException exception) {
            ResponseOutputer.appenderror("Произошла ошибка при обращении к базе данных!");
        } catch (PermissionDeniedException exception) {
            ResponseOutputer.appenderror("Недостаточно прав для выполнения данной команды!");
            ResponseOutputer.appendln("Принадлежащие другим пользователям объекты доступны только для чтения.");
        } catch (ManualDatabaseEditException exception) {
            ResponseOutputer.appenderror("Произошло прямое изменение базы данных!");
            ResponseOutputer.appendln("Перезапустите клиент для избежания возможных ошибок.");
        }
        return false;
    }
}
