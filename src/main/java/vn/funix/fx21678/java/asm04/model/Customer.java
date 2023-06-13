package vn.funix.fx21678.java.asm04.model;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Customer extends User implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String COMMA_DELIMITER = ",";
    List<Account> accounts;

    public Customer() {
    }

    public Customer(String[] arr) {
        this.setCustomerId(arr[0]);
        this.setName(arr[1]);
    }

    public Customer(String id, String name) {
        this.setCustomerId(id);
        this.setName(name);
    }

    public Customer(List<String> ls) {
        this.setCustomerId(ls.get(0));
        this.setName(ls.get(1));
    }

    public boolean isPremium() {
        if (this.getAccounts() != null && !this.getAccounts().isEmpty()) {
            for (Account acc : accounts) {
                if (acc.isPremium())
                    return true;
            }
        }
        return false;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public void displayInformation() {
        Locale locale = new Locale("vi", "VI");
        String pattern = "###,###,###,###";
        DecimalFormat dcf = (DecimalFormat) NumberFormat.getNumberInstance(locale);
        dcf.applyPattern(pattern);
        double tong = 0;

        if (this.getAccounts() != null && !this.getAccounts().isEmpty()) {
            for (Account acc : this.getAccounts()) {
                tong += acc.getBalance();
            }
        }
        System.out.printf("%18s|%18s|%18s|%18s\n", this.getCustomerId(), this.getName(), this.isPremium() ? "Premium" : "Normal", dcf.format(tong) + "đ");
        int i = 1;
        if (this.getAccounts() != null && !this.getAccounts().isEmpty()) {
            for (Account acc : this.getAccounts()) {
                System.out.printf("%-3s%15s|%18s|%37s\n", i, acc.getAccountNumber(), "SAVINGS", dcf.format(acc.getBalance()) + "đ");
                i++;
            }
        }
    }

}
