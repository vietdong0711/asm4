package vn.funix.fx21678.java.asm04;

import vn.funix.fx21678.java.asm04.dao.AccountDao;
import vn.funix.fx21678.java.asm04.dao.CustomerDao;
import vn.funix.fx21678.java.asm04.exception.CustomerIdNotValidException;
import vn.funix.fx21678.java.asm04.model.Account;
import vn.funix.fx21678.java.asm04.model.Customer;
import vn.funix.fx21678.java.asm04.model.User;
import vn.funix.fx21678.java.asm04.service.TextFileService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Asm04 {
    private static Scanner scanner = new Scanner(System.in);
    private static List<String> lsCustomerId = new ArrayList<>();

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        loadListCustomerId();
        while (true) {
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

                default:
            }
        }
    }
    public static void loadListCustomerId(){
        List<Customer> customers = CustomerDao.list();
        lsCustomerId = customers.stream().map(User::getCustomerId).collect(Collectors.toList());
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

    public static void themATM(){
        System.out.println("Nhap ma so khach hang: ");
        String customerId = scanner.nextLine();
        if (lsCustomerId.contains(customerId)){
            System.out.println("Khong tim thay khach hang "+ customerId +", tac vu khong thanh cong");
            return;
        }





    }



}
