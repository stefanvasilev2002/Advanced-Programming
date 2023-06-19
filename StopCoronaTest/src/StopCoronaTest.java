import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

interface ILocation{
    double getLongitude();

    double getLatitude();

    LocalDateTime getTimestamp();
}

public class StopCoronaTest {

    public static double timeBetweenInSeconds(ILocation location1, ILocation location2) {
        return Math.abs(Duration.between(location1.getTimestamp(), location2.getTimestamp()).getSeconds());
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        StopCoronaApp stopCoronaApp = new StopCoronaApp();

        while (sc.hasNext()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");

            switch (parts[0]) {
                case "REG": //register
                    String name = parts[1];
                    String id = parts[2];
                    try {
                        stopCoronaApp.addUser(name, id);
                    } catch (UserAlreadyExistException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case "LOC": //add locations
                    id = parts[1];
                    List<ILocation> locations = new ArrayList<>();
                    for (int i = 2; i < parts.length; i += 3) {
                        locations.add(createLocationObject(parts[i], parts[i + 1], parts[i + 2]));
                    }
                    stopCoronaApp.addLocations(id, locations);

                    break;
                case "DET": //detect new cases
                    id = parts[1];
                    LocalDateTime timestamp = LocalDateTime.parse(parts[2]);
                    stopCoronaApp.detectNewCase(id, timestamp);

                    break;
                case "REP": //print report
                    stopCoronaApp.createReport();
                    break;
                default:
                    break;
            }
        }
    }

    private static ILocation createLocationObject(String lon, String lat, String timestamp) {
        return new ILocation() {
            @Override
            public double getLongitude() {
                return Double.parseDouble(lon);
            }

            @Override
            public double getLatitude() {
                return Double.parseDouble(lat);
            }

            @Override
            public LocalDateTime getTimestamp() {
                return LocalDateTime.parse(timestamp);
            }
        };
    }
}
class User implements Comparable<User>{
    String name;
    String id;
    List<ILocation> locations;
    boolean hasVirus;
    LocalDateTime virusDetected;

    public User(String id, String name) {
        this.id = id;
        this.name = name;
        this.locations = new ArrayList<>();
        hasVirus = false;
        virusDetected = null;
    }

    public void setLocations(List<ILocation> locations) {
        this.locations.addAll(locations);
    }

    public void setVirusDetected(LocalDateTime virusDetected) {
        this.virusDetected = virusDetected;
        hasVirus = true;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    @Override
    public int compareTo(User o) {
        return Comparator.comparing(User::getName).thenComparing(User::getId).compare(this, o);
    }
}
class StopCoronaApp {
    Map<String, User> users;
    public StopCoronaApp() {
        users = new HashMap<>();
    }
    public void addUser(String name, String id) throws UserAlreadyExistException {
        if (users.containsKey(id)){
            throw new UserAlreadyExistException(id);
        }
        users.put(id, new User(id, name));
    }
    public void addLocations (String id, List<ILocation> iLocations){
        users.get(id).setLocations(iLocations);
    }
    public void detectNewCase (String id, LocalDateTime timestamp){
        users.get(id).setVirusDetected(timestamp);
    }
    public Map<User, Integer> getDirectContacts (User u){
        return users.values()
                .stream()
                .filter(x->euclidDistanceAndTime(x, u))
                .collect(Collectors.groupingBy(x -> x,
                        TreeMap::new,
                        Collectors.summingInt(s-> 1)));
    }
    public Collection<User> getIndirectContacts (User u){
        Map<User, Integer> direct= getDirectContacts(u);

        List<User> indirect = new ArrayList<>();

        for (User directContact : direct.keySet()) {
            Map<User, Integer> directContactsOfDirectContact = getDirectContacts(directContact);
            indirect.addAll(directContactsOfDirectContact.keySet());
        }
        //indirect.removeAll(direct.keySet());
        return indirect
                .stream()
                .sorted(Comparator.comparing(User::getName).thenComparing(User::getId))
                .distinct()
                .collect(Collectors.toList());
    }
    public void createReport (){
        List<User> carriers = users.values().stream()
                .filter(x-> x.hasVirus)
                .sorted(Comparator.comparing(x-> x.virusDetected))
                .collect(Collectors.toList());
        int sumDirect = 0;
        int sumIndirect = 0;

        for(User user : carriers){
            System.out.printf("%s %5s %s\n", user.name, user.id, user.virusDetected);
            System.out.println("Direct contacts:");
            Map<User, Integer> direct = getDirectContacts(user);
            for(User directUser : direct.keySet()){
                System.out.printf("%s %5s %d\n", directUser.name, directUser.id, direct.get(directUser));
            }
            System.out.printf("Count of direct contacts: %d\n", direct.values().stream().mapToInt(x->x).sum());
            sumDirect += direct.values().stream().mapToInt(x->x).sum();

            List<User> indirect = (List<User>) getIndirectContacts(user);
            System.out.println("Indirect contacts:");
            for(User indirectUser : indirect){
                System.out.printf("%s %s***\n", indirectUser.name, indirectUser.id.substring(0, 4));
            }
            System.out.printf("Count of indirect contacts: %d\n", indirect.size());
            sumIndirect += indirect.size();
        }
        System.out.printf("Average direct contacts: %f\n", (double)sumDirect / carriers.size());
        System.out.printf("Average indirect contacts: %f\n", (double)sumIndirect / carriers.size());

    }
    public boolean euclidDistanceAndTime(User x, User u) {
        for(ILocation u1 : u.locations){
            for(ILocation u2 : x.locations){
                if(Math.sqrt(Math.pow(u1.getLatitude() - u2.getLatitude(), 2)
                        + Math.pow(u1.getLongitude() - u2.getLongitude(), 2)) <= 2 && Math.abs(Duration.between(u1.getTimestamp(), u2.getTimestamp()).toMinutes())< 5){
                    return true;
                }
            }
        }
        return false;
    }
}
class UserAlreadyExistException extends Exception{
    public UserAlreadyExistException(String id) {
        super(String.format("User with id %s already exists", id));
    }
}