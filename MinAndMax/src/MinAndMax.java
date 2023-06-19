import java.util.Scanner;

public class MinAndMax {
    public static void main(String[] args) throws ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        MinMax<String> strings = new MinMax<String>();
        for(int i = 0; i < n; ++i) {
            String s = scanner.next();
            strings.update(s);
        }
        System.out.println(strings);
        System.out.println();
        MinMax<Integer> ints = new MinMax<Integer>();
        for(int i = 0; i < n; ++i) {
            int x = scanner.nextInt();
            ints.update(x);
        }
        System.out.println(ints);
    }
}
class MinMax<T extends Comparable<T>>{
    private T minimum;
    private T maximum;
    private int processed;
    private int minCount;
    private int maxCount;
    public MinMax() {
        minimum=null;
        maximum=null;
        processed=0;
        minCount=0;
        maxCount=0;
    }
    public void update(T element){
        if(minimum==null){
            minimum=element;
            maximum=element;
        }
        if(element.compareTo(minimum)==0){
            minCount++;
        }
        if(element.compareTo(maximum)==0){
            maxCount++;
        }
        if(element.compareTo(maximum)>0){
            maximum=element;
            maxCount=1;
        }
        else if(element.compareTo(minimum)<0){
            minimum=element;
            minCount=1;
        }

        processed++;
    }

    public T min() {
        return minimum;
    }

    public T max() {
        return maximum;
    }

    @Override
    public String toString() {
        return minimum + " " + maximum + " " +(processed-minCount-maxCount);
    }
}