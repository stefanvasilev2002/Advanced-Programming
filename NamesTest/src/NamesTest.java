import java.util.*;
import java.util.stream.Collectors;

public class NamesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        Names names = new Names();
        for (int i = 0; i < n; ++i) {
            String name = scanner.nextLine();
            names.addName(name);
        }
        n = scanner.nextInt();
        System.out.printf("===== PRINT NAMES APPEARING AT LEAST %d TIMES =====\n", n);
        names.printN(n);
        System.out.println("===== FIND NAME =====");
        int len = scanner.nextInt();
        int index = scanner.nextInt();
        System.out.println(names.findName(len, index));
        scanner.close();

    }
}
class Name{
    String name;
    int frequency;

    public Name(String name) {
        this.name = name;
        this.frequency = 1;
    }
    public void incrementFrequency(){
        frequency++;
    }

    public String getName() {
        return name;
    }

    public int getFrequency() {
        return frequency;
    }

    @Override
    public String toString() {
        return String.format("%s (%d) %d", name, frequency, uniqueChars());
    }

    private int uniqueChars() {
        return (int) name.toLowerCase().chars().distinct().count();
    }
}
class Names{
    Map<String, Name> names;

    public Names() {
        this.names = new HashMap<>();
    }
    public void addName(String name){
        if(!names.containsKey(name)){
            names.put(name, new Name(name));
        }
        else {
            names.get(name).incrementFrequency();
        }
    }
    public void printN(int n){
        names
                .values()
                .stream()
                .filter(x->x.frequency >= n)
                .sorted(Comparator.comparing(Name::getName))
                .forEach(System.out::println);
    }
    public String findName(int len, int a){
        List<Name> sorted = names
                .values()
                .stream()
                .filter(x-> x.getName().length() < len)
                .sorted(Comparator.comparing(Name::getName))
                .collect(Collectors.toList());
        return sorted.get(a % sorted.size()).getName();
    }
}