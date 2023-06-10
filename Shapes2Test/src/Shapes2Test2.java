import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Shapes2Test2{

    public static void main(String[] args) throws IOException {

        ShapesApplication2 shapesApplication = new ShapesApplication2(10000);

        System.out.println("===READING CANVASES AND SHAPES FROM INPUT STREAM===");
        shapesApplication.readCanvases(System.in);

        System.out.println("===PRINTING SORTED CANVASES TO OUTPUT STREAM===");
        shapesApplication.printCanvases(System.out);


    }
}
class Square{
    double side;
    public Square(double side) {
        this.side = side;
    }
    public double getArea(){
        return side*side;
    }
}
class Circle{
    double radius;
    public Circle(double radius) {
        this.radius = radius;
    }
    public double getArea(){
        return radius*radius*Math.PI;
    }
}
class Canvas2{
    List<Circle> circles;
    List<Square> squares;
    String id;

    public Canvas2(String id) {
        circles = new ArrayList<>();
        squares = new ArrayList<>();
        this.id = id;
    }
    public double getSum(){
        double sum = 0;
        sum += circles.stream().mapToDouble(Circle::getArea).sum();
        sum += squares.stream().mapToDouble(Square::getArea).sum();
        return sum;
    }

    @Override
    public String toString() {
        double minAreaCircle = circles.stream().mapToDouble(Circle::getArea).min().orElse(10000);
        double maxAreaCircle = circles.stream().mapToDouble(Circle::getArea).max().orElse(0);

        double minAreaSquare = squares.stream().mapToDouble(Square::getArea).min().orElse(10000);
        double maxAreaSquare = squares.stream().mapToDouble(Square::getArea).max().orElse(0);

        double min =Math.min(minAreaSquare, minAreaCircle);
        double max =Math.max(maxAreaSquare, maxAreaCircle);


        double sumCircles = circles.stream().mapToDouble(Circle::getArea).sum();
        double sumSquares = squares.stream().mapToDouble(Square::getArea).sum();
        double average = (sumSquares + sumCircles) / (circles.size() + squares.size());
        return String.format("%s %d %d %d %.2f %.2f %.2f\n",
                id,
                squares.size()+circles.size(),
                circles.size(),
                squares.size(),
                min,
                max,
                average);
    }
}
class ShapesApplication2{
    double maxArea;
    List<Canvas2> canvases;

    public ShapesApplication2(double maxArea) {
        this.maxArea = maxArea;
        canvases = new ArrayList<>();
    }
    public void readCanvases (InputStream inputStream){
        Scanner scanner = new Scanner(new InputStreamReader(inputStream));
        String []parts;

        while (scanner.hasNextLine()){
            parts = scanner.nextLine().split(" ");
            Canvas2 newCanvas = new Canvas2(parts[0]);
            boolean flag = false;

            for(int i=1; i<parts.length; i+=2){
                if(parts[i].equals("C")){
                    if (Double.parseDouble(parts[i+1]) * Double.parseDouble(parts[i+1]) * Math.PI > maxArea){
                        InvalidCanvasException e = new InvalidCanvasException(parts[0], maxArea);
                        flag = true;
                        break;
                    }
                    newCanvas.circles.add(new Circle(Double.parseDouble(parts[i+1])));
                }
                else{
                    if (Double.parseDouble(parts[i+1]) * Double.parseDouble(parts[i+1])> maxArea){
                        InvalidCanvasException e = new InvalidCanvasException(parts[0], maxArea);
                        flag = true;
                        break;
                    }
                    newCanvas.squares.add(new Square(Double.parseDouble(parts[i+1])));
                }
            }
            if(!flag){
                canvases.add(newCanvas);
            }
        }
    }
    public void printCanvases (OutputStream os) throws IOException {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
        List<Canvas2> c = canvases.stream().sorted(Comparator.comparing(Canvas2::getSum).reversed()).collect(Collectors.toList());
        for(Canvas2 canvas : c){
            bw.append(canvas.toString());
        }
        bw.flush();

    }
}
class InvalidCanvasException extends Exception{
    public InvalidCanvasException(String id, double maxArea) {
        System.out.printf("Canvas %s has a shape with area larger than %.2f\n", id, maxArea);
    }
}