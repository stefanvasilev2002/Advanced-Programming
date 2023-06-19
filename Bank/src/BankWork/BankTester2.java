
import java.text.DecimalFormat;
import java.util.*;
import java.util.Random;

import java.util.stream.Collectors;

public class BankTester2 {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        String test_type = jin.nextLine();
        switch (test_type) {
            case "typical_usage":
                testTypicalUsage(jin);
                break;
            case "equals":
                testEquals();
                break;
        }
        jin.close();
    }

    private static void testEquals() {
        Account a1 = new Account("Andrej", "20.00$");
        Account a2 = new Account("Andrej", "20.00$");
        Account a3 = new Account("Andrej", "30.00$");
        Account a4 = new Account("Gajduk", "20.00$");
        List<Account> all = Arrays.asList(a1, a2, a3, a4);
        if (!(a1.equals(a1)&&!a1.equals(a2)&&!a2.equals(a1) && !a3.equals(a1)
                && !a4.equals(a1)
                && !a1.equals(null))) {
            System.out.println("Your account equals method does not work properly.");
            return;
        }
        Set<Long> ids = all.stream().map(Account::getId).collect(Collectors.toSet());
        if (ids.size() != all.size()) {
            System.out.println("Different accounts have the same IDS. This is not allowed");
            return;
        }
        FlatAmountProvisionTransaction fa1 = new FlatAmountProvisionTransaction(10, 20, "20.00$", "10.00$");
        FlatAmountProvisionTransaction fa2 = new FlatAmountProvisionTransaction(20, 20, "20.00$", "10.00$");
        FlatAmountProvisionTransaction fa3 = new FlatAmountProvisionTransaction(20, 10, "20.00$", "10.00$");
        FlatAmountProvisionTransaction fa4 = new FlatAmountProvisionTransaction(10, 20, "50.00$", "50.00$");
        FlatAmountProvisionTransaction fa5 = new FlatAmountProvisionTransaction(30, 40, "20.00$", "10.00$");
        FlatPercentProvisionTransaction fp1 = new FlatPercentProvisionTransaction(10, 20, "20.00$", 10);
        FlatPercentProvisionTransaction fp2 = new FlatPercentProvisionTransaction(10, 20, "20.00$", 10);
        FlatPercentProvisionTransaction fp3 = new FlatPercentProvisionTransaction(10, 10, "20.00$", 10);
        FlatPercentProvisionTransaction fp4 = new FlatPercentProvisionTransaction(10, 20, "50.00$", 10);
        FlatPercentProvisionTransaction fp5 = new FlatPercentProvisionTransaction(10, 20, "20.00$", 30);
        FlatPercentProvisionTransaction fp6 = new FlatPercentProvisionTransaction(30, 40, "20.00$", 10);
        if (fa1.equals(fa1) &&
                !fa2.equals(null) &&
                fa2.equals(fa1) &&
                fa1.equals(fa2) &&
                fa1.equals(fa3) &&
                !fa1.equals(fa4) &&
                !fa1.equals(fa5) &&
                !fa1.equals(fp1) &&
                fp1.equals(fp1) &&
                !fp2.equals(null) &&
                fp2.equals(fp1) &&
                fp1.equals(fp2) &&
                fp1.equals(fp3) &&
                !fp1.equals(fp4) &&
                !fp1.equals(fp5) &&
                !fp1.equals(fp6)) {
            System.out.println("Your transactions equals methods do not work properly.");
            return;
        }
        Account accounts[] = new Account[]{a1, a2, a3, a4};
        Account accounts1[] = new Account[]{a2, a1, a3, a4};
        Account accounts2[] = new Account[]{a1, a2, a3};
        Account accounts3[] = new Account[]{a1, a2, a3, a4};

        Bank b1 = new Bank("Test", accounts);
        Bank b2 = new Bank("Test", accounts1);
        Bank b3 = new Bank("Test", accounts2);
        Bank b4 = new Bank("Sample", accounts);
        Bank b5 = new Bank("Test", accounts3);

        if (!(b1.equals(b1) &&
                !b1.equals(null) &&
                !b1.equals(b2) &&
                !b2.equals(b1) &&
                !b1.equals(b3) &&
                !b3.equals(b1) &&
                !b1.equals(b4) &&
                b1.equals(b5))) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        //accounts[2] = a1;
        if (!b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        long from_id = a2.getId();
        long to_id = a3.getId();
        Transaction t = new FlatAmountProvisionTransaction(from_id, to_id, "3.00$", "3.00$");
        b1.makeTransaction(t);
        if (b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        b5.makeTransaction(t);
        if (!b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        System.out.println("All your equals methods work properly.");
    }

    private static void testTypicalUsage(Scanner jin) {
        String bank_name = jin.nextLine();
        int num_accounts = jin.nextInt();
        jin.nextLine();
        Account accounts[] = new Account[num_accounts];
        for (int i = 0; i < num_accounts; ++i)
            accounts[i] = new Account(jin.nextLine(), jin.nextLine());
        Bank bank = new Bank(bank_name, accounts);
        while (true) {
            String line = jin.nextLine();
            switch (line) {
                case "stop":
                    return;
                case "transaction":
                    String descrption = jin.nextLine();
                    String amount = jin.nextLine();
                    String parameter = jin.nextLine();
                    int from_idx = jin.nextInt();
                    int to_idx = jin.nextInt();
                    jin.nextLine();
                    Transaction t = getTransaction(descrption, from_idx, to_idx, amount, parameter, bank);
                    System.out.println("Transaction amount: " + t.getAmount());
                    System.out.println("Transaction description: " + t.getDescription());
                    System.out.println("Transaction successful? " + bank.makeTransaction(t));
                    break;
                case "print":
                    System.out.println(bank.toString());
                    System.out.println("Total provisions: " + bank.totalProvision());
                    System.out.println("Total transfers: " + bank.totalTransfers());
                    System.out.println();
                    break;
            }
        }
    }

    private static Transaction getTransaction(String description, int from_idx, int to_idx, String amount, String o, Bank bank) {
        switch (description) {
            case "FlatAmount":
                return new FlatAmountProvisionTransaction(bank.getAccounts()[from_idx].getId(),
                        bank.getAccounts()[to_idx].getId(), amount, o);
            case "FlatPercent":
                return new FlatPercentProvisionTransaction(bank.getAccounts()[from_idx].getId(),
                        bank.getAccounts()[to_idx].getId(), amount, Integer.parseInt(o));
        }
        return null;
    }

}
class Account{
    String name;
    long id;
    double balance;
    public Account(String name, String balance){
        this.name = name;
        this.balance = Double.parseDouble(balance.substring(0, balance.length() - 1));
        Random random = new Random();
        this.id = random.nextLong();
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        DecimalFormat format = new DecimalFormat("0.00");
        return String.format("Name: %s\nBalance: %s$\n", name, format.format(balance));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (id != account.id) return false;
        if (Double.compare(account.balance, balance) != 0) return false;
        return Objects.equals(name, account.name);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = name != null ? name.hashCode() : 0;
        result = 31 * result + (int) (id ^ (id >>> 32));
        temp = Double.doubleToLongBits(balance);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
abstract class Transaction{
    long fromId;
    long toId;
    String description;
    double amount;

    public Transaction(long fromId, long toId, String description, String amount) {
        this.fromId = fromId;
        this.toId = toId;
        this.description = description;
        this.amount = Double.parseDouble(amount.substring(0, amount.length() - 1));
    }
    public long getFromId() {
        return fromId;
    }

    public long getToId() {
        return toId;
    }

    public String getAmount() {
        DecimalFormat format = new DecimalFormat("0.00");
        return format.format(amount) + "$";
    }

    public String getDescription() {
        return description;
    }

    public double getAmountDouble() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transaction that = (Transaction) o;

        if (fromId != that.fromId) return false;
        if (toId != that.toId) return false;
        if (Double.compare(that.amount, amount) != 0) return false;
        return Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (fromId ^ (fromId >>> 32));
        result = 31 * result + (int) (toId ^ (toId >>> 32));
        result = 31 * result + (description != null ? description.hashCode() : 0);
        temp = Double.doubleToLongBits(amount);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
class FlatAmountProvisionTransaction extends Transaction{
    double flatAmount;
    FlatAmountProvisionTransaction(long fromId, long toId, String amount, String flatProvision){
        super(fromId, toId, "FlatAmount", amount);
        this.flatAmount = Double.parseDouble(flatProvision.substring(0, flatProvision.length() - 1));
    }

    public double getFlatAmount() {
        return flatAmount;
    }

}
class FlatPercentProvisionTransaction extends Transaction{
    double percent;
    public FlatPercentProvisionTransaction(long fromId, long toId, String amount, int percent) {
        super(fromId, toId, "FlatPercent", amount);
        this.percent = percent / 100.00;
    }
    public double getPercent() {
        return percent;
    }
}
class Bank{
    String name;
    Map<Long, Account> accounts;
    List<Account> accountsOrder;
    List<Transaction>transactions;

    public Bank(String name, Account[]accounts) {
        this.name = name;
        this.accounts = new HashMap<>();
        this.accountsOrder = new ArrayList<>();
        for (Account account : accounts){
            this.accounts.put(account.getId(), account);
            this.accountsOrder.add(account);
        }
        this.transactions = new ArrayList<>();
    }
    public boolean makeTransaction(Transaction t){
        if (!accounts.containsKey(t.getFromId()) || !accounts.containsKey(t.getToId())){
            return false;
        }
        double provision = 0;
        if (t instanceof FlatPercentProvisionTransaction){
            provision = t.amount * ((FlatPercentProvisionTransaction) t).percent;
        }
        if (t instanceof FlatAmountProvisionTransaction){
            provision = ((FlatAmountProvisionTransaction) t).flatAmount;
        }
        if (t.amount + provision > accounts.get(t.getFromId()).getBalance()){
            return false;
        }

        accounts.get(t.getFromId()).setBalance(accounts.get(t.getFromId()).getBalance() - t.amount);
        if (t instanceof FlatPercentProvisionTransaction){
            accounts.get(t.getFromId()).setBalance(accounts.get(t.getFromId()).getBalance() - ((FlatPercentProvisionTransaction) t).getPercent()*t.amount);
        }
        if (t instanceof FlatAmountProvisionTransaction){
            accounts.get(t.getFromId()).setBalance(accounts.get(t.getFromId()).getBalance() - ((FlatAmountProvisionTransaction) t).getFlatAmount());
        }
        accounts.get(t.getToId()).setBalance(accounts.get(t.getToId()).getBalance() + t.amount);
        transactions.add(t);
        return true;
    }
    public String totalTransfers(){
        Double sum = transactions
                .stream()
                .mapToDouble(Transaction::getAmountDouble)
                .sum();
        DecimalFormat format = new DecimalFormat("0.00");
        return format.format(sum) + "$";
    }
    public String totalProvision(){
        double sum = 0;
        for(Transaction t : transactions){
            if(t instanceof FlatAmountProvisionTransaction){
                sum += ((FlatAmountProvisionTransaction) t).getFlatAmount();
            }
            else if( t instanceof FlatPercentProvisionTransaction){
                sum += (int)t.amount * ((FlatPercentProvisionTransaction) t).getPercent();
            }
        }
        DecimalFormat format = new DecimalFormat("0.00");
        return format.format(sum) + "$";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bank bank = (Bank) o;
        return Objects.equals(name, bank.name) &&
                Arrays.equals(accountsOrder.toArray(new Account[0]), bank.accountsOrder.toArray(new Account[0]));
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (accounts != null ? accounts.hashCode() : 0);
        result = 31 * result + (transactions != null ? transactions.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Name: %s\n\n", name));
        for (Account c : accountsOrder){
            sb.append(c.toString());
        }
        return sb.toString();
    }

    public Account[] getAccounts() {
        return accountsOrder.toArray(new Account[0]);
    }
}