import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Shapes2Test {

    public static void main(String[] args) throws IOException {

        ShapesApplication shapesApplication = new ShapesApplication(10000);

        System.out.println("===READING CANVASES AND SHAPES FROM INPUT STREAM===");
        shapesApplication.readCanvases(System.in);

        System.out.println("===PRINTING SORTED CANVASES TO OUTPUT STREAM===");
        shapesApplication.printCanvases(System.out);


    }
}
class IrregularCanvasException {
    String id;
    double maxArea;
    public IrregularCanvasException(String id, double maxArea){
        this.id=id;
        this.maxArea=maxArea;
    }
    public String getMessage(){
        return String.format("Canvas %s has a shape with area larger than %.2f",id,maxArea);
    }
}
class Shape{
    private int size;
    private String type;

    public Shape(int size, String type) {
        this.size = size;
        this.type = type;
    }

    public String getType() {
        return type;
    }
    public double getArea(){
        if(Objects.equals(type, "C")){
            return (double)size*size*Math.PI;
        }
        else return (double)size*size;
    }
}
class Canvas implements Comparable<Canvas> {
    private String id;
    private List<Shape> shapes;
    public Canvas(String input, double maxArea) {
        String[] parts = input.split("\\s+");
        id=parts[0];
        int size;
        String type;
        for (int i = 1; i < parts.length; i+=2) {
            size=Integer.parseInt(parts[i+1]);
            type=parts[i];
            if(Objects.equals(type, "S")){
                if((double)size*(double)size>maxArea){
                    IrregularCanvasException e=new IrregularCanvasException(id, maxArea);
                    System.out.println(e.getMessage());
                }
            }
            else if(Objects.equals(type, "C")){
                if((double)size*(double)size*Math.PI>maxArea){
                    IrregularCanvasException e=new IrregularCanvasException(id, maxArea);
                    System.out.println(e.getMessage());
                }
            }
        }
        id = parts[0];
        shapes = new ArrayList<Shape>();
        for (int i = 1; i < parts.length; i+=2) {
            size=Integer.parseInt(parts[i+1]);
            type=parts[i];
            shapes.add(new Shape(size, type));
        }
    }

    public String getId() {
        return id;
    }

    public double sum(){
        double sum=0;
        for(int i=0; i<shapes.size(); i++){
            sum+=shapes.get(i).getArea();
        }
        return sum;
    }
    @Override
    public int compareTo(Canvas o) {
        return Double.compare(o.sum(), sum());
    }

    public int getCircles() {
        int counter=0;
        for(int i=0; i<shapes.size(); i++){
            if(Objects.equals(shapes.get(i).getType(), "C")){
                counter++;
            }
        }
        return counter;
    }
    public int getSquares() {
        int counter=0;
        for(int i=0; i<shapes.size(); i++){
            if(Objects.equals(shapes.get(i).getType(), "S")){
                counter++;
            }
        }
        return counter;
    }

    public int getSize() {
        return shapes.size();
    }

    public double min() {
        int index=0;
        for(int i=0; i<shapes.size(); i++){
            if(shapes.get(i).getArea()<shapes.get(index).getArea()){
                index=i;
            }
        }
        return shapes.get(index).getArea();
    }
    public double max() {
        int index=0;
        for(int i=0; i<shapes.size(); i++){
            if(shapes.get(i).getArea()>shapes.get(index).getArea()){
                index=i;
            }
        }
        return shapes.get(index).getArea();
    }
    public double average() {
        double sum=sum();

        return sum/getSize();
    }
}
class ShapesApplication{
    private double maxArea;
    private List<Canvas> canvases;
    public ShapesApplication(double maxArea){
        this.maxArea=maxArea;
    }
    public void readCanvases (InputStream inputStream) throws IOException {
        BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
        canvases=reader.lines().map(x->new Canvas(x,maxArea)).collect(Collectors.toList());
        reader.close();
    }
    public void printCanvases(PrintStream out) {
        PrintWriter pw=new PrintWriter(new PrintStream(out));
        sort();
        for(int i=0; i<canvases.size(); i++){
            pw.println(String.format("%s %d %d %d %.2f %.2f %.2f",canvases.get(i).getId(),canvases.get(i).getSize(), canvases.get(i).getCircles(),canvases.get(i).getSquares(), canvases.get(i).min(),canvases.get(i).max(),canvases.get(i).average()));
        }
        pw.close();
    }
    public void sort(){
        canvases=canvases.stream().sorted().collect(Collectors.toList());
    }
}