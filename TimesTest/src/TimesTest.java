import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TimesTest {

    public static void main(String[] args) {
        TimeTable timeTable = new TimeTable();

            timeTable.readTimes(System.in);
        System.out.println("24 HOUR FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_24);
        System.out.println("AM/PM FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_AMPM);
    }
}
enum TimeFormat {
    FORMAT_24, FORMAT_AMPM
}
class TimeTable{
    private List<Time>times;

    public TimeTable() {
        times=new ArrayList<Time>();
    }
    public void addTime(String line){

    }
    public void readTimes(InputStream inputStream){
        BufferedReader bf=new BufferedReader(new InputStreamReader(inputStream));
        bf.lines().forEach(x-> Arrays.stream(x.split("\\s+")).forEach(a->{
            try {
                times.add(Time.addTime(a));
            }
        catch (UnsupportedFormatException e) {
            System.out.println("UnsupportedFormatException: " + e.getMessage());
        } catch (InvalidTimeException e) {
            System.out.println("InvalidTimeException: " + e.getMessage());
        }
    }));
    }
    public void writeTimes(PrintStream out, TimeFormat format) {
        PrintWriter pw = new PrintWriter(out);
        if(format==TimeFormat.FORMAT_24){
            times.stream().sorted().forEach(x -> pw.println(x));
        }else{
            times.stream().sorted().forEach(x -> pw.println(Time.convertToAMPM(x)));
        }
        pw.flush();
    }
}
class Time implements Comparable<Time>{
    private int hours;
    private int minutes;
    private TimeFormat timeFormat;
    private String postfix;
    public Time(int hours, int minutes, TimeFormat timeFormat) {
        this.hours = hours;
        this.minutes = minutes;
        this.timeFormat = timeFormat;
    }

    public Time(int hours, int minutes, TimeFormat timeFormat, String postfix) {
        this.hours = hours;
        this.minutes = minutes;
        this.timeFormat = timeFormat;
        this.postfix = postfix;
    }
    public static Time addTime(String tm) throws UnsupportedFormatException, InvalidTimeException{
        if(!tm.contains(":") && !tm.contains(".")) throw new UnsupportedFormatException(tm);
        String splitter = tm.contains(":") ? ":" : "\\.";
        int hours = Integer.parseInt(tm.split(splitter)[0]);
        int minutes = Integer.parseInt(tm.split(splitter)[1]);
        if(hours<0 || hours>23 || minutes<0 || minutes>60) throw new InvalidTimeException(tm);
        Time time = new Time(hours,minutes,TimeFormat.FORMAT_24);
        return time;
    }
    public static Time convertToAMPM(Time tm){
        if(tm.hours==0) return new Time(12,tm.minutes,TimeFormat.FORMAT_AMPM, "AM");
        if(tm.hours>0 && tm.hours<12) return new Time(tm.hours,tm.minutes,TimeFormat.FORMAT_AMPM,"AM");
        if(tm.hours==12) return new Time(tm.hours,tm.minutes,TimeFormat.FORMAT_AMPM,"PM");
        if(tm.hours>12) return new Time(tm.hours-12,tm.minutes,TimeFormat.FORMAT_AMPM,"PM");
        return null;
    }
    @Override
    public int compareTo(Time o) {
        if(this.hours>o.hours) return 1;
        if(o.hours>this.hours) return -1;
        if(o.minutes>this.minutes) return -1;
        if(this.minutes>o.minutes) return 1;
        return 0;
    }
    @Override
    public String toString() {
        if(timeFormat == TimeFormat.FORMAT_24){
            return String.format("%2d:%02d", hours, minutes);
        }
        else{
            return String.format("%2d:%02d %s", hours,minutes, postfix);
        }
    }
}
class UnsupportedFormatException extends Exception{
    public UnsupportedFormatException(String message) {
        super(message);
    }
}
class InvalidTimeException extends Exception{
    public InvalidTimeException(String message) {
        super(message);
    }
}