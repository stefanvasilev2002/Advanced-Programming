import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

class InvalidNameException extends Exception {
    private String name;

    public InvalidNameException() {
    }
}
class InvalidNumberException extends Exception{

}
class MaximumSizeExceddedException extends Exception{

}
class Contact {
    private String name;
    private String[] numbers;

    public Contact() throws InvalidNameException, InvalidNumberException, MaximumSizeExceddedException {
        if(!extracted()){
            return;
        }
        this.name = name;
        this.numbers = numbers;
    }
    public Contact(String name, String[] nums) throws InvalidNameException, InvalidNumberException, MaximumSizeExceddedException {
        if(!extracted()){
            return;
        }
        this.name = name;
        this.numbers = nums;
    }
    public Contact(String name, String num) throws InvalidNameException, InvalidNumberException, MaximumSizeExceddedException {
        if(!extracted()){
            return;
        }
        String []numTemp=new String[1];
        numTemp[0]=num;
        this.name = name;
        this.numbers = numTemp;
    }

    public Contact(String nextLine) throws InvalidNameException, InvalidNumberException, MaximumSizeExceddedException {
        if(!extracted()){
            return;
        }
        this.name = nextLine;
        this.numbers = numbers;
    }

    public Contact(String andrej, String randomLegitNumber, String randomLegitNumber1, String randomLegitNumber2) {
    }


    public String getName() {
        return name;
    }

    public String[] getNumbers() {
        //ToDo
        String []temp= new String[numbers.length];
        temp=Arrays.copyOf(numbers, numbers.length);
        Arrays.sort(temp);
        return temp;
    }
    public void addNumber(String phoneNumber)throws InvalidNameException, InvalidNumberException, MaximumSizeExceddedException{
        if(!extracted()){
            return;
        }
        int i=numbers.length+1;
        String[] temp =new String[i];
        temp=numbers;
        temp[i]=phoneNumber;
        numbers=temp;
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        System.out.println(name);
        sb.append(name).append("/n").append(numbers.length);
        String[]sorted=getNumbers();
        for(int i=0; i<numbers.length; i++){
            sb.append("/n").append(sorted[i]);
        }
        return sb.toString();
    }
    public static Contact valueOf(String s){
        //todo

        return null;
    }

    private boolean extracted() throws InvalidNameException, InvalidNumberException, MaximumSizeExceddedException {
        if (name.length() < 4 || name.length() > 10 || isAlphaNumerical(name)) {
            throw new InvalidNameException();
        }
        for (int i = 0; i < numbers.length; i++) {
            if (numbers[i].length() != 9 || starts(numbers[i])){
                throw new InvalidNumberException();
            }
        }
        if(numbers.length>5){
            throw new MaximumSizeExceddedException();
        }
        return true;
    }
    public boolean isAlphaNumerical(String name) {
        char[] niza = name.toCharArray();
        boolean flag = true;
        for (int i = 0; i < niza.length; i++) {
            if (!Character.isLetter(niza[i]) && !Character.isDigit(niza[i])) {
                flag = false;
            }
        }
        return flag;
    }

    public boolean starts(String number) {
        if (!number.startsWith("070") || !number.startsWith("071") || !number.startsWith("072") || !number.startsWith("075") || !number.startsWith("076") || !number.startsWith("077") || !number.startsWith("078")) {
            return false;
        }
        return true;
    }
}

class PhoneBook {
    private Contact[] contacts;

    public PhoneBook() {
        contacts = new Contact[0];
    }

    public void addContact(Contact contact) throws MaximumSizeExceddedException, InvalidNameException {
        if (contacts.length >= 250) {
            throw new MaximumSizeExceddedException();
        }
        for (int i = 0; i < contacts.length; i++) {
            if (contact.getName().compareTo(contacts[i].getName()) == 0) {
                throw new InvalidNameException();
            }
        }
        int i = contacts.length + 1;
        Contact[] temp = new Contact[i];
        temp = contacts;
        temp[i] = contact;
        contacts = temp;
    }

    public Contact getContactForName(String name) {
        for (int i = 0; i < contacts.length; i++) {
            if (name.compareTo(contacts[i].getName()) == 0) {
                return contacts[i];
            }
        }
        return null;
    }
    public int numberOfContacts(){
        return contacts.length;
    }
    public Contact[]getContacts(){
        Contact[]temp=new Contact[contacts.length];
        temp=Arrays.copyOf(contacts, contacts.length);
        Arrays.sort(temp);
        return temp;
    }
    public boolean removeContact(String name){
        boolean flag=false;
        int found=-1;
        for(int i=0; i<contacts.length; i++){
            if(name.compareTo(contacts[i].getName())==0){
                flag=true;
                found=i;
                break;
            }
        }
        if(flag){
            Contact []temp=new Contact[contacts.length-1];
            for(int i=found+1; i<contacts.length; i++){
                temp[i-1]=contacts[i];
            }
            contacts=temp;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        for(int i=0; i<contacts.length; i++){
            sb.append(contacts[i].getName()).append("/n");
        }
        return sb.toString();
    }
    public static boolean saveAsTextFile(PhoneBook phonebook,String path){
        return false;
    }
    public static PhoneBook loadFromTextFile(String path){
        return null;
    }
    public Contact[] getContactsForNumber(String number_prefix){
        Contact[]contacts1=new Contact[0];
        for(int i=0; i<contacts.length; i++){
            String[]numbers=new String[contacts[i].getNumbers().length];
            numbers=contacts[i].getNumbers();
            for(int j=0; j<contacts[i].getNumbers().length; j++){
                if(numbers[i].startsWith(number_prefix)){
                    int z = contacts1.length + 1;
                    Contact[] temp = new Contact[z];
                    temp = contacts1;
                    temp[i] = contacts[i];
                    contacts1 = temp;
                    break;
                }
            }
        }
        return contacts1;
    }
}

public class PhonebookTester {

    public static void main(String[] args) throws Exception {
        Scanner jin = new Scanner(System.in);
        String line = jin.nextLine();
        switch( line ) {
            case "test_contact":
                testContact(jin);
                break;
            case "test_phonebook_exceptions":
                testPhonebookExceptions(jin);
                break;
            case "test_usage":
                testUsage(jin);
                break;
        }
    }

    private static void testFile(Scanner jin) throws Exception {
        PhoneBook phonebook = new PhoneBook();
        while ( jin.hasNextLine() )
            phonebook.addContact(new Contact(jin.nextLine(),jin.nextLine().split("\\s++")));
        String text_file = "phonebook.txt";
        PhoneBook.saveAsTextFile(phonebook,text_file);
        PhoneBook pb = PhoneBook.loadFromTextFile(text_file);
        if ( ! pb.equals(phonebook) ) System.out.println("Your file saving and loading doesn't seem to work right");
        else System.out.println("Your file saving and loading works great. Good job!");
    }

    private static void testUsage(Scanner jin) throws Exception {
        PhoneBook phonebook = new PhoneBook();
        while ( jin.hasNextLine() ) {
            String command = jin.nextLine();
            switch ( command ) {
                case "add":
                    phonebook.addContact(new Contact(jin.nextLine(),jin.nextLine().split("\\s++")));
                    break;
                case "remove":
                    phonebook.removeContact(jin.nextLine());
                    break;
                case "print":
                    System.out.println(phonebook.numberOfContacts());
                    System.out.println(Arrays.toString(phonebook.getContacts()));
                    System.out.println(phonebook.toString());
                    break;
                case "get_name":
                    System.out.println(phonebook.getContactForName(jin.nextLine()));
                    break;
                case "get_number":
                    System.out.println(Arrays.toString(phonebook.getContactsForNumber(jin.nextLine())));
                    break;
            }
        }
    }

    private static void testPhonebookExceptions(Scanner jin) {
        PhoneBook phonebook = new PhoneBook();
        boolean exception_thrown = false;
        try {
            while ( jin.hasNextLine() ) {
                phonebook.addContact(new Contact(jin.nextLine()));
            }
        }
        catch ( InvalidNameException e ) {
            //todo System.out.println(e.name);
            exception_thrown = true;
        }
        catch ( Exception e ) {}
        if ( ! exception_thrown ) System.out.println("Your addContact method doesn't throw InvalidNameException");
        /*
		exception_thrown = false;
		try {
		phonebook.addContact(new Contact(jin.nextLine()));
		} catch ( MaximumSizeExceddedException e ) {
			exception_thrown = true;
		}
		catch ( Exception e ) {}
		if ( ! exception_thrown ) System.out.println("Your addContact method doesn't throw MaximumSizeExcededException");
        */
    }

    private static void testContact(Scanner jin) throws Exception {
        boolean exception_thrown = true;
        String names_to_test[] = { "And\nrej","asd","AAAAAAAAAAAAAAAAAAAAAA","Ð�Ð½Ð´Ñ€ÐµÑ˜A123213","Andrej#","Andrej<3"};
        for ( String name : names_to_test ) {
            try {
                new Contact(name);
                exception_thrown = false;
            } catch (InvalidNameException e) {
                exception_thrown = true;
            }
            if ( ! exception_thrown ) System.out.println("Your Contact constructor doesn't throw an InvalidNameException");
        }
        String numbers_to_test[] = { "+071718028","number","078asdasdasd","070asdqwe","070a56798","07045678a","123456789","074456798","073456798","079456798" };
        for ( String number : numbers_to_test ) {
            try {
                new Contact("Andrej",number);
                exception_thrown = false;
            } catch (InvalidNumberException e) {
                exception_thrown = true;
            }
            if ( ! exception_thrown ) System.out.println("Your Contact constructor doesn't throw an InvalidNumberException");
        }
        String nums[] = new String[10];
        for ( int i = 0 ; i < nums.length ; ++i ) nums[i] = getRandomLegitNumber();
        try {
            new Contact("Andrej",nums);
            exception_thrown = false;
        } catch (MaximumSizeExceddedException e) {
            exception_thrown = true;
        }
        if ( ! exception_thrown ) System.out.println("Your Contact constructor doesn't throw a MaximumSizeExceddedException");
        Random rnd = new Random(5);
        /*todo Contact contact = new Contact("Andrej",getRandomLegitNumber(rnd),getRandomLegitNumber(rnd),getRandomLegitNumber(rnd));
        System.out.println(contact.getName());
        System.out.println(Arrays.toString(contact.getNumbers()));
        System.out.println(contact.toString());
        contact.addNumber(getRandomLegitNumber(rnd));
        System.out.println(Arrays.toString(contact.getNumbers()));
        System.out.println(contact.toString());
        contact.addNumber(getRandomLegitNumber(rnd));
        System.out.println(Arrays.toString(contact.getNumbers()));
        System.out.println(contact.toString());*/
    }

    static String[] legit_prefixes = {"070","071","072","075","076","077","078"};
    static Random rnd = new Random();

    private static String getRandomLegitNumber() {
        return getRandomLegitNumber(rnd);
    }

    private static String getRandomLegitNumber(Random rnd) {
        StringBuilder sb = new StringBuilder(legit_prefixes[rnd.nextInt(legit_prefixes.length)]);
        for ( int i = 3 ; i < 9 ; ++i )
            sb.append(rnd.nextInt(10));
        return sb.toString();
    }
}
