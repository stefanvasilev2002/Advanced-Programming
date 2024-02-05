import java.util.*;

public class PhoneBookTest {

    public static void main(String[] args) {
        PhoneBook phoneBook = new PhoneBook();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            try {
                phoneBook.addContact(parts[0], parts[1]);
            } catch (DuplicateNumberException e) {
                System.out.println(e.getMessage());
            }
        }
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println(line);
            String[] parts = line.split(":");
            if (parts[0].equals("NUM")) {
                phoneBook.contactsByNumber(parts[1]);
            } else {
                phoneBook.contactsByName(parts[1]);
            }
        }
    }

}
class Contact implements Comparable<Contact> {
    String name;
    String number;

    public Contact(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    @Override
    public int compareTo(Contact o) {
        return Comparator
                .comparing(Contact::getName)
                .thenComparing(Contact::getNumber)
                .compare(this, o);
    }

    @Override
    public String toString() {
        return String.format("%s %s\n", name, number);
    }
}
class PhoneBook {
    Map<String, Contact> contactMap;
    public PhoneBook() {
        this.contactMap = new TreeMap<>();
    }

    public void addContact(String name, String number) throws DuplicateNumberException {
        if (contactMap.containsKey(number)){
            throw new DuplicateNumberException(number);
        }
        contactMap.put(number, new Contact(name, number));
    }
    public void contactsByNumber(String number){
        if (contactMap
                .values()
                .stream()
                .noneMatch(x -> x.number.contains(number))){
            System.out.println("NOT FOUND");
        }
        contactMap
                .values()
                .stream()
                .filter(x->x.number.contains(number))
                .sorted()
                .forEach(System.out::print);
    }
    public void contactsByName(String name){
        if (contactMap
                .values()
                .stream()
                .noneMatch(x -> x.name.contains(name))){
            System.out.println("NOT FOUND");
        }
        contactMap
                .values()
                .stream()
                .filter(x->x.name.contains(name))
                .sorted()
                .forEach(System.out::print);
    }
}
class DuplicateNumberException  extends Exception{
    public DuplicateNumberException(String number) {
        super(String.format("Duplicate number: %s", number));
    }
}