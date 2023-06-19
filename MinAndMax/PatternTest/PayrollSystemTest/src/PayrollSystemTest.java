import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class PayrollSystemTest {

    public static void main(String[] args) {

        Map<String, Double> hourlyRateByLevel = new LinkedHashMap<>();
        Map<String, Double> ticketRateByLevel = new LinkedHashMap<>();
        for (int i = 1; i <= 10; i++) {
            hourlyRateByLevel.put("level" + i, 10 + i * 2.2);
            ticketRateByLevel.put("level" + i, 5 + i * 2.5);
        }

        PayrollSystem payrollSystem = new PayrollSystem(hourlyRateByLevel, ticketRateByLevel);

        System.out.println("READING OF THE EMPLOYEES DATA");
        payrollSystem.readEmployees(System.in);

        System.out.println("PRINTING EMPLOYEES BY LEVEL");
        Set<String> levels = new LinkedHashSet<>();
        for (int i=5;i<=10;i++) {
            levels.add("level"+i);
        }
        Map<String, Set<Employee>> result = payrollSystem.printEmployeesByLevels(System.out, levels);
        result.forEach((level, employees) -> {
            System.out.println("LEVEL: "+ level);
            System.out.println("Employees: ");
            employees.forEach(System.out::println);
            System.out.println("------------");
        });


    }
}
abstract class Employee implements Comparable<Employee> {
    String id;
    String level;
    double sumHoursPoints;
    double moneyPerLevel;
    public abstract double calculateMoney();
    public String getLevel() {
        return level;
    }
    @Override
    public int compareTo(Employee o) {
        return Comparator
                .comparing(Employee::calculateMoney)
                .thenComparing(Employee::getLevel)
                .reversed()
                .compare(this, o);
    }
    @Override
    public String toString() {
        return String.format("Employee ID: %s Level: %s Salary: %.2f", id, level, calculateMoney());
    }
}
class HourlyEmployee extends Employee{
    public HourlyEmployee(String line, Map<String, Double> ticketRates) {
        String []parts = line.split(";");
        this.id = parts[1];
        this.level = parts[2];
        this.sumHoursPoints = Double.parseDouble(parts[3]);
        this.moneyPerLevel = ticketRates.get(level);
    }

    @Override
    public double calculateMoney() {
        if (sumHoursPoints > 40){
            return 40 * moneyPerLevel + (sumHoursPoints - 40) * moneyPerLevel * 1.5;
        }
        return  moneyPerLevel * sumHoursPoints;
    }
    @Override
    public String toString() {
        return super.toString() + String.format(" Regular hours: %.2f Overtime hours: %.2f", sumHoursPoints <= 40 ? sumHoursPoints : 40, sumHoursPoints <= 40 ? 0 : sumHoursPoints - 40);
    }
}
class FreelanceEmployee extends Employee{
    int count;
    public FreelanceEmployee(String line, Map<String, Double> ticketRates) {
        String []parts = line.split(";");
        this.id = parts[1];
        this.level = parts[2];
        this.count = (int) Arrays
                .stream(parts)
                .skip(3)
                .count();
        this.sumHoursPoints = Arrays
                .stream(parts)
                .skip(3)
                .mapToDouble(Double::parseDouble)
                .sum();
        this.moneyPerLevel = ticketRates.get(level);
    }

    @Override
    public double calculateMoney() {
        return moneyPerLevel * sumHoursPoints;
    }
    @Override
    public String toString() {
        return super.toString() + String.format(" Tickets count: %d Tickets points: %d", count, (int)sumHoursPoints);
    }
}
class PayrollSystem{
    List<Employee> employees;
    Map<String, Double> hourlyRates;
    Map<String, Double>ticketRates;
    PayrollSystem(Map<String,Double> hourlyRateByLevel, Map<String,Double> ticketRateByLevel){
        this.hourlyRates = hourlyRateByLevel;
        this.ticketRates = ticketRateByLevel;
        this.employees = new ArrayList<>();
    }
    public void readEmployees(InputStream is){
        Scanner scanner = new Scanner(is);

        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            String []parts = line.split(";");
            if(Objects.equals(parts[0], "F")){
                employees.add(new FreelanceEmployee(line, ticketRates));
            }
            else{
                employees.add(new HourlyEmployee(line, hourlyRates));
            }
        }
    }
    public Map<String, Set<Employee>> printEmployeesByLevels (OutputStream os, Set<String> levels){
        Map<String, Set<Employee>> grouped = employees
                .stream()
                .collect(Collectors.groupingBy(Employee::getLevel,
                        (Supplier<TreeMap<String, Set<Employee>>>)TreeMap::new,
                        Collectors.toCollection(TreeSet::new)));

        Set<String> keys = new HashSet<>(grouped.keySet());
        keys.stream().filter(x -> !levels.contains(x)).forEach(grouped::remove);
        return grouped;
    }
}