package common.data;

import common.interaction.User;

import java.time.LocalDateTime;

public class StudyGroup implements Comparable<StudyGroup>{
    public static final Integer MAX_X = 989;
    public static final int MAX_LENGTH = 23;
    public static final int MIN_LENGTH = 5;

    private static final long MIN_HEIGHT = 0;

    private static final double MIN_WEIGHT = 0;

    private static final long MIN_GROUP = 1;

    private static final long MAX_GROUP = 1000;

    private static final long MIN_STUDENTSCOUNT = 0;




    private Long id;

    private String name;

    private Coordinates coordinates;

    private java.time.LocalDateTime creationDate;

    private Long studentsCount;

    private int transferredStudents;

    private FormOfEducation formOfEducation;

    private Semester semesterEnum;

    private Person groupAdmin;

    private User owner;

    public StudyGroup(Long id, String name, Coordinates coordinates,LocalDateTime creationDate, Long studentsCount,int transferredStudents, FormOfEducation formOfEducation, Semester semesterEnum, Person groupAdmin, User owner) {
        this.id = id;
        this.name = name;
        this.creationDate = creationDate;
        this.coordinates = coordinates;
        this.creationDate = LocalDateTime.now();
        this.studentsCount = studentsCount;
        this.transferredStudents = transferredStudents;
        this.formOfEducation = formOfEducation;
        this.semesterEnum = semesterEnum;
        this.groupAdmin = groupAdmin;
        this.owner = owner;
    }

    /**
     * @return ID of studyGroup
     */

    public Long getId() {return id;}

    public void setId(Long id){
        this.id = id;
    }

    /**
     * @return name of studyGroup.
     */

    public String getName() {return name;}

    /**
     * @return coordinates of studyGroup.
     */

    public Coordinates getCoordinates() {return coordinates;}

    public String getCoordinatesString() {return coordinates.getX() + "," + coordinates.getY();}



    /**
     * @return number students of studyGroup.
     */

    public Long getStudentsCount() {return studentsCount;}

    /**
     * @return the number of transfer students.
     */

    public int getTransferredStudents() {return transferredStudents;}


    /**
     * @return Form of education of studyGroup.
     */

    public FormOfEducation getFormOfEducation() {return formOfEducation;}

    /**
     * @return Semester.
     */


    public Semester getSemesterEnum(){return semesterEnum;}

    /**
     * @return Creation date of the group.
     */
    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    /**
     * @return Name of group admin.
     */

    public Person getGroupAdmin() {return groupAdmin;}

    public User getOwner() {
        return owner;
    }


    public int compareTo(StudyGroup groupObj) {
        return studentsCount.compareTo(groupObj.getId());
    }

    /**
     * @return
     */
    @Override
    public String toString() {
        String info = "";
        info += "\nгруппа № " + id;
        info += "\n(добавлена " + creationDate.toLocalDate() + " " + creationDate.toLocalTime() + ")";
        info += "\n Имя: " + name;
        info += "\n Местоположение: " + coordinates;
        info += "\n количество студентов: " + studentsCount;
        info += "\n количество переводных студентов: " + transferredStudents;
        info += "\n форма образования: " + formOfEducation;
        info += "\n имя лидера: " + groupAdmin;
        return info;
    }

    @Override
    public int hashCode() {
        return name.hashCode() + coordinates.hashCode() + id.hashCode() + creationDate.hashCode() + coordinates.hashCode() + studentsCount.hashCode() + (int)transferredStudents
                +formOfEducation.hashCode() + groupAdmin.hashCode();
    }


}
