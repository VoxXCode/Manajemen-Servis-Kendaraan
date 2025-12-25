package com.manajemenservis.controller;

import com.manajemenservis.util.ExcelHandler; // Import helper yang baru kita buat
import com.manajemenservis.view.DashboardView;
import com.manajemenservis.view.LoginView;

import javax.swing.JOptionPane;

public class AuthController {

    public void login(String username, String password, LoginView currentView) {

        // 1. Validasi Input Kosong
        if (username.trim().isEmpty() || password.trim().isEmpty()) {
            JOptionPane.showMessageDialog(currentView,
                    "Username dan Password tidak boleh kosong!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. Cek Login menggunakan ExcelHandler
        // Kita tidak lagi pakai DatabaseConnection / SQL
        boolean isLoginSuccess = ExcelHandler.cekLogin(username, password);

        if (isLoginSuccess) {
            JOptionPane.showMessageDialog(currentView, "Login Berhasil");

            // Buka Dashboard
            DashboardView dashboard = new DashboardView();
            dashboard.setVisible(true);

            // Tutup Login
            currentView.dispose();
        } else {
            JOptionPane.showMessageDialog(currentView,
                    "Username atau Password salah!",
                    "Login Gagal", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void logout(javax.swing.JFrame currentView) {
        int confirm = JOptionPane.showConfirmDialog(currentView,
                "Yakin ingin keluar?",
                "Konfirmasi Logout", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            currentView.dispose();
            new LoginView().setVisible(true);
        }
    }
}