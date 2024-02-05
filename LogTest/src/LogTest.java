import java.util.*;
import java.util.stream.Collectors;

public class LogTest{
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        LogCollector collector = new LogCollector();
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (line.startsWith("addLog")) {
                collector.addLog(line.replace("addLog ", ""));
            } else if (line.startsWith("printServicesBySeverity")) {
                collector.printServicesBySeverity();
            } else if (line.startsWith("getSeverityDistribution")) {
                String[] parts = line.split("\\s+");
                String service = parts[1];
                String microservice = null;
                if (parts.length == 3) {
                    microservice = parts[2];
                }
                collector.getSeverityDistribution(service, microservice).forEach((k,v)-> System.out.printf("%d -> %d%n", k,v));
            } else if (line.startsWith("displayLogs")){
                String[] parts = line.split("\\s+");
                String service = parts[1];
                String microservice = null;
                String order = null;
                if (parts.length == 4) {
                    microservice = parts[2];
                    order = parts[3];
                } else {
                    order = parts[2];
                }
                collector.displayLogs(service, microservice, order);
            }
        }
    }
}
abstract class Log{
    String message;
    int timestamp;
    String microservice;

    public Log(String log) {
        String[]parts = log.split(" ");
        this.message = Arrays.stream(parts).skip(3).limit(parts.length - 4).collect(Collectors.joining(" "));
        this.timestamp = Integer.parseInt(parts[parts.length - 1]);
        this.microservice = parts[1];
    }
    public abstract int getSeverity();

    public String getMessage() {
        return message;
    }

    public int getTimestamp() {
        return timestamp;
    }
}
class InfoLog extends Log{

    public InfoLog(String log) {
        super(log);
    }

    @Override
    public int getSeverity() {
        return 0;
    }

    @Override
    public String toString() {
        return String.format("[INFO] %s %d T:%d\n", message, timestamp, timestamp);
    }
}
class WarnLog extends Log{

    public WarnLog(String log) {
        super(log);
    }

    @Override
    public int getSeverity() {
        return 1 + (getMessage().contains("might cause error") ? 1 : 0);
    }
    @Override
    public String toString() {
        return String.format("[WARN] %s %d T:%d\n", message, timestamp, timestamp);
    }
}
class ErrorLog extends Log{

    public ErrorLog(String log) {
        super(log);
    }

    @Override
    public int getSeverity() {
        return 3 +
                (getMessage().contains("fatal") ? 2 : 0)+
                (getMessage().contains("exception") ? 3 : 0);
    }
    @Override
    public String toString() {
        return String.format("[ERROR] %s %d T:%d\n", message, timestamp, timestamp);
    }
}
class Microservice{
    String name;
    List<Log> logs;

    public Microservice(String name) {
        this.name = name;
        this.logs = new ArrayList<>();
    }
    public void addLog(String log){
        String[]parts = log.split(" ");

        if (Objects.equals(parts[2], "INFO")){
            logs.add(new InfoLog(log));
        }
        else if (Objects.equals(parts[2], "ERROR")){
            logs.add(new ErrorLog(log));
        }
        else if (Objects.equals(parts[2], "WARN")){
            logs.add(new WarnLog(log));
        }
    }
}
class Service{
    String name;
    Map<String, Microservice> microservices;

    public Service(String name) {
        this.name = name;
        microservices = new HashMap<>();
    }
    public void addLog(String log){
        String[]parts = log.split(" ");
        microservices.putIfAbsent(parts[1], new Microservice(parts[1]));
        microservices.get(parts[1]).addLog(log);
    }
    public double avgSeverity(){
        int sum = microservices.values().stream()
                .flatMap(x->x.logs.stream())
                .mapToInt(Log::getSeverity)
                .sum();
        int count = microservices.values().stream()
                .mapToInt(x->x.logs.size())
                .sum();
        return (double)sum / count;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Service name: %s", name));
        sb.append(String.format(" Count of microservices: %d", microservices.values().size()));
        sb.append(String.format(" Total logs in service: %d", microservices.values().stream().mapToInt(x->x.logs.size()).sum()));
        sb.append(String.format(" Average severity for all logs: %.2f", avgSeverity()));
        sb.append(String.format(" Average number of logs per microservice: %.2f\n", microservices.values().stream().mapToInt(x->x.logs.size()).average().orElse(0)));

        return sb.toString();
    }
}
class LogCollector {
    Map<String, Service> services;
    public LogCollector(){
        this.services = new HashMap<>();
    }
    public void addLog (String log){
        String[]parts = log.split(" ");
        services.putIfAbsent(parts[0], new Service(parts[0]));
        services.get(parts[0]).addLog(log);
    }
    public void printServicesBySeverity(){
        services.values()
                .stream()
                .sorted(Comparator.comparing(Service::avgSeverity).reversed())
                .forEach(System.out::print);
    }
    public Map<Integer, Integer> getSeverityDistribution (String service, String microservice){
        if (microservice == null){
            return services.get(service)
                    .microservices
                    .values()
                    .stream()
                    .flatMap(x->x.logs.stream())
                    .collect(Collectors.groupingBy(
                            Log::getSeverity,
                            TreeMap::new,
                            Collectors.summingInt(x->1)
                    ));
        }
        return services.get(service)
                .microservices
                .get(microservice)
                .logs
                .stream()
                .collect(Collectors.groupingBy(
                        Log::getSeverity,
                        TreeMap::new,
                        Collectors.summingInt(x->1)
                ));
    }
    public void displayLogs(String service, String microservice, String order){
        Comparator<Log> c;
        if (Objects.equals(order, "NEWEST_FIRST")){
            c = Comparator.comparing(Log::getTimestamp).reversed();
        }
        else if (Objects.equals(order, "OLDEST_FIRST")){
            c = Comparator.comparing(Log::getTimestamp);
        }
        else if (Objects.equals(order, "MOST_SEVERE_FIRST")){
            c = Comparator.comparing(Log::getSeverity).thenComparing(Log::getTimestamp).reversed();
        }
        else{
            c = Comparator.comparing(Log::getSeverity);
        }
        List<Log> sorted;
        if (microservice == null){
            sorted = services.get(service)
                    .microservices
                    .values()
                    .stream().flatMap(x->x.logs.stream())
                    .sorted(c)
                    .collect(Collectors.toList());
        }
        else {
            sorted = services.get(service)
                    .microservices.get(microservice)
                    .logs.stream()
                    .sorted(c)
                    .collect(Collectors.toList());
        }
        if (microservice == null){
            System.out.printf("displayLogs %s %s\n", service, order);
        }
        else {
            System.out.printf("displayLogs %s %s %s\n", service, microservice, order);
        }

        for (Log l : sorted){
            System.out.printf("%s|%s %s", service, l.microservice, l.toString());
        }
    }
}