package com.manajemenservis.view;

import com.manajemenservis.controller.AuthController;
import com.manajemenservis.controller.ServiceController;
import com.manajemenservis.model.ServiceRecord;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class ServiceHistoryTable extends JFrame {

    // --- WARNA TEMA (Menyesuaikan CustomerTable) ---
    private final Color COLOR_BG_MAIN     = new Color(18, 18, 18);
    private final Color COLOR_BG_SIDEBAR  = new Color(10, 10, 10);
    private final Color COLOR_BG_TABLE    = new Color(30, 30, 30);
    private final Color COLOR_TEXT_WHITE  = new Color(255, 255, 255);
    private final Color COLOR_TEXT_GRAY   = new Color(150, 150, 150);
    private final Color COLOR_ACCENT      = new Color(65, 105, 225);
    private final Color COLOR_BORDER      = new Color(60, 60, 60);
    private final Color COLOR_SELECTION   = new Color(50, 50, 50);

    private final Font FONT_MAIN = new Font("SansSerif", Font.PLAIN, 14);
    private final Font FONT_BOLD = new Font("SansSerif", Font.BOLD, 14);
    private final Font FONT_TITLE = new Font("SansSerif", Font.BOLD, 28);

    private JTable table;
    private DefaultTableModel tableModel;
    private ServiceController controller;

    public ServiceHistoryTable() {
        controller = new ServiceController();

        setTitle("BENGKEL KU - Riwayat Servis");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(createSidebar(), BorderLayout.WEST);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(COLOR_BG_MAIN);
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(COLOR_BG_MAIN);
        headerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel lblTitle = new JLabel("Riwayat Servis");
        lblTitle.setFont(FONT_TITLE);
        lblTitle.setForeground(COLOR_ACCENT);
        headerPanel.add(lblTitle, BorderLayout.WEST);

        JButton btnRefresh = new JButton("Refresh Data");
        styleButtonSmall(btnRefresh);
        btnRefresh.addActionListener(e -> loadData());
        headerPanel.add(btnRefresh, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // B. Tabel Data (Mengikuti design CustomerTable)
        JScrollPane scrollPane = createTable();
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);

        loadData();
    }

    private JScrollPane createTable() {
        String[] columns = {"Tanggal", "Plat Nomor", "Deskripsi Servis", "Biaya Jasa", "Sparepart", "Total"};

        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(tableModel);
        table.setBackground(COLOR_BG_TABLE);
        table.setForeground(COLOR_TEXT_WHITE);
        table.setRowHeight(55); // Tinggi baris sesuai CustomerTable
        table.setFont(FONT_MAIN);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowSelectionAllowed(true);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFillsViewportHeight(true);

        // Header Styling (Sama dengan HeaderRenderer di CustomerTable)
        JTableHeader header = table.getTableHeader();
        header.setDefaultRenderer(new HeaderRenderer());
        header.setBackground(COLOR_BG_MAIN);
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(0, 50));

        // Renderer untuk isi sel (Sama dengan BaseTableCellRenderer di CustomerTable)
        BaseTableCellRenderer cellRenderer = new BaseTableCellRenderer();
        cellRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new LineBorder(COLOR_BORDER, 1));
        scrollPane.getViewport().setBackground(COLOR_BG_MAIN);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        // MENGHILANGKAN TITIK PUTIH DI POJOK KANAN BAWAH
        JPanel corner = new JPanel();
        corner.setBackground(COLOR_BG_MAIN);
        scrollPane.setCorner(JScrollPane.LOWER_RIGHT_CORNER, corner);

        return scrollPane;
    }

    // --- RENDERER CLASSES (Copy-Paste Design dari CustomerTable) ---

    class BaseTableCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, false, row, column);
            if (isSelected) setBackground(COLOR_SELECTION);
            else setBackground(COLOR_BG_TABLE);
            setForeground(COLOR_TEXT_WHITE);
            // Memberikan garis bawah per baris (MatteBorder)
            setBorder(BorderFactory.createCompoundBorder(
                    new MatteBorder(0, 0, 1, 0, COLOR_BORDER),
                    new EmptyBorder(0, 5, 0, 5)
            ));
            return this;
        }
    }

    class HeaderRenderer extends DefaultTableCellRenderer {
        public HeaderRenderer() {
            setOpaque(true);
            setHorizontalAlignment(JLabel.CENTER);
            setBackground(COLOR_BG_MAIN);
            setForeground(COLOR_TEXT_GRAY);
            setFont(FONT_BOLD);
            setBorder(BorderFactory.createCompoundBorder(
                    new MatteBorder(0, 0, 1, 0, COLOR_BORDER),
                    new EmptyBorder(10, 5, 10, 5)
            ));
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value != null ? value.toString() : "");
            return this;
        }
    }

    // --- LOGIKA DATA (Tetap Sama) ---
    private void loadData() {
        tableModel.setRowCount(0);
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

    // --- SIDEBAR (Tetap Sama) ---
    private JPanel createSidebar() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_BG_SIDEBAR);
        panel.setPreferredSize(new Dimension(240, 700));
        panel.setBorder(new EmptyBorder(30, 20, 30, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        JLabel brandLabel = new JLabel("<html><b>BENGKEL</b><br><b>KU</b></html>");
        brandLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        brandLabel.setForeground(COLOR_TEXT_WHITE);
        gbc.gridy = 0; gbc.insets = new Insets(0, 5, 40, 0);
        panel.add(brandLabel, gbc);

        gbc.insets = new Insets(0, 0, 10, 0);
        gbc.gridy = 1; panel.add(createMenuButton("Dashboard", "🏠", false), gbc);
        gbc.gridy = 2; panel.add(createMenuButton("Input Servis", "📄", false), gbc);
        gbc.gridy = 3; panel.add(createMenuButton("Customer", "👥", false), gbc);
        gbc.gridy = 4; panel.add(createMenuButton("Inventory", "📦", false), gbc);
        gbc.gridy = 5; panel.add(createMenuButton("History", "📜", true), gbc);

        gbc.gridy = 6; gbc.weighty = 1.0;
        panel.add(Box.createGlue(), gbc);

        gbc.weighty = 0;
        JButton btnLogout = createMenuButton("Back to Menu", "⬅", false);
        btnLogout.addActionListener(e -> {
            new DashboardView().setVisible(true);
            dispose();
        });
        gbc.gridy = 7;
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

        if (text.equals("Dashboard")) btn.addActionListener(e -> { new DashboardView().setVisible(true); dispose(); });
        if (text.equals("Customer")) btn.addActionListener(e -> { new CustomerTable().setVisible(true); dispose(); });

        return btn;
    }

    private void styleButtonSmall(JButton btn) {
        btn.setBackground(COLOR_ACCENT);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(5, 15, 5, 15));
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
}