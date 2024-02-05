import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Discounts
 */
public class DiscountsTest {
    public static void main(String[] args) {
        Discounts discounts = new Discounts();
        int stores = discounts.readStores(System.in);
        System.out.println("Stores read: " + stores);
        System.out.println("=== By average discount ===");
        discounts.byAverageDiscount().forEach(System.out::print);
        System.out.println("=== By total discount ===");
        discounts.byTotalDiscount().forEach(System.out::print);
    }
}
class Product{
    double discountPrice;
    double price;
    public Product(String line) {
        String[]parts = line.split(":");
        this.discountPrice = Double.parseDouble(parts[0]);
        this.price = Double.parseDouble(parts[1]);
    }
    public double discountPercent(){
        return ((price - discountPrice) / price * 100);
    }

    @Override
    public String toString() {
        return String.format("%2d%% %d/%d\n", (int)discountPercent(), (int)discountPrice, (int)price);
    }

    public int absDiscount() {
        return (int) ((int) price - discountPrice);
    }

    public int discountPercentINT() {
        return (int)((price - discountPrice) / price * 100);
    }
}
class Store{
    String name;
    List<Product> products;

    public Store(String line) {
        String []parts = line.split("\\s+");
        this.name = parts[0];
        this.products = Arrays
               .stream(parts)
               .skip(1).map(Product::new)
               .collect(Collectors.toList());
    }
    public double averageDiscount(){
        return products
                .stream()
                .mapToInt(i ->(int)i.discountPercent())
                .average()
                .orElse(0);
    }
    public double totalDiscount(){
        return products
                .stream()
                .mapToDouble(i -> i.price - i.discountPrice)
                .sum();
    }
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("\n");

        sb.append(String.format("Average discount: %.1f%%\n", averageDiscount()));
        sb.append(String.format("Total discount: %d\n", (int)totalDiscount()));

        List<Product> sorted = products
                .stream()
                .sorted(Comparator.comparing(Product::discountPercentINT)
                        .thenComparing(Product::absDiscount).reversed())
                .collect(Collectors.toList());
        for(Product p : sorted){
            sb.append(p.toString());
        }

        return sb.toString();
    }
}
class Discounts {
    List<Store> stores;

    public Discounts() {
        this.stores = new ArrayList<>();
    }

    public int readStores(InputStream inputStream){
        Scanner scanner = new Scanner(inputStream);

        while (scanner.hasNextLine()){
            stores.add(new Store(scanner.nextLine()));
        }
        return stores.size();
    }
    public List<Store> byAverageDiscount(){
        return stores
                .stream()
                .sorted(Comparator.comparing(Store::averageDiscount).thenComparing(Store::getName).reversed())
                .limit(3)
                .collect(Collectors.toList());

    }
    public List<Store> byTotalDiscount(){
        return stores
                .stream()
                .sorted(Comparator.comparing(Store::totalDiscount).thenComparing(Store::getName))
                .limit(3)
                .collect(Collectors.toList());
    }
}