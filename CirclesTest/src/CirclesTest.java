import java.util.*;

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
class ObjectCanNotBeMovedException extends Exception{
    Movable m;

    public ObjectCanNotBeMovedException(Movable m)
    {
        this.m = m;
    }

    @Override
    public String getMessage()
    {
        return "Point (" + m.getCurrentXPosition() + "," + m.getCurrentYPosition() + ") is out of bounds";
    }
}
class MovableObjectNotFittableException extends Exception{
    Movable mov;

    public MovableObjectNotFittableException(Movable mov)
    {
        this.mov = mov;
    }

    @Override
    public String getMessage() {
        return mov.toString().replace("coordinates ", "") + " can not be fitted into the collection";
    }
}

interface Movable{
    void moveUp()throws ObjectCanNotBeMovedException;
    void moveDown()throws ObjectCanNotBeMovedException;
    void moveLeft()throws ObjectCanNotBeMovedException;
    void moveRight()throws ObjectCanNotBeMovedException;
    int getCurrentXPosition();
    int getCurrentYPosition();

    TYPE typeOf();
}

class MovablePoint implements Movable{
    private int x, y;
    private int xSpeed, ySpeed;

    public MovablePoint(int x, int y, int xSpeed, int ySpeed) {
        this.x = x;
        this.y = y;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }
    public MovablePoint(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
    @Override
    public void moveUp() throws ObjectCanNotBeMovedException {
        if(y + ySpeed > MovablesCollection.y_MAX || y + ySpeed < 0)
        {
            throw new ObjectCanNotBeMovedException(new MovablePoint(x, y + ySpeed));
        }
        y += ySpeed;
    }
    @Override
    public void moveDown() throws ObjectCanNotBeMovedException {
        if(y - ySpeed > MovablesCollection.y_MAX || y - ySpeed < 0)
        {
            throw new ObjectCanNotBeMovedException(new MovablePoint(x, y - ySpeed));
        }
    }
    @Override
    public void moveLeft() throws ObjectCanNotBeMovedException {
        if(x - xSpeed > MovablesCollection.x_MAX || x - xSpeed < 0)
        {
            throw new ObjectCanNotBeMovedException(new MovablePoint(x-xSpeed, y));
        }
        x -= xSpeed;
    }
    @Override
    public void moveRight() throws ObjectCanNotBeMovedException {
        if(x + xSpeed > MovablesCollection.x_MAX || x + xSpeed < 0)
        {
            throw new ObjectCanNotBeMovedException(new MovablePoint(x+xSpeed, y));
        }
        x += xSpeed;
    }
    @Override
    public int getCurrentXPosition(){
        return x;
    }
    @Override
    public int getCurrentYPosition(){
        return y;
    }

    @Override
    public String toString() {
        return "Movable point with coordinates ("+x+","+y+")";
    }
    public TYPE typeOf()
    {
        return TYPE.POINT;
    }
}
class MovableCircle implements Movable{
    private int radius;
    MovablePoint center;

    public MovableCircle(int radius, MovablePoint center) {
        this.radius = radius;
        this.center = center;
    }
    @Override
    public void moveUp()throws ObjectCanNotBeMovedException{
        center.moveUp();

    }
    @Override
    public void moveDown()throws ObjectCanNotBeMovedException{
        center.moveDown();

    }
    @Override
    public void moveLeft()throws ObjectCanNotBeMovedException{
        center.moveLeft();

    }
    @Override
    public void moveRight()throws ObjectCanNotBeMovedException{
        center.moveRight();

    }
    @Override
    public int getCurrentXPosition(){
        return center.getCurrentXPosition();
    }
    @Override
    public int getCurrentYPosition(){
        return center.getCurrentYPosition();
    }

    @Override
    public String toString()
    {
        return "Movable circle with center coordinates "+"(" +
                center.getCurrentXPosition() + "," + center.getCurrentYPosition() + ')' +
                " and radius " + radius;
    }
    public TYPE typeOf()
    {
        return TYPE.CIRCLE;
    }
    public int getRadius() {
        return radius;
    }
}
class MovablesCollection{
    private Movable[] movables;
    static int x_MAX=0;
    static int y_MAX=0;

    public MovablesCollection(int x_MAX, int y_MAX)
    {
        this.x_MAX = x_MAX;
        this.y_MAX = y_MAX;
        movables = new Movable[0];
    }
    public static void setxMax(int i)
    {
        x_MAX = i;
    }

    public static void setyMax(int i)
    {
        y_MAX = i;
    }
    public void addMovableObject(Movable m) throws MovableObjectNotFittableException {
        if(m.typeOf() == TYPE.CIRCLE)
        {
            if(m.getCurrentXPosition() + ((MovableCircle) m).getRadius() > x_MAX || m.getCurrentXPosition() - ((MovableCircle) m).getRadius() < 0 || m.getCurrentYPosition() + ((MovableCircle) m).getRadius() > y_MAX || m.getCurrentYPosition() - ((MovableCircle) m).getRadius()< 0)
            {
                throw new MovableObjectNotFittableException(m);
            }
        }
        if(m.getCurrentXPosition() > x_MAX || m.getCurrentXPosition() < 0 || m.getCurrentYPosition() > y_MAX || m.getCurrentYPosition() < 0)
        {
            throw new MovableObjectNotFittableException(m);
        }
        movables = Arrays.copyOf(movables, movables.length + 1);
        movables[movables.length - 1] = m;
    }
    public void moveObjectsFromTypeWithDirection (TYPE type, DIRECTION direction){
        for(Movable m : movables)
        {
            if(m.typeOf() == type)
            {
                try
                {
                    if(direction == DIRECTION.UP)
                    {
                        m.moveUp();
                    }
                    else if(direction == DIRECTION.DOWN)
                    {
                        m.moveDown();
                    }
                    else if(direction == DIRECTION.LEFT)
                    {
                        m.moveLeft();
                    }
                    else if(direction == DIRECTION.RIGHT)
                    {
                        m.moveRight();
                    }
                }
                catch (Exception e)
                {
                    System.out.println(e.getMessage());
                }

            }
        }
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Collection of movable objects with size " +
                movables.length + ":\n");
        for (Movable m: movables)
        {
            sb.append(m.toString() + '\n');
        }
        return sb.toString();
    }
}

public class CirclesTest {

    public static void main(String[] args) {

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

            try
            {
                if (Integer.parseInt(parts[0]) == 0) { //point
                    collection.addMovableObject(new MovablePoint(x, y, xSpeed, ySpeed));
                } else { //circle
                    int radius = Integer.parseInt(parts[5]);
                    collection.addMovableObject(new MovableCircle(radius, new MovablePoint(x, y, xSpeed, ySpeed)));
                }
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
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