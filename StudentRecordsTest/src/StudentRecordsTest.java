import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.*;
import java.util.stream.Collectors;

public class StudentRecordsTest {
    public static void main(String[] args) throws IOException {
        System.out.println("=== READING RECORDS ===");
        StudentRecords studentRecords = new StudentRecords();
        int total = studentRecords.readRecords(System.in);
        System.out.printf("Total records: %d\n", total);
        System.out.println("=== WRITING TABLE ===");
        studentRecords.writeTable(System.out);
        System.out.println("=== WRITING DISTRIBUTION ===");
        studentRecords.writeDistribution(System.out);
    }
}
class Student implements Comparable<Student>{
    String code;
    String way;
    List<Integer> grades;

    public Student(String line) {
        String []parts = line.split(" ");
        this.code = parts[0];
        this.way = parts[1];
        grades = new ArrayList<>();
        for(int i = 2; i < parts.length; i++){
            grades.add(Integer.parseInt(parts[i]));
        }
    }
    public double getAvg(){
        return grades.stream().mapToDouble(x->x).average().orElse(0);
    }
    public int get10Count(){
        return (int) grades
                .stream()
                .filter(x-> x== 10)
                .count();
    }
    public int getCountByGrade(int i){
        return (int) grades
                .stream()
                .filter(x-> x == i)
                .count();
    }
    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return String.format("%s %.2f\n", code, getAvg());
    }

    @Override
    public int compareTo(Student o) {
        return Comparator.
                comparing(Student::getAvg)
                .reversed()
                .thenComparing(Student::getCode)
                .compare(this, o);
    }
}
class StudentRecords {
    Map<String, Set<Student>> studentsByWay;
    public StudentRecords(){
        studentsByWay = new TreeMap<>();
    }
    public int readRecords(InputStream inputStream){
        Scanner scanner = new Scanner(inputStream);

        while (scanner.hasNextLine()){
            String line = scanner.nextLine();
            String []parts = line.split(" ");
            studentsByWay.putIfAbsent(parts[1], new TreeSet<>());
            studentsByWay.get(parts[1]).add(new Student(line));
        }
        return (int) studentsByWay.values().stream().mapToLong(Collection::size).sum();
    }
    public void writeTable(OutputStream outputStream) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(outputStream);

        for (String key : studentsByWay.keySet()){
            writer.append(key).append("\n");
            Set<Student> tmp = studentsByWay.get(key);
            for (Student s : tmp){
                writer.append(s.toString());
            }
        }
        writer.flush();
    }
    public void writeDistribution(OutputStream outputStream) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(outputStream);

        List<String> sortedKeys = studentsByWay
                .entrySet()
                .stream()
                .sorted(Comparator.comparing(x->x.getValue().stream().mapToInt(Student::get10Count).sum()))
                .map(Map.Entry::getKey)
                        .collect(Collectors.toList());

        for (int j = sortedKeys.size() - 1; j >= 0; j--){
            writer.append(sortedKeys.get(j)).append("\n");
            Set<Student> tmp = studentsByWay.get(sortedKeys.get(j));
            for (int i = 6; i <= 10; i++){
                int finalI = i;
                int count = tmp.stream().mapToInt(x->x.getCountByGrade(finalI)).sum();
                writer.append(String.format("%2d | %s(%d)\n", i, getStars(count), count));
            }
        }

        writer.flush();
    }

    private String getStars(int count) {
        int ceil = (int) Math.ceil(count / 10.0);
        StringBuilder a = new StringBuilder();
        for (int i = 0; i < ceil; i++){
            a.append("*");
        }
        return a.toString();
    }
}