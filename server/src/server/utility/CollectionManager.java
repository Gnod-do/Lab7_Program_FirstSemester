package server.utility;


import common.data.Semester;
import common.data.StudyGroup;
import common.exceptions.CollectionIsEmptyException;
import common.exceptions.DatabaseHandlingException;
import common.utility.Outputer;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Operates the collection itself.
 */
public class CollectionManager {

    private LinkedList<StudyGroup> groupCollection;

    private LocalDateTime lastInitTime;

    private DatabaseCollectionManager databaseCollectionManager;

    public CollectionManager(DatabaseCollectionManager databaseCollectionManager) {
        this.databaseCollectionManager = databaseCollectionManager;

        loadCollection();
    }

    /**
     * @return Last initialization time or null if there wasn't initialization.
     */
    public LocalDateTime getLastInitTime() {
        return lastInitTime;
    }

    /**
     * @return Name of the collection's type.
     */
    public String collectionType() {
        return groupCollection.getClass().getName();
    }

    /**
     * @return Size of the collection.
     */
    public int collectionSize() {
        return groupCollection.size();
    }

    /**
     * @return The first element of the collection or null if collection is empty.
     */

    public StudyGroup getFirst() {return groupCollection.stream().findFirst().orElse(null);}

    /**
     * @return The last element of the collection or null if collection is empty.
     */

    public StudyGroup getLast() {
        if (groupCollection.isEmpty()) return null;
        return groupCollection.getLast();
    }


    public LinkedList<StudyGroup> getCollection() {
        return groupCollection;
    }

    /**
     * @param id ID of the group.
     * @return A group by his ID or null if group isn't found.
     */

    public StudyGroup getById(Long id) {
        return groupCollection.stream().filter(group -> group.getId().equals(id)).findFirst().orElse(null);
    }

    /**
     * @return A group by his index or null if group isn't found.
     */

    public StudyGroup getByIndex(int index) {
        if (index == 0) {
            return groupCollection.stream().findFirst().orElse(null);
        }
        return groupCollection.stream().skip(index).findFirst().orElse(null);
    }

    public int getIdByIndex(int index) {
        StudyGroup group = getByIndex(index);
        if (group != null) {
            return group.getId().intValue();
        }
        return -1;
    }



    /**
     * @param groupToFind A group who's value will be found.
     * @return A group by his value or null if group isn't found.
     */

    public StudyGroup getByValue(StudyGroup groupToFind) {
        return groupCollection.stream().filter(group -> group.equals(groupToFind)).findFirst().orElse(null);
    }

    /**
     * @return Sum of all transferred students or 0 if collection is empty.
     */

    public int sumOfTransferredStudents() {
        return groupCollection.stream()
                .reduce(0, (sum, p) -> sum + p.getTransferredStudents(), Integer::sum);
    }

    /**
     * @return Group, whose semesterEnum field value is the minimum.
     * @throws CollectionIsEmptyException If collection is empty.
     */

    public String minBySemester() throws CollectionIsEmptyException {
        if (groupCollection.isEmpty()) throw new CollectionIsEmptyException();

        Semester minSemester = groupCollection.stream().map(group -> group.getSemesterEnum())
                .min(Enum::compareTo).get();
        return groupCollection.stream().filter(group -> group.getSemesterEnum().equals(minSemester)).findFirst().get().toString();
    }

    /**
     *
     * @return a sorted collection.
     */

    public void sortByNameAscending() {
        Collections.sort(groupCollection, new Comparator<StudyGroup>() {
            @Override
            public int compare(StudyGroup group1, StudyGroup group2) {
                return group1.getName().compareTo(group2.getName());
            }
        });
    }

    public void addToCollection(StudyGroup group) {
//        group.setId(IDprovider.getInstance().getID());
        groupCollection.add(group);
    }

    /**
     * Removes a group from collection.
     * @param group A group to remove.
     */

    public void removeFromCollection(StudyGroup group) {
        groupCollection.remove(group);
    }

    /**
     * Remove group given the index.
     */
    public void removeByIndex(int index) {
        groupCollection.remove(index);
    }

    /**
     * Group of groups with the same coordinates.
     */

    public Map<String, Long> groupCountingByCoordinates() {
        return groupCollection.stream()
                .collect(Collectors.groupingBy(StudyGroup::getCoordinatesString, Collectors.counting()));
    }

    /**
     * Clears the collection.
     */

    public void clearCollection() {
        groupCollection.clear();
        loadCollection();
    }


    /**
     * @return Collection content or corresponding string if collection is empty.
     */

    public String showCollection() {
        if (groupCollection.isEmpty()) return "Коллекция пуста!";
        return groupCollection.stream().reduce("", (sum, m) -> sum += m + "\n\n", (sum1, sum2) -> sum1 + sum2).trim();
    }

    /**
     * @return Sum of all transferred students or 0 if collection is empty.
     */
    public int getSumOfTransferredStudents() {
        return groupCollection.stream()
                .reduce(0, (sum, p) -> sum += p.getTransferredStudents(), Integer::sum);
    }

    private void loadCollection() {
        try {
            groupCollection = databaseCollectionManager.getCollection();
            lastInitTime = LocalDateTime.now();
            Outputer.println("Коллекция загружена.");
        } catch (DatabaseHandlingException exception) {
            groupCollection = new LinkedList<>();
            Outputer.printerror(exception.getMessage());
            Outputer.printerror("Коллекция не может быть загружена!");
        }
    }

}
