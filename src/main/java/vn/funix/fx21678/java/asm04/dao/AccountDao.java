package vn.funix.fx21678.java.asm04.dao;

import vn.funix.fx21678.java.asm04.model.Account;
import vn.funix.fx21678.java.asm04.service.BinaryFileService;

import java.io.IOException;
import java.util.List;

public class AccountDao {

    private static final String FILE_PATH = "store/accounts.dat";
    public static void save(List<Account> accounts) throws IOException {
        BinaryFileService.writeFile(FILE_PATH, accounts);
    }

    public static List<Account> list() throws IOException {
        return BinaryFileService.readFile(FILE_PATH);
    }
}
