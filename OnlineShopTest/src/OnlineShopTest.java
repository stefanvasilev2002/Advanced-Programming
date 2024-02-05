import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

enum COMPARATOR_TYPE {
    NEWEST_FIRST,
    OLDEST_FIRST,
    LOWEST_PRICE_FIRST,
    HIGHEST_PRICE_FIRST,
    MOST_SOLD_FIRST,
    LEAST_SOLD_FIRST
}

class ProductNotFoundException extends Exception {
    ProductNotFoundException(String message) {
        super(message);
    }
}

class Product {
    String category;
    String id;
    String name;
    LocalDateTime createdAt;
    double price;
    int quantitySold;

    public Product(String category, String id, String name, LocalDateTime createdAt, double price) {
        this.category = category;
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.price = price;
    }

    public double buyProduct(int quantity) {
        quantitySold += quantity;
        return quantity * price;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public double getPrice() {
        return price;
    }

    public int getSold() {
        return quantitySold;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", createdAt=" + createdAt +
                ", price=" + price +
                ", quantitySold=" + quantitySold +
                '}';
    }

    public String getCategory() {
        return category;
    }
}

class OnlineShop {
    Map<String, Product> products;
    OnlineShop() {
        this.products = new HashMap<>();
    }
    void addProduct(String category, String id, String name, LocalDateTime createdAt, double price){
        products.put(id, new Product(category, id, name, createdAt, price));
    }

    double buyProduct(String id, int quantity) throws ProductNotFoundException{
        if (!products.containsKey(id)){
            throw new ProductNotFoundException(String.format("Product with id %s does not exist in the online shop!", id));
        }

        return products.get(id).buyProduct(quantity);
    }

    List<List<Product>> listProducts(String category, COMPARATOR_TYPE comparatorType, int pageSize) {
        Comparator<Product> c;
        if(comparatorType == COMPARATOR_TYPE.HIGHEST_PRICE_FIRST){
            c = Comparator.comparing(Product::getPrice).reversed();
        }
        else if(comparatorType == COMPARATOR_TYPE.LOWEST_PRICE_FIRST){
            c = Comparator.comparing(Product::getPrice);
        }
        else if(comparatorType == COMPARATOR_TYPE.MOST_SOLD_FIRST){
            c = Comparator.comparing(Product::getSold).reversed();
        }
        else if(comparatorType == COMPARATOR_TYPE.LEAST_SOLD_FIRST){
            c = Comparator.comparing(Product::getSold);
        }
        else if(comparatorType == COMPARATOR_TYPE.OLDEST_FIRST){
            c = Comparator.comparing(Product::getCreatedAt);
        }
        else{
            c = Comparator.comparing(Product::getCreatedAt).reversed();
        }
        List<Product> sorted;
        if (category == null){
            sorted = products
                    .values()
                    .stream()
                    .sorted(c)
                    .collect(Collectors.toList());
        }
        else {
            sorted = products
                    .values()
                    .stream()
                    .filter(x-> Objects.equals(x.getCategory(), category))
                    .sorted(c)
                    .collect(Collectors.toList());
        }

        List<List<Product>> pages = new ArrayList<>();
        for (int i = 0; i < sorted.size(); i += pageSize){
            List<Product> tmp = new ArrayList<>();
            for (int j = 0; j < pageSize; j++){
                if (j + i >= sorted.size()){
                    break;
                }
                tmp.add(sorted.get(j + i));
            }
            pages.add(tmp);
        }
        return pages;
    }

}

public class OnlineShopTest {

    public static void main(String[] args) {
        OnlineShop onlineShop = new OnlineShop();
        double totalAmount = 0.0;
        Scanner sc = new Scanner(System.in);
        String line;
        while (sc.hasNextLine()) {
            line = sc.nextLine();
            String[] parts = line.split("\\s+");
            if (parts[0].equalsIgnoreCase("addproduct")) {
                String category = parts[1];
                String id = parts[2];
                String name = parts[3];
                LocalDateTime createdAt = LocalDateTime.parse(parts[4]);
                double price = Double.parseDouble(parts[5]);
                onlineShop.addProduct(category, id, name, createdAt, price);
            } else if (parts[0].equalsIgnoreCase("buyproduct")) {
                String id = parts[1];
                int quantity = Integer.parseInt(parts[2]);
                try {
                    totalAmount += onlineShop.buyProduct(id, quantity);
                } catch (ProductNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                String category = parts[1];
                if (category.equalsIgnoreCase("null"))
                    category=null;
                String comparatorString = parts[2];
                int pageSize = Integer.parseInt(parts[3]);
                COMPARATOR_TYPE comparatorType = COMPARATOR_TYPE.valueOf(comparatorString);
                printPages(onlineShop.listProducts(category, comparatorType, pageSize));
            }
        }
        System.out.println("Total revenue of the online shop is: " + totalAmount);

    }

    private static void printPages(List<List<Product>> listProducts) {
        for (int i = 0; i < listProducts.size(); i++) {
            System.out.println("PAGE " + (i + 1));
            listProducts.get(i).forEach(System.out::println);
        }
    }
}

