package com.manajemenservis.view;

import com.manajemenservis.controller.CustomerController;
import com.manajemenservis.model.Customer;
import com.manajemenservis.model.Vehicle;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class CustomerForm extends JFrame {

    // --- WARNA TEMA ---
    private final Color COLOR_BG_MAIN     = new Color(18, 18, 18);
    private final Color COLOR_BG_FORM     = new Color(30, 30, 30);
    private final Color COLOR_TEXT_WHITE  = new Color(255, 255, 255);
    private final Color COLOR_TEXT_GRAY   = new Color(170, 170, 170);
    private final Color COLOR_ACCENT      = new Color(65, 105, 225);
    private final Color COLOR_BORDER      = new Color(60, 60, 60);
    private final Color COLOR_INPUT_BG    = new Color(45, 45, 45);

    private final Font FONT_LABEL = new Font("SansSerif", Font.BOLD, 13);
    private final Font FONT_INPUT = new Font("SansSerif", Font.PLAIN, 14);
    private final Font FONT_TITLE = new Font("SansSerif", Font.BOLD, 24);

    private JTextField txtNama, txtHp, txtAlamat, txtPlat, txtMerk, txtTipe, txtTahun;
    private CustomerController controller;
    private boolean isEditMode = false;
    private String oldPlat = "";

    public CustomerForm() {
        this(null);
    }

    public CustomerForm(String[] dataToEdit) {
        controller = new CustomerController();
        setTitle(dataToEdit == null ? "Tambah Data Baru" : "Edit Data");

        // [UBAH UKURAN] Menjadi Landscape (Lebar > Tinggi)
        setSize(900, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false); // Fix ukuran agar tidak berantakan
        setLayout(new BorderLayout());

        // --- HEADER ---
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 25));
        headerPanel.setBackground(COLOR_BG_MAIN);
        JLabel lblTitle = new JLabel(dataToEdit == null ? "Tambah Pelanggan Baru" : "Edit Data Pelanggan");
        lblTitle.setFont(FONT_TITLE);
        lblTitle.setForeground(COLOR_TEXT_WHITE);
        headerPanel.add(lblTitle);
        add(headerPanel, BorderLayout.NORTH);

        // --- FORM CONTENT (SPLIT 2 KOLOM) ---
        JPanel mainContent = new JPanel(new GridLayout(1, 2, 40, 0)); // 1 Baris, 2 Kolom, Jarak 40px
        mainContent.setBackground(COLOR_BG_MAIN);
        mainContent.setBorder(new EmptyBorder(10, 40, 10, 40));

        // Inisialisasi TextFields
        txtNama = createTextField();
        txtHp = createTextField();
        txtAlamat = createTextField();
        txtPlat = createTextField();
        txtMerk = createTextField();
        txtTipe = createTextField();
        txtTahun = createTextField();

        // -- PANEL KIRI (PELANGGAN) --
        JPanel pnlLeft = new JPanel(new GridBagLayout());
        pnlLeft.setBackground(COLOR_BG_MAIN);
        createSectionTitle(pnlLeft, "Data Pemilik", 0);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0; gbc.gridx = 0; gbc.insets = new Insets(5, 0, 5, 0);

        addFormItem(pnlLeft, gbc, 1, "Nama Pemilik", txtNama);
        addFormItem(pnlLeft, gbc, 2, "Nomor HP", txtHp);
        addFormItem(pnlLeft, gbc, 3, "Alamat", txtAlamat);

        // Spacer Vertikal agar konten naik ke atas
        gbc.gridy = 10; gbc.weighty = 1.0; pnlLeft.add(Box.createGlue(), gbc);

        // -- PANEL KANAN (KENDARAAN) --
        JPanel pnlRight = new JPanel(new GridBagLayout());
        pnlRight.setBackground(COLOR_BG_MAIN);
        createSectionTitle(pnlRight, "Data Kendaraan", 0);

        // Reset GBC
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0; gbc.gridx = 0; gbc.insets = new Insets(5, 0, 5, 0);

        addFormItem(pnlRight, gbc, 1, "Plat Nomor", txtPlat);
        addFormItem(pnlRight, gbc, 2, "Merk Kendaraan", txtMerk);
        addFormItem(pnlRight, gbc, 3, "Tipe Kendaraan", txtTipe);
        addFormItem(pnlRight, gbc, 4, "Tahun Pembuatan", txtTahun);

        // Spacer Vertikal
        gbc.gridy = 10; gbc.weighty = 1.0; pnlRight.add(Box.createGlue(), gbc);

        // Gabungkan Panel
        mainContent.add(pnlLeft);
        mainContent.add(pnlRight);
        add(mainContent, BorderLayout.CENTER);

        // --- FOOTER BUTTONS ---
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 20));
        footerPanel.setBackground(COLOR_BG_MAIN);
        footerPanel.setBorder(new EmptyBorder(0, 40, 25, 40));

        JButton btnCancel = new JButton("Batal");
        styleButtonSecondary(btnCancel);
        btnCancel.addActionListener(e -> backToTable());

        JButton btnSave = new JButton("Simpan Data");
        styleButtonPrimary(btnSave);
        btnSave.addActionListener(e -> saveData());

        footerPanel.add(btnCancel);
        footerPanel.add(btnSave);
        add(footerPanel, BorderLayout.SOUTH);

        // Isi Data Mode Edit
        if (dataToEdit != null) {
            isEditMode = true;
            txtNama.setText(dataToEdit[0]);
            txtHp.setText(dataToEdit[1]);
            txtAlamat.setText(dataToEdit[2]);
            txtPlat.setText(dataToEdit[3]);
            txtMerk.setText(dataToEdit[4]);
            txtTipe.setText(dataToEdit[5]);
            txtTahun.setText(dataToEdit[6]);
            oldPlat = dataToEdit[3];
        }
    }

    // --- HELPER METHODS ---

    private void createSectionTitle(JPanel panel, String title, int row) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = row; gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 15, 0);

        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 16));
        lbl.setForeground(COLOR_ACCENT);
        panel.add(lbl, gbc);
    }

    private void addFormItem(JPanel panel, GridBagConstraints gbc, int row, String labelText, JTextField field) {
        gbc.gridy = row * 2;
        gbc.weighty = 0;
        JLabel label = new JLabel(labelText);
        label.setFont(FONT_LABEL);
        label.setForeground(COLOR_TEXT_GRAY);
        panel.add(label, gbc);

        gbc.gridy = row * 2 + 1;
        panel.add(field, gbc);
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(0, 35));
        field.setFont(FONT_INPUT);
        field.setBackground(COLOR_INPUT_BG);
        field.setForeground(COLOR_TEXT_WHITE);
        field.setCaretColor(COLOR_TEXT_WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_BORDER, 1),
                new EmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }

    private void styleButtonPrimary(JButton btn) {
        btn.setBackground(COLOR_ACCENT);
        btn.setForeground(Color.WHITE);
        btn.setFont(FONT_LABEL);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(10, 30, 10, 30));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private void styleButtonSecondary(JButton btn) {
        btn.setBackground(COLOR_BG_FORM);
        btn.setForeground(COLOR_TEXT_GRAY);
        btn.setFont(FONT_LABEL);
        btn.setFocusPainted(false);
        btn.setBorder(new LineBorder(COLOR_BORDER));
        btn.setPreferredSize(new Dimension(100, 38));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private void saveData() {
        if (txtNama.getText().isEmpty() || txtPlat.getText().isEmpty()) {
            // [BARU] Panggil ModernDialog Warning
            ModernDialog.showWarning(this, "Nama dan Plat Nomor wajib diisi!");
            return;
        }

        Customer c = new Customer(txtNama.getText(), txtHp.getText(), txtAlamat.getText());
        Vehicle v = new Vehicle(txtPlat.getText(), txtMerk.getText(), txtTipe.getText(), txtTahun.getText());

        boolean success;
        if (isEditMode) {
            success = controller.updateData(oldPlat, c, v);
        } else {
            controller.addCustomerAndVehicle(c, v);
            success = true;
        }

        if (success) {
            // [BARU] Panggil ModernDialog Success
            ModernDialog.showSuccess(this, "Data berhasil disimpan ke database!");
            backToTable();
        } else {
            // [BARU] Panggil ModernDialog Error
            ModernDialog.showError(this, "Terjadi kesalahan saat menyimpan data.");
        }
    }

    private void backToTable() {
        new CustomerTable().setVisible(true);
        dispose();
    }
}