import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Shapes1Test {

    public static void main(String[] args) throws IOException {
        ShapesApplication shapesApplication = new ShapesApplication();

        System.out.println("===READING SQUARES FROM INPUT STREAM===");
        System.out.println(shapesApplication.readCanvases(System.in));
        System.out.println("===PRINTING LARGEST CANVAS TO OUTPUT STREAM===");
        shapesApplication.printLargestCanvasTo(System.out);

    }
}
class Canvas implements Comparable<Canvas>{
    private String id;
    private List<Integer> sizes;

    public Canvas(String input) {
        String[]parts=input.split("\\s+");
        id=parts[0];
        sizes=new ArrayList<Integer>();
        for(int i=1; i<parts.length; i++){
            sizes.add(Integer.parseInt(parts[i]));
        }
    }
    public int count(){
        return sizes.size();
    }
    public int sum(){
        int sum=0;
        for(int i=0; i<sizes.size(); i++){
            sum+=sizes.get(i);
        }
        return sum*4;
    }
    @Override
    public int compareTo(Canvas o) {
        return Integer.compare(sum(), o.sum());
    }

    @Override
    public String toString() {
        return String.format("%s %d %d", id, count(), sum());
    }
}

class ShapesApplication{
    private List<Canvas> canvases;
    public ShapesApplication() {
    }
    public int readCanvases (InputStream inputStream) throws IOException {
        int counter=0;
        BufferedReader br=new BufferedReader(new InputStreamReader(inputStream));
        canvases=br.lines().map(i->new Canvas(i)).collect(Collectors.toList());
        for(int i=0; i<canvases.size(); i++){
            counter+= canvases.get(i).count();
        }
        br.close();

        return counter;
    }

    void printLargestCanvasTo (OutputStream outputStream) {
        PrintWriter pw=new PrintWriter(new PrintStream(outputStream));
        pw.println(max().toString());
        pw.close();
    }
    public Canvas max(){
        return canvases.stream().max(Comparator.naturalOrder()).get();
    }
}