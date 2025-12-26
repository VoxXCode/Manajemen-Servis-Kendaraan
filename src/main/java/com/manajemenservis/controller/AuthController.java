package com.manajemenservis.controller;
import com.manajemenservis.util.ExcelHandler;
import com.manajemenservis.view.DashboardView;
import com.manajemenservis.view.LoginView;

import javax.swing.JOptionPane;

public class AuthController {

    public void login(String username, String password, LoginView currentView) {

        if (username.trim().isEmpty() || password.trim().isEmpty()) {
            JOptionPane.showMessageDialog(currentView,
                    "Username dan Password tidak boleh kosong!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean isLoginSuccess = ExcelHandler.cekLogin(username, password);

        if (isLoginSuccess) {
            JOptionPane.showMessageDialog(currentView, "Login Berhasil");

            DashboardView dashboard = new DashboardView();
            dashboard.setVisible(true);

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