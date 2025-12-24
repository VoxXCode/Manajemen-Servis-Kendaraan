package com.manajemenservis.controller;

import com.manajemenservis.util.DatabaseConnection;
import com.manajemenservis.view.DashboardView;
import com.manajemenservis.view.LoginView;

import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthController {

    /**
     * Fungsi untuk menangani proses login.
     * @param username Input dari user
     * @param password Input dari user
     * @param currentView Referensi ke window Login saat ini (untuk ditutup jika sukses)
     */
    public void login(String username, String password, LoginView currentView) {

        if (username.trim().isEmpty() || password.trim().isEmpty()) {
            JOptionPane.showMessageDialog(currentView, "Username dan Password tidak boleh kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();

            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(currentView, "Login Berhasil! Selamat datang.");

                DashboardView dashboard = new DashboardView();
                dashboard.setVisible(true);

                currentView.dispose();
            } else {
                JOptionPane.showMessageDialog(currentView, "Username atau Password salah!", "Login Gagal", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(currentView, "Terjadi kesalahan database: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Fungsi opsional untuk Logout
     */
    public void logout(javax.swing.JFrame currentView) {
        int confirm = JOptionPane.showConfirmDialog(currentView, "Yakin ingin keluar?", "Konfirmasi Logout", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            currentView.dispose();
            new LoginView().setVisible(true);
        }
    }
}