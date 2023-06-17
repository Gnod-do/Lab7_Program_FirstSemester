package common.data;

public class IDprovider {
    private static IDprovider instance = new IDprovider();

    public static IDprovider getInstance(){
        return instance;
    }

    Long count = 0l;

    public Long getID(){
        return count++;
    }
}
