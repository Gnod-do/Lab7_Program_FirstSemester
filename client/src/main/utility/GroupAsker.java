package main.utility;

import common.data.*;
import common.exceptions.IncorrectInputInScriptException;
import common.exceptions.MustBeNotEmptyException;
import common.exceptions.NotInDeclaredLimitsException;
import main.App;
import common.utility.Outputer;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class GroupAsker {
    private Scanner userScanner;

    private boolean fileMode;

    public GroupAsker(Scanner userScanner) {
        this.userScanner = userScanner;
        fileMode = false;
    }

    /**
     * Sets group asker mode to 'File Mode'.
     */
    public void setFileMode() {
        fileMode = true;
    }


    /**
     * Sets group asker mode to 'User Mode'.
     */
    public void setUserMode() {
        fileMode = false;
    }

    /**
     * Asks a user the group's name.
     *
     * @return Group's name.
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */

    public String askName() throws IncorrectInputInScriptException {
        String name;
        while (true) {
            try {
                Outputer.println("Введите имя:");
                Outputer.print(App.PS2);
                name = userScanner.nextLine().trim();
                if (fileMode) Outputer.println(name);
                if(name.equals("")) throw new MustBeNotEmptyException();
                break;
            } catch (NoSuchElementException exception) {
                Outputer.printerror("Имя не распознано!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (MustBeNotEmptyException exception) {
                Outputer.printerror("Имя не может быть пустым!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (IllegalStateException exception) {
                Outputer.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return name;
    }

    /**
     * Asks a user the group's X coordinate.
     * @return Group's X coordinate.
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */

    public Integer askX() throws IncorrectInputInScriptException {
        String strX;
        Integer x;
        while (true) {
            try {
                Outputer.println("Введите координату X:");
                Outputer.print(App.PS2);
                strX = userScanner.nextLine().trim();
                if (fileMode) Outputer.println(strX);
                x = Integer.parseInt(strX);
                if (x >= StudyGroup.MAX_X) throw new NotInDeclaredLimitsException();
                break;
            } catch (NoSuchElementException exception) {
                Outputer.printerror("Координата X не распознана!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NotInDeclaredLimitsException exception) {
                Outputer.printerror("Координата X не может превышать " + StudyGroup.MAX_X + "!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NumberFormatException exception) {
                Outputer.printerror("Координата X должна быть представлена числом!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NullPointerException | IllegalStateException exception) {
                Outputer.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return x;

    }

    /**
     * Asks a user the group's Y coordinate.
     * @return Group's Y coordinate.
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */

    public Long askY() throws IncorrectInputInScriptException {
        String strY;
        Long y;
        while (true) {
            try {
                Outputer.println("Введите координату Y:");
                Outputer.print(App.PS2);
                strY = userScanner.nextLine().trim();
                y = Long.parseLong(strY);
                break;
            } catch (NoSuchElementException exception) {
                Outputer.printerror("Координата Y не распознана!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NumberFormatException exception) {
                Outputer.printerror("Координата Y должна быть представлена числом!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NullPointerException | IllegalStateException exception) {
                Outputer.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return y;
    }

    /**
     * Asks a user the group's coordinates.
     * @return group's coordinates.
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */

    public Coordinates askCoordinates() throws IncorrectInputInScriptException {
        Integer x;
        Long y;
        x = askX();
        y = askY();
        return new Coordinates(x, y);
    }

    /**
     * Asks a user the number of students in this group.
     * @return Group's studentsCount.
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */

    public Long askStudentsCount() throws IncorrectInputInScriptException {
        String strStudentsCount;
        Long studentsCount;
        while (true) {
            try {
                Outputer.println("Введите количество студентов группа");
                Outputer.print(App.PS2);
                strStudentsCount = userScanner.nextLine().trim();
                if (fileMode) Outputer.println(strStudentsCount);
                studentsCount = Long.parseLong(strStudentsCount);
                if (studentsCount <= 0) throw new NotInDeclaredLimitsException();
                break;
            } catch (NoSuchElementException exception) {
                Outputer.printerror("количество студентов не распознано!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NotInDeclaredLimitsException exception) {
                Outputer.printerror("количество студентов должно быть больше нуля!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NumberFormatException exception) {
                Outputer.printerror("количество студентов должно быть представлено числом!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NullPointerException | IllegalStateException exception) {
                Outputer.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return studentsCount;
    }

    /**
     * Asks a user the transferred students.
     * @return Number students transferred.
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */

    public int askTransferredStudents() throws IncorrectInputInScriptException {
        String strTransferredStudents;
        int transferredStudents;
        while (true) {
            try {
                Outputer.println("Введите количество переведенных студентов");
                Outputer.print(App.PS2);
                strTransferredStudents = userScanner.nextLine().trim();
                if (fileMode) Outputer.println(strTransferredStudents);
                transferredStudents = Integer.parseInt(strTransferredStudents);
                if (transferredStudents <= 0 ) throw new NotInDeclaredLimitsException();
                break;
            } catch (NoSuchElementException exception) {
                Outputer.printerror("количество переведенных студентов не распознано!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NotInDeclaredLimitsException exception) {
                Outputer.printerror("количество переведенных студентов должно быть больше нуля!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NumberFormatException exception) {
                Outputer.printerror("количество переведенных студентов должно быть представлено числом!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NullPointerException | IllegalStateException exception) {
                Outputer.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return transferredStudents;
    }

    /**
     * Asks a user the group's form of education.
     * @return Group's form of education.
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */

    public FormOfEducation askFormOfEducation() throws IncorrectInputInScriptException {
        String strFormOfEducation;
        FormOfEducation formOfEducation;
        while (true) {
            try {
                Outputer.println("Введите форма обучения");
                Outputer.print(App.PS2);
                strFormOfEducation = userScanner.nextLine().trim();
                if (fileMode) Outputer.println(strFormOfEducation);
                formOfEducation = FormOfEducation.valueOf(strFormOfEducation.toUpperCase());
                break;
            } catch (NoSuchElementException exception) {
                Outputer.printerror("форма обучения не распознано!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (IllegalArgumentException exception) {
                Outputer.printerror("форма обучения нет в списке!\n" +
                        "DISTANCE_EDUCATION,\n" +
                        "FULL_TIME_EDUCATION,\n" +
                        "EVENING_CLASSES");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (IllegalStateException exception) {
                Outputer.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return formOfEducation;
    }

    /**
     * Asks a user the group's semester.
     * @return Group's semester.
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */

    public Semester askSemester() throws IncorrectInputInScriptException {
        String strSemester;
        Semester semester;
        while (true) {
            try {
                Outputer.println("Введите семестр");
                Outputer.print(App.PS2);
                strSemester = userScanner.nextLine().trim();
                if (fileMode) Outputer.println(strSemester);
                semester = Semester.valueOf(strSemester.toUpperCase());
                break;
            } catch (NoSuchElementException exception) {
                Outputer.printerror("Семестр не распознано!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (IllegalArgumentException exception) {
                Outputer.printerror("Семестр нет в списке!\n" +
                        "FOURTH,\n" +
                        "SIXTH,\n" +
                        "EIGHTH");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (IllegalStateException exception) {
                Outputer.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return semester;
    }

    /**
     * Asks a user the name of group admin.
     * @return group admin name.
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */

    public String askGroupAdminName() throws IncorrectInputInScriptException {
        String groupAdminName;
        while (true) {
            try {
                Outputer.println("Введите имя администратора:");
                Outputer.print(App.PS2);
                groupAdminName = userScanner.nextLine().trim();
                if  (fileMode) Outputer.println(groupAdminName);
                if (groupAdminName.equals("")) throw new MustBeNotEmptyException();
                break;
            } catch (NoSuchElementException exception) {
                Outputer.printerror("Имя администратора не распознано!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (MustBeNotEmptyException exception) {
                Outputer.printerror("Имя администратора не может быть пустым!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (IllegalStateException exception) {
                Outputer.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return groupAdminName;
    }

    /**
     * Asks a user the birthday of admin group.
     * @return birthday of admin group.
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */

    public LocalDate askBirthday() throws IncorrectInputInScriptException {
        String strBirthday;
        LocalDate birthday;
        while (true) {
            try {
                Outputer.println("Введите день рождения");
                Outputer.print(App.PS2);
                strBirthday = userScanner.nextLine().trim();
                if (fileMode) Outputer.println(strBirthday);
                birthday = LocalDate.parse(strBirthday);
                break;
            }catch (DateTimeParseException exception){
                Outputer.printerror("Неверный формат даты! Должно быть в формате YYYY-MM-DD");
            } catch (NoSuchElementException exception) {
                Outputer.printerror("день рождения не распознано!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (IllegalStateException exception) {
                Outputer.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return birthday;
    }

    /**
     * Asks a user the height of group admin.
     * @return group admin height.
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */

    public Long askHeight() throws IncorrectInputInScriptException {
        String strHeight;
        Long height;
        while (true) {
            try {
                Outputer.println("Введите высота администратора");
                Outputer.print(App.PS2);
                strHeight = userScanner.nextLine().trim();
                height = Long.parseLong(strHeight);
                if (height <= 0) throw new NotInDeclaredLimitsException();
                break;
            } catch (NoSuchElementException exception) {
                Outputer.printerror("высота не распознано!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NotInDeclaredLimitsException exception) {
                Outputer.printerror("высота должно быть больше нуля!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NumberFormatException exception) {
                Outputer.printerror("высота должно быть представлено числом!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NullPointerException | IllegalStateException exception) {
                Outputer.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return height;
    }

    /**
     * Asks a user the weight of group admin.
     * @return group admin weight.
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */

    public Double asWeight() throws IncorrectInputInScriptException {
        String strWeight;
        Double weight;
        while (true) {
            try {
                Outputer.println("Введите масса администратора");
                Outputer.print(App.PS2);
                strWeight = userScanner.nextLine().trim();
                weight = Double.parseDouble(strWeight);
                if (weight <= 0) throw new NotInDeclaredLimitsException();
                break;
            } catch (NoSuchElementException exception) {
                Outputer.printerror("масса не распознано!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NotInDeclaredLimitsException exception) {
                Outputer.printerror("масса должно быть больше нуля!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NumberFormatException exception) {
                Outputer.printerror("масса должно быть представлено числом!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NullPointerException | IllegalStateException exception) {
                Outputer.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return weight;
    }
    /**
     * Asks a user the passport ID of group admin.
     * @return group admin passport ID.
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */

    public String askPassportID() throws IncorrectInputInScriptException {
        String passportID;
        while (true) {
            try {
                Outputer.println("введите номер паспорта c длиной < " + StudyGroup.MAX_LENGTH + "и > " + StudyGroup.MIN_LENGTH);
                Outputer.print(App.PS2);
                passportID = userScanner.nextLine().trim();
                if (fileMode) Outputer.println(passportID);
                if (passportID.equals("")) throw new MustBeNotEmptyException();
                if (passportID.length() < 5 || passportID.length() > 23) throw  new NotInDeclaredLimitsException();
                break;
            } catch (NoSuchElementException exception) {
                Outputer.printerror("Номер паспорта не распознана!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NotInDeclaredLimitsException exception) {
                Outputer.printerror("Номер паспорта не может превышать " + StudyGroup.MAX_LENGTH + "!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NullPointerException | IllegalStateException exception) {
                Outputer.printerror("Непредвиденная ошибка!");
                System.exit(0);
            } catch (MustBeNotEmptyException exception) {
                Outputer.printerror("номер паспорта может быть пустым!");
                if (fileMode) throw new IncorrectInputInScriptException();
            }
        }
        return passportID;
    }

    /**
     * Asks a user the group admin.
     * @return group admin.
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */

    public Person askGroupAdmin() throws IncorrectInputInScriptException {
        String name;
        LocalDate birthday;
        Long height;
        Double weight;
        String passportID;

        name = askGroupAdminName();
        height = askHeight();
        weight = asWeight();
        passportID = askPassportID();
        birthday = askBirthday();

        return  new Person(name,birthday,height,weight,passportID);
    }

    /**
     * Asks a user a question.
     * @return Answer (true/false).
     * @param question A question.
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */

    public boolean askQuestion(String question) throws IncorrectInputInScriptException {
        String finalQuestion = question + " (+/-):";
        String answer;
        while (true) {
            try {
                Outputer.println(finalQuestion);
                Outputer.print(App.PS2);
                answer = userScanner.nextLine().trim();
                if (fileMode) Outputer.println(answer);
                if (!answer.equals("+") && !answer.equals("-")) throw new NotInDeclaredLimitsException();
                break;
            } catch (NoSuchElementException exception) {
                Outputer.printerror("Ответ не распознан!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NotInDeclaredLimitsException exception) {
                Outputer.printerror("Ответ должен быть представлен знаками '+' или '-'!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (IllegalStateException exception) {
                Outputer.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return (answer.equals("+")) ? true : false;
    }

    @Override
    public String toString() {return "GroupAsker (вспомогательный класс для запросов пользователю)";}
}