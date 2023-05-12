package vn.funix.fx21678.java.asm04.model;

import vn.funix.fx21678.java.asm04.service.BinaryFileService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Account implements Serializable {
    private static final long serialVersionUID = 1L;

    private String accountNumber;
    private double balance;
    private String customerId;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Customer getCustomer(){
        return null;
    }

    public List<Transaction> getTransactions(){
        List<Transaction> ls = BinaryFileService.readFile("store/transaction.dat");
        List<Transaction> rs = ls.stream().filter(item -> item.getAccountNumber().equals(this.accountNumber)).collect(Collectors.toList());
        return rs;
    }

    public void displayTransactionsList(){
        List<Transaction> transactions = getTransactions();
        for (Transaction transaction: transactions){
            System.out.println();
        }
    }

    public boolean isPremium() {
        if (this.balance < 10000000)
            return false;
        return true;
    }
}
