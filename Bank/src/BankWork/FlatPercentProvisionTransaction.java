package BankWork;
import java.util.Objects;

public class FlatPercentProvisionTransaction extends Transaction implements Parser {
    private int centsPerDollar;

    public FlatPercentProvisionTransaction(long fromId, long toId, String amount, int centsPerDollar) {
        super(fromId, toId, amount, "FlatPercent");
        this.centsPerDollar = centsPerDollar;
    }

    public int getCentsPerDollar() {
        return centsPerDollar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof FlatPercentProvisionTransaction)) return false;
        return super.equals(o);
    }
    @Override
    public double getProvision() {
        return (centsPerDollar/100.0)*(int)Parser.parseStringToDouble(super.getAmount());
    }

    @Override
    public int hashCode() {
        return Objects.hash(centsPerDollar);
    }
}
