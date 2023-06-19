import java.util.*;

public class ShapesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Canvas canvas = new Canvas();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            int type = Integer.parseInt(parts[0]);
            String id = parts[1];
            if (type == 1) {
                Color color = Color.valueOf(parts[2]);
                float radius = Float.parseFloat(parts[3]);
                canvas.add(id, color, radius);
            } else if (type == 2) {
                Color color = Color.valueOf(parts[2]);
                float width = Float.parseFloat(parts[3]);
                float height = Float.parseFloat(parts[4]);
                canvas.add(id, color, width, height);
            } else if (type == 3) {
                float scaleFactor = Float.parseFloat(parts[2]);
                System.out.println("ORIGNAL:");
                System.out.print(canvas);
                canvas.scale(id, scaleFactor);
                System.out.printf("AFTER SCALING: %s %.2f\n", id, scaleFactor);
                System.out.print(canvas);
            }

        }
    }
}

enum Color {
    RED, GREEN, BLUE
}
interface Scalable{
    void scale(float scaleFactor);
}
interface Stackable{
    float weight();
}
abstract class Shape implements Scalable, Stackable{
    String id;
    Color color;

    public String getId() {
        return id;
    }
}
class Circle extends Shape{
    float radius;

    public Circle(String id, Color color, float radius) {
        this.radius = radius;
        this.id = id;
        this.color = color;
    }

    @Override
    public void scale(float scaleFactor) {
        radius *= scaleFactor;
    }
    @Override
    public float weight() {
        return (float) (radius*radius*Math.PI);
    }
    @Override
    public String toString() {
        return String.format("C: %-5s%-10s%10.2f\n", id, color, weight());
    }

}
class Rectangle extends Shape{
    float height;
    float width;

    public Rectangle(String id, Color color, float width, float height) {
        this.height = height;
        this.width = width;
        this.id = id;
        this.color = color;
    }

    @Override
    public void scale(float scaleFactor) {
        height *= scaleFactor;
        width *= scaleFactor;
    }

    @Override
    public float weight() {
        return width * height;
    }

    @Override
    public String toString() {
        return String.format("R: %-5s%-10s%10.2f\n", id, color, weight());
    }
}
class Canvas {
    TreeSet<Shape> shapes;

    public Canvas() {
        shapes = new TreeSet<>(Comparator.comparing(Shape::weight).reversed().thenComparing(Shape::getId));
    }

    void add(String id, Color color, float radius){
        shapes.add(new Circle(id, color, radius));
    }
    void add(String id, Color color, float width, float height){
        shapes.add(new Rectangle(id, color, width, height));
    }
    void scale(String id, float scaleFactor){
        for(Shape s : shapes){
            if(Objects.equals(s.id, id)){
                shapes.remove(s);
                s.scale(scaleFactor);
                shapes.add(s);
                break;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Shape s : shapes){
            sb.append(s.toString());
        }
        return sb.toString();
    }
}