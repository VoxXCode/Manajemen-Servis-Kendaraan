package com.manajemenservis.controller;

import com.manajemenservis.model.ServiceRecord;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ServiceController {
    private static final String FILE_PATH = "data/services.xlsx";
    private DataFormatter formatter = new DataFormatter();

    public ServiceController() {
        checkAndCreateFile();
    }

    // --- SETUP FILE ---
    private void checkAndCreateFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("History Servis");
                Row header = sheet.createRow(0);
                String[] headers = {"Tanggal", "Plat Nomor", "Deskripsi", "Biaya Jasa", "Biaya Part", "Total"};
                for (int i = 0; i < headers.length; i++) header.createCell(i).setCellValue(headers[i]);

                File folder = new File("data");
                if (!folder.exists()) folder.mkdirs();

                try (FileOutputStream out = new FileOutputStream(file)) { workbook.write(out); }
            } catch (IOException e) { e.printStackTrace(); }
        }
    }

    // --- [PENTING] METODE PENERIMA LOG ---
    public void addLog(String deskripsi, String platNomor) {
        String tanggal = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        // Simpan dengan biaya 0 karena ini hanya log aktivitas
        ServiceRecord log = new ServiceRecord(tanggal, platNomor, deskripsi, 0, 0);
        addServiceRecord(log);
    }

    // --- SIMPAN KE EXCEL ---
    public void addServiceRecord(ServiceRecord record) {
        try (FileInputStream fis = new FileInputStream(FILE_PATH);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            int lastRow = sheet.getLastRowNum();
            Row row = sheet.createRow(lastRow + 1);

            row.createCell(0).setCellValue(record.getDate());
            row.createCell(1).setCellValue(record.getPlateNumber());
            row.createCell(2).setCellValue(record.getDescription());
            row.createCell(3).setCellValue(record.getServiceCost());
            row.createCell(4).setCellValue(record.getPartCost());
            row.createCell(5).setCellValue(record.getTotalCost());

            try (FileOutputStream out = new FileOutputStream(FILE_PATH)) {
                workbook.write(out);
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    // --- BACA DATA UNTUK TABEL ---
    public List<ServiceRecord> getAllServiceRecords() {
        List<ServiceRecord> list = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(FILE_PATH);
             Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    String date = formatter.formatCellValue(row.getCell(0));
                    String plat = formatter.formatCellValue(row.getCell(1));
                    String desc = formatter.formatCellValue(row.getCell(2));

                    // Handle numeric cells safely
                    double jasa = 0;
                    double part = 0;
                    try { jasa = row.getCell(3).getNumericCellValue(); } catch (Exception e) {}
                    try { part = row.getCell(4).getNumericCellValue(); } catch (Exception e) {}

                    list.add(new ServiceRecord(date, plat, desc, jasa, part));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
}