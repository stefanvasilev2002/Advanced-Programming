import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

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
class DailyTemperatures{
    private List<Day> days;
    public DailyTemperatures(){

    }
    public void readTemperatures(InputStream in) {
        BufferedReader bf=new BufferedReader(new InputStreamReader(in));
        days=bf.lines().map(Day::new).collect(Collectors.toList());
    }

    public void writeDailyStats(OutputStream outputStream, char scale) {
        days=days.stream().sorted().collect(Collectors.toList());
        PrintWriter pw= new PrintWriter(new OutputStreamWriter(outputStream));
        if(scale=='C'){
            for(int i=0; i<days.size(); i++){
                Day temp=days.get(i);
                if(temp.getFormat()=='C'){
                    pw.println(String.format("%3d: Count:%4d Min:%7.2f%c Max:%7.2f%c Avg:%7.2f%c",
                            temp.getNumberOfDay(), temp.getNumberOfTemperatures(), temp.getMin(),scale, temp.getMax(),scale, temp.getAverage(),scale));
                }
                else {
                    pw.println(String.format("%3d: Count:%4d Min:%7.2f%c Max:%7.2f%c Avg:%7.2f%c",
                            temp.getNumberOfDay(), temp.getNumberOfTemperatures(), (temp.getMin()-32)*5/9,scale, (temp.getMax()-32)*5/9,scale, (temp.getAverage()-32)*5/9,scale));

                }

            }
        }
        else {
            for(int i=0; i<days.size(); i++){
                Day temp=days.get(i);
                if(temp.getFormat()=='F'){
                    pw.println(String.format("%3d: Count:%4d Min:%7.2f%c Max:%7.2f%c Avg:%7.2f%c",
                            temp.getNumberOfDay(), temp.getNumberOfTemperatures(), temp.getMin(),scale, temp.getMax(),scale, temp.getAverage(),scale));
                }
                else {
                    pw.println(String.format("%3d: Count:%4d Min:%7.2f%c Max:%7.2f%c Avg:%7.2f%c",
                            temp.getNumberOfDay(), temp.getNumberOfTemperatures(), (temp.getMin()*9)/5+32,scale, (temp.getMax()*9)/5+32,scale, (temp.getAverage()*9)/5+32,scale));

                }

            }
        }
        pw.flush();
    }
}
class Day implements Comparable<Day>{
    private int numberOfDay;
    private List<Temperature> temperatures;

    public Day(String t) {
        temperatures=new ArrayList<Temperature>();
        String[]parts=t.split(" ");
        numberOfDay=Integer.parseInt(parts[0]);
        for(int i=1; i<parts.length; i++){
            temperatures.add(new Temperature(parts[i]));
        }
    }
    public char getFormat(){
        return temperatures.get(0).getFormat();
    }

    public int getNumberOfDay() {
        return numberOfDay;
    }
    public int getNumberOfTemperatures() {
        return temperatures.size();
    }
    public float getMin() {
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < temperatures.size(); i++) {
            Temperature temp=temperatures.get(i);
            if(temp.getTemp()<min){
                min=temp.getTemp();
            }
        }
        return min;
    }
    public float getMax() {
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < temperatures.size(); i++) {
            Temperature temp=temperatures.get(i);
            if(temp.getTemp()>max){
                max=temp.getTemp();
            }
        }
        return max;
    }
    public float getAverage() {
        int avg=0;
        for (int i = 0; i < temperatures.size(); i++) {
            Temperature temp=temperatures.get(i);
            avg+=temp.getTemp();
        }
        return (float) avg/temperatures.size();
    }
    @Override
    public int compareTo(Day o) {
        return Integer.compare(numberOfDay, o.numberOfDay);
    }
}
class Temperature{
    private int temp;
    private char format;

    public Temperature(String part) {
        format=part.charAt(part.length()-1);
        part=part.substring(0,part.length()-1);
        temp=Integer.parseInt(part);
    }

    public char getFormat() {
        return format;
    }
    public int getTemp() {
        return temp;
    }
}