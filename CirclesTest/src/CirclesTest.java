import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

enum TYPE {
    POINT,
    CIRCLE
}

enum DIRECTION {
    UP,
    DOWN,
    LEFT,
    RIGHT
}

public class CirclesTest {

    public static void main(String[] args) throws MovableObjectNotFittableException, ObjectCanNotBeMovedException {

        System.out.println("===COLLECTION CONSTRUCTOR AND ADD METHOD TEST===");
        MovablesCollection collection = new MovablesCollection(100, 100);
        Scanner sc = new Scanner(System.in);
        int samples = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < samples; i++) {
            String inputLine = sc.nextLine();
            String[] parts = inputLine.split(" ");

            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);
            int xSpeed = Integer.parseInt(parts[3]);
            int ySpeed = Integer.parseInt(parts[4]);

            if (Integer.parseInt(parts[0]) == 0) { //point
                collection.addMovableObject(new MovablePoint(x, y, xSpeed, ySpeed));
            } else { //circle
                int radius = Integer.parseInt(parts[5]);
                try {
                    collection.addMovableObject(new MovableCircle(radius, new MovablePoint(x, y, xSpeed, ySpeed)));
                }catch (MovableObjectNotFittableException e){
                    System.out.println(e.getMessage());;
                }
            }

        }
        System.out.println(collection.toString());

        System.out.println("MOVE POINTS TO THE LEFT");
        collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.LEFT);
        System.out.println(collection.toString());

        System.out.println("MOVE CIRCLES DOWN");
        collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.DOWN);
        System.out.println(collection.toString());

        System.out.println("CHANGE X_MAX AND Y_MAX");
        MovablesCollection.setxMax(90);
        MovablesCollection.setyMax(90);

        System.out.println("MOVE POINTS TO THE RIGHT");
        collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.RIGHT);

        System.out.println(collection.toString());

        System.out.println("MOVE CIRCLES UP");
        collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.UP);

        System.out.println(collection.toString());


    }


}
interface Movable{
    void moveUp(int y_MAX) throws ObjectCanNotBeMovedException;
    void moveLeft() throws ObjectCanNotBeMovedException;
    void moveRight(int x_MAX) throws ObjectCanNotBeMovedException;
    void moveDown() throws ObjectCanNotBeMovedException;
    int getCurrentXPosition();
    int getCurrentYPosition();
}
class MovablePoint implements Movable{
    int x;
    int y;
    int xSpeed;
    int ySpeed;

    public MovablePoint(int x, int y, int xSpeed, int ySpeed) {
        this.x = x;
        this.y = y;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    @Override
    public void moveUp(int y_MAX) throws ObjectCanNotBeMovedException {
        if (y + ySpeed > y_MAX){
            throw new ObjectCanNotBeMovedException(x, y + ySpeed);
        }
        x = x + xSpeed;
    }

    @Override
    public void moveLeft() throws ObjectCanNotBeMovedException {
        if (x - xSpeed < 0){
            throw new ObjectCanNotBeMovedException(x - xSpeed, y);
        }
        x = x - xSpeed;
    }
    @Override
    public void moveRight(int x_MAX) throws ObjectCanNotBeMovedException {
        if (x + xSpeed > x_MAX){
            throw new ObjectCanNotBeMovedException(x + xSpeed, y);
        }
        x = x + xSpeed;
    }
    @Override
    public void moveDown() throws ObjectCanNotBeMovedException {
        if (y - ySpeed < 0){
            throw new ObjectCanNotBeMovedException(x, y - ySpeed);
        }
        y = y - ySpeed;
    }
    @Override
    public int getCurrentXPosition() {
        return x;
    }
    @Override
    public int getCurrentYPosition() {
        return y;
    }
    @Override
    public String toString() {
        return String.format("Movable point with coordinates (%d,%d)\n", x, y);
    }
}
class MovableCircle implements Movable{
    int radius;
    MovablePoint center;

    public MovableCircle(int radius, MovablePoint center) {
        this.radius = radius;
        this.center = center;
    }

    @Override
    public void moveUp(int y_MAX) throws ObjectCanNotBeMovedException {
        if (center.y + center.ySpeed > y_MAX){
            throw new ObjectCanNotBeMovedException(center.x, center.y + center.ySpeed);
        }
        center.y = center.y + center.ySpeed;
    }

    @Override
    public void moveLeft() throws ObjectCanNotBeMovedException {
        if (center.x - center.xSpeed < 0){
            throw new ObjectCanNotBeMovedException(center.x - center.xSpeed, center.y);
        }
        center.x = center.x - center.xSpeed;
    }

    @Override
    public void moveRight(int x_MAX) throws ObjectCanNotBeMovedException {
        if (center.x + center.xSpeed > x_MAX){
            throw new ObjectCanNotBeMovedException(center.x + center.xSpeed, center.y);
        }
        center.x = center.x + center.xSpeed;
    }

    @Override
    public void moveDown() throws ObjectCanNotBeMovedException {
        if (center.y - center.ySpeed < 0){
            throw new ObjectCanNotBeMovedException(center.x, center.y - center.ySpeed);
        }
        center.y = center.y - center.ySpeed;
    }

    @Override
    public int getCurrentXPosition() {
        return center.x;
    }

    @Override
    public int getCurrentYPosition() {
        return center.y;
    }
    @Override
    public String toString() {
        return String.format(
                "Movable circle with center coordinates (%d,%d) and radius %d\n",
                center.getCurrentXPosition(), center.getCurrentYPosition(), radius);
    }
}
class MovablesCollection{
    List<Movable> movables;
    static int x_MAX;
    static int y_MAX;

    public MovablesCollection(int x_MAX, int y_MAX) {
        MovablesCollection.x_MAX = x_MAX;
        MovablesCollection.y_MAX = y_MAX;
        movables = new ArrayList<>();
    }

    public static void setxMax(int i) {
        x_MAX = i;
    }

    public static void setyMax(int i) {
        y_MAX = i;
    }

    public void addMovableObject(Movable m) throws MovableObjectNotFittableException {
        if (m instanceof MovablePoint){
            if (m.getCurrentXPosition() < 0 || m.getCurrentXPosition() > x_MAX){
                //throw new MovableObjectNotFittableException();
            }
            if (m.getCurrentYPosition() < 0 || m.getCurrentYPosition() > y_MAX){
                //throw new MovableObjectNotFittableException();
            }
        }
        else {
            MovableCircle tmp = (MovableCircle) m;
            if (m.getCurrentXPosition() - tmp.radius< 0 || m.getCurrentXPosition() + tmp.radius > x_MAX){
                throw new MovableObjectNotFittableException(m);
            }
            if (m.getCurrentYPosition() - tmp.radius < 0 || m.getCurrentYPosition() + tmp.radius > y_MAX){
                throw new MovableObjectNotFittableException(m);
            }
        }
        movables.add(m);
    }
    public void moveObjectsFromTypeWithDirection (TYPE type, DIRECTION direction) throws ObjectCanNotBeMovedException {
        for (Movable m : movables) {
            Movable tmp;
            if (type == TYPE.CIRCLE && m instanceof MovableCircle) {
                tmp = (MovableCircle) m;
            } else if (type == TYPE.POINT && m instanceof MovablePoint) {
                tmp = (MovablePoint) m;
            } else {
                continue;
            }

            try {
                switch (direction) {
                    case UP:
                        tmp.moveUp(y_MAX);
                        break;
                    case DOWN:
                        tmp.moveDown();
                        break;
                    case LEFT:
                        tmp.moveLeft();
                        break;
                    case RIGHT:
                        tmp.moveRight(x_MAX);
                        break;
                }
            } catch (ObjectCanNotBeMovedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Collection of movable objects with size %d:\n", movables.size()));
        for(Movable m : movables){
            sb.append(m.toString());
        }
        return sb.toString();
    }
}
class MovableObjectNotFittableException extends Exception{
    public MovableObjectNotFittableException(Movable m) {
        super(String.format("Movable circle with center (%d,%d) and radius %d can not be fitted into the collection",
                ((MovableCircle) m).center.x, ((MovableCircle) m).center.y, ((MovableCircle) m).radius));
    }
}
class ObjectCanNotBeMovedException extends RuntimeException{
    public ObjectCanNotBeMovedException(int x, int y) {
        super(String.format("Point (%d,%d) is out of bounds", x, y));
    }
}