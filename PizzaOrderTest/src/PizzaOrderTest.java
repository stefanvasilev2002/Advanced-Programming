import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class PizzaOrderTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) { //test Item
            try {
                String type = jin.next();
                String name = jin.next();
                Item item = null;
                if (type.equals("Pizza")) item = new PizzaItem(name);
                else item = new ExtraItem(name);
                System.out.println(item.getPrice());
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
        if (k == 1) { // test simple order
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 2) { // test order with removing
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (jin.hasNextInt()) {
                try {
                    int idx = jin.nextInt();
                    order.removeItem(idx);
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 3) { //test locking & exceptions
            Order order = new Order();
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.addItem(new ExtraItem("Coke"), 1);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.removeItem(0);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
    }

}
interface Item{
    int getPrice();
    String getType();
}
class ExtraItem implements Item{
    String type;
    public ExtraItem(String type) throws InvalidExtraTypeException {
        if (type != "Ketchup" && type != "Coke") {
            throw new InvalidExtraTypeException();
        }
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public int getPrice() {
        if (Objects.equals(type, "Coke")){
            return 5;
        }
        return 3;
    }
}
class PizzaItem implements Item{
    String type;
    public PizzaItem(String type) throws InvalidPizzaTypeException {
        if (type != "Pepperoni" && type != "Standard" && type != "Vegetarian") {
            throw new InvalidPizzaTypeException();
        }
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public int getPrice() {
        if (Objects.equals(type, "Standard")){
            return 10;
        }
        else if (Objects.equals(type, "Pepperoni")){
            return 12;
        }
        return 8;
    }
}
class Order{
    List<Item> items;
    List<Integer> counts;
    boolean locked;
    public Order(){
        items = new LinkedList<>();
        counts = new LinkedList<>();
        locked = false;
    }
    public void addItem(Item item, int count) throws ItemOutOfStockException, OrderLockedException {
        if (locked){
            throw new OrderLockedException();
        }
        if (count > 10){
            throw new ItemOutOfStockException(item);
        }
        for(Item i : items){
            if (Objects.equals(i.getType(), item.getType())){
                counts.set(items.indexOf(i), count);
                return;
            }
        }
        items.add(item);
        counts.add(count);
    }
    public int getPrice(){
        return items
                .stream()
                .mapToInt(x-> x.getPrice() * counts.get(items.indexOf(x)))
                .sum();
    }
    public void displayOrder(){
        for(int i = 1; i <= items.size(); i++){
            System.out.printf("%3d.%-15sx%2d%5d$", i, items.get(i).getType(), counts.get(i), items.get(i).getPrice());
        }
    }
    public void removeItem(int idx) throws ArrayIndexOutOfBoundsException, OrderLockedException {
        if (locked){
            throw new OrderLockedException();
        }
        if (idx < 0 || idx > items.size()){
            throw new ArrayIndexOutOfBoundsException(idx);
        }
        items.remove(idx);
        counts.remove(idx);
    }
    public void lock() throws EmptyOrder {
        if (items.size() < 1){
            throw new EmptyOrder();
        }
        locked = true;
    }
}
class InvalidPizzaTypeException extends Exception{
    public InvalidPizzaTypeException() {
        super();
    }
}
class InvalidExtraTypeException extends Exception{
    public InvalidExtraTypeException() {
        super();
    }
}
class ItemOutOfStockException extends Exception{

    public ItemOutOfStockException(Item item) {
        super(String.format("Item out of stock: %s", item.toString()));
    }
}
class ArrayIndexOutOfBoundsException extends Exception{
    public ArrayIndexOutOfBoundsException(int idx) {
        super(String.format("Index %d our of bounds", idx));
    }
}
class EmptyOrder extends Exception{
    public EmptyOrder() {
        super();
    }
}
class OrderLockedException extends Exception{
    public OrderLockedException() {
        super();
    }
}