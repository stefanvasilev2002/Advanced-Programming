import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
public class EventCalendarTest {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        int year = scanner.nextInt();
        scanner.nextLine();
        EventCalendar eventCalendar = new EventCalendar(year);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            String name = parts[0];
            String location = parts[1];
            Date date = df.parse(parts[2]);
            try {
                eventCalendar.addEvent(name, location, date);
            } catch (WrongDateException e) {
                System.out.println(e.getMessage());
            }
        }
        Date date = df.parse(scanner.nextLine());
        eventCalendar.listEvents(date);
        eventCalendar.listByMonth();
    }
}
class EventCalendar{
    int year;
    Map<Integer, Set<Event>> events;
    Map<Integer, Integer> months;
    public EventCalendar(int year) {
        this.year=year;
        events=new HashMap<>();
        months=new HashMap<>();
    }
    public void addEvent(String name, String location, Date date) throws WrongDateException {
        if(year!=getYear(date)){
            throw new WrongDateException(date);
        }
        Event event=new Event(name,location,date);
        int day=getDayOfYear(date);
        Set<Event> set=events.get(day);
        if(set==null){
            set=new TreeSet<Event>();
        }
        set.add(event);
        int monthKey=getMonth(date);
        Integer count=months.get(monthKey);
        if(count==null){
            count=0;
        }
        ++count;
        months.put(monthKey,count);
        events.put(day,set);
    }
    public void listEvents(Date date){
        int day=getDayOfYear(date);
        Set<Event> set=events.get(day);
        if(set!=null){
            for(Event e : set){
                System.out.println(e);
            }
        }
        else {
            System.out.println("No events on this day!");
        }
    }
    public void listByMonth(){
        for(int i=0; i<12; i++){
            System.out.printf("%d : %d\n", i + 1, months.get(i) == null ? 0
                    : months.get(i));
        }
    }
    static int getDayOfYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_YEAR);
    }
    static int getMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH);
    }
    static int getYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }
}
class Event implements Comparable<Event> {
    String name;
    String location;
    Date date;

    public Event(String name, String location, Date date) {
        this.name = name;
        this.location = location;
        this.date = date;
    }

    @Override
    public int compareTo(Event o) {
        int a=date.compareTo(o.date);
        if(a==0){
            return name.compareTo(o.name);
        }
        return a;
    }
    @Override
    public String toString() {
        DateFormat df = new SimpleDateFormat("dd MMM, YYY HH:mm");
        return String.format("%s at %s, %s", df.format(date), location, name);
    }
}
class WrongDateException extends Exception{
    public WrongDateException(Date date) {
        super(String.format("Wrong date: %s", date).replace("GMT","UTC"));
    }
}