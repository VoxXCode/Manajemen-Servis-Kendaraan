package com.manajemenservis.view;

import com.manajemenservis.controller.CustomerController;
import com.manajemenservis.model.Customer;
import com.manajemenservis.model.Vehicle;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CustomerForm extends JFrame {

    private JTextField txtNama, txtHp, txtAlamat;
    private JTextField txtPlat, txtMerk, txtTipe, txtTahun;
    private CustomerController controller;

    // Variabel untuk mode Edit
    private boolean isEditMode = false;
    private String oldPlatNumber = "";

    // Constructor 1: Untuk Tambah Data Baru
    public CustomerForm() {
        initUI();
    }

    // Constructor 2: Untuk Edit Data (Menerima data yang dipilih)
    public CustomerForm(String[] data) {
        initUI();
        isEditMode = true;
        oldPlatNumber = data[3]; // Simpan plat lama untuk referensi update

        // Isi form dengan data lama
        txtNama.setText(data[0]);
        txtHp.setText(data[1]);
        txtAlamat.setText(data[2]);
        txtPlat.setText(data[3]);
        txtMerk.setText(data[4]);
        txtTipe.setText(data[5]);
        txtTahun.setText(data[6]);

        setTitle("Edit Data Pelanggan");
    }

    private void initUI() {
        controller = new CustomerController();
        setTitle("Form Data Pelanggan");
        setSize(500, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Event listener saat form ditutup -> Buka tabel lagi
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                new CustomerTable().setVisible(true);
            }
        });

        JPanel mainPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        mainPanel.add(new JLabel("Nama Pemilik:"));
        txtNama = new JTextField(); mainPanel.add(txtNama);

        mainPanel.add(new JLabel("Nomor HP:"));
        txtHp = new JTextField(); mainPanel.add(txtHp);

        mainPanel.add(new JLabel("Alamat:"));
        txtAlamat = new JTextField(); mainPanel.add(txtAlamat);

        mainPanel.add(new JLabel("Plat Nomor:"));
        txtPlat = new JTextField(); mainPanel.add(txtPlat);

        mainPanel.add(new JLabel("Merk Kendaraan:"));
        txtMerk = new JTextField(); mainPanel.add(txtMerk);

        mainPanel.add(new JLabel("Tipe Kendaraan:"));
        txtTipe = new JTextField(); mainPanel.add(txtTipe);

        mainPanel.add(new JLabel("Tahun:"));
        txtTahun = new JTextField(); mainPanel.add(txtTahun);

        JButton btnSimpan = new JButton("Simpan Data");
        btnSimpan.setBackground(new Color(65, 105, 225));
        btnSimpan.setForeground(Color.WHITE);
        btnSimpan.addActionListener(e -> simpan());

        add(mainPanel, BorderLayout.CENTER);
        add(btnSimpan, BorderLayout.SOUTH);
    }

    private void simpan() {
        String nama = txtNama.getText();
        String hp = txtHp.getText();
        String alamat = txtAlamat.getText();
        String plat = txtPlat.getText();
        String merk = txtMerk.getText();
        String tipe = txtTipe.getText();
        String tahunStr = txtTahun.getText();

        if (nama.isEmpty() || plat.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama dan Plat Nomor wajib diisi!");
            return;
        }

        int tahun = 0;
        try { tahun = Integer.parseInt(tahunStr); } catch (NumberFormatException e) { tahun = 2024; }

        Customer c = new Customer(nama, hp, alamat);
        Vehicle v = new Vehicle(plat, merk, tipe, tahun, nama);

        if (isEditMode) {
            // Panggil fungsi Update
            boolean success = controller.updateData(oldPlatNumber, c, v);
            if(success) JOptionPane.showMessageDialog(this, "Data Berhasil Diperbarui!");
            else JOptionPane.showMessageDialog(this, "Gagal memperbarui data.");
        } else {
            // Panggil fungsi Tambah Baru
            controller.addCustomerAndVehicle(c, v);
            JOptionPane.showMessageDialog(this, "Data Baru Berhasil Disimpan!");
        }

        dispose(); // Tutup form
    }
}