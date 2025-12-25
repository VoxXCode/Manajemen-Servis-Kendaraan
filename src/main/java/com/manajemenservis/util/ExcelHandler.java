package com.manajemenservis.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelHandler {

    // Menyimpan file di folder "data" agar root project tidak berantakan
    private static final String FOLDER_NAME = "data";
    private static final String FILE_NAME = "users.xlsx";
    private static final String FILE_PATH = FOLDER_NAME + File.separator + FILE_NAME;

    public static void pastikanFileAda() {
        // 1. Buat folder "data" jika belum ada
        File folder = new File(FOLDER_NAME);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // 2. Buat file "users.xlsx" jika belum ada
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            System.out.println("File database belum ada. Membuat baru di: " + FILE_PATH);

            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Users");

                // Header
                Row headerRow = sheet.createRow(0);
                headerRow.createCell(0).setCellValue("Username");
                headerRow.createCell(1).setCellValue("Password");

                // Akun Default
                Row dataRow = sheet.createRow(1);
                dataRow.createCell(0).setCellValue("admin");
                dataRow.createCell(1).setCellValue("admin123");

                try (FileOutputStream fileOut = new FileOutputStream(FILE_PATH)) {
                    workbook.write(fileOut);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean cekLogin(String usernameInput, String passwordInput) {
        pastikanFileAda();

        try (FileInputStream fis = new FileInputStream(new File(FILE_PATH));
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    Cell cellUser = row.getCell(0);
                    Cell cellPass = row.getCell(1);

                    if (cellUser != null && cellPass != null) {
                        String userExcel = cellUser.getStringCellValue();
                        String passExcel = cellPass.getStringCellValue();

                        if (userExcel.equals(usernameInput) && passExcel.equals(passwordInput)) {
                            return true;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}