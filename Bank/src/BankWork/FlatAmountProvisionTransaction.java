package BankWork;

public class FlatAmountProvisionTransaction extends Transaction implements Parser {
    private String flatProvision ;

    public FlatAmountProvisionTransaction(long fromId, long toId, String amount, String flatProvision) {
        super(fromId, toId, amount, "FlatAmount");
        this.flatProvision = flatProvision;
    }

    public String getFlatProvision() {
        return flatProvision;
    }
    @Override
    public double getProvision() {
        return Parser.parseStringToDouble(flatProvision);
    }
}
