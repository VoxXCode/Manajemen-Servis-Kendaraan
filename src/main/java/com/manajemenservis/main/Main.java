package com.manajemenservis.main;

import com.manajemenservis.util.ExcelHandler; // Import Handler
import com.manajemenservis.view.LoginView;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {

        // 1. Cek & Buat Database Excel Otomatis
        ExcelHandler.pastikanFileAda();

        // 2. Jalankan GUI
        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
            loginView.setVisible(true);
        });
    }
}