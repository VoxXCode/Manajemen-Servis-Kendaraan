package com.manajemenservis.view;

import com.manajemenservis.controller.*;
import com.manajemenservis.model.ServiceRecord;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.List;

public class ServiceEntryForm extends JFrame {

    // --- COLORS (Dashboard Theme) ---
    private final Color COLOR_BG_MAIN     = new Color(18, 18, 18);
    private final Color COLOR_BG_SIDEBAR  = new Color(10, 10, 10);
    private final Color COLOR_TEXT_WHITE  = new Color(255, 255, 255);
    private final Color COLOR_TEXT_GRAY   = new Color(150, 150, 150);
    private final Color COLOR_ACCENT      = new Color(65, 105, 225);
    private final Color COLOR_INPUT_BG    = new Color(45, 45, 45);
    private final Color COLOR_BORDER      = new Color(60, 60, 60);

    private final Font FONT_MAIN = new Font("SansSerif", Font.PLAIN, 14);
    private final Font FONT_TITLE = new Font("SansSerif", Font.BOLD, 28);

    // Form Components
    private JComboBox<String> comboPelanggan;
    private JComboBox<String> comboSparepart;
    private JTextField txtBiayaJasa, txtQty, txtDeskripsi;
    private JLabel lblHargaPart;

    // Data Cache
    private List<String[]> listCustomer;
    private List<String[]> listInventory;

    public ServiceEntryForm() {
        setTitle("BENGKEL KU - Input Servis Baru");
        setSize(1350, 800); // Ukuran standar Dashboard
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 1. SIDEBAR (KIRI)
        add(createSidebar(), BorderLayout.WEST);

        // 2. KONTEN UTAMA (KANAN)
        JPanel mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.setBackground(COLOR_BG_MAIN);
        mainContentPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        // -- Header --
        JLabel lblTitle = new JLabel("Input Transaksi Servis Baru");
        lblTitle.setFont(FONT_TITLE);
        lblTitle.setForeground(COLOR_ACCENT);
        lblTitle.setBorder(new EmptyBorder(0, 0, 30, 0));
        mainContentPanel.add(lblTitle, BorderLayout.NORTH);

        // -- Form Container (Agar form tidak terlalu lebar) --
        JPanel formWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        formWrapper.setBackground(COLOR_BG_MAIN);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(COLOR_BG_MAIN);
        formPanel.setPreferredSize(new Dimension(600, 600)); // Batasi lebar form

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridx = 0; gbc.weightx = 1.0;

        // --- ISI FORM ---
        // 1. Pilih Pelanggan
        addLabel(formPanel, gbc, 0, "Pilih Pelanggan (Plat - Nama)");
        comboPelanggan = new JComboBox<>();
        styleCombo(comboPelanggan);
        gbc.gridy = 1; formPanel.add(comboPelanggan, gbc);

        // 2. Pilih Sparepart (Data dari Product)
        addLabel(formPanel, gbc, 2, "Ganti Sparepart (Opsional) - Sesuai Stok Product");
        comboSparepart = new JComboBox<>();
        styleCombo(comboSparepart);
        comboSparepart.addActionListener(e -> updateHargaPart());
        gbc.gridy = 3; formPanel.add(comboSparepart, gbc);

        // Info Harga Part
        lblHargaPart = new JLabel("Harga Satuan: Rp 0");
        lblHargaPart.setForeground(Color.CYAN);
        lblHargaPart.setFont(new Font("SansSerif", Font.BOLD, 12));
        gbc.gridy = 4; gbc.insets = new Insets(-5, 0, 15, 0); formPanel.add(lblHargaPart, gbc);
        gbc.insets = new Insets(10, 0, 10, 0); // Reset insets

        // 3. Qty & Deskripsi
        addLabel(formPanel, gbc, 5, "Jumlah Part (Qty)");
        txtQty = createTextField(); txtQty.setText("1");
        gbc.gridy = 6; formPanel.add(txtQty, gbc);

        addLabel(formPanel, gbc, 7, "Deskripsi Servis (Cth: Ganti Oli & Tune Up)");
        txtDeskripsi = createTextField();
        gbc.gridy = 8; formPanel.add(txtDeskripsi, gbc);

        // 4. Biaya Jasa
        addLabel(formPanel, gbc, 9, "Biaya Jasa Mekanik (Rp)");
        txtBiayaJasa = createTextField(); txtBiayaJasa.setText("0");
        gbc.gridy = 10; formPanel.add(txtBiayaJasa, gbc);

        // Tombol Simpan
        JButton btnSave = new JButton("Simpan Transaksi");
        styleButtonPrimary(btnSave);
        btnSave.addActionListener(e -> simpanTransaksi());
        gbc.gridy = 11; gbc.insets = new Insets(30, 0, 0, 0);
        formPanel.add(btnSave, gbc);

        formWrapper.add(formPanel);
        mainContentPanel.add(formWrapper, BorderLayout.CENTER);

        add(mainContentPanel, BorderLayout.CENTER);

        // Load Data setelah UI siap
        loadDataCombo();
    }

    // --- LOGIC DATA LOADING ---
    private void loadDataCombo() {
        // Load Pelanggan
        listCustomer = new CustomerController().getAllData();
        comboPelanggan.removeAllItems();
        comboPelanggan.addItem("- Pilih Pelanggan -");
        for(String[] row : listCustomer) {
            comboPelanggan.addItem(row[3] + " - " + row[0]); // Plat - Nama
        }

        // Load Sparepart dari Inventory
        listInventory = new InventoryController().getAllData();
        comboSparepart.removeAllItems();
        comboSparepart.addItem("- Tidak Ganti Part -");
        for(String[] row : listInventory) {
            // format: Nama Barang (Stok: X)
            comboSparepart.addItem(row[1] + " (Stok: " + row[3] + " " + row[5] + ")");
        }
    }

    private void updateHargaPart() {
        int idx = comboSparepart.getSelectedIndex();
        if (idx <= 0) {
            lblHargaPart.setText("Harga Satuan: Rp 0");
        } else {
            // Ambil harga dari listInventory (idx-1 karena ada opsi default)
            String hargaStr = listInventory.get(idx - 1)[4]; // Kolom Harga
            lblHargaPart.setText("Harga Satuan: " + hargaStr);
        }
    }

    // --- LOGIC SIMPAN ---
    private void simpanTransaksi() {
        try {
            if (comboPelanggan.getSelectedIndex() <= 0) {
                ModernDialog.showWarning(this, "Pilih pelanggan terlebih dahulu!");
                return;
            }

            // 1. Ambil Data Pelanggan
            String selectedCust = (String) comboPelanggan.getSelectedItem();
            String platNomor = selectedCust.split(" - ")[0];

            // 2. Hitung Sparepart
            double totalPart = 0;
            String partInfo = "";
            int idxPart = comboSparepart.getSelectedIndex();

            if (idxPart > 0) {
                String[] partData = listInventory.get(idxPart - 1);
                String namaPart = partData[1];

                // Parsing Harga (Hapus Rp dan titik)
                String rawHarga = partData[4].replaceAll("[^0-9]", "");
                double hargaSatuan = Double.parseDouble(rawHarga);
                int qty = Integer.parseInt(txtQty.getText());

                totalPart = hargaSatuan * qty;
                partInfo = namaPart + " (x" + qty + ")";
            }

            // 3. Hitung Jasa
            double biayaJasa = Double.parseDouble(txtBiayaJasa.getText().replaceAll("[^0-9]", ""));

            // 4. Deskripsi Final
            String desc = txtDeskripsi.getText();
            if (desc.isEmpty()) desc = "Servis Rutin";
            if (!partInfo.isEmpty()) {
                desc += " + Ganti " + partInfo;
            }

            // 5. Simpan ke ServiceController (yang akan mencatat ke History)
            ServiceRecord record = new ServiceRecord(
                    LocalDate.now().toString(),
                    platNomor,
                    desc,
                    biayaJasa,
                    totalPart
            );

            new ServiceController().addServiceRecord(record);

            ModernDialog.showSuccess(this, "Transaksi Berhasil Disimpan ke History!");

            // Reset Form setelah simpan
            comboPelanggan.setSelectedIndex(0);
            comboSparepart.setSelectedIndex(0);
            txtQty.setText("1");
            txtDeskripsi.setText("");
            txtBiayaJasa.setText("0");
            lblHargaPart.setText("Harga Satuan: Rp 0");

        } catch (NumberFormatException e) {
            ModernDialog.showError(this, "Format angka salah! Cek Qty atau Biaya Jasa.");
        } catch (Exception e) {
            e.printStackTrace();
            ModernDialog.showError(this, "Gagal menyimpan: " + e.getMessage());
        }
    }

    // --- SIDEBAR NAVIGATION (Sama dengan Dashboard) ---
    private JPanel createSidebar() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_BG_SIDEBAR);
        panel.setPreferredSize(new Dimension(240, 800));
        panel.setBorder(new EmptyBorder(30, 20, 30, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.gridx = 0;

        JLabel brandLabel = new JLabel("<html><b>BENGKEL</b><br><b>KU</b></html>");
        brandLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        brandLabel.setForeground(COLOR_TEXT_WHITE);
        gbc.gridy = 0; gbc.insets = new Insets(0, 5, 25, 0); panel.add(brandLabel, gbc);

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(new Color(30, 30, 30));
        searchPanel.setBorder(new EmptyBorder(8, 10, 8, 10));
        JLabel icon = new JLabel("🔍"); icon.setForeground(COLOR_TEXT_GRAY);
        searchPanel.add(icon, BorderLayout.WEST);
        gbc.gridy = 1; panel.add(searchPanel, gbc);
        gbc.insets = new Insets(0, 0, 8, 0);

        // Menu Items (Input Servis Aktif)
        gbc.gridy = 2; panel.add(createMenuButton("Overview", "📊", false), gbc);
        gbc.gridy = 3; panel.add(createMenuButton("Customer", "👥", false), gbc);
        gbc.gridy = 4; panel.add(createMenuButton("Product", "🛍️", false), gbc);
        gbc.gridy = 5; panel.add(createMenuButton("Input Servis", "📄", true), gbc); // ACTIVE
        gbc.gridy = 6; panel.add(createMenuButton("History", "📜", false), gbc);

        gbc.gridy = 7; gbc.weighty = 1.0; panel.add(Box.createGlue(), gbc);
        gbc.weighty = 0;
        gbc.gridy = 8;
        JButton btnLogout = createMenuButton("Logout", "❓", false);
        btnLogout.addActionListener(e -> new AuthController().logout(this));
        panel.add(btnLogout, gbc);

        return panel;
    }

    private JButton createMenuButton(String text, String icon, boolean isActive) {
        JButton btn = new JButton("  " + icon + "    " + text);
        btn.setFont(FONT_MAIN);
        btn.setForeground(isActive ? COLOR_TEXT_WHITE : COLOR_TEXT_GRAY);
        btn.setBackground(COLOR_BG_SIDEBAR);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(8, 5, 8, 0));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setForeground(COLOR_TEXT_WHITE); }
            public void mouseExited(MouseEvent e) { if(!isActive) btn.setForeground(COLOR_TEXT_GRAY); }
        });

        if (text.equals("Overview")) btn.addActionListener(e -> { new DashboardView().setVisible(true); dispose(); });
        if (text.equals("Customer")) btn.addActionListener(e -> { new CustomerTable().setVisible(true); dispose(); });
        if (text.equals("Product")) btn.addActionListener(e -> { new InventoryTable().setVisible(true); dispose(); });
        if (text.equals("History")) btn.addActionListener(e -> { new ServiceHistoryTable().setVisible(true); dispose(); });
        // Input Servis tidak perlu action karena sedang aktif

        return btn;
    }

    // --- UI HELPERS ---
    private void addLabel(JPanel p, GridBagConstraints gbc, int row, String text) {
        JLabel l = new JLabel(text);
        l.setForeground(COLOR_TEXT_GRAY);
        l.setFont(new Font("SansSerif", Font.BOLD, 13));
        gbc.gridy = row; p.add(l, gbc);
    }

    private JTextField createTextField() {
        JTextField f = new JTextField();
        f.setPreferredSize(new Dimension(0, 40));
        f.setBackground(COLOR_INPUT_BG);
        f.setForeground(COLOR_TEXT_WHITE);
        f.setCaretColor(COLOR_TEXT_WHITE);
        f.setBorder(BorderFactory.createCompoundBorder(new LineBorder(COLOR_BORDER), new EmptyBorder(5, 10, 5, 10)));
        return f;
    }

    private void styleCombo(JComboBox box) {
        box.setPreferredSize(new Dimension(0, 40));
        box.setBackground(COLOR_INPUT_BG);
        box.setForeground(COLOR_TEXT_WHITE);
        box.setBorder(BorderFactory.createLineBorder(COLOR_BORDER));
    }

    private void styleButtonPrimary(JButton btn) {
        btn.setBackground(COLOR_ACCENT);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(12, 0, 12, 0));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
}