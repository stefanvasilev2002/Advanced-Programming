import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MojDDVTest {

    public static void main(String[] args) {

        MojDDV mojDDV = new MojDDV();

        System.out.println("===READING RECORDS FROM INPUT STREAM===");
        mojDDV.readRecords(System.in);

        System.out.println("===PRINTING TAX RETURNS RECORDS TO OUTPUT STREAM ===");
        mojDDV.printTaxReturns(System.out);
        
        System.out.println("===PRINTING SUMMARY STATISTICS FOR TAX RETURNS TO OUTPUT STREAM===");
        mojDDV.printStatistics(System.out);
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
    public float getTax() {
        float tax=0;
        for(int i=0; i<products.size(); i++){
            Product temp=products.get(i);
            if(temp.getType().equals("A")){
                tax+=(float)temp.getPrice()*0.18;
            }
            else if(temp.getType().equals("B")){
                tax+=(float)temp.getPrice()*0.05;
            }
        }
        return (float) (tax*0.15);
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
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());

    }
    public void printTaxReturns (OutputStream outputStream){
        PrintWriter pw= new PrintWriter(new OutputStreamWriter(outputStream));
        //ID SUM_OF_AMOUNTS TAX_RETURN
        for(int i=0; i<receipts.size(); i++){
            Receipt temp=receipts.get(i);
            if(temp.getSum()==0){
                continue;
            }
            pw.println(String.format("%10s\t%10d\t%10.5f", temp.getID(), temp.getSum(), temp.getTax()));
        }
        pw.flush();
    }

    public void printStatistics(OutputStream outputStream) {
        PrintWriter pw= new PrintWriter(new OutputStreamWriter(outputStream));
        float min=Float.MAX_VALUE, max=0, avg, sum=0;
        int count=receipts.size();
        for(int i=0; i<receipts.size(); i++){
            Receipt temp=receipts.get(i);
            if(temp.getSum()==0){
                count--;
                continue;
            }
            if(min>temp.getTax()){
                min=temp.getTax();
            }
            if(max<temp.getTax()){
                max=temp.getTax();
            }
            sum+=temp.getTax();
        }
        avg=sum/(float)count;
        pw.println(String.format("min:\t%4.3f", min));
        pw.println(String.format("max:\t%4.3f", max));
        pw.println(String.format("sum:\t%4.3f", sum));
        pw.println(String.format("count:\t%-5d", count));
        pw.println(String.format("avg:\t%4.3f", avg));
        pw.flush();
    }
}
class AmountNotAllowedException extends Exception{
    public AmountNotAllowedException(int message) {
        super("Receipt with amount "+message+" is not allowed to be scanned");
    }
}