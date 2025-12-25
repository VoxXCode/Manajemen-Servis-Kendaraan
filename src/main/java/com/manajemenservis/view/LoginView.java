package com.manajemenservis.view;

import com.manajemenservis.controller.AuthController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginView extends JFrame {

    // Komponen UI
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    // Controller untuk logika
    private AuthController authController;

    public LoginView() {
        // 1. Setup Controller
        authController = new AuthController();

        // 2. Setup Jendela (Frame)
        setTitle("Login Aplikasi");
        setSize(350, 180); // Ukuran jendela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Agar muncul di tengah layar
        setResizable(false); // Agar tidak bisa di-resize sembarangan

        // 3. Setup Layout (Tata letak komponen)
        // Kita gunakan panel agar bisa memberi jarak (padding) di pinggir
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(3, 2, 10, 10)); // 3 Baris, 2 Kolom, Jarak 10px
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Padding tepi

        // --- Baris 1: Username ---
        mainPanel.add(new JLabel("Username:"));
        txtUsername = new JTextField();
        mainPanel.add(txtUsername);

        // --- Baris 2: Password ---
        mainPanel.add(new JLabel("Password:"));
        txtPassword = new JPasswordField();
        mainPanel.add(txtPassword);

        // --- Baris 3: Tombol Login ---
        mainPanel.add(new JLabel("")); // Label kosong sebagai penyeimbang layout
        btnLogin = new JButton("Login");
        mainPanel.add(btnLogin);

        // Masukkan panel ke dalam Frame
        add(mainPanel);

        // 4. Aksi Tombol Login
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
    }

    private void handleLogin() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword()); // Ambil password dengan aman

        // Panggil controller untuk cek data
        authController.login(username, password, this);
    }
}