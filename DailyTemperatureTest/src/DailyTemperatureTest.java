import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * I partial exam 2016
 */
public class DailyTemperatureTest {
    public static void main(String[] args) {
        DailyTemperatures dailyTemperatures = new DailyTemperatures();
        dailyTemperatures.readTemperatures(System.in);
        System.out.println("=== Daily temperatures in Celsius (C) ===");
        dailyTemperatures.writeDailyStats(System.out, 'C');
        System.out.println("=== Daily temperatures in Fahrenheit (F) ===");
        dailyTemperatures.writeDailyStats(System.out, 'F');
    }
}
class Day{
    List<Integer> temperatures;
    int day;
    String type;

    public Day(int day, String line) {
        this.day = day;
        temperatures = new ArrayList<>();
        String type = null;
        String []parts = line.split(" ");
        type = String.valueOf(parts[1].charAt(parts[1].length() - 1));
        List<String> tmp = Arrays.stream(parts).skip(1).collect(Collectors.toList());
        for(String p : tmp){
            temperatures.add(Integer.parseInt(p.substring(0, p.length() - 1)));
        }
        this.type = type;
    }

    public String toString(char scale) {
        temperatures = temperatures.stream().sorted().collect(Collectors.toList());
        if (scale == 'C' && Objects.equals(type, "F")){
            return String.format("%3d: Count: %3d Min: %6.2fC Max: %6.2fC Avg: %6.2fC", day, temperatures.size(), FToC(temperatures.get(0)), FToC(temperatures.get(temperatures.size()-1)), FToC(temperatures.stream().mapToInt(x->x).average().orElse(0)));
        }
        else if (scale == 'F' && Objects.equals(type, "C")){
            return String.format("%3d: Count: %3d Min: %6.2fF Max: %6.2fF Avg: %6.2fF", day, temperatures.size(), CToF(temperatures.get(0)), CToF(temperatures.get(temperatures.size()-1)), CToF(temperatures.stream().mapToInt(x->x).average().orElse(0)));
        }
        else if (scale == 'F' && Objects.equals(type, "F")){
            return String.format("%3d: Count: %3d Min: %6.2fF Max: %6.2fF Avg: %6.2fF", day, temperatures.size(), (double)temperatures.get(0), (double)temperatures.get(temperatures.size()-1), temperatures.stream().mapToInt(x->x).average().orElse(0));
        }
        else{
            return String.format("%3d: Count: %3d Min: %6.2fC Max: %6.2fC Avg: %6.2fC", day, temperatures.size(), (double)temperatures.get(0), (double)temperatures.get(temperatures.size()-1), temperatures.stream().mapToInt(x->x).average().orElse(0));
        }
    }

    public double FToC(double temp) {
        return (temp - 32) * 5 / 9;
    }
    public double CToF(double temp) {
        return temp*9/5 + 32;
    }
}
class DailyTemperatures {
    Map<Integer, Day> dayMap;

    public DailyTemperatures() {
        this.dayMap = new HashMap<>();
    }
    public void readTemperatures(InputStream inputStream){
        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNextLine()){
            String line = scanner.nextLine();
            String []parts = line.split(" ");
            int day = Integer.parseInt(parts[0]);

            dayMap.put(day, new Day(day, line));
        }

    }
    public void writeDailyStats(OutputStream outputStream, char scale){
        dayMap
                .values()
                .stream()
                .sorted(Comparator.comparingInt(x-> x.day))
                .forEach(x-> System.out.println(x.toString(scale)));
    }
}