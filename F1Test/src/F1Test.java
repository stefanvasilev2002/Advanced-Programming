import java.io.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class F1Test {

    public static void main(String[] args) throws IOException {
        F1Race f1Race = new F1Race();
        f1Race.readResults(System.in);
        f1Race.printSorted(System.out);
    }

}
class F1Race {
    private List<Pilot> pilots;

    public F1Race() {

    }

    public void readResults(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader((new InputStreamReader(in)));
        pilots = reader.lines().map(x->new Pilot(x)).collect(Collectors.toList());
        reader.close();
    }

    public void printSorted(OutputStream out) {
        PrintWriter writer = new PrintWriter(new PrintStream(out));
        sort();

        for (int i = 0; i < pilots.size(); i++) {
            writer.println((i + 1) + ". " + pilots.get(i));
        }
        writer.close();
    }

    public void sort() {
        pilots.sort(Comparator.naturalOrder());
    }
}
class Pilot implements Comparable<Pilot>{
    private String name;
    private String time;

    public Pilot(String input) {
        String[] parts=input.split("\\s+");
        name=parts[0];
        int bestTime=Integer.MAX_VALUE, bestIndex=-1;
        for(int i=1; i<parts.length; i++){
            int temp=getTime(parts[i]);
            if(temp<bestTime){
                bestTime=temp;
                bestIndex=i;
            }
        }
        time=parts[bestIndex];
    }
    public int getTime(String a){
        String[]parts=a.split(":");
        return Integer.parseInt(parts[0])*60000+Integer.parseInt(parts[1])*1000+Integer.parseInt(parts[2]);
    }

    @Override
    public String toString() {
        return String.format("%-10s%10s", name, time);
    }

    @Override
    public int compareTo(Pilot o) {
        return Integer.compare(getTime(time), o.getTime(o.time));
    }
}