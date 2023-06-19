import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Scanner;

public class MojDDVTest {

    public static void main(String[] args) throws IOException {

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
    char typeDDV;

    public Product(int price, char typeDDV) {
        this.price = price;
        this.typeDDV = typeDDV;
    }
    public double getTax(){
        if (typeDDV == 'A'){
            return price * 0.18 * 0.15;
        } else if (typeDDV == 'B') {
            return price * 0.05 * 0.15;
        }
        return 0;
    }

    public int getPrice() {
        return price;
    }
}
class Receipt{
    List<Product> products;
    String id;
    public Receipt(String id, String[]parts) throws AmountNotAllowedException {
        this.products = new ArrayList<>();
        this.id = id;
        int sum = 0;
        for(int i = 1; i < parts.length; i+=2){
            sum+=Integer.parseInt(parts[i]);
        }

        if (sum > 30000){
            throw new AmountNotAllowedException(sum);
        }
        for(int i = 1; i < parts.length; i+=2){
            products.add(new Product(Integer.parseInt(parts[i]), parts[i+1].charAt(0)));
        }
    }
    public double getDDV(){
        return products.stream().mapToDouble(Product::getTax).sum();
    }
    @Override
    public String toString() {
        return String.format("%10s\t%10d\t%10.5f\n",
                id, products.stream().mapToInt(Product::getPrice).sum(),
                products.stream().mapToDouble(Product::getTax).sum());
    }
}
class MojDDV{
    List<Receipt> receipts;

    public MojDDV() {
        this.receipts = new ArrayList<>();
    }

    public void readRecords (InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNextLine()){
            String []parts = scanner.nextLine().split(" ");
            try{
                receipts.add(new Receipt(parts[0], parts));
            }catch (AmountNotAllowedException e){
                System.out.println(e.getMessage());
            }
        }
    }
    public void printTaxReturns (OutputStream outputStream) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(outputStream);

        receipts.stream().forEach(x-> {
            try {
                writer.append(x.toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        writer.flush();
    }
    public void printStatistics (OutputStream outputStream) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(outputStream);

        DoubleSummaryStatistics a = receipts
                .stream()
                .mapToDouble(Receipt::getDDV)
                .summaryStatistics();
        writer.append(String.format("min:\t%.3f\n", a.getMin()));
        writer.append(String.format("max:\t%.3f\n", a.getMax()));
        writer.append(String.format("sum:\t%.3f\n", a.getSum()));
        writer.append(String.format("count:\t%-5d\n", a.getCount()));
        writer.append(String.format("avg:\t%.3f\n", a.getAverage()));

        writer.flush();
    }
}
class AmountNotAllowedException extends Exception{
    public AmountNotAllowedException(int sum) {
        super(String.format("Receipt with amount %d is not allowed to be scanned", sum));
    }
}