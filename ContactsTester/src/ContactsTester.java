import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

enum Operator{VIP, ONE, TMOBILE};
abstract class Contact{
    private String date;

    public Contact(String date) {
        this.date = date;
    }
    public boolean isNewerThan(Contact c){
        return date.compareTo(c.date)>0;
    }
    public abstract String getType();
    @Override
    public abstract String toString();
}

class EmailContact extends Contact{
    private String email;

    public EmailContact(String date, String email) {
        super(date);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getType() {
        return "Email";
    }
    @Override
    public String toString() {
        return "\"" + email + '\"';
    }
}

class PhoneContact extends Contact{
    private String phone;
    private Operator operator;

    public PhoneContact(String date, String phone) {
        super(date);
        this.phone = phone;
        this.operator =Operator.VIP;
    }

    public String getPhone() {
        return phone;
    }

    public Operator getOperator()
    {
        String pocetok = phone.substring(0, 3);
        switch (pocetok){
            case "070": return Operator.TMOBILE;
            case "071": return Operator.TMOBILE;
            case "072": return Operator.TMOBILE;
            case "075": return Operator.ONE;
            case "076" : return Operator.ONE;
            case "077" : return Operator.VIP;
            case "078" : return Operator.VIP;
            default: return null;
        }
    }
    @Override
    public String getType() {
        return "Phone";
    }
    @Override
    public String toString() {
        return "\"" + phone + '\"';
    }
}

class Student {
    private String firstName;
    private String lastName;
    private String city;
    private int age;
    private long index;
    private Contact[] contacts;

    public Student(String firstName, String lastName, String city, int age, long index) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.age = age;
        this.index = index;
        contacts = new Contact[0];
    }

    public void addEmailContact(String date, String email) {
        ArrayList<Contact> contactsList = new ArrayList<Contact>(Arrays.asList(contacts));
        contactsList.add(new EmailContact(date, email));
        contacts = contactsList.toArray(contacts);
    }

    public void addPhoneContact(String date, String phone) {
        ArrayList<Contact> contactsList = new ArrayList<Contact>(Arrays.asList(contacts));
        contactsList.add(new PhoneContact(date, phone));
        contacts = contactsList.toArray(contacts);
    }

    public Contact[] getEmailContacts()
    {
        return Arrays.stream(contacts).filter(contact -> contact.getType().equals("Email")).toArray(Contact[]::new);
    }

    public Contact[] getPhoneContacts()
    {
        return Arrays.stream(contacts).filter(contact -> contact.getType().equals("Phone")).toArray(Contact[]::new);
    }

    public String getFullName() {
        return (firstName.toUpperCase()+" "+lastName.toUpperCase());
    }

    public String getCity() {
        return city;
    }

    public long getIndex() {
        return index;
    }
    public Contact getLatestContact(){
        Contact latest=contacts[0];
        for(int i=1; i< contacts.length; i++){
            if(contacts[i].isNewerThan(latest)){
                latest=contacts[i];

            }
        }
        return latest;
    }
    public int numberOfContacts() {
        return contacts.length;
    }
    @Override
    public String toString() {
        return "{" +
                "\"ime\":\"" + firstName + '\"' +
                ", \"prezime\":\"" + lastName + '\"' +
                ", \"vozrast\":" + age +
                ", \"grad\":\"" + city + '\"' +
                ", \"indeks\":" + index +
                ", \"telefonskiKontakti\":" + Arrays.toString(getPhoneContacts()) +
                ", \"emailKontakti\":" + Arrays.toString(getEmailContacts()) +
                '}';
    }
}

class Faculty{
    private String name;
    private Student[]students;

    public Faculty(String name, Student[] students) {
        this.name = name;
        this.students = students;
    }
    public int countStudentsFromCity(String cityName){
        int counter=0;
        for(int i=0; i<students.length; i++){
            if(students[i].getCity().compareTo(cityName)==0){
                counter++;
            }
        }
        return counter;
    }
    public Student getStudent(long index){
        int i;
        for(i=0; i<students.length; i++){
            if(students[i].getIndex()==index){
                break;
            }
        }
        return students[i];
    }
    public double getAverageNumberOfContacts(){
        double globalSum=0;
        for(int i=0; i<students.length; i++){
            globalSum+=students[i].numberOfContacts();
        }
        return globalSum/students.length;
    }
    public Student getStudentWithMostContacts(){
        int mostStudent=0;
        for(int i=0; i<students.length; i++){
            if(students[i].numberOfContacts()>students[mostStudent].numberOfContacts()){
                mostStudent=i;
            }
            else if(students[i].numberOfContacts()==students[mostStudent].numberOfContacts()){
                if(students[i].getIndex()>students[mostStudent].getIndex()){
                    mostStudent=i;
                }
            }
        }
        return students[mostStudent];
    }
    @Override
    public String toString() {
        return "{" +
                "\"fakultet\":\"" + name + '\"' +
                ", \"studenti\":" + Arrays.toString(students) +
                "}";
    }
}

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
