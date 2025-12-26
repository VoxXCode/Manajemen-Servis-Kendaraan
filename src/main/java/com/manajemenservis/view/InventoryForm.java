package com.manajemenservis.view;

import com.manajemenservis.controller.InventoryController;
import com.manajemenservis.model.SparePart;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicComboBoxUI; // Import untuk Custom UI Dropdown
import java.awt.*;

public class InventoryForm extends JFrame {

    // --- WARNA TEMA ---
    private final Color COLOR_BG_MAIN     = new Color(18, 18, 18);
    // [FIX 1] Menambahkan variabel warna yang hilang
    private final Color COLOR_BG_FORM     = new Color(30, 30, 30);

    private final Color COLOR_TEXT_WHITE  = new Color(255, 255, 255);
    private final Color COLOR_TEXT_GRAY   = new Color(170, 170, 170);
    private final Color COLOR_ACCENT      = new Color(65, 105, 225);
    private final Color COLOR_BORDER      = new Color(60, 60, 60);
    private final Color COLOR_INPUT_BG    = new Color(45, 45, 45);

    private final Font FONT_LABEL = new Font("SansSerif", Font.BOLD, 13);
    private final Font FONT_INPUT = new Font("SansSerif", Font.PLAIN, 14);
    private final Font FONT_TITLE = new Font("SansSerif", Font.BOLD, 24);

    // --- OPSI DROPDOWN ---
    private final String[] OPTIONS_KATEGORI = {
            "Oli Mesin", "Oli Gardan", "Sparepart", "Ban Luar", "Ban Dalam",
            "Aki / Baterai", "Kampas Rem", "Lampu", "Lain-lain"
    };

    private final String[] OPTIONS_SATUAN = {
            "Pcs", "Botol", "Set", "Unit", "Kaleng", "Dus", "Lembar"
    };

    // Components
    private JTextField txtKode, txtNama, txtStok, txtHarga;
    private JComboBox<String> cmbKategori, cmbSatuan;

    private InventoryController controller;
    private boolean isEditMode = false;
    private String oldKode = "";

    public InventoryForm() {
        this(null);
    }

    public InventoryForm(String[] dataToEdit) {
        controller = new InventoryController();
        setTitle(dataToEdit == null ? "Tambah Produk Baru" : "Edit Produk");

        setSize(900, 520);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // --- HEADER ---
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 25));
        headerPanel.setBackground(COLOR_BG_MAIN);
        JLabel lblTitle = new JLabel(dataToEdit == null ? "Tambah Sparepart Baru" : "Edit Data Sparepart");
        lblTitle.setFont(FONT_TITLE);
        lblTitle.setForeground(COLOR_TEXT_WHITE);
        headerPanel.add(lblTitle);
        add(headerPanel, BorderLayout.NORTH);

        // --- FORM CONTENT (2 KOLOM) ---
        JPanel mainContent = new JPanel(new GridLayout(1, 2, 40, 0));
        mainContent.setBackground(COLOR_BG_MAIN);
        mainContent.setBorder(new EmptyBorder(10, 40, 10, 40));

        // Inisialisasi Input Text
        txtKode = createTextField();
        txtNama = createTextField();
        txtStok = createTextField();
        txtHarga = createTextField();

        // Inisialisasi Dropdown (Sudah dirapikan)
        cmbKategori = createComboBox(OPTIONS_KATEGORI);
        cmbSatuan = createComboBox(OPTIONS_SATUAN);

        // -- PANEL KIRI --
        JPanel pnlLeft = new JPanel(new GridBagLayout());
        pnlLeft.setBackground(COLOR_BG_MAIN);
        createSectionTitle(pnlLeft, "Informasi Barang", 0);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0; gbc.gridx = 0; gbc.insets = new Insets(5, 0, 5, 0);

        addFormItem(pnlLeft, gbc, 1, "Kode Barang", txtKode);
        addFormItem(pnlLeft, gbc, 2, "Nama Barang", txtNama);
        addFormItem(pnlLeft, gbc, 3, "Kategori", cmbKategori);

        gbc.gridy = 10; gbc.weighty = 1.0; pnlLeft.add(Box.createGlue(), gbc);

        // -- PANEL KANAN --
        JPanel pnlRight = new JPanel(new GridBagLayout());
        pnlRight.setBackground(COLOR_BG_MAIN);
        createSectionTitle(pnlRight, "Detail Stok & Harga", 0);

        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0; gbc.gridx = 0; gbc.insets = new Insets(5, 0, 5, 0);

        addFormItem(pnlRight, gbc, 1, "Jumlah Stok", txtStok);
        addFormItem(pnlRight, gbc, 2, "Harga Jual (Rp)", txtHarga);
        addFormItem(pnlRight, gbc, 3, "Satuan", cmbSatuan);

        gbc.gridy = 10; gbc.weighty = 1.0; pnlRight.add(Box.createGlue(), gbc);

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

        // [FIX 2] Fitur Enter langsung Simpan
        this.getRootPane().setDefaultButton(btnSave);

        // --- ISI DATA (EDIT MODE) ---
        if (dataToEdit != null) {
            isEditMode = true;
            txtKode.setText(dataToEdit[0]);
            oldKode = dataToEdit[0];
            txtNama.setText(dataToEdit[1]);
            cmbKategori.setSelectedItem(dataToEdit[2]);
            txtStok.setText(dataToEdit[3]);
            String cleanHarga = dataToEdit[4].replaceAll("[^0-9]", "");
            txtHarga.setText(cleanHarga);
            cmbSatuan.setSelectedItem(dataToEdit[5]);
        }
    }

    // --- LOGIKA SIMPAN ---
    private void saveData() {
        if (txtKode.getText().isEmpty() || txtNama.getText().isEmpty() ||
                txtStok.getText().isEmpty() || txtHarga.getText().isEmpty()) {
            ModernDialog.showWarning(this, "Semua data wajib diisi!");
            return;
        }

        try {
            int stok = Integer.parseInt(txtStok.getText().trim());
            double harga = Double.parseDouble(txtHarga.getText().trim());
            String kategori = cmbKategori.getSelectedItem().toString();
            String satuan = cmbSatuan.getSelectedItem().toString();

            SparePart sp = new SparePart(
                    txtKode.getText(),
                    txtNama.getText(),
                    kategori,
                    stok,
                    harga,
                    satuan
            );

            boolean success;
            if (isEditMode) {
                success = controller.updateSparePart(oldKode, sp);
            } else {
                controller.addSparePart(sp);
                success = true;
            }

            if (success) {
                ModernDialog.showSuccess(this, "Data berhasil disimpan!");
                backToTable();
            } else {
                ModernDialog.showError(this, "Gagal menyimpan data.");
            }

        } catch (NumberFormatException e) {
            ModernDialog.showError(this, "Stok dan Harga harus berupa angka!");
        } catch (Exception e) {
            ModernDialog.showError(this, "Terjadi kesalahan: " + e.getMessage());
        }
    }

    private void backToTable() {
        new InventoryTable().setVisible(true);
        dispose();
    }

    // --- UI HELPER ---
    private void createSectionTitle(JPanel panel, String title, int row) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = row; gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 15, 0);

        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 16));
        lbl.setForeground(COLOR_ACCENT);
        panel.add(lbl, gbc);
    }

    private void addFormItem(JPanel panel, GridBagConstraints gbc, int row, String labelText, JComponent field) {
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

    // [FIX 3] ComboBox Modern dengan BasicComboBoxUI
    private JComboBox<String> createComboBox(String[] items) {
        JComboBox<String> box = new JComboBox<>(items);
        box.setPreferredSize(new Dimension(0, 35));
        box.setFont(FONT_INPUT);

        // Memaksa UI ComboBox menjadi "Basic" agar warna bisa dikontrol penuh
        box.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                // Membuat tombol panah custom (Gelap)
                JButton b = new JButton();
                b.setBorder(BorderFactory.createEmptyBorder());
                b.setVisible(false); // Hilangkan tombol default jika ingin bersih, atau style ulang
                return super.createArrowButton();
            }

            @Override
            public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
                // Paksa background utama jadi gelap
                g.setColor(COLOR_INPUT_BG);
                g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
            }
        });

        box.setBackground(COLOR_INPUT_BG);
        box.setForeground(COLOR_TEXT_WHITE);
        box.setBorder(new LineBorder(COLOR_BORDER, 1));

        // Custom Renderer untuk Warna Popup List (Agar Dark Mode)
        box.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (isSelected) {
                    setBackground(COLOR_ACCENT); // Biru saat dipilih
                    setForeground(Color.WHITE);
                } else {
                    setBackground(COLOR_INPUT_BG); // Gelap saat diam
                    setForeground(COLOR_TEXT_WHITE);
                }
                setBorder(new EmptyBorder(5, 5, 5, 5));
                return this;
            }
        });

        return box;
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
        btn.setBackground(COLOR_BG_FORM); // Sekarang variabel ini sudah ada
        btn.setForeground(COLOR_TEXT_GRAY);
        btn.setFont(FONT_LABEL);
        btn.setFocusPainted(false);
        btn.setBorder(new LineBorder(COLOR_BORDER));
        btn.setPreferredSize(new Dimension(100, 38));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
}