import java.util.*;
import java.util.stream.Collectors;

public class FacultyTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int testCase = sc.nextInt();

        if (testCase == 1) {
            System.out.println("TESTING addStudent AND printFirstNStudents");
            Faculty faculty = new Faculty();
            for (int i = 0; i < 10; i++) {
                faculty.addStudent("student" + i, (i % 2 == 0) ? 3 : 4);
            }
            faculty.printFirstNStudents(10);

        } else if (testCase == 2) {
            System.out.println("TESTING addGrade and exception");
            Faculty faculty = new Faculty();
            faculty.addStudent("123", 3);
            faculty.addStudent("1234", 4);
            try {
                faculty.addGradeToStudent("123", 7, "NP", 10);
            } catch (OperationNotAllowedException e) {
                System.out.println(e.getMessage());
            }
            try {
                faculty.addGradeToStudent("1234", 9, "NP", 8);
            } catch (OperationNotAllowedException e) {
                System.out.println(e.getMessage());
            }
        } else if (testCase == 3) {
            System.out.println("TESTING addGrade and exception");
            Faculty faculty = new Faculty();
            faculty.addStudent("123", 3);
            faculty.addStudent("1234", 4);
            for (int i = 0; i < 4; i++) {
                try {
                    faculty.addGradeToStudent("123", 1, "course" + i, 10);
                } catch (OperationNotAllowedException e) {
                    System.out.println(e.getMessage());
                }
            }
            for (int i = 0; i < 4; i++) {
                try {
                    faculty.addGradeToStudent("1234", 1, "course" + i, 10);
                } catch (OperationNotAllowedException e) {
                    System.out.println(e.getMessage());
                }
            }
        } else if (testCase == 4) {
            System.out.println("Testing addGrade for graduation");
            Faculty faculty = new Faculty();
            faculty.addStudent("123", 3);
            faculty.addStudent("1234", 4);
            int counter = 1;
            for (int i = 1; i <= 6; i++) {
                for (int j = 1; j <= 3; j++) {
                    try {
                        faculty.addGradeToStudent("123", i, "course" + counter, (i % 2 == 0) ? 7 : 8);
                    } catch (OperationNotAllowedException e) {
                        System.out.println(e.getMessage());
                    }
                    ++counter;
                }
            }
            counter = 1;
            for (int i = 1; i <= 8; i++) {
                for (int j = 1; j <= 3; j++) {
                    try {
                        faculty.addGradeToStudent("1234", i, "course" + counter, (j % 2 == 0) ? 7 : 10);
                    } catch (OperationNotAllowedException e) {
                        System.out.println(e.getMessage());
                    }
                    ++counter;
                }
            }
            System.out.println("LOGS");
            System.out.println(faculty.getFacultyLogs());
            System.out.println("PRINT STUDENTS (there shouldn't be anything after this line!");
            faculty.printFirstNStudents(2);
        } else if (testCase == 5 || testCase == 6 || testCase == 7) {
            System.out.println("Testing addGrade and printFirstNStudents (not graduated student)");
            Faculty faculty = new Faculty();
            for (int i = 1; i <= 10; i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j < ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= ((j % 2 == 1) ? 3 : 2); k++) {
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), i % 5 + 6);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }
            }
            if (testCase == 5)
                faculty.printFirstNStudents(10);
            else if (testCase == 6)
                faculty.printFirstNStudents(3);
            else
                faculty.printFirstNStudents(20);
        } else if (testCase == 8 || testCase == 9) {
            System.out.println("TESTING DETAILED REPORT");
            Faculty faculty = new Faculty();
            faculty.addStudent("student1", ((testCase == 8) ? 3 : 4));
            int grade = 6;
            int counterCounter = 1;
            for (int i = 1; i < ((testCase == 8) ? 6 : 8); i++) {
                for (int j = 1; j < 3; j++) {
                    try {
                        faculty.addGradeToStudent("student1", i, "course" + counterCounter, grade);
                    } catch (OperationNotAllowedException e) {
                        e.printStackTrace();
                    }
                    grade++;
                    if (grade == 10)
                        grade = 5;
                    ++counterCounter;
                }
            }
            System.out.println(faculty.getDetailedReportForStudent("student1"));
        } else if (testCase==10) {
            System.out.println("TESTING PRINT COURSES");
            Faculty faculty = new Faculty();
            for (int i = 1; i <= 10; i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j < ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= ((j % 2 == 1) ? 3 : 2); k++) {
                        int grade = sc.nextInt();
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), grade);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }
            }
            faculty.printCourses();
        } else if (testCase==11) {
            System.out.println("INTEGRATION TEST");
            Faculty faculty = new Faculty();
            for (int i = 1; i <= 10; i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j <= ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= ((j % 2 == 1) ? 2 : 3); k++) {
                        int grade = sc.nextInt();
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), grade);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }

            }

            for (int i=11;i<15;i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j <= ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= 3; k++) {
                        int grade = sc.nextInt();
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), grade);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }
            }
            System.out.println("LOGS");
            System.out.println(faculty.getFacultyLogs());
            System.out.println("DETAILED REPORT FOR STUDENT");
            System.out.println(faculty.getDetailedReportForStudent("student2"));
            try {
                System.out.println(faculty.getDetailedReportForStudent("student11"));
                System.out.println("The graduated students should be deleted!!!");
            } catch (NullPointerException e) {
                System.out.println("The graduated students are really deleted");
            }
            System.out.println("FIRST N STUDENTS");
            faculty.printFirstNStudents(10);
            System.out.println("COURSES");
            faculty.printCourses();
        }
    }
}
class Student implements Comparable<Student>{
    String id;
    int yearOfStudies;
    Set<String> courses;
    Map<Integer, List<Integer>> gradesPerTerm;

    public Student(String id, int yearOfStudies) {
        this.id = id;
        this.yearOfStudies = yearOfStudies;
        this.courses = new TreeSet<>();
        this.gradesPerTerm = new TreeMap<>();
        for (int i = 1; i <= yearOfStudies*2 ; i++){
            gradesPerTerm.put(i, new ArrayList<>());
        }
    }

    public void addGrade(int term, String courseName, int grade) throws OperationNotAllowedException {
        if (term > yearOfStudies * 2){
            throw new OperationNotAllowedException(String.format("Term %d is not possible for student with ID %s", term, id));
        }
        List<Integer> tmp = gradesPerTerm.get(term);
        if (tmp.size() >= 3){
            throw new OperationNotAllowedException(String.format("Student %s already has 3 grades in term %d", id, term));
        }
        gradesPerTerm.get(term).add(grade);
        courses.add(courseName);
    }

    public boolean checkGraduated() {
        int grades = courses.size();

        return grades == yearOfStudies * 6;
    }

    public double averageGrade() {
        return gradesPerTerm.values()
                .stream()
                .flatMap(Collection::stream)
                .mapToDouble(x->x)
                .average()
                .orElse(5);
    }
    public int passedCourse(){
        return courses.size();
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Student: %s\n", id));

        for (Map.Entry<Integer, List<Integer>> entry : gradesPerTerm.entrySet()){
            sb.append(String.format("Term %d\n", entry.getKey()));
            sb.append(String.format("Courses: %d\n", entry.getValue().size()));
            sb.append(String.format("Average grade for term: %.2f\n", entry.getValue().size() == 0 ? 5 : entry.getValue().stream().mapToDouble(x->x).average().orElse(0)));
        }
        sb.append(String.format("Average grade: %.2f\n", averageGrade()));
        sb.append(String.format("Courses attended: %s", courses.stream().collect(Collectors.joining(","))));

        return sb.toString();
    }

    public String getId() {
        return id;
    }

    @Override
    public int compareTo(Student o) {
        return Comparator.comparing(Student::passedCourse)
                .thenComparing(Student::averageGrade)
                .thenComparing(Student::getId)
                .reversed()
                .compare(this, o);
    }
}
class Course implements Comparable<Course>{
    String name;
    int countStudents;
    int sumGrades;

    public Course(String courseName) {
        this.name = courseName;
        countStudents = 1;
        sumGrades = 1;
    }
    public Course(String courseName, int i, int s) {
        this.name = courseName;
        countStudents = i;
        sumGrades = s;
    }
    public int getCountStudents() {
        return countStudents;
    }

    public double getSumGrades() {
        return (double) sumGrades / countStudents;
    }

    @Override
    public int compareTo(Course o) {
        return Comparator.comparing(Course::getCountStudents)
                .thenComparing(Course::getSumGrades)
                .thenComparing(x->x.name)
                .compare(this, o);
    }
    public boolean compareName(Course o){
        return Objects.equals(name, o.name);
    }
}
class Faculty {
    Map<String, Student> students;
    List<String> logs;
    Set<Course> courses;
    public Faculty() {
        students = new HashMap<>();
        logs = new ArrayList<>();
        courses = new TreeSet<>();
    }
    void addStudent(String id, int yearsOfStudies) {
        students.put(id, new Student(id, yearsOfStudies));
    }

    void addGradeToStudent(String studentId, int term, String courseName, int grade) throws OperationNotAllowedException {
        students.get(studentId).addGrade(term, courseName, grade);
        if (students.get(studentId).checkGraduated()){
            Student tmp = students.get(studentId);
            logs.add(String.format("Student with ID %s graduated with average grade %.2f in %d years.", tmp.id, tmp.averageGrade(), tmp.yearOfStudies));
            students.remove(studentId);
        }
        Course tmp = new Course(courseName);
        for (Course c : courses){
            if (c.compareName(tmp)){
                c.countStudents++;
                c.sumGrades += grade;
                return;
            }
        }
        courses.add(new Course(courseName, 1, grade));
    }

    String getFacultyLogs() {
        return String.join("\n", logs);
    }

    String getDetailedReportForStudent(String id) {
        return students.get(id).toString();
    }

    void printFirstNStudents(int n) {
        TreeSet<Student> sortedSet =  new TreeSet<>(new ArrayList<>(students.values()));
        int i = 0;
        for (Student s : sortedSet){
            if (i >= n){
                break;
            }
            System.out.printf("Student: %s Courses passed: %d Average grade: %.2f\n", s.id, s.passedCourse(), s.averageGrade());
            i++;
        }
    }

    void printCourses() {
        List<Course> sorted = courses.stream().sorted(Comparator.comparing(Course::getCountStudents)
                .thenComparing(Course::getSumGrades)
                .thenComparing(x->x.name))
                .collect(Collectors.toList());
        for (Course c : sorted){
            System.out.printf("%s %d %.2f\n", c.name, c.countStudents, c.getSumGrades());
        }
    }
}
class OperationNotAllowedException extends Exception{

    public OperationNotAllowedException(String format) {
        super(format);
    }
}