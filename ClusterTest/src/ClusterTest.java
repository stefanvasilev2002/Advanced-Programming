import java.util.*;
import java.util.stream.Collectors;

/**
 * January 2016 Exam problem 2
 */
public class ClusterTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Cluster<Point2D> cluster = new Cluster<>();
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            long id = Long.parseLong(parts[0]);
            float x = Float.parseFloat(parts[1]);
            float y = Float.parseFloat(parts[2]);
            cluster.addItem(new Point2D(id, x, y));
        }
        int id = scanner.nextInt();
        int top = scanner.nextInt();
        cluster.near(id, top);
        scanner.close();
    }
}
interface Clusterable<T>{
    double euclidDistance(T t);
    Long getID();
}
class Cluster<T extends Clusterable<T>>{
    Map<Long, T>list;

    public Cluster() {
        this.list = new HashMap<>();
    }

    public void addItem(T element){
        list.put(element.getID(), element);
    }
    public void near(long id, int top){
        T tmp = list.get(id);
        List<T> sorted = list.values().stream()
                .sorted(Comparator.comparing(tmp::euclidDistance))
                .skip(1)
                .limit(top)
                .collect(Collectors.toList());
        for (int i = 0; i < sorted.size(); i++){
            System.out.printf("%d. %d -> %.3f\n", i + 1, sorted.get(i).getID(), tmp.euclidDistance(sorted.get(i)));
        }
    }
}
class Point2D implements Clusterable<Point2D>{
    long id;
    float x;
    float y;

    public Point2D(long id, float x, float y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }
    @Override
    public double euclidDistance(Point2D point2){
        return Math.sqrt(Math.pow(x - point2.x, 2) + Math.pow(y - point2.y, 2));
    }

    @Override
    public Long getID() {
        return id;
    }
}