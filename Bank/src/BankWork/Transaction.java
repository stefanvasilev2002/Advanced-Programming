package BankWork;

import javax.swing.text.html.parser.Parser;

abstract class Transaction{
    private long fromId;
    private long toId;
    private String amount;
    private String description;

    public Transaction(long fromId, long toId, String amount, String description) {
        this.fromId = fromId;
        this.toId = toId;
        this.amount = amount;
        this.description = description;
    }

    public long getFromId() {
        return fromId;
    }

    public long getToId() {
        return toId;
    }

    public String getAmount() {
        return amount;
    }
    public String getDescription() {
        return description;
    }
    public abstract double getProvision();
}
