package server.utility;

import common.data.*;
import common.exceptions.DatabaseHandlingException;
import common.interaction.GroupRaw;
import common.interaction.User;
import common.utility.Outputer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;

/**
 * Operates the database collection itself.
 */
public class DatabaseCollectionManager {

    private final String SELECT_ALL_GROUPS = "SELECT * FROM " + DatabaseHandler.GROUP_TABLE;

    private final String SELECT_GROUP_BY_ID = SELECT_ALL_GROUPS + " WHERE " +
            DatabaseHandler.GROUP_TABLE_ID_COLUMN + " = ?";

    private final String SELECT_GROUP_BY_ID_AND_USER_ID = SELECT_GROUP_BY_ID + " AND " +
            DatabaseHandler.GROUP_TABLE_USER_ID_COLUMN + " = ?";

    private final String INSERT_GROUP = "INSERT INTO " +
            DatabaseHandler.GROUP_TABLE + " (" +
            DatabaseHandler.GROUP_TABLE_NAME_COLUMN + ", " +
            DatabaseHandler.GROUP_TABLE_CREATION_DATE_COLUMN + ", " +
            DatabaseHandler.GROUP_TABLE_STUDENTS_COUNT_COLUMN + ", " +
            DatabaseHandler.GROUP_TABLE_TRANSFERRED_STUDENTS_COLUMN + ", " +
            DatabaseHandler.GROUP_TABLE_FORM_OF_EDUCATION_COLUMN + ", " +
            DatabaseHandler.GROUP_TABLE_SEMESTER_COLUMN + ", " +
            DatabaseHandler.GROUP_TABLE_PERSON_ID_COLUMN + ", " +
            DatabaseHandler.GROUP_TABLE_USER_ID_COLUMN + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private final String DELETE_GROUP_BY_ID = "DELETE FROM " + DatabaseHandler.GROUP_TABLE +
            " WHERE " + DatabaseHandler.GROUP_TABLE_ID_COLUMN + " = ?";

    private final String DELETE_COORDINATES_BY_ID = "DELETE FROM " + DatabaseHandler.COORDINATES_TABLE +
            " WHERE " + DatabaseHandler.COORDINATES_TABLE_STUDY_GROUP_ID_COLUMN + " = ?";

    private final String UPDATE_GROUP_NAME_BY_ID = "UPDATE " + DatabaseHandler.GROUP_TABLE + " SET " +
            DatabaseHandler.GROUP_TABLE_NAME_COLUMN + " = ?" + " WHERE " +
            DatabaseHandler.GROUP_TABLE_ID_COLUMN + " = ?";

    private final String UPDATE_GROUP_STUDENTS_COUNT_BY_ID = "UPDATE " + DatabaseHandler.GROUP_TABLE + " SET " +
            DatabaseHandler.GROUP_TABLE_STUDENTS_COUNT_COLUMN + " = ?" + " WHERE " +
            DatabaseHandler.GROUP_TABLE_ID_COLUMN + " = ?";

    private final String UPDATE_GROUP_TRANSFERRED_STUDENTS_BY_ID = "UPDATE " + DatabaseHandler.GROUP_TABLE + " SET " +
            DatabaseHandler.GROUP_TABLE_TRANSFERRED_STUDENTS_COLUMN + " = ?" + " WHERE " +
            DatabaseHandler.GROUP_TABLE_ID_COLUMN + " = ?";

    private final String UPDATE_GROUP_FORM_OF_EDUCATION_BY_ID = "UPDATE " + DatabaseHandler.GROUP_TABLE + " SET " +
            DatabaseHandler.GROUP_TABLE_FORM_OF_EDUCATION_COLUMN + " = ?" + " WHERE " +
            DatabaseHandler.GROUP_TABLE_ID_COLUMN + " = ?";

    private final String UPDATE_GROUP_SEMESTER_BY_ID = "UPDATE " + DatabaseHandler.GROUP_TABLE + " SET " +
            DatabaseHandler.GROUP_TABLE_SEMESTER_COLUMN + " = ?" + " WHERE " +
            DatabaseHandler.GROUP_TABLE_ID_COLUMN + " = ?";

    // COORDINATES_TABLE

    private final String SELECT_ALL_COORDINATES = "SELECT * FROM " + DatabaseHandler.COORDINATES_TABLE;

    private final String SELECT_COORDINATES_BY_ID = SELECT_ALL_COORDINATES +
            " WHERE " + DatabaseHandler.COORDINATES_TABLE_STUDY_GROUP_ID_COLUMN + " = ?";

    private final String INSERT_COORDINATES = "INSERT INTO " +
            DatabaseHandler.COORDINATES_TABLE + " (" +
            DatabaseHandler.COORDINATES_TABLE_STUDY_GROUP_ID_COLUMN + ", " +
            DatabaseHandler.COORDINATES_TABLE_X_COLUMN + ", " +
            DatabaseHandler.COORDINATES_TABLE_Y_COLUMN + ") VALUES (?, ?, ?)";

    private final String UPDATE_COORDINATES_BY_GROUP_ID = "UPDATE " + DatabaseHandler.COORDINATES_TABLE + " SET " +
            DatabaseHandler.COORDINATES_TABLE_X_COLUMN + " = ?, " +
            DatabaseHandler.COORDINATES_TABLE_Y_COLUMN + " = ?" + " WHERE " +
            DatabaseHandler.COORDINATES_TABLE_STUDY_GROUP_ID_COLUMN + " = ?";

    // ADMIN GROUP

    private final String SELECT_ALL_PERSON = "SELECT * FROM " + DatabaseHandler.PERSON_TABLE;

    private final String SELECT_PERSON_BY_ID = SELECT_ALL_PERSON +
            " WHERE " + DatabaseHandler.PERSON_TABLE_ID_COLUMN + " = ?";

    private final String INSERT_PERSON = "INSERT INTO " +
            DatabaseHandler.PERSON_TABLE + " (" +
            DatabaseHandler.PERSON_TABLE_NAME_COLUMN + ", " +
            DatabaseHandler.PERSON_TABLE_BIRTHDAY_COLUMN + ", " +
            DatabaseHandler.PERSON_TABLE_HEIGHT_COLUMN + ", " +
            DatabaseHandler.PERSON_TABLE_WEIGHT_COLUMN + ", " +
            DatabaseHandler.PERSON_TABLE_PASSPORT_ID_COLUMN + ") VALUES (?, ?, ?, ?, ?)" ;

    private final String UPDATE_PERSON_BY_ID = "UPDATE " + DatabaseHandler.PERSON_TABLE + " SET " +
            DatabaseHandler.PERSON_TABLE_NAME_COLUMN + " = ?, " +
            DatabaseHandler.PERSON_TABLE_BIRTHDAY_COLUMN + " = ?, " +
            DatabaseHandler.PERSON_TABLE_HEIGHT_COLUMN + " = ?, " +
            DatabaseHandler.PERSON_TABLE_WEIGHT_COLUMN + " = ?, " +
            DatabaseHandler.PERSON_TABLE_PASSPORT_ID_COLUMN  + " = ?" + " WHERE " +
            DatabaseHandler.PERSON_TABLE_ID_COLUMN + " = ?";

    private final String DELETE_PERSON_BY_ID = "DELETE FROM " + DatabaseHandler.PERSON_TABLE + " CASCADE " +
            " WHERE " + DatabaseHandler.PERSON_TABLE_ID_COLUMN + " = ?";

//    private final String DELETE_GROUP_BY_ID = "DELETE FROM " + DatabaseHandler.GROUP_TABLE + " CASCADE" +
//            " WHERE " + DatabaseHandler.GROUP_TABLE_ID_COLUMN + " = ?";

    private DatabaseHandler databaseHandler;

    private DatabaseUserManager databaseUserManager;

    public DatabaseCollectionManager(DatabaseHandler databaseHandler, DatabaseUserManager databaseUserManager) {
        this.databaseHandler = databaseHandler;
        this.databaseUserManager = databaseUserManager;
    }

    /**
     * Create Group.
     *
     * @param resultSet Result set parameters of Marine.
     * @return New Marine.
     * @throws SQLException When there's exception inside.
     */

    private StudyGroup createGroup(ResultSet resultSet) throws SQLException {
        long id = resultSet.getLong(DatabaseHandler.GROUP_TABLE_ID_COLUMN);
        String name = resultSet.getString(DatabaseHandler.GROUP_TABLE_NAME_COLUMN);
        LocalDateTime creationDate = resultSet.getTimestamp(DatabaseHandler.GROUP_TABLE_CREATION_DATE_COLUMN).toLocalDateTime();
        Long studentsCount = resultSet.getLong(DatabaseHandler.GROUP_TABLE_STUDENTS_COUNT_COLUMN);
        int transferredStudents = resultSet.getInt(DatabaseHandler.GROUP_TABLE_TRANSFERRED_STUDENTS_COLUMN);
        FormOfEducation formOfEducation = FormOfEducation.valueOf(resultSet.getString(DatabaseHandler.GROUP_TABLE_FORM_OF_EDUCATION_COLUMN));
        Semester semester = Semester.valueOf(resultSet.getString(DatabaseHandler.GROUP_TABLE_SEMESTER_COLUMN));
        Coordinates coordinates = getCoordinatesByGroupId(id);
        Person groupAdmin = getPersonById(resultSet.getLong(DatabaseHandler.GROUP_TABLE_PERSON_ID_COLUMN));
        User owner = databaseUserManager.getUserById((resultSet.getLong(DatabaseHandler.GROUP_TABLE_USER_ID_COLUMN)));
        return new StudyGroup(
                id,
                name,
                coordinates,
                creationDate,
                studentsCount,
                transferredStudents,
                formOfEducation,
                semester,
                groupAdmin,
                owner
        );
    }

    /**
     * @return List of Groups.
     * @throws DatabaseHandlingException When there's exception inside.
     */

    public LinkedList<StudyGroup> getCollection() throws DatabaseHandlingException {
        LinkedList<StudyGroup> groupList = new LinkedList<>();
        PreparedStatement preparedSelectAllStatement = null;
        try {
            preparedSelectAllStatement = databaseHandler.getPreparedStatement(SELECT_ALL_GROUPS, false);
            ResultSet resultSet = preparedSelectAllStatement.executeQuery();
            while (resultSet.next()) {
                groupList.add(createGroup(resultSet));
            }
        } catch (SQLException exception) {
            Outputer.printerror(exception.getMessage());
            throw new DatabaseHandlingException();
        } finally {
            databaseHandler.closePreparedStatement(preparedSelectAllStatement);
        }
        return groupList;
    }

    /**
     * @param groupId Id of Marine.
     * @return Person id.
     * @throws SQLException When there's exception inside.
     */

    private long getPersonIdByGroupId(long groupId) throws SQLException {
        long personId;
        PreparedStatement preparedSelectGroupByIdStatement = null;
        try {
            preparedSelectGroupByIdStatement = databaseHandler.getPreparedStatement(SELECT_GROUP_BY_ID, false);
            preparedSelectGroupByIdStatement.setLong(1, groupId);
            ResultSet resultSet = preparedSelectGroupByIdStatement.executeQuery();
            Outputer.println("Выполнен запрос SELECT_GROUP_BY_ID.");
            if (resultSet.next()) {
                personId = resultSet.getLong(DatabaseHandler.GROUP_TABLE_PERSON_ID_COLUMN);
            } else throw new SQLException();
        } catch (SQLException exception) {
            Outputer.printerror("An Error occurred while getting the person's id");
            throw new SQLException(exception);
        } finally {
            databaseHandler.closePreparedStatement(preparedSelectGroupByIdStatement);
        }
        return personId;
    }


    /**
     * @param groupId Id of Group.
     * @return coordinates.
     * @throws SQLException When there's exception inside.
     */

    private Coordinates getCoordinatesByGroupId(long groupId) throws SQLException {
        Coordinates coordinates;
        PreparedStatement preparedSelectCoordinatesByGroupIdStatement = null;
        try {
            preparedSelectCoordinatesByGroupIdStatement =
                    databaseHandler.getPreparedStatement(SELECT_COORDINATES_BY_ID, false);
            preparedSelectCoordinatesByGroupIdStatement.setLong(1, groupId);
            ResultSet resultSet = preparedSelectCoordinatesByGroupIdStatement.executeQuery();
            Outputer.println("Выполнен запрос SELECT_COORDINATES_BY_GROUP_ID.");
            if (resultSet.next()) {
                coordinates = new Coordinates(
                        resultSet.getInt(DatabaseHandler.COORDINATES_TABLE_X_COLUMN),
                        resultSet.getLong(DatabaseHandler.COORDINATES_TABLE_Y_COLUMN)
                );
            } else throw new SQLException();
        } catch (SQLException exception) {
            Outputer.printerror("Произошла ошибка при выполнении запроса SELECT_COORDINATES_BY_MARINE_ID!");
            throw new SQLException(exception);
        } finally {
            databaseHandler.closePreparedStatement(preparedSelectCoordinatesByGroupIdStatement);
        }
        return coordinates;
    }


    /**
     * @param personId Id of Admin Group.
     * @return Person.
     * @throws SQLException When there's exception inside.
     */

    private Person getPersonById(long personId) throws SQLException {
        Person person;
        PreparedStatement preparedSelectPersonByIdStatement = null;
        try {
            preparedSelectPersonByIdStatement =
                    databaseHandler.getPreparedStatement(SELECT_PERSON_BY_ID, false);
            preparedSelectPersonByIdStatement.setLong(1, personId);
            ResultSet resultSet = preparedSelectPersonByIdStatement.executeQuery();
            Outputer.println("Выполнен запрос SELECT_PERSON_BY_ID.");
            if (resultSet.next()) {
                person = new Person(
                        resultSet.getString(DatabaseHandler.PERSON_TABLE_NAME_COLUMN),
                        resultSet.getTimestamp(DatabaseHandler.PERSON_TABLE_BIRTHDAY_COLUMN).toLocalDateTime().toLocalDate(),
                        resultSet.getLong(DatabaseHandler.PERSON_TABLE_HEIGHT_COLUMN),
                        resultSet.getDouble(DatabaseHandler.PERSON_TABLE_WEIGHT_COLUMN),
                        resultSet.getString(DatabaseHandler.PERSON_TABLE_PASSPORT_ID_COLUMN)
                );
            } else throw new SQLException();
        } catch (SQLException exception) {
            Outputer.printerror(exception.getMessage());
            Outputer.printerror("Произошла ошибка при выполнении запроса SELECT_PERSON_BY_ID!");
            throw new SQLException(exception);
        } finally {
            databaseHandler.closePreparedStatement(preparedSelectPersonByIdStatement);
        }
        return person;
    }

    /**
     * @param groupRaw Group raw.
     * @param user      User.
     * @return Group.
     * @throws DatabaseHandlingException When there's exception inside.
     */
    public StudyGroup insertGroup(GroupRaw groupRaw, User user) throws DatabaseHandlingException {
        StudyGroup group;
        PreparedStatement preparedInsertGroupStatement = null;
        PreparedStatement preparedInsertCoordinatesStatement = null;
        PreparedStatement preparedInsertPersonStatement = null;
        try {
            databaseHandler.setCommitMode();
            databaseHandler.setSavePoint();

            LocalDateTime creationTime = LocalDateTime.now();

            preparedInsertGroupStatement = databaseHandler.getPreparedStatement(INSERT_GROUP, true);
            preparedInsertCoordinatesStatement = databaseHandler.getPreparedStatement(INSERT_COORDINATES, true);
            preparedInsertPersonStatement = databaseHandler.getPreparedStatement(INSERT_PERSON, true);

            preparedInsertPersonStatement.setString(1, groupRaw.getGroupAdmin().getName());

            LocalDate birthday = groupRaw.getGroupAdmin().getBirthday();
            Timestamp timeStamp = Timestamp.valueOf(birthday.atStartOfDay());

            preparedInsertPersonStatement.setTimestamp(2, timeStamp);
            preparedInsertPersonStatement.setLong(3, groupRaw.getGroupAdmin().getHeight());
            preparedInsertPersonStatement.setDouble(4, groupRaw.getGroupAdmin().getWeight());
            preparedInsertPersonStatement.setString(5, groupRaw.getGroupAdmin().getPassportID());
            if (preparedInsertPersonStatement.executeUpdate() == 0) throw new SQLException();
            ResultSet generatedChapterKeys = preparedInsertPersonStatement.getGeneratedKeys();
            long personId;
            if (generatedChapterKeys.next()) {
                personId = generatedChapterKeys.getLong(1);
            } else throw new SQLException();
            Outputer.println("Выполнен запрос INSERT_PERSON.");

            preparedInsertGroupStatement.setString(1, groupRaw.getName());
            preparedInsertGroupStatement.setTimestamp(2, Timestamp.valueOf(creationTime));
            preparedInsertGroupStatement.setLong(3, groupRaw.getStudentsCount());
            preparedInsertGroupStatement.setInt(4, groupRaw.getTransferredStudents());
            preparedInsertGroupStatement.setString(5, groupRaw.getFormOfEducation().toString());
            preparedInsertGroupStatement.setString(6,groupRaw.getSemester().toString());
            preparedInsertGroupStatement.setLong(7,personId);
            preparedInsertGroupStatement.setLong(8,databaseUserManager.getUserIdByUsername(user));
            if (preparedInsertGroupStatement.executeUpdate() == 0) throw new SQLException();
            ResultSet generatedGroupKeys = preparedInsertGroupStatement.getGeneratedKeys();
            long studyGroupId;
            if (generatedGroupKeys.next()) {
                studyGroupId = generatedGroupKeys.getLong(1);
            } else throw new SQLException();
            Outputer.println("Выполнен запрос INSERT_GROUP.");

            preparedInsertCoordinatesStatement.setLong(1, studyGroupId);
            preparedInsertCoordinatesStatement.setInt(2,groupRaw.getCoordinates().getX());
            preparedInsertCoordinatesStatement.setLong(3, groupRaw.getCoordinates().getY());
            if (preparedInsertCoordinatesStatement.executeUpdate() == 0) throw new SQLException();
            Outputer.println("Выполнен запрос INSERT_COORDINATES.");

            group = new StudyGroup(
                    studyGroupId,
                    groupRaw.getName(),
                    groupRaw.getCoordinates(),
                    creationTime,
                    groupRaw.getStudentsCount(),
                    groupRaw.getTransferredStudents(),
                    groupRaw.getFormOfEducation(),
                    groupRaw.getSemester(),
                    groupRaw.getGroupAdmin(),
                    user
            );

            databaseHandler.commit();
            return group;
        } catch (SQLException exception) {
            Outputer.printerror(exception.getMessage());
            Outputer.printerror("Произошла ошибка при выполнении группы запросов на добавление нового объекта!");
            databaseHandler.rollback();
            throw new DatabaseHandlingException();
        } finally {
            databaseHandler.closePreparedStatement(preparedInsertGroupStatement);
            databaseHandler.closePreparedStatement(preparedInsertCoordinatesStatement);
            databaseHandler.closePreparedStatement(preparedInsertPersonStatement);
            databaseHandler.setNormalMode();
        }
    }

    /**
     * @param groupRaw Marine raw.
     * @param groupId  Id of Marine.
     * @throws DatabaseHandlingException When there's exception inside.
     */

    public void updateGroupById(long groupId, GroupRaw groupRaw) throws DatabaseHandlingException {
        PreparedStatement preparedUpdateGroupNameByIdStatement = null;
        PreparedStatement preparedUpdateGroupStudentsCountByIdStatement = null;
        PreparedStatement preparedUpdateGroupTransferredStudentsByIdStatement = null;
        PreparedStatement preparedUpdateGroupFormOfEducationByIdStatement = null;
        PreparedStatement preparedUpdateGroupSemesterByIdStatement = null;
        PreparedStatement preparedUpdateGroupCoordinatesByIdStatement = null;
        PreparedStatement preparedUpdateGroupPersonByIdStatement = null;
        try {
            databaseHandler.setCommitMode();
            databaseHandler.setSavePoint();

            preparedUpdateGroupNameByIdStatement = databaseHandler.getPreparedStatement(UPDATE_GROUP_NAME_BY_ID, false);
            preparedUpdateGroupStudentsCountByIdStatement = databaseHandler.getPreparedStatement(UPDATE_GROUP_STUDENTS_COUNT_BY_ID, false);
            preparedUpdateGroupTransferredStudentsByIdStatement = databaseHandler.getPreparedStatement(UPDATE_GROUP_TRANSFERRED_STUDENTS_BY_ID, false);
            preparedUpdateGroupFormOfEducationByIdStatement = databaseHandler.getPreparedStatement(UPDATE_GROUP_FORM_OF_EDUCATION_BY_ID, false);
            preparedUpdateGroupSemesterByIdStatement = databaseHandler.getPreparedStatement(UPDATE_GROUP_SEMESTER_BY_ID, false);
            preparedUpdateGroupCoordinatesByIdStatement = databaseHandler.getPreparedStatement(UPDATE_COORDINATES_BY_GROUP_ID, false);
            preparedUpdateGroupPersonByIdStatement = databaseHandler.getPreparedStatement(UPDATE_PERSON_BY_ID, false);

            if (groupRaw.getName() != null) {
                preparedUpdateGroupNameByIdStatement.setString(1, groupRaw.getName());
                preparedUpdateGroupNameByIdStatement.setLong(2, groupId);
                if (preparedUpdateGroupNameByIdStatement.executeUpdate() == 0) throw new SQLException();
                Outputer.println("Выполнен запрос UPDATE_GROUP_NAME_BY_ID.");
            }
            if (groupRaw.getCoordinates() != null) {
                preparedUpdateGroupCoordinatesByIdStatement.setInt(1, groupRaw.getCoordinates().getX());
                preparedUpdateGroupCoordinatesByIdStatement.setLong(2,groupRaw.getCoordinates().getY());
                preparedUpdateGroupCoordinatesByIdStatement.setLong(3, groupId);
                if (preparedUpdateGroupCoordinatesByIdStatement.executeUpdate() == 0) throw new SQLException();
                Outputer.println("Выполнен запрос UPDATE_COORDINATES_BY_MARINE_ID.");
            }
            if (groupRaw.getStudentsCount() != -1) {
                preparedUpdateGroupStudentsCountByIdStatement.setLong(1, groupRaw.getStudentsCount());
                preparedUpdateGroupStudentsCountByIdStatement.setLong(2,groupId);
                if (preparedUpdateGroupStudentsCountByIdStatement.executeUpdate() == 0) throw new SQLException();
                Outputer.println("Выполнен запрос UPDATE_GROUP_STUDENTS_COUNT_BY_ID.");
            }
            if (groupRaw.getTransferredStudents() != -1) {
                preparedUpdateGroupTransferredStudentsByIdStatement.setInt(1, groupRaw.getTransferredStudents());
                preparedUpdateGroupTransferredStudentsByIdStatement.setLong(2, groupId);
                if (preparedUpdateGroupTransferredStudentsByIdStatement.executeUpdate() == 0) throw new SQLException();
                Outputer.println("Выполнен запрос UPDATE_GROUP_TRANSFERRED_STUDENTS_BY_ID.");
            }
            if (groupRaw.getFormOfEducation() != null) {
                preparedUpdateGroupFormOfEducationByIdStatement.setString(1, groupRaw.getFormOfEducation().toString());
                preparedUpdateGroupFormOfEducationByIdStatement.setLong(2, groupId);
                if (preparedUpdateGroupFormOfEducationByIdStatement.executeUpdate() == 0) throw new SQLException();
                Outputer.println("Выполнен запрос UPDATE_GROUP_FORM_OF_EDUCATION_BY_ID.");
            }
            if (groupRaw.getSemester() != null) {
                preparedUpdateGroupSemesterByIdStatement.setString(1, groupRaw.getSemester().toString());
                preparedUpdateGroupSemesterByIdStatement.setLong(2, groupId);
                if (preparedUpdateGroupSemesterByIdStatement.executeUpdate() == 0) throw new SQLException();
                Outputer.println("Выполнен запрос UPDATE_GROUP_SEMESTER_BY_ID.");
            }
            if (groupRaw.getGroupAdmin() != null) {
                Outputer.println(groupRaw.getGroupAdmin());
                preparedUpdateGroupPersonByIdStatement.setString(1, groupRaw.getGroupAdmin().getName());
                LocalDate birthday = groupRaw.getGroupAdmin().getBirthday();
                Timestamp timeStamp = Timestamp.valueOf(birthday.atStartOfDay());
                Outputer.println("Day la thoi gian da duoc convert: "+timeStamp);
                preparedUpdateGroupPersonByIdStatement.setTimestamp(2, timeStamp);
                preparedUpdateGroupPersonByIdStatement.setInt(3, (groupRaw.getGroupAdmin().getHeight()).intValue());
                preparedUpdateGroupPersonByIdStatement.setDouble(4, groupRaw.getGroupAdmin().getWeight());
                preparedUpdateGroupPersonByIdStatement.setString(5, groupRaw.getGroupAdmin().getPassportID());
                preparedUpdateGroupPersonByIdStatement.setLong(6, getPersonIdByGroupId(groupId));
                if (preparedUpdateGroupPersonByIdStatement.executeUpdate() == 0) {
                    Outputer.println("NO chua hoan thanh o buoc nay");
                    throw new SQLException();
                }
                Outputer.println("Выполнен запрос UPDATE_GROUP_PERSON_BY_ID.");
            }
            databaseHandler.commit();
        } catch (SQLException exception) {
            Outputer.printerror(exception.getMessage());
            Outputer.printerror("Произошла ошибка при выполнении группы запросов на обновление объекта!");
            databaseHandler.rollback();
            throw new DatabaseHandlingException();
        } finally {
            databaseHandler.closePreparedStatement(preparedUpdateGroupNameByIdStatement);
            databaseHandler.closePreparedStatement(preparedUpdateGroupStudentsCountByIdStatement);
            databaseHandler.closePreparedStatement(preparedUpdateGroupTransferredStudentsByIdStatement);
            databaseHandler.closePreparedStatement(preparedUpdateGroupFormOfEducationByIdStatement);
            databaseHandler.closePreparedStatement(preparedUpdateGroupSemesterByIdStatement);
            databaseHandler.closePreparedStatement(preparedUpdateGroupCoordinatesByIdStatement);
            databaseHandler.closePreparedStatement(preparedUpdateGroupPersonByIdStatement);
            databaseHandler.setNormalMode();
        }
    }

    /**
     * Delete Group by id.
     *
     * @param groupId Id of Marine.
     * @throws DatabaseHandlingException When there's exception inside.
     */

    public void deleteGroupById(long groupId) throws DatabaseHandlingException {
        PreparedStatement preparedDeletePersonByIdStatement = null;
//        PreparedStatement preparedDeleteGroupByIdStatement = null;
//        PreparedStatement preparedDeleteCoordinatesByIdStatement = null;
        try {
            //Xóa các bản ghi trong "coordinates" liên quan đến "study_group_id"

//            preparedDeleteCoordinatesByIdStatement = databaseHandler.getPreparedStatement(DELETE_COORDINATES_BY_ID, false);
//            preparedDeleteCoordinatesByIdStatement.setLong(1, groupId);
//            preparedDeleteGroupByIdStatement.executeUpdate();
//
//            //Xóa các bản ghi trong "study_group" liên quan đến "person" có groupId tương ứng
//            preparedDeleteGroupByIdStatement = databaseHandler.getPreparedStatement(DELETE_GROUP_BY_ID, false);
//            preparedDeleteGroupByIdStatement.setLong(1, groupId);
//            preparedDeleteGroupByIdStatement.executeUpdate();


            //Xóa nội dung bản ghi "person" theo groupId
            preparedDeletePersonByIdStatement = databaseHandler.getPreparedStatement(DELETE_PERSON_BY_ID, false);
            preparedDeletePersonByIdStatement.setLong(1, getPersonIdByGroupId(groupId));
            if (preparedDeletePersonByIdStatement.executeUpdate() == 0 ) Outputer.println(3);
            Outputer.println("Выполнен запрос DELETE_PERSON_BY_ID.");
        } catch (SQLException exception) {
            Outputer.printerror(exception.getMessage());
            Outputer.println("Произошла ошибка при выполнении запроса DELETE_PERSON_BY_ID!");
            throw new DatabaseHandlingException();
        } finally {
            databaseHandler.closePreparedStatement(preparedDeletePersonByIdStatement);
//            databaseHandler.closePreparedStatement(preparedDeleteCoordinatesByIdStatement);
//            databaseHandler.closePreparedStatement(preparedDeleteGroupByIdStatement);
        }
    }

    /**
     * Checks Group user id.
     *
     * @param groupId Id of Group.
     * @param user Owner of group.
     * @throws DatabaseHandlingException When there's exception inside.
     * @return Is everything ok.
     */

    public boolean checkGroupUserId(long groupId, User user) throws DatabaseHandlingException {
        PreparedStatement preparedSelectGroupByIdAndUserIdStatement = null;
        try {
            preparedSelectGroupByIdAndUserIdStatement = databaseHandler.getPreparedStatement(SELECT_GROUP_BY_ID_AND_USER_ID, false);
            preparedSelectGroupByIdAndUserIdStatement.setLong(1, groupId);
            preparedSelectGroupByIdAndUserIdStatement.setLong(2, databaseUserManager.getUserIdByUsername(user));
            ResultSet resultSet = preparedSelectGroupByIdAndUserIdStatement.executeQuery();
            Outputer.println("Выполнен запрос SELECT_GROUP_BY_ID_AND_USER_ID.");
            return resultSet.next();
        } catch (SQLException exception) {
            Outputer.printerror(exception.getMessage());
            Outputer.printerror("Произошла ошибка при выполнении запроса SELECT_GROUP_BY_ID_AND_USER_ID!");
            throw new DatabaseHandlingException();
        } finally {
            databaseHandler.closePreparedStatement(preparedSelectGroupByIdAndUserIdStatement);
        }
    }

    /**
     * Clear the collection.
     *
     * @throws DatabaseHandlingException When there's exception inside.
     */

    public void clearCollection() throws DatabaseHandlingException {
        LinkedList<StudyGroup> groupList = getCollection();
        for (StudyGroup group : groupList) {
            deleteGroupById(group.getId());
        }
    }

}
