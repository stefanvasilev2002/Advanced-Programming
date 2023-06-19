import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class SuperStringTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (  k == 0 ) {
            SuperString s = new SuperString();
            while ( true ) {
                int command = jin.nextInt();
                if ( command == 0 ) {//append(String s)
                    s.append(jin.next());
                }
                if ( command == 1 ) {//insert(String s)
                    s.insert(jin.next());
                }
                if ( command == 2 ) {//contains(String s)
                    System.out.println(s.contains(jin.next()));
                }
                if ( command == 3 ) {//reverse()
                    s.reverse();
                }
                if ( command == 4 ) {//toString()
                    System.out.println(s);
                }
                if ( command == 5 ) {//removeLast(int k)
                    s.removeLast(jin.nextInt());
                }
                if ( command == 6 ) {//end
                    break;
                }
            }
        }
    }

}
class SuperString{
    List<String> list;
    List<Integer> lastAdded;
    public SuperString(){
        this.list = new LinkedList<>();
        this.lastAdded = new LinkedList<>();
    }
    public void append(String s){
        list.add(s);
        lastAdded.add(0, 1);
    }
    public void insert(String s){
        list.add(0, s);
        lastAdded.add(0, -1);
    }
    public boolean contains(String s){
        StringBuilder a = new StringBuilder();
        for (String tmp : list){
            a.append(tmp);
        }
        return a.toString().contains(s);
    }
    public void reverse(){
        LinkedList<String> tmp = new LinkedList<>();
        for (int i = list.size() - 1; i>=0; i--){
            tmp.add(reverseString(list.get(i)));
        }
        list = tmp;
    }

    private String reverseString(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = s.length() - 1; i>=0; i--){
            sb.append(s.charAt(i));
        }
        return sb.toString();
    }

    public void removeLast(int k){
        for ( int i = 0 ; i < k ; ++i )
            if ( lastAdded.get(0) < 0 ){
                lastAdded.remove(0);
                list.remove(0);
            }
            else{
                lastAdded.remove(lastAdded.size() - 1);
                list.remove(list.size() - 1);
            }
    }

    @Override
    public String toString() {
        StringBuilder a = new StringBuilder();
        for (String tmp : list){
            a.append(tmp);
        }
        return a.toString();
    }
}
