package vn.funix.fx21678.java.asm04.model;

import java.util.List;

public class Customer extends User {
    List<Account> accounts;

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }
}
