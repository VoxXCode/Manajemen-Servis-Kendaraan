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

    public VehicleController() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Vehicles");
                Row header = sheet.createRow(0);
                header.createCell(0).setCellValue("Plat Nomor");
                header.createCell(1).setCellValue("Merk");
                header.createCell(2).setCellValue("Tipe");
                header.createCell(3).setCellValue("Tahun");
                header.createCell(4).setCellValue("Pemilik");

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

    public void addVehicle(Vehicle vehicle) {
        try (FileInputStream fis = new FileInputStream(FILE_PATH);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            Row row = sheet.createRow(sheet.getLastRowNum() + 1);

            row.createCell(0).setCellValue(vehicle.getPlateNumber());
            row.createCell(1).setCellValue(vehicle.getBrand());
            row.createCell(2).setCellValue(vehicle.getType());
            row.createCell(3).setCellValue(vehicle.getYear());
            row.createCell(4).setCellValue(vehicle.getOwnerName());

            try (FileOutputStream out = new FileOutputStream(FILE_PATH)) {
                workbook.write(out);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Vehicle> getAllVehicles() {
        List<Vehicle> list = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(FILE_PATH);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    Vehicle v = new Vehicle();
                    v.setPlateNumber(row.getCell(0).getStringCellValue());
                    v.setBrand(row.getCell(1).getStringCellValue());
                    v.setType(row.getCell(2).getStringCellValue());
                    v.setYear((int) row.getCell(3).getNumericCellValue());
                    v.setOwnerName(row.getCell(4).getStringCellValue());
                    list.add(v);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}