package BankWork;

import java.util.Arrays;

public class Bank implements Parser {
    private String name;
    private Account[] accounts;
    private double totalTransfer;
    private double totalProvision;

    public Bank(String name, Account[] accounts) {
        this.name = name;
        this.accounts=new Account[accounts.length];
        for(int i=0; i<accounts.length; i++)
        {
            this.accounts[i]=accounts[i];
        }
        this.totalProvision=0;
        this.totalTransfer=0;
    }
    private int findId(long id)
    {
        for(int i=0; i<accounts.length; i++)
        {
            if(accounts[i].getId()==id)
            return i;
        }
        return -1;
    }
    public boolean makeTransaction(Transaction t)
    {
        int fromIndex=findId(t.getFromId());
        int toIndex=findId(t.getToId());
        if(fromIndex==-1 || toIndex==-1)
            return false;
        double balanceFrom=Parser.parseStringToDouble(accounts[fromIndex].getBalance());
        double balanceTo=Parser.parseStringToDouble(accounts[toIndex].getBalance());
        double amount=Parser.parseStringToDouble(t.getAmount());
        if(balanceFrom<amount)
            return false;
        double provision=t.getProvision();
        totalTransfer += amount;
        totalProvision += provision;
        balanceFrom -= (amount + provision);
        if(fromIndex==toIndex)
            balanceTo-=provision;
        else
            balanceTo += amount;
        accounts[fromIndex].setBalance(String.format("%.2f", balanceFrom) + "$");
        accounts[toIndex].setBalance(String.format("%.2f", balanceTo) + "$");
        return true;
    }
    public String totalTransfers() {
        return String.format("%.2f", totalTransfer) + "$";
    }

    public String totalProvision() {
        return String.format("%.2f", totalProvision) + "$";
    }

    public Account[] getAccounts() {
        return accounts;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ");
        sb.append(name);
        sb.append("\n\n");
        for (Account account : accounts)
            sb.append(account);
        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hash = 1;
        hash = hash * prime + name.hashCode();
        hash = hash * prime + (int) totalTransfer;
        hash = hash * prime + (int) totalProvision;
        hash = hash * prime + Arrays.hashCode(accounts);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        Bank bank = (Bank) obj;
        return this.name.equals(bank.name) && Arrays.equals(this.accounts, bank.accounts)
                && this.totalTransfer == bank.totalTransfer && this.totalProvision == bank.totalProvision
                && this.hashCode() == bank.hashCode();
    }
}
