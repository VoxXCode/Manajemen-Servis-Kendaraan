package com.manajemenservis.main;

import com.manajemenservis.util.ExcelHandler; // Import Handler
import com.manajemenservis.view.LoginView;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {

        ExcelHandler.pastikanFileAda();

        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
            loginView.setVisible(true);
        });
    }
}