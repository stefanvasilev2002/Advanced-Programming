import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class BooksTest2{
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        BookCollection2 booksCollection = new BookCollection2();
        Set<String> categories = fillCollection(scanner, booksCollection);
        System.out.println("=== PRINT BY CATEGORY ===");
        for (String category : categories) {
            System.out.println("CATEGORY: " + category);
            booksCollection.printByCategory(category);
        }
        System.out.println("=== TOP N BY PRICE ===");
        print(booksCollection.getCheapestN(n));
    }

    static void print(List<Book2> books) {
        for (Book2 book : books) {
            System.out.println(book);
        }
    }

    static TreeSet<String> fillCollection(Scanner scanner,
                                          BookCollection2 collection) {
        TreeSet<String> categories = new TreeSet<String>();
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            Book2 book = new Book2(parts[0], parts[1], Float.parseFloat(parts[2]));
            collection.addBook(book);
            categories.add(parts[1]);
        }
        return categories;
    }
}
class Book2{
    String title;
    String category;
    float price;

    public Book2(String title, String category, float price) {
        this.title = title;
        this.category = category;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public float getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return String.format("%s (%s) %.2f", getTitle(), getCategory(), getPrice());
    }
}
class BookCollection2{
    List<Book2> books;

    public BookCollection2() {
        books = new ArrayList<>();
    }
    public void addBook(Book2 book){
        books.add(book);
    }
    public void printByCategory(String category){
        books.stream()
                .filter(x -> category.equalsIgnoreCase(x.getCategory()))
                .sorted(Comparator.comparing(Book2::getTitle)
                        .thenComparing(Book2::getPrice))
                .forEach(x -> System.out.printf("%s (%s) %.2f\n", x.getTitle(), x.getCategory(), x.getPrice()));
    }
    public List<Book2> getCheapestN(int n){
        return books.stream()
                .sorted(Comparator
                        .comparing(Book2::getPrice)
                        .thenComparing(Book2::getTitle))
                .limit(n)
                .collect(Collectors.toList());
    }
}