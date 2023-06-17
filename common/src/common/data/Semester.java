package common.data;

public enum Semester {
    FOURTH,
    SIXTH,
    EIGHTH;

    /**
     * Generates a beautiful list of enum string values.
     * @return String with all enum values splitted by comma.
     */

    public static String nameList() {
        String nameList = "";
        for(Semester semester: values()) {
            nameList += semester.name() + ", ";
        }
        return nameList.substring(0, nameList.length()-2);
    }
}
