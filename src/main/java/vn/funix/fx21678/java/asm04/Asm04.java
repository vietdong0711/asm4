package vn.funix.fx21678.java.asm04;

import vn.funix.fx21678.java.asm04.dao.AccountDao;
import vn.funix.fx21678.java.asm04.dao.CustomerDao;
import vn.funix.fx21678.java.asm04.dao.TransactionDao;
import vn.funix.fx21678.java.asm04.exception.CustomerIdNotValidException;
import vn.funix.fx21678.java.asm04.model.Account;
import vn.funix.fx21678.java.asm04.model.Customer;
import vn.funix.fx21678.java.asm04.model.Transaction;
import vn.funix.fx21678.java.asm04.model.TransactionType;
import vn.funix.fx21678.java.asm04.model.User;
import vn.funix.fx21678.java.asm04.service.TextFileService;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Asm04 {
    private static Scanner scanner = new Scanner(System.in);
    private static List<String> lsCustomerId = new ArrayList<>();
    private static List<String> lsAccountId = new ArrayList<>();
    private static List<Account> lsAccount = new ArrayList<>();
    private static List<Transaction> transactions = new ArrayList<>();
    private static Map<Customer, List<Account>> map = new HashMap<>();

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        while (true) {
            loadListCustomerId();
            System.out.println("+----------+-------------------------+---------+");
            System.out.println("| NGAN HANG SO | FX21678@v4.0.0                |");
            System.out.println("+----------+-------------------------+---------+");
            System.out.printf("|%-46s|\n", "1. Xem danh sach khach hang");
            System.out.printf("|%-46s|\n", "2. Nhap danh sach khach hang");
            System.out.printf("|%-46s|\n", "3. Them tai khoan ATM");
            System.out.printf("|%-46s|\n", "4. Chuyen tien");
            System.out.printf("|%-46s|\n", "5. Rut tien");
            System.out.printf("|%-46s|\n", "6. Tra cuu lich su giao dich");
            System.out.printf("|%-46s|\n", "0. Thoát");
            System.out.println("+----------+-------------------------+---------+");
            System.out.println("Chuc nang: ");
            String key = scanner.nextLine();
            switch (key) {
                case "1":
                    System.out.println("Xem ds");
                    showCustomer();
                    break;
                case "2":
                    System.out.println("Nhap ds khach hang. Nhap dia chi tep: ");
                    String path = scanner.nextLine();
                    uploadCustomers(path);
                    break;
                case "3":
                    System.out.println("them tai khoan ATM");
                    themATM();
                case "4":
                    System.out.println("chuyen tien");
                    chuyentien();
                case "6":
                    System.out.println("tra cuu lich su");
                    getHistoryByCustomerId();

                default:
            }
        }
    }

    public static void getHistoryByCustomerId(){
        System.out.println("Nhap ma so khach hang: ");
        String customerId = scanner.nextLine();
//        List<Transaction> ls = transactions.stream().map(item -> item.getAccountNumber())
    }

    public static void loadListCustomerId() throws IOException {
        List<Customer> customers = CustomerDao.list();
        lsCustomerId = customers.stream().map(User::getCustomerId).collect(Collectors.toList());
        lsAccount = AccountDao.list();
        lsAccountId = lsAccount.stream().map(Account::getAccountNumber).collect(Collectors.toList());
        transactions = TransactionDao.list();
        for (Customer cus : customers) {
            map.put(cus, lsAccount.stream().filter(item -> item.getCustomerId().equals(cus.getCustomerId())).collect(Collectors.toList()));
        }
    }


    public static void showCustomer() throws IOException {
        List<Customer> customers = CustomerDao.list();
        List<Account> accounts = AccountDao.list();
        Map<String, List<Account>> listMap = accounts.stream().collect(Collectors.groupingBy(Account::getCustomerId));

        for (Customer cus : customers) {
            cus.setAccounts(listMap.get(cus.getCustomerId()));
            cus.displayInformation();
        }
    }

    public static void uploadCustomers(String part) throws IOException {
        File file = new File(part);
        if (!file.exists()) {
            System.out.println("File nay ko ton tai.");
            return;
        }
        List<List<String>> lists = TextFileService.readFile(part);
        List<Customer> customersInput = lists.stream().map(item -> new Customer(item.get(0), item.get(1))).collect(Collectors.toList());
        List<Customer> customers = CustomerDao.list();
        List<String> customerIds = customers.stream().map(User::getCustomerId).collect(Collectors.toList());
        for (Customer cus : customersInput) {
            if (!CustomerIdNotValidException.checkCCCD(cus.getCustomerId())) {
                System.out.println("Customer co id: " + cus.getCustomerId() + " sai thong tin");
                continue;
            } else if (customerIds.contains(cus.getCustomerId())) {
                System.out.println("Customer co id: " + cus.getCustomerId() + " da ton tai");
                continue;
            } else {
                System.out.println("Da them moi tai khoan co id: " + cus.getCustomerId());
                customers.add(cus);
            }
        }
        CustomerDao.save(customers);
    }

    public static void themATM() throws IOException {
        System.out.println("Nhap ma so khach hang: ");
        String customerId = scanner.nextLine();
        if (!lsCustomerId.contains(customerId)) {
            System.out.println("Khong tim thay khach hang " + customerId + ", tac vu khong thanh cong");
            return;
        }
        String accountNumber = inputAccountNumber();
        double balance = inputBalance();
        Account account = new Account(customerId, accountNumber, balance);
        AccountDao.update(account);
        System.out.println("Them tai khoan " + accountNumber + " thanh cong");
    }

    public static void chuyentien() throws IOException {
        System.out.println("Nhap ma so khach hang: ");
        String customerId = scanner.nextLine();
        if (!lsCustomerId.contains(customerId)) {
            System.out.println("Khong tim thay khach hang " + customerId + ", tac vu khong thanh cong");
            return;
        }
        Customer customer = getByCustomerId(customerId);
        customer.displayInformation();
        String accChuyen = "";
        String accNhan = "";
        List<String> accountNumbers = customer.getAccounts().stream().map(Account::getAccountNumber).collect(Collectors.toList());
        while (true) {
            System.out.println("Nhap so tai khoan: ");
            accChuyen = scanner.nextLine();

            if (!accountNumbers.contains(accChuyen))
                System.out.println("Tai khoan khong dung");
            else
                break;
        }
        while (true) {
            System.out.print("Nhap so tai khoan nhan(exit de thoat): ");
            accNhan = scanner.nextLine();
            switch (accNhan) {
                case "exit":
                    System.out.println("Thoat");
                    return;
                default:
                    if (!lsAccountId.contains(accNhan) || accChuyen.equals(accNhan))
                        System.out.print("Tai khoan khong dung, hoac trung vơi tk nhận. Nhap lại: ");
                    else
                        System.out.println("Gui tien den so tai khoan " + accNhan + " | " + getByAccountNumber(accNhan).getName());
                    break;
            }
            break;
        }

        String finalAccChuyen = accChuyen;
        String finalAccNhan = accNhan;
        Account accountChuyen = lsAccount.stream().filter(item -> item.getAccountNumber().equals(finalAccChuyen)).findFirst().get();
        Account accountNhan = lsAccount.stream().filter(item -> item.getAccountNumber().equals(finalAccNhan)).findFirst().get();
        double sotien = inputBalanceChuyen(accountChuyen);

        while (true){
            System.out.print("Xac nhan chuyen "+ sotien + " từ tai khoan ["+ accountChuyen+ "] den tai khoan ["+accountNhan+"] (Y/N): " );
            String choose = scanner.nextLine();
            switch (choose){
                case "Y":
                    break;
                case "N":
                    return;
                default:
                    System.out.println("Nhap lai: ");
                    continue;
            }
            break;
        }


        accountNhan.setBalance(accountNhan.getBalance() + sotien);
        accountChuyen.setBalance(accountChuyen.getBalance() - sotien);

        AccountDao.update(accountNhan);
        AccountDao.update(accountChuyen);
        System.out.println("Chuyen tien thanh cong");

        String localDateTime = String.valueOf(LocalDateTime.now());
        //luu lai ls
        Transaction transactionNhan = new Transaction(String.valueOf(transactions.size()), accNhan, sotien, localDateTime, true, TransactionType.TRANSFERS);
        Transaction transactionChuyen = new Transaction(String.valueOf(transactions.size()), accNhan, -sotien, localDateTime, true, TransactionType.TRANSFERS);
        transactions.add(transactionChuyen);
        transactions.add(transactionNhan);
        TransactionDao.save(transactions);
    }


    public static Customer getByCustomerId(String customerId) throws IOException {
        List<Customer> customers = CustomerDao.list();
        List<Account> accounts = AccountDao.list();
        Map<String, List<Account>> listMap = accounts.stream().collect(Collectors.groupingBy(Account::getCustomerId));
        Customer customer = customers.stream().filter(item -> item.getCustomerId().equals(customerId)).findFirst().get();

        customer.setAccounts(listMap.get(customerId));
        return customer;
    }

    public static Customer getByAccountNumber(String accountNumber) {
//        Customer customer = map.keySet().stream().filter(item -> map.get(item).stream().filter(i -> i.getAccountNumber().equals(accountNumber)).findFirst().get().getCustomerId().equals(item.getCustomerId())).findFirst().get();
        Customer customer = map.keySet().stream().filter(item -> map.get(item).stream().anyMatch(i -> i.getAccountNumber().equals(accountNumber))).findFirst().get();
        return customer;
    }

    public static String inputAccountNumber() {
        while (true) {
            System.out.print("Nhap tai khoan gom 6 chu: ");
            String accountNumber = scanner.nextLine();
            //check do dai
            if (accountNumber.trim().length() != 6)
                continue;
            //check full so
            String[] strings = accountNumber.trim().split("");
            for (String s : strings) {
                try {
                    Integer.parseInt(s);
                } catch (Exception e) {
                    continue;
                }
            }
            //check accountnumber ton tai chua
            if (lsAccountId.contains(accountNumber)) {
                System.out.println("account number này đã tồn tại");
                continue;
            }
            return accountNumber;
        }
    }

    public static double inputBalanceChuyen(Account account) {
        while (true) {
            System.out.print("Nhap tien muon chuyen(so du sau khi chuyen >= 50000đ): ");
            String sotienString = scanner.nextLine();
            try {
                double sotien = Double.parseDouble(sotienString);
                if (account.getBalance() - sotien < 50000)
                    continue;
                return sotien;
            } catch (Exception e) {
                continue;
            }
        }
    }

    public static double inputBalance() {
        while (true) {
            System.out.print("Nhap so du tai khoan >= 50000đ: ");
            String balace = scanner.nextLine();
            // check full so
            String[] strings = balace.trim().split("");
            for (String s : strings) {
                try {
                    Integer.parseInt(s);
                } catch (Exception e) {
                    continue;
                }
            }
            if (Double.parseDouble(balace) < 50000)
                continue;
            return Double.parseDouble(balace);
        }
    }


}
