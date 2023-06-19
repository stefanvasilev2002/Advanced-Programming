
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class TimesTest {

    public static void main(String[] args) throws IOException {
        TimeTable timeTable = new TimeTable();
        try {
            timeTable.readTimes(System.in);
        } catch (UnsupportedFormatException e) {
            System.out.println("UnsupportedFormatException: " + e.getMessage());
        } catch (InvalidTimeException e) {
            System.out.println("InvalidTimeException: " + e.getMessage());
        }
        System.out.println("24 HOUR FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_24);
        System.out.println("AM/PM FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_AMPM);
    }

}
class Time{
    int hour;
    int minutes;

    public Time(String s) throws UnsupportedFormatException, InvalidTimeException {

        if (s.split(":").length > 1){
            hour = Integer.parseInt(s.split(":")[0]);
            minutes = Integer.parseInt(s.split(":")[1]);
        }
        else if (s.split("\\.").length > 1) {
            hour = Integer.parseInt(s.split("\\.")[0]);
            minutes = Integer.parseInt(s.split("\\.")[1]);
        }
        else {
            throw new UnsupportedFormatException(s);
        }
        if(hour < 0 || hour > 23){
            throw new InvalidTimeException(s);
        }
        if (minutes < 0 || minutes > 59){
            throw new InvalidTimeException(s);
        }
    }

    public int getHour() {
        return hour;
    }

    public int getMinutes() {
        return minutes;
    }

    public String toString(TimeFormat format) {
        if (format == TimeFormat.FORMAT_24){
            return String.format("%2d:%2s\n", hour, minutes < 10 ? "0" + minutes : minutes);
        }
        else return String.format("%s", getAMPMFormat());
    }

    public String getAMPMFormat() {

        if(hour == 0){
            return String.format("%2d:%2s AM\n", hour + 12, minutes < 10 ? "0" + minutes : minutes);
        }
        else if (hour == 12) {
            return String.format("%2d:%2s PM\n", hour, minutes < 10 ? "0" + minutes : minutes);
        }
        else if (hour >= 13 && hour <= 23) {
            return String.format("%2d:%2s PM\n", hour - 12, minutes < 10 ? "0" + minutes : minutes);
        }
        else {
            return String.format("%2d:%2s AM\n", hour, minutes < 10 ? "0" + minutes : minutes);
        }
    }
}
class TimeTable{
    List<Time> times;
    public TimeTable(){
        times = new ArrayList<>();
    }
    public void readTimes(InputStream inputStream) throws UnsupportedFormatException, InvalidTimeException {
        Scanner scanner = new Scanner(inputStream);

        while (scanner.hasNextLine()){
            String[]parts = scanner.nextLine().split(" ");
            for(String s : parts){
                times.add(new Time(s));
            }
        }
    }
    public void writeTimes(OutputStream outputStream, TimeFormat format) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(outputStream);

            times
                    .stream()
                    .sorted(Comparator.comparing(Time::getHour).thenComparing(Time::getMinutes))
                    .forEach(x-> {
                        try {
                            writer.append(x.toString(format));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        writer.flush();
    }
}
enum TimeFormat {
    FORMAT_24, FORMAT_AMPM
}
class UnsupportedFormatException extends Exception{
    public UnsupportedFormatException(String s) {
        super(String.format("%s", s));
    }
}
class InvalidTimeException extends Exception{
    public InvalidTimeException(String s) {
        super(String.format("%s", s));
    }
}