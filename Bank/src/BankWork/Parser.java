package BankWork;

public interface Parser {
    static double parseStringToDouble(String num) {
        return Double.parseDouble(num.substring(0, num.length() - 1));
    }
}
