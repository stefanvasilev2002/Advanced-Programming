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
class Names{
    Map<String,Name> names;

    public Names() {
        names=new TreeMap<>();
    }

    public void addName(String name){
        if(names.containsKey(name)){
            names.get(name).show++;
        }
        else {
            names.put(name, new Name(name));
        }
    }
    public void printN(int n){
        List<Name> list=names.values().stream().collect(Collectors.toList());
        for(int i=0; i< names.size(); i++){
            if(list.get(i).show>=n){
                System.out.print(list.get(i));
            }
        }
    }
    public String findName(int len, int x){
        List<Name> list=names.values().stream().collect(Collectors.toList());
        List<String> printed=new ArrayList<>();
        for(int i=0; i< names.size(); i++){
            if(list.get(i).name.length()<len){
                printed.add(list.get(i).name);
            }
        }
        return printed.get((x%printed.size()));
    }
}
class Name implements Comparable<Name>{
    int show;
    String name;
    int uniqueLetters;

    public Name(String name) {
        this.name = name;
        show=1;
        uniqueLetters=unique();
    }
    int unique(){
        int counter=0;
        char []a=name.toLowerCase().toCharArray();
        Set<Character> b=new HashSet<>();
        for(int i=0; i<a.length; i++){
            b.add(a[i]);
        }
        return b.size();
    }

    @Override
    public int compareTo(Name o) {
        return name.compareTo(o.name);
    }

    @Override
    public String toString() {
        return String.format("%s (%d) %d\n", name, show, uniqueLetters);
    }
}