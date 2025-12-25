package com.manajemenservis.view;

import javax.swing.*;
import java.awt.*;

public class DashboardView extends JFrame {

    public DashboardView() {
        setTitle("Dashboard Utama");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Tambahkan Label Selamat Datang
        JLabel welcomeLabel = new JLabel("Selamat Datang di Sistem Manajemen Servis!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));

        add(welcomeLabel); // Menambahkan label ke tengah layar
    }
}