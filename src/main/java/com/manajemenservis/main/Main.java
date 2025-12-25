package com.manajemenservis.main;

import com.manajemenservis.view.LoginView;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Menjalankan UI di thread yang aman (Event Dispatch Thread)
        SwingUtilities.invokeLater(() -> {
            // Membuat instance LoginView dan menampilkannya
            LoginView loginPage = new LoginView();
            loginPage.setVisible(true);
        });
    }
}