package com.manajemenservis.controller;

import com.manajemenservis.model.SparePart;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InventoryController {
    private static final String FILE_PATH = "data/inventory.xlsx";

    public InventoryController() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Inventory");
                Row header = sheet.createRow(0);
                header.createCell(0).setCellValue("Nama Barang");
                header.createCell(1).setCellValue("Harga");
                header.createCell(2).setCellValue("Stok");

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

    public void addSparePart(SparePart part) {
        try (FileInputStream fis = new FileInputStream(FILE_PATH);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            Row row = sheet.createRow(sheet.getLastRowNum() + 1);

            row.createCell(0).setCellValue(part.getItemName());
            row.createCell(1).setCellValue(part.getPrice());
            row.createCell(2).setCellValue(part.getStock());

            try (FileOutputStream out = new FileOutputStream(FILE_PATH)) {
                workbook.write(out);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<SparePart> getAllSpareParts() {
        List<SparePart> list = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(FILE_PATH);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    SparePart p = new SparePart();
                    p.setItemName(row.getCell(0).getStringCellValue());
                    p.setPrice(row.getCell(1).getNumericCellValue());
                    p.setStock((int) row.getCell(2).getNumericCellValue());
                    list.add(p);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}