package BankWork;

import javax.swing.text.html.parser.Parser;
import java.util.Random;

public class Account {
    private String name;
    private long id;
    private String balance;

    public Account(String name, String balance) {
        this.name = name;
        this.balance = balance;
        Random random=new Random();
        this.id=random.nextLong();
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Name: "+name+"\nBalance: "+balance+"\n";
    }
}
