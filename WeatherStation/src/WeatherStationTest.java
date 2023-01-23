import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


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
            ws.addMeasurment(temp, wind, hum, vis, date);
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

class WeatherStation {
    int days;
    Set<Measurement> measurements;
    public WeatherStation(int n) {
        days=n;
        measurements= new TreeSet<>();
    }
    public void addMeasurment(float temperature, float wind, float humidity, float visibility, Date date){
       Measurement measurement=new Measurement(temperature, wind, humidity, visibility, date);
       long time=measurement.getDate().getTime();
       Iterator<Measurement> iterator=measurements.iterator();
       while(iterator.hasNext()){
           Measurement m=iterator.next();
           long d=time-m.getDate().getTime();
           if(d>days*24*60*60*1000){
               iterator.remove();
           }
       }
        measurements.add(measurement);
    }
    public int total(){
        return measurements.size();
    }
    public void status(Date from, Date to){
        Iterator<Measurement> iterator=measurements.iterator();
        float sumTemp=0;
        int n=0;

        while(iterator.hasNext()){
            Measurement m=iterator.next();
            if(m.getDate().compareTo(from)>=0 && m.getDate().compareTo(to)<=0){
                System.out.println(m);
                sumTemp+=m.getTemperature();
                n++;
            }
        }
        if(n==0){
            throw new RuntimeException();
        }
        System.out.printf("Average temperature: %.2f\n",sumTemp/n);
    }
}
class Measurement implements Comparable<Measurement> {
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

    public float getTemperature() {
        return temperature;
    }

    public float getWind() {
        return wind;
    }

    public float getHumidity() {
        return humidity;
    }

    public float getVisibility() {
        return visibility;
    }

    public Date getDate() {
        return date;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public void setWind(float wind) {
        this.wind = wind;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public void setVisibility(float visibility) {
        this.visibility = visibility;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public int compareTo(Measurement o) {
        long t1=this.date.getTime();
        long t2=o.getDate().getTime();
        if(Math.abs(t1-t2)<150*1000){
            return 0;
        }
        return this.date.compareTo(o.date);
    }

    @Override
    public String toString() {
        return String.format("%.1f %.1f km/h %.1f%% %.1f km %s", temperature,
                wind, humidity, visibility, date);
    }
}