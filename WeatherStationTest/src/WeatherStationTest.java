import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;

public class WeatherStationTest {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        int n = scanner.nextInt();
        scanner.nextLine();
        WeatherStation ws = new WeatherStation(n);
        while (true) {
            String line = scanner.nextLine();
            if (line.equals("=====")) {
                break;
            }
            String[] parts = line.split(" ");
            float temp = Float.parseFloat(parts[0]);
            float wind = Float.parseFloat(parts[1]);
            float hum = Float.parseFloat(parts[2]);
            float vis = Float.parseFloat(parts[3]);
            line = scanner.nextLine();
            Date date = df.parse(line);
            ws.addMeasurement(temp, wind, hum, vis, date);
        }
        String line = scanner.nextLine();
        Date from = df.parse(line);
        line = scanner.nextLine();
        Date to = df.parse(line);
        scanner.close();
        System.out.println(ws.total());
        try {
            ws.status(from, to);
        } catch (RuntimeException e) {
            System.out.println(e);
        }
    }
}
class Measurement{
    float temperature;
    float wind;
    float humidity;
    float visibility;
    Date date;

    public Measurement(float temperature, float wind, float humidity, float visibility, Date date) {
        this.temperature = temperature;
        this.wind = wind;
        this.humidity = humidity;
        this.visibility = visibility;
        this.date = date;
    }

    @Override
    public String toString() {
        return String.format("%.1f %.1f km/h %.1f%% %.1f km %s",
                temperature, wind, humidity, visibility, date.toString().replace("UTC", "GMT"));
    }

    public Date getDate() {
        return date;
    }
}
class WeatherStation{
    int days;
    List<Measurement> measurements;
    public WeatherStation(int days) {
        this.days = days;
        this.measurements = new ArrayList<>();
    }
    public boolean checkTwoMinutes(Measurement m){
        for (Measurement tmp : measurements){
            if(m.date.getTime() - tmp.date.getTime() <= 150*1000 ){
                return false;
            }
        }
        return true;
    }
    public boolean checkXDays(Measurement x, Measurement m){
        return m.date.getTime() - (long) days * 24 * 60 * 60*1000 <= x.date.getTime();
    }
    public void addMeasurement(float temperature, float wind, float humidity, float visibility, Date date){
        Measurement m = new Measurement(temperature, wind, humidity, visibility, date);
        if (checkTwoMinutes(m)){
            measurements.add(m);
            measurements = measurements
                    .stream()
                    .filter(x-> checkXDays(x, m))
                    .collect(Collectors.toList());
        }
    }
    public int total(){
        return measurements.size();
    }
    public void status(Date from, Date to){
        List<Measurement> tmp = measurements
                .stream()
                .filter(x-> x.date.getTime() >= from.getTime() && x.date.getTime() <= to.getTime())
                .sorted(Comparator
                        .comparing(Measurement::getDate))
                .collect(Collectors.toList());
        if (tmp.size() == 0){
            throw new RuntimeException();
        }
        tmp.stream().forEach(System.out::println);
        System.out.printf("Average temperature: %.2f\n", tmp.stream().mapToDouble(x->x.temperature).average().orElse(0));
    }
}