import java.util.*;

public class AuditionTest2{
    public static void main(String[] args) {
        Audition2 audition = new Audition2();
        List<String> cities = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            if (parts.length > 1) {
                audition.addParticipant(parts[0], parts[1], parts[2],
                        Integer.parseInt(parts[3]));
            } else {
                cities.add(line);
            }
        }
        for (String city : cities) {
            System.out.printf("+++++ %s +++++\n", city);
            audition.listByCity(city);
        }
        scanner.close();
    }
}
class Candidate{
    private String code;
    private String name;
    private int age;

    public Candidate(String code, String name, int age) {
        this.code = code;
        this.name = name;
        this.age = age;
    }
    public String getCode() {
        return code;
    }
    public String getName() {
        return name;
    }
    public int getAge() {
        return age;
    }
}
class Audition2{
    Map<String, List<Candidate>> candidates;

    public Audition2() {
        this.candidates = new HashMap<>();
    }
    void addParticipant(String city, String code, String name, int age){
        List<Candidate> currCity = candidates.get(city);
        if(currCity == null){
            currCity = new ArrayList<>();
        }

        for(Candidate c : currCity){
            if(c.getCode().equals(code)){
                return;
            }
        }

        currCity.add(new Candidate(code, name, age));
        candidates.put(city, currCity);
    }
    void listByCity(String city){
        List<Candidate> currCity = candidates.get(city);
        currCity.stream()
                .sorted(Comparator
                        .comparing(Candidate::getName)
                        .thenComparing(Candidate::getAge)
                        .thenComparing(Candidate::getCode))
                .forEach(x -> System.out.printf("%s %s %d\n", x.getCode(), x.getName(), x.getAge()));
    }
}