package common.data;

import java.io.Serializable;

public class Coordinates  implements  Serializable{
    private int x;

    private Long y;

    public Coordinates(int x, Long y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return X-coordinate.
     */

    public int getX(){return x;}

    /**
     * @return Y-coordinate.
     */

    public Long getY(){return y;}

    @Override
    public String toString(){return "X: " + x + " and " + " Y: " +y;}

    @Override
    public int hashCode() {
        return y.hashCode() + Integer.valueOf(x).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof Coordinates) {
            Coordinates coordinatesObj = (Coordinates) obj;
            return (x == coordinatesObj.getX()) && y.equals(coordinatesObj.getY());
        }
        return false;
    }

}
