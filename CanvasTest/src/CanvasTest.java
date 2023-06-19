import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class CanvasTest {

    public static void main(String[] args) throws IOException, InvalidDimensionException {
        Canvas canvas = new Canvas();

        System.out.println("READ SHAPES AND EXCEPTIONS TESTING");
        try {
            canvas.readShapes(System.in);
        }
        catch (InvalidDimensionException e){
            System.out.println(e.getMessage());
        }


        System.out.println("BEFORE SCALING");
        canvas.printAllShapes(System.out);
        canvas.scaleShapes("123456", 1.5);
        System.out.println("AFTER SCALING");
        canvas.printAllShapes(System.out);

        System.out.println("PRINT BY USER ID TESTING");
        canvas.printByUserId(System.out);

        System.out.println("PRINT STATISTICS");
        canvas.statistics(System.out);
    }
}
abstract class Shape{
    String id;
    double side;
     public abstract double getPerimeter();
     public abstract double getArea();
    public void setSide(double coef) {
        this.side *= coef;
    }
}
class Circle extends Shape{
    public Circle(String part, double v) {
        this.id = part;
        this.side = v;
    }

    @Override
    public double getPerimeter() {
        return 2 * side * Math.PI;
    }

    @Override
    public double getArea() {
        return side * side * Math.PI;
    }

    @Override
    public String toString() {
        return String.format("Circle -> Radius: %.2f Area: %.2f Perimeter: %.2f\n", side, getArea(), getPerimeter());
    }
}
class Square extends Shape{
    public Square(String part, double v) {
        this.id = part;
        this.side = v;
    }

    @Override
    public double getPerimeter() {
        return 4 * side;
    }

    @Override
    public double getArea() {
        return side * side;
    }
    @Override
    public String toString() {
        return String.format("Square: -> Side: %.2f Area: %.2f Perimeter: %.2f\n", side, getArea(), getPerimeter());
    }
}
class Rectangle extends Shape{
    double side2;

    public Rectangle(String part, double v, double v1) {
        this.id = part;
        this.side = v;
        this.side2 = v1;
    }

    @Override
    public double getPerimeter() {
        return 2 * side + 2 * side2;
    }

    @Override
    public double getArea() {
        return side * side2;
    }

    public void setSide2(double coef) {
        this.side2 *= coef;
    }
    @Override
    public String toString() {
        return String.format("Rectangle: -> Sides: %.2f, %.2f Area: %.2f Perimeter: %.2f\n", side, side2, getArea(), getPerimeter());
    }
}
class User{
    List<Shape> shapes;
    String id;
    public User(String part) {
        shapes = new ArrayList<>();
        id = part;
    }

    public void addShape(String line) throws InvalidDimensionException {
        String []parts = line.split(" ");

        if(Objects.equals(parts[0], "1")){
            shapes.add(new Circle(parts[1], Double.parseDouble(parts[2])));
        }
        else if(Objects.equals(parts[0], "2")){
            shapes.add(new Square(parts[1], Double.parseDouble(parts[2])));
        }
        else{
            if(Double.parseDouble(parts[3]) == 0){
                throw new InvalidDimensionException();
            }
            shapes.add(new Rectangle(parts[1], Double.parseDouble(parts[2]), Double.parseDouble(parts[3])));
        }
    }
    public int shapeCount(){
        return shapes.size();
    }

    public double sumArea() {
        return shapes
                .stream()
                .mapToDouble(Shape::getArea)
                .sum();
    }

    public String shapesToString() {
        StringBuilder sb = new StringBuilder();

        List<Shape> sorted = shapes
                .stream()
                .sorted(Comparator.comparing(Shape::getPerimeter))
                .collect(Collectors.toList());
        for (Shape s : sorted){
            sb.append(s.toString());
        }
        return sb.toString();
    }
}
class Canvas{
    Map<String, User> users;
    List<Shape> shapes;

    public Canvas() {
        users = new HashMap<>();
        shapes = new ArrayList<>();
    }
    public void readShapes (InputStream is) throws InvalidDimensionException {
        Scanner scanner = new Scanner(is);

        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            String []parts = line.split(" ");

            if(!parts[1].matches("^[a-zA-Z0-9]+$") || parts[1].length() != 6){
                InvalidIDException e = new InvalidIDException(parts[1]);
                e.getMessage();
                continue;
            }
            if(Double.parseDouble(parts[2]) == 0){
                throw new InvalidDimensionException();
            }

            User tmp = users.computeIfAbsent(parts[1], v -> new User(parts[1]));
            tmp.addShape(line);

            if(Objects.equals(parts[0], "1")){
                shapes.add(new Circle(parts[1], Double.parseDouble(parts[2])));
            }
            else if(Objects.equals(parts[0], "2")){
                shapes.add(new Square(parts[1], Double.parseDouble(parts[2])));
            }
            else{
                if(Double.parseDouble(parts[3]) == 0){
                    throw new InvalidDimensionException();
                }
                shapes.add(new Rectangle(parts[1], Double.parseDouble(parts[2]), Double.parseDouble(parts[3])));
            }
        }
    }
    public void scaleShapes (String userID, double coef){

        for (Shape shape : shapes){
            if(Objects.equals(shape.id, userID)) {
                shape.setSide(coef);
                if (shape instanceof Rectangle) {
                    ((Rectangle) shape).setSide2(coef);
                }
            }
        }

    }
    public void printAllShapes (OutputStream os) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(os);
        List<Shape> sorted = shapes
                .stream()
                .collect(Collectors.toList());

        for(Shape s : sorted){
            writer.append(s.toString());
        }
        writer.flush();
    }
    public void printByUserId (OutputStream os) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(os);

        List<User> sorted = users
                .values()
                .stream()
                .sorted(Comparator.comparing(User::shapeCount).reversed())
                .collect(Collectors.toList());

        for(User u : sorted){
            writer.append(String.format("Shapes of user: %s\n", u.id));
            writer.append(u.shapesToString());
        }
        writer.flush();
    }
    public void statistics (OutputStream os) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(os);
        DoubleSummaryStatistics stat = users
                .values()
                .stream()
                .flatMap(s -> s.shapes.stream())
                .mapToDouble(Shape::getArea)
                .summaryStatistics();
        writer.append(String.format("count: %d\n", stat.getCount()));
        writer.append(String.format("sum: %.2f\n", stat.getSum()));
        writer.append(String.format("min: %.2f\n", stat.getMin()));
        writer.append(String.format("average: %.2f\n", stat.getAverage()));
        writer.append(String.format("max: %.2f", stat.getMax()));

        writer.flush();
    }
}
class InvalidIDException extends Exception{

    public InvalidIDException(String part) {
        this.getMessage(part);
    }

    public void getMessage(String part) {
        System.out.printf("ID %s is not valid\n", part);
    }

}
class InvalidDimensionException extends Exception{
    public InvalidDimensionException() {
        this.getMessage();
    }

    public String getMessage() {
        return String.format("Dimension 0 is not allowed!");
    }
}