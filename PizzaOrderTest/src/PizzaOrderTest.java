import java.util.Arrays;
import java.util.Scanner;

interface Item{
    int getPrice();
    String getType();
}
class InvalidExtraTypeException extends Exception{

}
class InvalidPizzaTypeException extends Exception{

}
class ItemOutOfStockException extends Exception{
    Item item;

    public ItemOutOfStockException(Item item) {
        this.item = item;
    }
}
class ArrayIndexOutOfBоundsException extends Exception{
    int index;

    public ArrayIndexOutOfBоundsException(int index)
    {
        this.index = index;
    }
}
class EmptyOrder extends Exception{

}
class OrderLockedException extends Exception{

}
class ExtraItem implements Item{
    private String type;
    private int price;

    public ExtraItem(String type) throws InvalidExtraTypeException{
        if(!type.equals("Ketchup") && !type.equals("Coke")) {
            throw new InvalidExtraTypeException();
        }
        else if(type.equals("Ketchup")){
            price=3;
        }
        else {
            price=5;
        }
        this.type = type;
    }
    public int getPrice(){
        return price;
    }
    public String getType(){
        return type;
    }
}
class PizzaItem implements Item{
    private String type;
    private int price;

    public PizzaItem(String type) throws InvalidPizzaTypeException{
        if(!type.equals("Standard") && !type.equals("Pepperoni") && !type.equals("Vegetarian")) {
            throw new InvalidPizzaTypeException();
        }
        else if(type.equals("Standard")){
            price=10;
        }
        else if(type.equals("Pepperoni")){
            price=12;
        }
        else {
            price=8;
        }
        this.type = type;
    }
    public int getPrice(){
        return price;
    }
    public String getType(){
        return type;
    }
}

class Order{
    private Item[] items;
    private int[]quantity;
    boolean locked;

    public Order() {
        items=new Item[0];
        quantity=new int[0];
    }
    public int check(Item item){
        for(int i=0; i< items.length; i++){
            if(items[i].getType().equals(item.getType())){
                return i;
            }
        }
        return -1;
    }
    public void addItem(Item item, int count) throws ItemOutOfStockException, OrderLockedException{
        if(locked){
            throw new OrderLockedException();
        }
        if(count>10){
            throw new ItemOutOfStockException(item);
        }
        int a=check(item);
        if(a!=-1){
            items[a]=item;
            quantity[a]=count;
        }
        else {
            items = Arrays.copyOf(items, items.length + 1);
            quantity = Arrays.copyOf(quantity, quantity.length + 1);
            items[items.length - 1] = item;
            quantity[quantity.length - 1] = count;
        }
    }
    public int getPrice(){
        int sum=0;
        for(int i=0; i<items.length; i++){
            sum+=(items[i].getPrice()*quantity[i]);
        }
        return sum;
    }
    public void displayOrder(){
        for(int i=0; i< items.length; i++){
            System.out.format("%3d.%-14s x%2d%5d$", i+1, items[i].getType(), quantity[i], items[i].getPrice()*quantity[i]);
            System.out.println();
        }
        System.out.format("%-22s%5d$", "Total:", getPrice());
        System.out.println();
    }
    public void removeItem(int idx)throws ArrayIndexOutOfBоundsException, OrderLockedException{
        if(locked){
            throw new OrderLockedException();
        }
        if(idx>items.length || idx<0){
            throw new ArrayIndexOutOfBoundsException(idx);
        }
        for(int i=idx+1; i<items.length; i++){
            items[i-1]=items[i];
        }
        items=Arrays.copyOf(items, items.length-1);
    }
    public void lock() throws EmptyOrder
    {
        if(items.length < 1)
        {
            throw new EmptyOrder();
        }
        locked = true;
    }
}

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