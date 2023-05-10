package vn.funix.fx21678.java.asm04.dao;

import vn.funix.fx21678.java.asm04.model.Account;

import java.util.ArrayList;
import java.util.List;

public class AccountDao {

    public static void update(Account editAccount) {

        List<Account> accounts = new ArrayList<>();
        boolean hasExist = accounts.stream().anyMatch(account -> account.getAccountNumber().equals(editAccount.getAccountNumber()));

        List<Account> updateAccounts;
        if (!hasExist) {
            updateAccounts = new ArrayList<>();
            updateAccounts.add(editAccount);
        } else {
            updateAccounts = new ArrayList<>();
            for (Account account : accounts) {
                if (account.getAccountNumber().equals(editAccount.getAccountNumber()))
                    updateAccounts.add(editAccount);
                else
                    updateAccounts.add(account);
            }
        }
        // save(updateAccounts);
    }
}
