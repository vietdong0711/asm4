package vn.funix.fx21678.java.asm04.model;

import vn.funix.fx21678.java.asm04.dao.AccountDao;
import vn.funix.fx21678.java.asm04.dao.CustomerDao;
import vn.funix.fx21678.java.asm04.dao.TransactionDao;
import vn.funix.fx21678.java.asm04.exception.CustomerIdNotValidException;
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

public class DigitalBank extends Bank {

    private Scanner scanner = new Scanner(System.in);
    private List<String> lsCustomerId = new ArrayList<>();
    private List<String> lsAccountId = new ArrayList<>();
    private List<Account> lsAccount = new ArrayList<>();
    private List<Transaction> transactions = new ArrayList<>();
    private Map<Customer, List<Account>> map = new HashMap<>();
    private List<Customer> customers = new ArrayList<>();

    public DigitalBank() throws IOException {
        loadData();
    }

    public void loadData() throws IOException {
        customers = CustomerDao.list();
        lsCustomerId = customers.stream().map(User::getCustomerId).collect(Collectors.toList());
        lsAccount = AccountDao.list();
        lsAccountId = lsAccount.stream().map(Account::getAccountNumber).collect(Collectors.toList());
        transactions = TransactionDao.list();
        for (Customer cus : customers) {
            map.put(cus, lsAccount.stream().filter(item -> item.getCustomerId().equals(cus.getCustomerId())).collect(Collectors.toList()));
        }
    }


    public void showCustomers() throws IOException {
        if (map.isEmpty()) {
            System.out.println("Chưa có khách hàng");
            return;
        }
        for (Customer cus : customers) {
            cus.setAccounts(map.get(cus));
            cus.displayInformation();
        }
    }

    public void addCustomers(String fileName) throws IOException {
        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("File nay ko ton tai.");
            return;
        }
        List<List<String>> lists = TextFileService.readFile(fileName);
        List<Customer> customersInput = lists.stream().map(item -> new Customer(item.get(0), item.get(1))).collect(Collectors.toList());
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

    public void addSavingAccount(String customerId) throws IOException {
        if (!lsCustomerId.contains(customerId)) {
            System.out.println("Khong tim thay khach hang " + customerId + ", tac vu khong thanh cong");
            return;
        }
        String accountNumber = inputAccountNumber();
        double balance = inputBalance();
        Account account = new Account(customerId, accountNumber, balance);
        AccountDao.update(account);
        List<Transaction> transactions = TransactionDao.list();
        Transaction transaction = new Transaction(String.valueOf(transactions.size()), accountNumber, balance, String.valueOf(LocalDateTime.now()), true, TransactionType.DEPOSIT);
        transactions.add(transaction);
        TransactionDao.save(transactions);
        System.out.println("Them tai khoan " + accountNumber + " thanh cong");
    }

    public double inputBalance() throws IOException {
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

    public String inputAccountNumber() throws IOException {
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

    public void withdraw(String customerId) throws IOException {
        if (!lsCustomerId.contains(customerId)) {
            System.out.println("Khong tim thay khach hang " + customerId + ", tac vu khong thanh cong");
            return;
        }
        Customer customer = getCustomerById(customerId);
        customer.displayInformation();
//        String accRut = "";
//        List<String> accountNumbers = customer.getAccounts().stream().map(Account::getAccountNumber).collect(Collectors.toList());
//        while (true) {
//            System.out.println("Nhap so tai khoan: ");
//            accRut = scanner.nextLine();
//
//            if (!accountNumbers.contains(accRut))
//                System.out.println("Tai khoan khong dung");
//            else
//                break;
//        }
//        String finalAccRut = accRut;
//        Account accountRut = lsAccount.stream().filter(item -> item.getAccountNumber().equals(finalAccRut)).findFirst().get();
//        String localDateTime = String.valueOf(LocalDateTime.now());
//        double sotien = inputBalanceChuyen(accountRut);
//        accountRut.setBalance(accountRut.getBalance() - sotien);
//        AccountDao.update(accountRut);
//
//        Transaction transaction = new Transaction();
//        if (sotien > 0)
//            transaction = new Transaction(String.valueOf(transactions.size()), finalAccRut, -sotien, localDateTime, true, TransactionType.WITHDRAW);
//        else
//            transaction = new Transaction(String.valueOf(transactions.size()), finalAccRut, -sotien, localDateTime, true, TransactionType.DEPOSIT);
//        transactions.add(transaction);
//        TransactionDao.save(transactions);
        customer.withdraw(scanner);
        System.out.println("Rut tien thanh cong, bien lai giao dich.");
//        accountRut.log(sotien, TransactionType.WITHDRAW, "");
    }

    public void tranfers(String customerId) throws IOException {
        if (!lsCustomerId.contains(customerId)) {
            System.out.println("Khong tim thay khach hang " + customerId + ", tac vu khong thanh cong");
            return;
        }
        Customer customer = getCustomerById(customerId);
        customer.displayInformation();
//        String accChuyen = "";
//        String accNhan = "";
//        List<String> accountNumbers = customer.getAccounts().stream().map(Account::getAccountNumber).collect(Collectors.toList());
//        while (true) {
//            System.out.println("Nhap so tai khoan: ");
//            accChuyen = scanner.nextLine();
//
//            if (!accountNumbers.contains(accChuyen))
//                System.out.println("Tai khoan khong dung");
//            else
//                break;
//        }
//        while (true) {
//            System.out.print("Nhap so tai khoan nhan(exit de thoat): ");
//            accNhan = scanner.nextLine();
//            switch (accNhan) {
//                case "exit":
//                    System.out.println("Thoat");
//                    return;
//                default:
//                    if (!lsAccountId.contains(accNhan) || accChuyen.equals(accNhan))
//                        System.out.print("Tai khoan khong dung, hoac trung vơi tk nhận. Nhap lại: ");
//                    else
//                        System.out.println("Gui tien den so tai khoan " + accNhan + " | " + getByAccountNumber(accNhan).getName());
//                    break;
//            }
//            break;
//        }
//
//        String finalAccChuyen = accChuyen;
//        String finalAccNhan = accNhan;
//        Account accountChuyen = (lsAccount.stream().filter(item -> item.getAccountNumber().equals(finalAccChuyen)).findFirst().get());
//        Account accountNhan = lsAccount.stream().filter(item -> item.getAccountNumber().equals(finalAccNhan)).findFirst().get();
//        double sotien = inputBalanceChuyen(accountChuyen);
//
//        while (true) {
//            System.out.print("Xac nhan chuyen " + sotien + " từ tai khoan [" + accChuyen + "] den tai khoan [" + accNhan + "] (Y/N): ");
//            String choose = scanner.nextLine();
//            switch (choose) {
//                case "Y":
//                    break;
//                case "N":
//                    return;
//                default:
//                    System.out.println("Nhap lai: ");
//                    continue;
//            }
//            break;
//        }
//
//        accountNhan.setBalance(accountNhan.getBalance() + sotien);
//        accountChuyen.setBalance(accountChuyen.getBalance() - sotien);
//
//        AccountDao.update(accountNhan);
//        AccountDao.update(accountChuyen);
        customer.transfers(scanner);


//        String localDateTime = String.valueOf(LocalDateTime.now());
        //luu lai ls
//        Transaction transactionNhan = new Transaction(String.valueOf(transactions.size()), accNhan, sotien, localDateTime, true, TransactionType.TRANSFERS);
//        Transaction transactionChuyen = new Transaction(String.valueOf(transactions.size()), accChuyen, -sotien, localDateTime, true, TransactionType.TRANSFERS);
//        transactions.add(transactionChuyen);
//        transactions.add(transactionNhan);
//        TransactionDao.save(transactions);
//
//        accountChuyen.log(sotien, TransactionType.TRANSFERS, accNhan);

    }

    public boolean isAccountExisted(Account newAccount) {
        List<String> lsAccountNumber = lsAccount.stream().map(Account::getAccountNumber).collect(Collectors.toList());
        return lsAccountNumber.contains(newAccount.getAccountNumber());
    }

    public boolean isCustomerExisted(Customer newCustomer) {
        List<String> customerNumbers = customers.stream().map(User::getCustomerId).collect(Collectors.toList());
        return customerNumbers.contains(newCustomer.getCustomerId());
    }

    public Customer getCustomerById(String customerId) {


        Map<String, List<Account>> listMap = lsAccount.stream().collect(Collectors.groupingBy(Account::getCustomerId));
        Customer customer = customers.stream().filter(item -> item.getCustomerId().equals(customerId)).findFirst().get();

        customer.setAccounts(listMap.get(customerId));
        return customer;
    }

    public double inputBalanceChuyen(Account account) throws IOException {
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

    public Customer getByAccountNumber(String accountNumber) {
        Customer customer = map.keySet().stream().filter(item -> map.get(item).stream().anyMatch(i -> i.getAccountNumber().equals(accountNumber))).findFirst().get();
        return customer;
    }


}
