package com.manajemenservis.controller;

import com.manajemenservis.model.ServiceRecord;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ServiceController {
    private static final String FILE_PATH = "data/services.xlsx";

    public ServiceController() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Services");
                Row header = sheet.createRow(0);
                header.createCell(0).setCellValue("Tanggal");
                header.createCell(1).setCellValue("Plat Nomor");
                header.createCell(2).setCellValue("Deskripsi");
                header.createCell(3).setCellValue("Biaya Jasa");
                header.createCell(4).setCellValue("Biaya Part");
                header.createCell(5).setCellValue("Total");

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

    public void addServiceRecord(ServiceRecord record) {
        try (FileInputStream fis = new FileInputStream(FILE_PATH);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            Row row = sheet.createRow(sheet.getLastRowNum() + 1);

            row.createCell(0).setCellValue(record.getDate());
            row.createCell(1).setCellValue(record.getPlateNumber());
            row.createCell(2).setCellValue(record.getDescription());
            row.createCell(3).setCellValue(record.getServiceCost());
            row.createCell(4).setCellValue(record.getPartCost());
            row.createCell(5).setCellValue(record.getTotalCost());

            try (FileOutputStream out = new FileOutputStream(FILE_PATH)) {
                workbook.write(out);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<ServiceRecord> getAllServiceRecords() {
        List<ServiceRecord> list = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(FILE_PATH);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    ServiceRecord s = new ServiceRecord();
                    s.setDate(row.getCell(0).getStringCellValue());
                    s.setPlateNumber(row.getCell(1).getStringCellValue());
                    s.setDescription(row.getCell(2).getStringCellValue());
                    s.setServiceCost(row.getCell(3).getNumericCellValue());
                    s.setPartCost(row.getCell(4).getNumericCellValue());
                    // Total cost otomatis terhitung di Model saat setServiceCost/setPartCost
                    list.add(s);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}