
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
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
    //се пресметува како плоштина на соодветната форма
}
class Shape implements Scalable, Stackable{{

}
    private String id;
    Color color;
    double area;
    String type;

    public Shape(String id, Color color, float radius) {
        this.id=id;
        this.color=color;
        area=radius*radius*Math.PI;
        type="C";
    }
    public Shape(String id, Color color, float width, float height) {
        this.id=id;
        this.color=color;
        area=width*height;
        type="R";
    }

    @Override
    public void scale(float scaleFactor) {
        area=area*scaleFactor*scaleFactor;
    }

    @Override
    public float weight() {
        return (float) area;
    }
    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("%s: %-5s%-10s%10.2f\n", type, id, color, weight());
    }
}
class Canvas {
    List<Shape> shapes;

    public Canvas() {
        shapes=new ArrayList<Shape>();
    }

    public void add(String id, Color color, float radius){
        float area= (float) (radius*radius*Math.PI);
        if(shapes.size()==0){
            shapes.add(new Shape(id, color, radius));
        }
        else{
            for(int i=0; i<shapes.size(); i++){
                if(area>shapes.get(i).area){
                    shapes.add(i, new Shape(id, color, radius));
                    return;
                }
                else if(i+1>=shapes.size()){
                    shapes.add(new Shape(id, color, radius));
                    return;
                }
            }
        }
    }
    public void add(String id, Color color, float width, float height){
        float area=height*width;
        if(shapes.size()==0){
            shapes.add(new Shape(id, color, width, height));
        }
        else{
            for(int i=0; i<shapes.size(); i++){
                if(area>shapes.get(i).area){
                    shapes.add(i, new Shape(id, color, width, height));
                    return;
                }
                else if(i+1>=shapes.size()){
                    shapes.add(new Shape(id, color, width, height));
                    return;
                }
            }
        }
    }
    public void scale(String id, float scaleFactor){
        Shape temp = null;
        for(int i=0; i<shapes.size(); i++){
            if(id.equals(shapes.get(i).getId())){
                temp=shapes.get(i);
                shapes.remove(i);
                break;
            }
        }
        if(temp!=null){
            temp.scale(scaleFactor);
            if(shapes.size()==0){
                shapes.add(temp);
            }
            else{
                for(int i=0; i<shapes.size(); i++){
                    if(temp.area>shapes.get(i).area){
                        shapes.add(i, temp);
                        return;
                    }
                    else if(i+1>=shapes.size()){
                        shapes.add(temp);
                        return;
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<shapes.size(); i++){
            sb.append(shapes.get(i).toString());
        }
        return sb.toString();
    }
}