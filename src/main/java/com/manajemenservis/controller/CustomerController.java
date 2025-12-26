package com.manajemenservis.controller;

import com.manajemenservis.model.Customer;
import com.manajemenservis.model.Vehicle;
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
        checkAndCreateFile();
    }

    private void checkAndCreateFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Data Gabungan");
                Row header = sheet.createRow(0);
                header.createCell(0).setCellValue("Nama");
                header.createCell(1).setCellValue("No HP");
                header.createCell(2).setCellValue("Alamat");
                header.createCell(3).setCellValue("Plat Nomor");
                header.createCell(4).setCellValue("Merk");
                header.createCell(5).setCellValue("Tipe");
                header.createCell(6).setCellValue("Tahun");

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

    public List<String[]> getAllData() {
        List<String[]> list = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(FILE_PATH);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    String[] data = new String[7];
                    data[0] = getCellValue(row.getCell(0));
                    data[1] = getCellValue(row.getCell(1));
                    data[2] = getCellValue(row.getCell(2));
                    data[3] = getCellValue(row.getCell(3)); // Plat Nomor
                    data[4] = getCellValue(row.getCell(4));
                    data[5] = getCellValue(row.getCell(5));

                    Cell cellTahun = row.getCell(6);
                    if (cellTahun != null && cellTahun.getCellType() == CellType.NUMERIC) {
                        data[6] = String.valueOf((int) cellTahun.getNumericCellValue());
                    } else {
                        data[6] = getCellValue(cellTahun);
                    }
                    list.add(data);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void addCustomerAndVehicle(Customer c, Vehicle v) {
        try (FileInputStream fis = new FileInputStream(FILE_PATH);
             Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheetAt(0);
            Row row = sheet.createRow(sheet.getLastRowNum() + 1);
            writeDataToRow(row, c, v);
            try (FileOutputStream out = new FileOutputStream(FILE_PATH)) {
                workbook.write(out);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --- FITUR BARU: UPDATE ---
    public boolean updateData(String oldPlat, Customer c, Vehicle v) {
        try (FileInputStream fis = new FileInputStream(FILE_PATH);
             Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheetAt(0);
            boolean found = false;

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    String currentPlat = getCellValue(row.getCell(3));
                    if (currentPlat.equals(oldPlat)) {
                        writeDataToRow(row, c, v); // Timpa data lama
                        found = true;
                        break;
                    }
                }
            }
            if (found) {
                try (FileOutputStream out = new FileOutputStream(FILE_PATH)) {
                    workbook.write(out);
                }
            }
            return found;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // --- FITUR BARU: DELETE ---
    public boolean deleteData(String platNomor) {
        List<String[]> allData = getAllData();
        boolean found = false;

        // Hapus dari List memory
        for (int i = 0; i < allData.size(); i++) {
            if (allData.get(i)[3].equals(platNomor)) {
                allData.remove(i);
                found = true;
                break;
            }
        }

        if (found) {
            // Tulis ulang file Excel dari awal (paling aman agar tidak ada baris kosong)
            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Data Gabungan");
                Row header = sheet.createRow(0);
                String[] headers = {"Nama", "No HP", "Alamat", "Plat Nomor", "Merk", "Tipe", "Tahun"};
                for(int k=0; k<headers.length; k++) header.createCell(k).setCellValue(headers[k]);

                for (int i = 0; i < allData.size(); i++) {
                    Row row = sheet.createRow(i + 1);
                    String[] d = allData.get(i);
                    for(int j=0; j<7; j++) row.createCell(j).setCellValue(d[j]);
                }

                try (FileOutputStream out = new FileOutputStream(FILE_PATH)) {
                    workbook.write(out);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return found;
    }

    private void writeDataToRow(Row row, Customer c, Vehicle v) {
        row.createCell(0).setCellValue(c.getName());
        row.createCell(1).setCellValue(c.getPhoneNumber());
        row.createCell(2).setCellValue(c.getAddress());
        row.createCell(3).setCellValue(v.getPlateNumber());
        row.createCell(4).setCellValue(v.getBrand());
        row.createCell(5).setCellValue(v.getType());
        row.createCell(6).setCellValue(v.getYear());
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        return cell.getCellType() == CellType.STRING ? cell.getStringCellValue() : cell.toString();
    }
}