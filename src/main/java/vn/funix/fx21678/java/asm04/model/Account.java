package vn.funix.fx21678.java.asm04.model;

import vn.funix.fx21678.java.asm04.common.IReport;
import vn.funix.fx21678.java.asm04.dao.TransactionDao;
import vn.funix.fx21678.java.asm04.service.BinaryFileService;

import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class Account implements Serializable, IReport{
    private static final String COMMA_DELIMITER = ",";
    private static final long serialVersionUID = 1L;


    private String accountNumber;
    private double balance;
    private String customerId;

    public Account(){
    }

    public Account(String customerId, String accountNumber, double balance){
        this.customerId = customerId;
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public Account(String[] arr){
        this.customerId = arr[0];
        this.accountNumber = arr[1];
        this.balance = Double.parseDouble(arr[2]);
    }

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

    public List<Transaction> getTransactions() throws IOException {
        List<Transaction> ls = TransactionDao.list();
        List<Transaction> rs = ls.stream().filter(item -> item.getAccountNumber().equals(this.accountNumber)).collect(Collectors.toList());
        return rs;
    }

    public void displayTransactionsList() throws IOException {
        List<Transaction> transactions = getTransactions();
        for (Transaction transaction: transactions){
            transaction.show();
        }
    }

    public boolean isPremium() {
        if (this.balance < 10000000)
            return false;
        return true;
    }

    @Override
    public void log(double amount, TransactionType type, String receiveAccount) {
        Locale locale = new Locale("vi", "VI");
        String pattern = "###,###,###,###";
        DecimalFormat dcf = (DecimalFormat) NumberFormat.getNumberInstance(locale);
        dcf.applyPattern(pattern);
        System.out.println("+------------------------------------------------------+");

        System.out.println("|                BIEN LAI GIAO DICH SAVINGS            |");
        System.out.printf("|%-24s%30s|\n", "Ngay GD: ", String.valueOf(LocalDateTime.now()));
        System.out.printf("|%-24s%30s|\n", "ATM ID: ", "DIGITAL-BANK-ATM " + LocalDate.now().getYear());
        System.out.printf("|%-24s%30s|\n", "So TK: ", this.getAccountNumber());
        if (type.equals(TransactionType.TRANSFERS)) {
            System.out.printf("|%-24s%30s|\n", "So TK NHAN: ", receiveAccount);
            System.out.printf("|%-24s%30s|\n", "So tien chuyen: ", dcf.format(amount) + "đ");
        } else
            System.out.printf("|%-24s%30s|\n", "So tien rut: ", dcf.format(amount) + "đ");
        System.out.printf("|%-24s%30s|\n", "So du: ", dcf.format(this.getBalance()) + "đ");
        System.out.printf("|%-24s%30s|\n", "Phi + VAT:", "0đ");
        System.out.println("+------------------------------------------------------+");
    }
}
