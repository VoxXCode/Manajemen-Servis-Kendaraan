package com.manajemenservis.view;

import com.manajemenservis.controller.AuthController;
import com.manajemenservis.controller.ServiceController;
import com.manajemenservis.model.ServiceRecord;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class ServiceHistoryTable extends JFrame {

    // --- WARNA TEMA (Dark Mode) ---
    private final Color COLOR_BG_MAIN     = new Color(18, 18, 18);
    private final Color COLOR_BG_SIDEBAR  = new Color(10, 10, 10);
    private final Color COLOR_BG_TABLE    = new Color(30, 30, 30); // Warna background tabel
    private final Color COLOR_HEADER      = new Color(45, 45, 45); // Warna header tabel
    private final Color COLOR_ACCENT      = new Color(65, 105, 225); // Royal Blue (mirip referensi)
    private final Color COLOR_TEXT_WHITE  = new Color(255, 255, 255);
    private final Color COLOR_TEXT_GRAY   = new Color(170, 170, 170);

    private final Font FONT_MAIN = new Font("SansSerif", Font.PLAIN, 14);
    private final Font FONT_HEADER = new Font("SansSerif", Font.BOLD, 14);
    private final Font FONT_TITLE = new Font("SansSerif", Font.BOLD, 24);

    private JTable table;
    private DefaultTableModel tableModel;
    private ServiceController controller;

    public ServiceHistoryTable() {
        controller = new ServiceController();

        setTitle("BENGKEL KU - Riwayat Servis");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Agar tidak menutup seluruh aplikasi
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 1. Sidebar (Kiri)
        add(createSidebar(), BorderLayout.WEST);

        // 2. Konten Utama (Kanan)
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(COLOR_BG_MAIN);
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30)); // Padding konten

        // A. Judul Halaman
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(COLOR_BG_MAIN);
        headerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel lblTitle = new JLabel("Riwayat Servis");
        lblTitle.setFont(FONT_TITLE);
        lblTitle.setForeground(COLOR_ACCENT); // Warna biru seperti di referensi
        headerPanel.add(lblTitle, BorderLayout.WEST);

        // Tombol Filter / Refresh (Opsional)
        JButton btnRefresh = new JButton("Refresh Data");
        styleButtonSmall(btnRefresh);
        btnRefresh.addActionListener(e -> loadData());
        headerPanel.add(btnRefresh, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // B. Tabel Data
        JScrollPane scrollPane = createTable();
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);

        // Load data awal
        loadData();
    }

    // --- SETUP TABEL MODERN ---
    private JScrollPane createTable() {
        String[] columns = {"Tanggal", "Plat Nomor", "Deskripsi Servis", "Biaya Jasa", "Sparepart", "Total"};

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabel tidak bisa diedit langsung
            }
        };

        table = new JTable(tableModel);
        table.setFont(FONT_MAIN);
        table.setForeground(COLOR_TEXT_WHITE);
        table.setBackground(COLOR_BG_TABLE);
        table.setRowHeight(40); // Tinggi baris agar lega (mirip referensi)
        table.setGridColor(new Color(50, 50, 50));
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(60, 60, 60));
        table.setSelectionForeground(Color.WHITE);
        table.setShowVerticalLines(false); // Hilangkan garis vertikal agar bersih

        // Styling Header Tabel
        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_HEADER);
        header.setBackground(COLOR_HEADER);
        header.setForeground(COLOR_TEXT_WHITE);
        header.setPreferredSize(new Dimension(0, 45)); // Header lebih tinggi
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, COLOR_ACCENT)); // Garis bawah biru

        // Rata Tengah untuk Header
        ((DefaultTableCellRenderer)header.getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);

        // Custom Cell Renderer (Padding teks dalam sel)
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setBorder(new EmptyBorder(0, 10, 0, 10)); // Padding kiri-kanan
        centerRenderer.setBackground(COLOR_BG_TABLE);
        centerRenderer.setForeground(COLOR_TEXT_WHITE);

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // ScrollPane tanpa border kasar
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new LineBorder(new Color(50, 50, 50), 1));
        scrollPane.getViewport().setBackground(COLOR_BG_MAIN); // Latar belakang area kosong

        return scrollPane;
    }

    // --- LOGIKA LOAD DATA ---
    private void loadData() {
        tableModel.setRowCount(0); // Bersihkan tabel
        List<ServiceRecord> list = controller.getAllServiceRecords();

        for (ServiceRecord r : list) {
            Object[] row = {
                    r.getDate(),
                    r.getPlateNumber(),
                    r.getDescription(),
                    formatRupiah(r.getServiceCost()),
                    formatRupiah(r.getPartCost()),
                    formatRupiah(r.getTotalCost())
            };
            tableModel.addRow(row);
        }
    }

    private String formatRupiah(double number) {
        return String.format("Rp %,.0f", number);
    }

    // --- SIDEBAR (Sama persis dengan DashboardView) ---
    private JPanel createSidebar() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_BG_SIDEBAR);
        panel.setPreferredSize(new Dimension(240, 700));
        panel.setBorder(new EmptyBorder(30, 20, 30, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // Logo
        JLabel brandLabel = new JLabel("<html><b>BENGKEL</b><br><b>KU</b></html>");
        brandLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        brandLabel.setForeground(COLOR_TEXT_WHITE);
        gbc.gridy = 0; gbc.insets = new Insets(0, 5, 40, 0);
        panel.add(brandLabel, gbc);

        // Menu Items
        gbc.insets = new Insets(0, 0, 10, 0);

        gbc.gridy = 1; panel.add(createMenuButton("Dashboard", "🏠", true), gbc); // Kembali ke Dashboard
        gbc.gridy = 2; panel.add(createMenuButton("Input Servis", "📄", false), gbc);
        gbc.gridy = 3; panel.add(createMenuButton("Customer", "👥", false), gbc);
        gbc.gridy = 4; panel.add(createMenuButton("Inventory", "📦", false), gbc);

        gbc.gridy = 5; gbc.weighty = 1.0;
        panel.add(Box.createGlue(), gbc);

        gbc.weighty = 0;
        JButton btnLogout = createMenuButton("Back to Menu", "⬅", false);
        btnLogout.addActionListener(e -> {
            new DashboardView().setVisible(true);
            dispose();
        });
        gbc.gridy = 6;
        panel.add(btnLogout, gbc);

        return panel;
    }

    // Helper membuat tombol menu sidebar
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

        // Aksi Navigasi Sederhana
        if (text.equals("Dashboard")) btn.addActionListener(e -> {
            new DashboardView().setVisible(true); dispose();
        });
        if (text.equals("Input Servis")) btn.addActionListener(e -> {
            new ServiceEntryForm().setVisible(true);
        });
        if (text.equals("Customer")) btn.addActionListener(e -> {
            new CustomerForm().setVisible(true);
        });

        return btn;
    }

    // Helper styling tombol kecil (Refresh)
    private void styleButtonSmall(JButton btn) {
        btn.setBackground(COLOR_ACCENT);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(5, 15, 5, 15));
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
}