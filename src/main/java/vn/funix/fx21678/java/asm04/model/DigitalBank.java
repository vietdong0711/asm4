package vn.funix.fx21678.java.asm04.model;

import vn.funix.fx21678.java.asm04.service.TextFileService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class DigitalBank extends Bank {


    public static void showCustomers() throws IOException {
        List<List<String>> ls = TextFileService.readFile("store/customers.txt");
        if (ls.isEmpty()) {
            System.out.println("Chưa có khách hàng nào trong danh sách!");
            return;
        }
        List<Customer> customers = ls.stream().map(Customer::new).collect(Collectors.toList());

        for (Customer customer : customers) {

        }

    }
}
