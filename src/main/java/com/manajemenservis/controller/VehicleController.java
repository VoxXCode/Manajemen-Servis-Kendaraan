package com.manajemenservis.controller;

import com.manajemenservis.model.Vehicle;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VehicleController {
    private static final String FILE_PATH = "data/vehicles.xlsx";
    private DataFormatter formatter = new DataFormatter(); // Untuk membaca angka/tahun sebagai String

    public VehicleController() {
        checkAndCreateFile();
    }

    private void checkAndCreateFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Data Kendaraan");
                Row header = sheet.createRow(0);
                // Sesuaikan header dengan Model Vehicle yang baru (Tanpa Owner Name)
                header.createCell(0).setCellValue("Plat Nomor");
                header.createCell(1).setCellValue("Merk");
                header.createCell(2).setCellValue("Tipe");
                header.createCell(3).setCellValue("Tahun");

                File folder = new File("data");
                if (!folder.exists()) {
                    boolean created = folder.mkdirs(); // Fix warning result ignored
                }

                try (FileOutputStream out = new FileOutputStream(file)) {
                    workbook.write(out);
                }
            } catch (IOException e) {
                System.err.println("Gagal membuat file Excel: " + e.getMessage());
            }
        }
    }

    // --- GET ALL VEHICLES ---
    public List<Vehicle> getAllVehicles() {
        List<Vehicle> list = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(FILE_PATH);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    // Gunakan DataFormatter agar tahun terbaca sebagai String (misal "2022" bukan "2022.0")
                    String plat = formatter.formatCellValue(row.getCell(0));
                    String merk = formatter.formatCellValue(row.getCell(1));
                    String tipe = formatter.formatCellValue(row.getCell(2));
                    String tahun = formatter.formatCellValue(row.getCell(3));

                    // Menggunakan Constructor baru (4 Parameter String)
                    Vehicle v = new Vehicle(plat, merk, tipe, tahun);
                    list.add(v);
                }
            }
        } catch (IOException e) {
            System.err.println("Gagal membaca data kendaraan: " + e.getMessage());
        }
        return list;
    }

    // --- ADD VEHICLE ---
    public void addVehicle(Vehicle v) {
        try (FileInputStream fis = new FileInputStream(FILE_PATH);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            Row row = sheet.createRow(sheet.getLastRowNum() + 1);

            row.createCell(0).setCellValue(v.getPlateNumber());
            row.createCell(1).setCellValue(v.getBrand());
            row.createCell(2).setCellValue(v.getType());
            row.createCell(3).setCellValue(v.getYear()); // Sekarang String, jadi aman

            try (FileOutputStream out = new FileOutputStream(FILE_PATH)) {
                workbook.write(out);
            }
        } catch (IOException e) {
            System.err.println("Gagal menyimpan data kendaraan: " + e.getMessage());
        }
    }
}