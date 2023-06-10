import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Shapes1Test2{
    public static void main(String[] args) throws IOException {
        ShapesApplication2 shapesApplication = new ShapesApplication2();

        System.out.println("===READING SQUARES FROM INPUT STREAM===");
        System.out.println(shapesApplication.readCanvases(System.in));
        System.out.println("===PRINTING LARGEST CANVAS TO OUTPUT STREAM===");
        shapesApplication.printLargestCanvasTo(System.out);

    }
}
class Square{
    private int side;

    public Square(int side) {
        this.side = side;
    }
    public int perimeter(){
        return this.side*4;
    }
}
class Canvas2{
    String id;
    List<Square> squares;
    public Canvas2(String line) {
        int counter = 0;
        String []parts = line.split(" ");
        this.id = parts[0];
        squares = new ArrayList<>();

        for(int i = 1; i < parts.length; i++){
            squares.add(new Square(Integer.parseInt(parts[i])));
            counter++;
        }
    }
    public int SumOfPerimeters(){
        return squares.stream().mapToInt(Square::perimeter).sum();
    }

    @Override
    public String toString() {
        return String.format("%s %d %d", id, squares.size(), SumOfPerimeters());
    }
}
class ShapesApplication2{
    List<Canvas2> canvases;
    public ShapesApplication2() {
        canvases = new ArrayList<>();
    }
    public int readCanvases (InputStream inputStream){
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        canvases=br.lines().map(Canvas2::new).collect(Collectors.toList());

        int counter = 0;
        for(Canvas2 c : canvases){
            counter += c.squares.size();
        }
        return counter;
    }
    public void printLargestCanvasTo (OutputStream outputStream) throws IOException {
        Canvas2 c = canvases.stream().max(Comparator.comparing(Canvas2::SumOfPerimeters)).get();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));
        bw.append(c.toString());
        bw.flush();
    }
}