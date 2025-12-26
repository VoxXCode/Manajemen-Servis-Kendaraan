package com.manajemenservis.view;

import com.manajemenservis.controller.ServiceController;
import com.manajemenservis.model.ServiceRecord;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServiceEntryForm extends JFrame {

    private JTextField txtPlat, txtDeskripsi, txtBiayaJasa, txtBiayaPart;
    private ServiceController controller;

    public ServiceEntryForm() {
        controller = new ServiceController();

        setTitle("Input Transaksi Servis");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Plat Nomor:"));
        txtPlat = new JTextField();
        panel.add(txtPlat);

        panel.add(new JLabel("Deskripsi Servis:"));
        txtDeskripsi = new JTextField();
        panel.add(txtDeskripsi);

        panel.add(new JLabel("Biaya Jasa (Rp):"));
        txtBiayaJasa = new JTextField();
        panel.add(txtBiayaJasa);

        panel.add(new JLabel("Biaya Sparepart (Rp):"));
        txtBiayaPart = new JTextField();
        panel.add(txtBiayaPart);

        JButton btnSimpan = new JButton("Simpan Transaksi");
        panel.add(new JLabel(""));
        panel.add(btnSimpan);

        add(panel);

        btnSimpan.addActionListener(e -> simpan());
    }

    private void simpan() {
        try {
            String plat = txtPlat.getText();
            String desc = txtDeskripsi.getText();
            double jasa = Double.parseDouble(txtBiayaJasa.getText());
            double part = Double.parseDouble(txtBiayaPart.getText());
            String tanggal = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

            ServiceRecord record = new ServiceRecord(tanggal, plat, desc, jasa, part);
            controller.addServiceRecord(record);

            JOptionPane.showMessageDialog(this, "Transaksi Berhasil Disimpan!");
            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Biaya harus berupa angka!");
        }
    }
}