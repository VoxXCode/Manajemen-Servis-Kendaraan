package com.manajemenservis.controller;

import com.manajemenservis.model.Customer;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomerController {
    private static final String FILE_PATH = "data/customers.xlsx";

    public CustomerController() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Customers");
                Row header = sheet.createRow(0);
                header.createCell(0).setCellValue("Nama");
                header.createCell(1).setCellValue("No HP");
                header.createCell(2).setCellValue("Alamat");

                File folder = new File("data");
                if (!folder.exists()) folder.mkdirs();

                try (FileOutputStream out = new FileOutputStream(file)) {
                    workbook.write(out);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addCustomer(Customer customer) {
        try (FileInputStream fis = new FileInputStream(FILE_PATH);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            Row row = sheet.createRow(sheet.getLastRowNum() + 1);

            row.createCell(0).setCellValue(customer.getName());
            row.createCell(1).setCellValue(customer.getPhoneNumber());
            row.createCell(2).setCellValue(customer.getAddress());

            try (FileOutputStream out = new FileOutputStream(FILE_PATH)) {
                workbook.write(out);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Customer> getAllCustomers() {
        List<Customer> list = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(FILE_PATH);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    Customer c = new Customer();
                    c.setName(row.getCell(0).getStringCellValue());
                    c.setPhoneNumber(row.getCell(1).getStringCellValue());
                    c.setAddress(row.getCell(2).getStringCellValue());
                    list.add(c);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}