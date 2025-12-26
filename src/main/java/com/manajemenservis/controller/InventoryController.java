package com.manajemenservis.controller;

import com.manajemenservis.model.SparePart;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class InventoryController {
    private static final String FILE_PATH = "data/inventory.xlsx";
    private DataFormatter formatter = new DataFormatter();

    public InventoryController() {
        checkAndCreateFile();
    }

    private void checkAndCreateFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Stok Sparepart");
                Row header = sheet.createRow(0);
                // Sesuaikan header dengan Excel Anda (Ada kolom No.)
                String[] headers = {"No.", "Kode", "Nama Barang", "Kategori", "Stok", "Harga Jual", "Satuan"};
                for (int i = 0; i < headers.length; i++) {
                    header.createCell(i).setCellValue(headers[i]);
                }

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

    // --- READ (GET ALL) ---
    public List<String[]> getAllData() {
        List<String[]> list = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(FILE_PATH);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            // Mulai dari baris 1 (karena baris 0 adalah Header)
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    String[] data = new String[6];

                    // [PERBAIKAN INDEKS KOLOM SESUAI EXCEL ANDA]
                    // Kolom 0 = No. (Diabaikan)
                    data[0] = getCellValue(row.getCell(1)); // Kolom B: Kode
                    data[1] = getCellValue(row.getCell(2)); // Kolom C: Nama Barang
                    data[2] = getCellValue(row.getCell(3)); // Kolom D: Kategori
                    data[3] = getCellValue(row.getCell(4)); // Kolom E: Stok

                    // Format Harga (Kolom F)
                    String rawPrice = getCellValue(row.getCell(5));
                    data[4] = formatRupiah(rawPrice);

                    data[5] = getCellValue(row.getCell(6)); // Kolom G: Satuan

                    list.add(data);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    // --- CREATE (ADD) ---
    public void addSparePart(SparePart sp) {
        try (FileInputStream fis = new FileInputStream(FILE_PATH);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            int lastRow = sheet.getLastRowNum();
            Row row = sheet.createRow(lastRow + 1);

            // [PERBAIKAN] Tambahkan No. Urut di Kolom 0
            row.createCell(0).setCellValue(lastRow); // No Urut = Baris Terakhir

            row.createCell(1).setCellValue(sp.getKode());
            row.createCell(2).setCellValue(sp.getNama());
            row.createCell(3).setCellValue(sp.getKategori());
            row.createCell(4).setCellValue(sp.getStok());
            row.createCell(5).setCellValue(sp.getHargaJual());
            row.createCell(6).setCellValue(sp.getSatuan());

            try (FileOutputStream out = new FileOutputStream(FILE_PATH)) {
                workbook.write(out);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --- UPDATE (EDIT) ---
    public boolean updateSparePart(String oldCode, SparePart sp) {
        try (FileInputStream fis = new FileInputStream(FILE_PATH);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            boolean found = false;

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    // Cek Kode di Kolom B (Index 1)
                    String currentCode = getCellValue(row.getCell(1));
                    if (currentCode.equals(oldCode)) {
                        row.getCell(1).setCellValue(sp.getKode());
                        row.getCell(2).setCellValue(sp.getNama());
                        row.getCell(3).setCellValue(sp.getKategori());
                        row.getCell(4).setCellValue(sp.getStok());
                        row.getCell(5).setCellValue(sp.getHargaJual());
                        row.getCell(6).setCellValue(sp.getSatuan());
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

    // --- DELETE ---
    public boolean deleteSparePart(String code) {
        // Ambil semua data (termasuk kolom No. agar struktur terjaga saat ditulis ulang)
        List<String[]> allData = getAllDataRawWithNo();
        boolean found = false;

        for (int i = 0; i < allData.size(); i++) {
            // Cek Kode di Index 1
            if (allData.get(i)[1].equals(code)) {
                allData.remove(i);
                found = true;
                break;
            }
        }

        if (found) {
            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Stok Sparepart");
                Row header = sheet.createRow(0);
                String[] headers = {"No.", "Kode", "Nama Barang", "Kategori", "Stok", "Harga Jual", "Satuan"};
                for(int k=0; k<headers.length; k++) header.createCell(k).setCellValue(headers[k]);

                for (int i = 0; i < allData.size(); i++) {
                    Row row = sheet.createRow(i + 1);
                    String[] d = allData.get(i);

                    // Tulis ulang dengan nomor urut baru (agar rapi 1, 2, 3...)
                    row.createCell(0).setCellValue(i + 1);

                    row.createCell(1).setCellValue(d[1]);
                    row.createCell(2).setCellValue(d[2]);
                    row.createCell(3).setCellValue(d[3]);

                    // Parsing angka
                    try { row.createCell(4).setCellValue(Double.parseDouble(d[4])); } catch (Exception e) { row.createCell(4).setCellValue(d[4]); }

                    // Parsing harga (hilangkan format Rp jika ada, agar bisa dihitung Excel)
                    String hargaClean = d[5].replace("Rp", "").replace(".", "").replace(",", ".").trim();
                    try { row.createCell(5).setCellValue(Double.parseDouble(hargaClean)); } catch (Exception e) { row.createCell(5).setCellValue(d[5]); }

                    row.createCell(6).setCellValue(d[6]);
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

    // --- HELPER METHODS ---
    // Helper khusus untuk Delete (mengambil semua kolom termasuk No.)
    private List<String[]> getAllDataRawWithNo() {
        List<String[]> list = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(FILE_PATH);
             Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    String[] data = new String[7];
                    for(int j=0; j<7; j++) data[j] = getCellValue(row.getCell(j));
                    list.add(data);
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
        return list;
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        return formatter.formatCellValue(cell);
    }

    private String formatRupiah(String raw) {
        // Bersihkan dulu karakter non-angka agar parsing aman
        String clean = raw.replaceAll("[^0-9]", "");
        if (clean.isEmpty()) return raw;

        try {
            double amount = Double.parseDouble(clean);
            NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
            return format.format(amount).replace("Rp", "Rp ");
        } catch (NumberFormatException e) {
            return raw;
        }
    }
}