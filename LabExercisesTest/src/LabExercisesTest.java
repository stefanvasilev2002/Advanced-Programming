import java.util.*;
import java.util.stream.Collectors;

public class LabExercisesTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        LabExercises labExercises = new LabExercises();
        while (sc.hasNextLine()) {
            String input = sc.nextLine();
            String[] parts = input.split("\\s+");
            String index = parts[0];
            List<Integer> points = Arrays.stream(parts).skip(1)
                    .mapToInt(Integer::parseInt)
                    .boxed()
                    .collect(Collectors.toList());

            labExercises.addStudent(new Student(index, points));
        }

        System.out.println("===printByAveragePoints (ascending)===");
        labExercises.printByAveragePoints(true, 100);
        System.out.println("===printByAveragePoints (descending)===");
        labExercises.printByAveragePoints(false, 100);
        System.out.println("===failed students===");
        labExercises.failedStudents().forEach(System.out::println);
        System.out.println("===statistics by year");
        labExercises.getStatisticsByYear().entrySet().stream()
                .map(entry -> String.format("%d : %.2f", entry.getKey(), entry.getValue()))
                .forEach(System.out::println);

    }
}
class Student{
    String index;
    List<Integer> points;

    public Student(String index, List<Integer> points) {
        this.index = index;
        this.points = points;
    }
    public double sumPoints(){
        return points.size() > 0 ? (double) points.stream().mapToInt(x -> x).sum() / 10 : 0;
    }

    public String getIndex() {
        return index;
    }
    public boolean isFailed(){
        return points.size() < 8;
    }
    @Override
    public String toString() {
        return String.format("%s %s %.2f", index, isFailed() ? "NO" : "YES", sumPoints());
    }
    public int yearOfStudying(){
        return 20 - (Integer.parseInt(String.valueOf(index.charAt(0)))*10 +
                Integer.parseInt(String.valueOf(index.charAt(1))));
    }
}
class LabExercises {
    List<Student> students;

    public LabExercises() {
        this.students = new ArrayList<>();
    }
    public void addStudent (Student student){
        students.add(student);
    }
    public void printByAveragePoints (boolean ascending, int n){
        if(ascending){
            students
                    .stream()
                    .sorted(Comparator
                            .comparing(Student::sumPoints)
                            .thenComparing(Student::getIndex))
                    .limit(n)
                    .forEach(System.out::println);
        }
        else {
            students
                    .stream()
                    .sorted(Comparator
                            .comparing(Student::sumPoints)
                            .thenComparing(Student::getIndex)
                            .reversed())
                    .limit(n)
                    .forEach(System.out::println);
        }
    }
    public List<Student> failedStudents(){
        return students
                .stream()
                .filter(Student::isFailed)
                .sorted(Comparator
                        .comparing(Student::getIndex)
                        .thenComparing(Student::sumPoints))
                .collect(Collectors.toList());
    }
    public Map<Integer,Double> getStatisticsByYear(){
        return students
                .stream()
                .filter(x->!x.isFailed())
                .collect(Collectors.groupingBy(Student::yearOfStudying,
                        TreeMap::new,
                        Collectors.averagingDouble(Student::sumPoints)));
    }
}