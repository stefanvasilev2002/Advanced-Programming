import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class F1Test {

    public static void main(String[] args) throws IOException {
        F1Race f1Race = new F1Race();
        f1Race.readResults(System.in);
        f1Race.printSorted(System.out);
    }

}
class Driver implements  Comparable<Driver>{
    String name;
    List<Integer> laps;

    public Driver(String part, String line) {
        this.name = part;
        this.laps = new ArrayList<>();
        String []parts = line.split(" ");

        laps.add(parseLap(parts[1]));
        laps.add(parseLap(parts[2]));
        laps.add(parseLap(parts[3]));
    }

    private Integer parseLap(String lap) {
        int milis = 0;
        String []parts = lap.split(":");
        milis += Integer.parseInt(parts[0]) * 60 * 1000;
        milis += Integer.parseInt(parts[1]) * 1000;
        milis += Integer.parseInt(parts[2]);
        return milis;
    }
    public int bestLap(){
        return laps.stream().sorted().collect(Collectors.toList()).get(0);
    }
    @Override
    public int compareTo(Driver o) {
        return Comparator.comparing(Driver::bestLap).compare(this, o);
    }

    @Override
    public String toString() {
        return String.format("%-10s  %-10s\n", name, getStringLap());
    }

    private String getStringLap() {
        int best = bestLap();
        StringBuilder sb = new StringBuilder();

        sb.append((int)best/1000/60).append(":");
        best -= (best/1000/60) * 1000 * 60;

        String sec = String.valueOf((int)best/1000);
        sec = sec.length() < 2 ? "0" + sec : sec;
        sb.append(sec).append(":");
        best -= (best/1000) * 1000;

        String mili = String.valueOf(best);
        mili = mili.length() == 2 ? "0" + mili : mili;
        mili = mili.length() == 1 ? "00" + mili : mili;
        sb.append(mili);

        return sb.toString();
    }
}
class F1Race {
    List<Driver> drivers;

    public F1Race() {
        this.drivers = new ArrayList<>();
    }

    public void readResults(InputStream in) {
        Scanner scanner = new Scanner(in);

        while (scanner.hasNextLine()){
            String line = scanner.nextLine();
            String []parts = line.split(" ");
            drivers.add(new Driver(parts[0], line));
        }
    }


    public void printSorted(PrintStream out) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(out);
        int i = 1;
        for (Driver driver : drivers.stream().sorted().collect(Collectors.toList())){
            writer.append(String.format("%d. ", i)).append(driver.toString());
            i++;
        }
        writer.flush();
    }
}