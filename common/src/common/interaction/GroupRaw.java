package common.interaction;

import common.data.*;

import java.io.Serializable;

public class GroupRaw implements Serializable{

    private String name;

    private Coordinates coordinates;

    private Long studentsCount;

    private int transferredStudents;

    private FormOfEducation formOfEducation;

    private Semester semesterEnum;

    private Person groupAdmin;

    public GroupRaw(String name, Coordinates coordinates, Long studentsCount, int transferredStudents,
                    FormOfEducation formOfEducation, Semester semesterEnum, Person groupAdmin) {
        this.name = name;
        this.coordinates = coordinates;
//        this.creationDate = LocalDateTime.now();
        this.studentsCount = studentsCount;
        this.transferredStudents = transferredStudents;
        this.formOfEducation = formOfEducation;
        this.semesterEnum = semesterEnum;
        this.groupAdmin = groupAdmin;
    }

    /**
     * @return Name of the group.
     */

    public String getName() {return name;}

    /**
     * @return Coordinates of the group.
     */

    public Coordinates getCoordinates() {return coordinates;}

    /**
     * @return the number of student transferred from group.
     */

    public int getTransferredStudents() {return transferredStudents;}

    /**
     * @return the number of student in group.
     */

    public Long getStudentsCount() {return studentsCount;}

    /**
     * @return the form of education.
     */

    public FormOfEducation getFormOfEducation() {return formOfEducation;}

    /**
     * @return the semester.
     */

    public Semester getSemester() {return semesterEnum;}

    /**
     * @return the group admin.
     */

    public Person getGroupAdmin() {return groupAdmin;}

    @Override
    public String toString() {
        String info = "";
        info += "'Сырой' group";
        info += "\n Имя: " + name;
        info += "\n Местоположение: " + coordinates;
        info += "\n количество студентов: " + studentsCount;
        info += "\n количество переводных студентов: " + transferredStudents;
        info += "\n форма образования: " + formOfEducation;
        info += "\n Семестр: " + semesterEnum;
        info += "\n имя лидера: " + groupAdmin;
        return info;
    }

    @Override
    public int hashCode() {
        return name.hashCode() + coordinates.hashCode() + coordinates.hashCode() + studentsCount.hashCode() + (int)transferredStudents
                +formOfEducation.hashCode() + groupAdmin.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof StudyGroup) {
            StudyGroup groupObj = (StudyGroup) obj;
            return name.equals(groupObj.getName()) && coordinates.equals(groupObj.getCoordinates()) &&
                    (studentsCount == groupObj.getStudentsCount()) && (transferredStudents == groupObj.getTransferredStudents()) &&
                    (formOfEducation == groupObj.getFormOfEducation())&&(semesterEnum == groupObj.getSemesterEnum()) && groupAdmin.equals(groupObj.getGroupAdmin());
        }
        return false;
    }

}
