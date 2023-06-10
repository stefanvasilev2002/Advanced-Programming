import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MojDDVTest {

    public static void main(String[] args) {

        MojDDV mojDDV = new MojDDV();

        System.out.println("===READING RECORDS FROM INPUT STREAM===");
        mojDDV.readRecords(System.in);

        System.out.println("===PRINTING TAX RETURNS RECORDS TO OUTPUT STREAM ===");
        mojDDV.printTaxReturns(System.out);

    }
}
class Product{
    int price;
    String type;

    public Product(int price, String type) {
        this.price=price;
        this.type=type;
    }

    public int getPrice() {
        return price;
    }
    public String getType() {
        return type;
    }
}
class Receipt{
    private List<Product> products;
    private String id;

    public Receipt() {
        products=new ArrayList<>();
        id="";
    }

    public Receipt(String line) throws AmountNotAllowedException {
        String[]parts=line.split("\\s+");
        id=parts[0];
        products=new ArrayList<Product>();
        int sum=0;
        for(int i=1; i<parts.length; i+=2){
            int price=Integer.parseInt(parts[i]);
            sum+=price;
        }
        if(sum>30000){
            throw new AmountNotAllowedException(sum);
        }
        for(int i=1; i<parts.length; i+=2){
            int price=Integer.parseInt(parts[i]);
            String type=parts[i+1];
            products.add(new Product(price, type));
        }
    }

    public String getID() {
        return id;
    }
    public int getSum() {
        int sum=0;
        for(int i=0; i<products.size(); i++){
            Product temp=products.get(i);
            sum+=temp.getPrice();
        }
        return sum;
    }
    public double getTax() {
        double tax=0;
        for(int i=0; i<products.size(); i++){
            Product temp=products.get(i);
            if(temp.getType().equals("A")){
                tax+=(double)temp.getPrice()*0.18;
            }
            else if(temp.getType().equals("B")){
                tax+=(double)temp.getPrice()*0.05;
            }
        }
        return tax*0.15;
    }
}
class MojDDV{
    List<Receipt> receipts;
    void readRecords (InputStream inputStream){
        BufferedReader bf=new BufferedReader(new InputStreamReader(inputStream));
        receipts=bf.lines().map(x-> {
            try {
                return new Receipt(x);
            } catch (AmountNotAllowedException e) {
                System.out.println(e.getMessage());
                return new Receipt();
            }
        }).collect(Collectors.toList());

    }
    public void printTaxReturns (OutputStream outputStream){
        PrintWriter pw= new PrintWriter(new OutputStreamWriter(outputStream));
        //ID SUM_OF_AMOUNTS TAX_RETURN
        for(int i=0; i<receipts.size(); i++){
            Receipt temp=receipts.get(i);
            if(temp.getSum()==0){
                continue;
            }
            pw.println(String.format("%s %d %.2f", temp.getID(), temp.getSum(), temp.getTax()));
        }
        pw.flush();
    }
}
class AmountNotAllowedException extends Exception{
    public AmountNotAllowedException(int message) {
        super("Receipt with amount "+message+" is not allowed to be scanned");
    }
}