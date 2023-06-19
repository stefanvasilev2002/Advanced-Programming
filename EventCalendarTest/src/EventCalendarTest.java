import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Calendar;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
class Event implements Comparable<Event> {
    String name;
    String location;
    Date date;

    public Event(String name, String location, Date date) {
        this.name = name;
        this.location = location;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public int compareTo(Event o) {
        return Comparator
                .comparing(Event::getDate)
                .thenComparing(Event::getName)
                .compare(this, o);
    }

    @Override
    public String toString() {
        DateFormat d = new SimpleDateFormat("dd MMM, yyy HH:mm");
        return String.format("%s at %s, %s\n", d.format(date), location, name);
    }
}
class EventCalendar{
    Map<Integer, Set<Event>> events;
    int year;
    public EventCalendar(int year) {
        this.year = year;
        this.events = new HashMap<>();
    }
    public void addEvent(String name, String location, Date date) throws WrongDateException {
        if(year != getYear(date)){
            throw new WrongDateException(date);
        }
        Event event = new Event(name, location, date);
        Set<Event> e = events.computeIfAbsent(getDay(date), v -> new TreeSet<>());
        e.add(event);
    }
    public void listEvents(Date date) {
        if(events.get(getDay(date)) == null){
            System.out.println("No events on this day!");
            return;
        }
        events
                .get(getDay(date))
                .stream()
                .forEach(System.out::print);
    }
    public void listByMonth() {
        Map<Integer, Integer> byMonth = events
                .values()
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(x->getMonth(x.getDate()) + 1,
                        TreeMap::new,
                        Collectors.summingInt(x-> 1)));
        IntStream.range(1,13).forEach(i->byMonth.putIfAbsent(i, 0));
        byMonth.entrySet().stream().forEach(x-> System.out.printf("%d : %d\n", x.getKey(), x.getValue()));
    }
    public int getYear(Date date){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.YEAR);
    }
    public int getDay(Date date){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_YEAR);
    }
    public int getMonth(Date date){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MONTH);
    }
}
class WrongDateException extends Exception{

    public WrongDateException(Date date) {
        super(String.format("Wrong date: %s", date));
    }
}