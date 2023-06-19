import java.io.OutputStream;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class PayrollSystemTest2 {

    public static void main(String[] args) {

        Map<String, Double> hourlyRateByLevel = new LinkedHashMap<>();
        Map<String, Double> ticketRateByLevel = new LinkedHashMap<>();
        for (int i = 1; i <= 10; i++) {
            hourlyRateByLevel.put("level" + i, 11 + i * 2.2);
            ticketRateByLevel.put("level" + i, 5.5 + i * 2.5);
        }

        Scanner sc = new Scanner(System.in);

        int employeesCount = Integer.parseInt(sc.nextLine());

        PayrollSystem ps = new PayrollSystem(hourlyRateByLevel, ticketRateByLevel);
        Employee emp = null;
        for (int i = 0; i < employeesCount; i++) {
            try {
                emp = ps.createEmployee(sc.nextLine());
            } catch (BonusNotAllowedException e) {
                System.out.println(e.getMessage());
            }
        }

        int testCase = Integer.parseInt(sc.nextLine());

        switch (testCase) {
            case 1: //Testing createEmployee
                if (emp != null)
                    System.out.println(emp);
                break;
            case 2: //Testing getOvertimeSalaryForLevels()
                ps.getOvertimeSalaryForLevels().forEach((level, overtimeSalary) -> {
                    System.out.printf("Level: %s Overtime salary: %.2f\n", level, overtimeSalary);
                });
                break;
            case 3: //Testing printStatisticsForOvertimeSalary()
                ps.printStatisticsForOvertimeSalary();
                break;
            case 4: //Testing ticketsDoneByLevel
                ps.ticketsDoneByLevel().forEach((level, overtimeSalary) -> {
                    System.out.printf("Level: %s Tickets by level: %d\n", level, overtimeSalary);
                });
                break;
            case 5: //Testing getFirstNEmployeesByBonus (int n)
                ps.getFirstNEmployeesByBonus(Integer.parseInt(sc.nextLine())).forEach(System.out::println);
                break;
        }

    }
}
abstract class Employee implements Comparable<Employee> {
    String id;
    String level;
    double sumHoursPoints;
    double moneyPerLevel;
    float bonus;
    boolean isFixedBonus;
    int count;
    public abstract double calculateMoney();
    public abstract double overtimeMoney();
    public double getBonusMoney(){
        return isFixedBonus ? bonus : calculateMoney() * bonus / 10;

    }
    public int getSumHoursPointsInt() {
        return (int)sumHoursPoints;
    }

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
        return String.format("Employee ID: %s Level: %s Salary: %.2f", id, level, calculateMoney() + getBonusMoney());
    }

    public int getCount() {
        return count;
    }

    public abstract double getOvertime();
}
class HourlyEmployee extends Employee{
    public HourlyEmployee(String line, Map<String, Double> ticketRates) {
        String []parts = line.split(";");
        this.id = parts[1];
        this.level = parts[2];
        this.sumHoursPoints = Double.parseDouble(parts[3].split(" ")[0]);
        this.moneyPerLevel = ticketRates.get(level);

        String []tmp1 = parts[parts.length - 1].split(" ");
        String bonus = tmp1.length > 1 ? tmp1[1] : "0";
        float a;
        if(bonus.endsWith("%")){
            a = bonus.length() > 1 ? Float.parseFloat(bonus.substring(0, bonus.length() - 2)) : 0;
            this.isFixedBonus = false;
        }
        else {
            a = bonus.length() > 1 ? Float.parseFloat(bonus.substring(0, bonus.length() - 1)) : 0;
            this.isFixedBonus = true;
        }

        this.count = 0;
        this.bonus = a;
    }

    @Override
    public double calculateMoney() {
        if (sumHoursPoints > 40){
            return 40 * moneyPerLevel + (sumHoursPoints - 40) * moneyPerLevel * 1.5;
        }
        return  moneyPerLevel * sumHoursPoints;
    }

    @Override
    public double overtimeMoney() {
        if (sumHoursPoints > 40){
            return (sumHoursPoints - 40) * moneyPerLevel * 1.5;
        }
        return  0;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" Regular hours: %.2f Overtime hours: %.2f Bonus: %.2f", sumHoursPoints <= 40 ? sumHoursPoints : 40, sumHoursPoints <= 40 ? 0 : sumHoursPoints - 40, getBonusMoney());
    }

    @Override
    public double getOvertime() {
        if (sumHoursPoints <= 40){
            return 0;
        }
        return (sumHoursPoints - 40) * moneyPerLevel * 1.5;
    }
}
class FreelanceEmployee extends Employee{
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
                .limit(parts.length - 4)
                .mapToDouble(Double::parseDouble)
                .sum();
        this.sumHoursPoints += Double.parseDouble(parts[parts.length -1].split(" ")[0]);
        this.moneyPerLevel = ticketRates.get(level);

        String []tmp1 = parts[parts.length - 1].split(" ");
        String bonus = tmp1.length > 1 ? tmp1[1] : "0";
        float a;
        if(bonus.endsWith("%")){
            a = bonus.length() > 1 ? Float.parseFloat(bonus.substring(0, bonus.length() - 2)) : 0;
            this.isFixedBonus = false;
        }
        else {
            a = bonus.length() > 1 ? Float.parseFloat(bonus.substring(0, bonus.length() - 1)) : 0;
            this.isFixedBonus = true;
        }

        this.bonus = a;
    }

    @Override
    public double calculateMoney() {
        return moneyPerLevel * sumHoursPoints;
    }
    @Override
    public double overtimeMoney() {
        return 0;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" Tickets count: %d Tickets points: %d Bonus: %.2f", count, (int)sumHoursPoints, getBonusMoney());
    }

    public int getCount() {
        return count;
    }

    @Override
    public double getOvertime() {
        return 0;
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
    public Employee createEmployee (String line) throws BonusNotAllowedException {
        String []parts = line.split(";");
        Employee tmp = null;

        String []tmp1 = parts[parts.length - 1].split(" ");
        String bonus = tmp1.length > 1 ? tmp1[1] : "0";
        if(bonus.endsWith("%")){
            float a = bonus.length() > 1 ? Float.parseFloat(bonus.substring(0, bonus.length() - 2)) : 0;
            if(a > 20){
                throw new BonusNotAllowedException(a);
            }
        }
        else {
            float a = bonus.length() > 1 ? Float.parseFloat(bonus.substring(0, bonus.length() - 1)) : 0;
            if (a > 1000){
                throw new BonusNotAllowedException(a);
            }
        }

        if(Objects.equals(parts[0], "F")){
            tmp = new FreelanceEmployee(line, ticketRates);
            employees.add(tmp);
        }
        else{
            tmp = new HourlyEmployee(line, hourlyRates);
            employees.add(tmp);
        }
        return tmp;
    }
    public Map<String, Double> getOvertimeSalaryForLevels (){
        return employees
                .stream()
                .filter(x->x instanceof HourlyEmployee)
                .collect(Collectors.groupingBy(x -> x.level,
                        Collectors.summingDouble(Employee::getOvertime)));
    }
    public void printStatisticsForOvertimeSalary (){

    }
    public Map<String, Integer> ticketsDoneByLevel(){
        return employees
                .stream()
                .filter(x -> x instanceof FreelanceEmployee)
                .collect(Collectors.groupingBy(x -> x.level,
                        Collectors.summingInt(Employee::getCount)));
    }
    public Collection<Employee> getFirstNEmployeesByBonus (int n){
        return employees
                .stream()
                .sorted(Comparator.comparing(Employee::getBonusMoney).reversed())
                .limit(n)
                .collect(Collectors.toList());
    }
}
class BonusNotAllowedException extends Exception{
    float b;
    public BonusNotAllowedException(float bonus) {
        b = bonus;
    }

    @Override
    public String getMessage() {
        return String.format("Bonus of %.0f$$ is not allowed", b);
    }
}