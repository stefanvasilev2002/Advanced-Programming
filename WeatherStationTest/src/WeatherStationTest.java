import java.sql.SQLOutput;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
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

class WeatherStation{
    private int days;
    List<DataForMeasurement> datas;
    public WeatherStation(int days){
        this.days=days;
        datas=new ArrayList<DataForMeasurement>();
    }
    public void addMeasurment(float temperature, float wind, float humidity, float visibility, Date date){
        deleteOld(date);
        if(!isRecent(date)) datas.add(new DataForMeasurement(temperature,wind,humidity,visibility,date));
    }

    private void deleteOld(Date newDate) {
        long miliDays= (long) days *3600*24*1000;
        List<DataForMeasurement> nova=new ArrayList<DataForMeasurement>();
        for(int i=0; i<datas.size(); i++){
            long diff=newDate.getTime()-datas.get(i).getDate().getTime();
            if(diff<miliDays){
                nova.add(datas.get(i));
            }
        }
        datas=nova;
    }
    private boolean isRecent(Date newDate){
        if(datas.size()==0) return false;
        return newDate.getTime() - datas.get(datas.size()-1).getDate().getTime()<2.5*60*1000;
    }

    public int total() {
        return datas.size();
    }

    public void status(Date from, Date to) {
        datas.sort(Comparator.naturalOrder());
        float temp=0;
        int counter=0;
        for (int i=0; i< datas.size(); i++){
            DataForMeasurement m=datas.get(i);
            if(m.getDate().compareTo(from)!=-1 && m.getDate().compareTo(to)!=1){
                temp+=m.getTemperature();
                System.out.println(m.toString());
                counter++;
            }
        }
        if(counter==0){
            throw new RuntimeException();
        }
        System.out.printf("Average temperature: %.2f%n",temp/counter);
    }
}
class DataForMeasurement implements Comparable<DataForMeasurement>{
    private float temperature;
    private float wind;
    private float humidity;

    private float visibility;
    private Date date;

    public DataForMeasurement(float temperature, float wind, float humidity,  float visibility, Date date) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.wind = wind;
        this.visibility = visibility;
        this.date = date;
    }

    @Override
    public int compareTo(DataForMeasurement o) {
        return date.compareTo(o.date);
    }

    public Date getDate() {
        return date;
    }

    public float getTemperature() {
        return temperature;
    }

    @Override
    public String toString() {
        return temperature +" "+ wind + " km/h " +humidity + "% "+ visibility +" km "+ date.toString();
    }
}