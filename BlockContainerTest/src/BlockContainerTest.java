import java.util.*;

public class BlockContainerTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int size = scanner.nextInt();
        BlockContainer<Integer> integerBC = new BlockContainer<Integer>(size);
        scanner.nextLine();
        Integer lastInteger = null;
        for(int i = 0; i < n; ++i) {
            int element = scanner.nextInt();
            lastInteger = element;
            integerBC.add(element);
        }
        System.out.println("+++++ Integer Block Container +++++");
        System.out.println(integerBC);
        System.out.println("+++++ Removing element +++++");
        integerBC.remove(lastInteger);
        System.out.println("+++++ Sorting container +++++");
        integerBC.sort();
        System.out.println(integerBC);
        BlockContainer<String> stringBC = new BlockContainer<String>(size);
        String lastString = null;
        for(int i = 0; i < n; ++i) {
            String element = scanner.next();
            lastString = element;
            stringBC.add(element);
        }
        System.out.println("+++++ String Block Container +++++");
        System.out.println(stringBC);
        System.out.println("+++++ Removing element +++++");
        stringBC.remove(lastString);
        System.out.println("+++++ Sorting container +++++");
        stringBC.sort();
        System.out.println(stringBC);
    }
}
class BlockContainer<T extends Comparable<T>>{
    int maxElements;
    List<Set<T>> list;
    public BlockContainer(int n){
     this.maxElements = n;
     this.list = new ArrayList<Set<T>>();
    }
    public void add(T a){
        if (list.size() == 0){
            list.add(new TreeSet<T>());
            list.get(0).add(a);
        }
        else {
            if (list.get(list.size() - 1).size() == maxElements){
                list.add(new TreeSet<T>());
                list.get(list.size() - 1).add(a);
            }
            else {
                list.get(list.size() - 1).add(a);
            }
        }
    }
    public void remove(T a){
        list.get(list.size() - 1).remove(a);
        if (list.get(list.size() - 1).size() == 0){
            list.remove(list.size() - 1);
        }
    }
    public void sort(){
        ArrayList<T> all = new ArrayList<>();
        for(Set<T> t : list){
            all.addAll(t);
        }
        Collections.sort(all);
        list = new ArrayList<>();
        for (T element : all) {
            add(element);
        }
    }
    public String toString(){
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < list.size(); ++i) {
            sb.append(list.get(i).toString());
            if (i < list.size() - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }
}