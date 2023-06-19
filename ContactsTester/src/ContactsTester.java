import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

public class ContactsTester {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int tests = scanner.nextInt();
        Faculty faculty = null;

        int rvalue = 0;
        long rindex = -1;

        DecimalFormat df = new DecimalFormat("0.00");

        for (int t = 0; t < tests; t++) {

            rvalue++;
            String operation = scanner.next();

            switch (operation) {
                case "CREATE_FACULTY": {
                    String name = scanner.nextLine().trim();
                    int N = scanner.nextInt();

                    Student[] students = new Student[N];

                    for (int i = 0; i < N; i++) {
                        rvalue++;

                        String firstName = scanner.next();
                        String lastName = scanner.next();
                        String city = scanner.next();
                        int age = scanner.nextInt();
                        long index = scanner.nextLong();

                        if ((rindex == -1) || (rvalue % 13 == 0))
                            rindex = index;

                        Student student = new Student(firstName, lastName, city,
                                age, index);
                        students[i] = student;
                    }

                    faculty = new Faculty(name, students);
                    break;
                }

                case "ADD_EMAIL_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String email = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addEmailContact(date, email);
                    break;
                }

                case "ADD_PHONE_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String phone = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addPhoneContact(date, phone);
                    break;
                }

                case "CHECK_SIMPLE": {
                    System.out.println("Average number of contacts: "
                            + df.format(faculty.getAverageNumberOfContacts()));

                    rvalue++;

                    String city = faculty.getStudent(rindex).getCity();
                    System.out.println("Number of students from " + city + ": "
                            + faculty.countStudentsFromCity(city));

                    break;
                }

                case "CHECK_DATES": {

                    rvalue++;

                    System.out.print("Latest contact: ");
                    Contact latestContact = faculty.getStudent(rindex)
                            .getLatestContact();
                    if (latestContact.getType().equals("Email"))
                        System.out.println(((EmailContact) latestContact)
                                .getEmail());
                    if (latestContact.getType().equals("Phone"))
                        System.out.println(((PhoneContact) latestContact)
                                .getPhone()
                                + " ("
                                + ((PhoneContact) latestContact).getOperator()
                                .toString() + ")");

                    if (faculty.getStudent(rindex).getEmailContacts().length > 0
                            && faculty.getStudent(rindex).getPhoneContacts().length > 0) {
                        System.out.print("Number of email and phone contacts: ");
                        System.out
                                .println(faculty.getStudent(rindex)
                                        .getEmailContacts().length
                                        + " "
                                        + faculty.getStudent(rindex)
                                        .getPhoneContacts().length);

                        System.out.print("Comparing dates: ");
                        int posEmail = rvalue
                                % faculty.getStudent(rindex).getEmailContacts().length;
                        int posPhone = rvalue
                                % faculty.getStudent(rindex).getPhoneContacts().length;

                        System.out.println(faculty.getStudent(rindex)
                                .getEmailContacts()[posEmail].isNewerThan(faculty
                                .getStudent(rindex).getPhoneContacts()[posPhone]));
                    }

                    break;
                }

                case "PRINT_FACULTY_METHODS": {
                    System.out.println("Faculty: " + faculty.toString());
                    System.out.println("Student with most contacts: "
                            + faculty.getStudentWithMostContacts().toString());
                    break;
                }

            }

        }

        scanner.close();
    }
}
abstract class Contact{
    String date;
    String type;
    public Contact(String date){
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public boolean isNewerThan(Contact c){
        return Comparator.comparing(Contact::getDate).compare(this, c) > 0;
    }
    public abstract String getType();

}
class EmailContact extends Contact{
    String email;

    public EmailContact(String date, String email) {
        super(date);
        this.email = email;
        this.type = "Email";
    }
    public String getEmail() {
        return email;
    }
    @Override
    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return email;
    }
}
enum Operator { VIP, ONE, TMOBILE }
class PhoneContact extends Contact{
    String phone;
    Operator operator;

    public PhoneContact(String date, String phone) {
        super(date);
        this.phone = phone;
        if (phone.charAt(2) == '0' || phone.charAt(2) == '1' || phone.charAt(2) == '2'){
            this.operator = Operator.TMOBILE;
        }
        else if(phone.charAt(2) == '5' || phone.charAt(2) == '6'){
            this.operator = Operator.ONE;
        }
        else {
            this.operator = Operator.VIP;
        }
        this.type = "Phone";
    }
    @Override
    public String getType() {
        return type;
    }

    public Operator getOperator() {
        return operator;
    }

    public String getPhone() {
        return phone;
    }
    @Override
    public String toString() {
        return String.format("%s (%s)", phone, operator);
    }
}
class Student{
    String firstName;
    String lastName;
    String city;
    int age;
    long index;
    List<Contact> contacts;

    public Student(String firstName, String lastName, String city, int age, long index) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.age = age;
        this.index = index;
        this.contacts = new ArrayList<>();
    }
    public void addEmailContact(String date, String email){
        contacts.add(new EmailContact(date, email));
    }
    public void addPhoneContact(String date, String phone){
        contacts.add(new PhoneContact(date, phone));
    }
    public Contact[] getEmailContacts(){
        return contacts
                .stream()
                .filter(x-> Objects.equals(x.getType(), "Email"))
                .toArray(Contact[]::new);
    }
    public Contact[] getPhoneContacts(){
        return contacts
                .stream()
                .filter(x-> Objects.equals(x.getType(), "Phone"))
                .toArray(Contact[]::new);
    }

    public String getFullName() {
        return String.format("%s %s", firstName, lastName);
    }

    public String getCity() {
        return city;
    }

    public long getIndex() {
        return index;
    }
    public Contact getLatestContact(){
        List<Contact> sorted = contacts.stream()
                .sorted(Comparator
                        .comparing(Contact::getDate).reversed())
                .collect(Collectors.toList());
        return sorted.get(0);
    }
    public int getNumContacts(){
        return getEmailContacts().length + getPhoneContacts().length;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"ime\":\"" + firstName + "\", " +
                "\"prezime\":\"" + lastName + "\", " +
                "\"vozrast\":" + age + ", " +
                "\"grad\":\"" + city + "\", " +
                "\"indeks\":" + index + ", " +
                "\"telefonskiKontakti\":" + "[");
                int i = 0;
                for (Contact c : getPhoneContacts()){
                    PhoneContact tmp = (PhoneContact)c;
                    sb.append("\"" + tmp.phone + "\", ");
                    i++;
                }
                if (i != 0){
                    sb = new StringBuilder(sb.substring(0, sb.length() - 2));
                }
                sb.append("], ");

                sb.append("\"emailKontakti\":" + "[");
                i = 0;
                for (Contact c : getEmailContacts()){
                    EmailContact tmp = (EmailContact) c;
                    sb.append("\"" + tmp.email + "\", ");
                    i++;
                }
                if (i != 0){
                    sb = new StringBuilder(sb.substring(0, sb.length() - 2));
                }
                sb.append("]");
                sb.append('}');
                return sb.toString();
    }
}
class Faculty{
    String name;
    List<Student> students;

    public Faculty(String name, Student[]students) {
        this.name = name;
        this.students = Arrays.stream(students).collect(Collectors.toList());
    }
    public int countStudentsFromCity(String cityName){
        return (int) students.stream()
                .filter(x-> Objects.equals(x.getCity(), cityName))
                .count();
    }
    public Student getStudent(long index){
        for (Student s : students){
            if (s.getIndex() == index){
                return s;
            }
        }
        return null;
    }
    public double getAverageNumberOfContacts(){
        int sum = students
                .stream()
                .mapToInt(x-> x.getPhoneContacts().length + x.getEmailContacts().length)
                .sum();
        int count = students.size();
        return (double)sum / count;
    }
    public Student getStudentWithMostContacts(){
        return students
                .stream()
                .sorted(Comparator
                        .comparing(Student::getNumContacts)
                        .thenComparing(Student::getIndex).reversed())
                .collect(Collectors.toList())
                .get(0);
    }

    @Override
    public String toString() {
        return "{\"fakultet\":\"" + name + "\", " +
                "\"studenti\":" + students +
                '}';
    }
}