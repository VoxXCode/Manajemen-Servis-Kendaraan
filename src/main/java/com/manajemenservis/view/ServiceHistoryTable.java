package com.manajemenservis.view;

import com.manajemenservis.controller.AuthController;
import com.manajemenservis.controller.ServiceController;
import com.manajemenservis.model.ServiceRecord;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class ServiceHistoryTable extends JFrame {

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
        setSize(1350, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Auto-Refresh saat window aktif
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
                loadData();
            }
        });

        add(createSidebar(), BorderLayout.WEST);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(COLOR_BG_MAIN);
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        // -- Header Tanpa Tombol Refresh --
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(COLOR_BG_MAIN);
        headerPanel.setBorder(new EmptyBorder(0, 0, 25, 0));

        JLabel lblTitle = new JLabel("Arsip Riwayat Servis");
        lblTitle.setFont(FONT_TITLE);
        lblTitle.setForeground(COLOR_ACCENT);
        headerPanel.add(lblTitle, BorderLayout.WEST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // -- Table Content --
        JScrollPane scrollPane = createTable();
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);

        loadData();
    }

    private JScrollPane createTable() {
        String[] columns = {"Tanggal", "Plat Nomor", "Deskripsi Servis", "Jasa", "Sparepart", "Total"};

        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(tableModel);
        table.setBackground(COLOR_BG_TABLE);
        table.setForeground(COLOR_TEXT_WHITE);
        table.setRowHeight(50);
        table.setFont(FONT_MAIN);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFillsViewportHeight(true);

        JTableHeader header = table.getTableHeader();
        header.setDefaultRenderer(new HeaderRenderer());
        header.setBackground(COLOR_BG_MAIN);
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(0, 50));

        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(350);
        table.getColumnModel().getColumn(5).setPreferredWidth(120);

        BaseTableCellRenderer cellRenderer = new BaseTableCellRenderer();
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        // [FIX] Menghilangkan Garis Putih (Border Luar)
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(COLOR_BG_MAIN);

        JPanel corner = new JPanel(); corner.setBackground(COLOR_BG_MAIN);
        scrollPane.setCorner(JScrollPane.LOWER_RIGHT_CORNER, corner);

        return scrollPane;
    }

    // --- RENDERERS ---
    class BaseTableCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, false, row, column);

            if (isSelected) setBackground(COLOR_SELECTION);
            else setBackground(COLOR_BG_TABLE);

            setForeground(COLOR_TEXT_WHITE);
            setHorizontalAlignment(JLabel.CENTER);
            if(column == 2) setHorizontalAlignment(JLabel.LEFT);

            setBorder(BorderFactory.createCompoundBorder(
                    new MatteBorder(0, 0, 1, 0, COLOR_BORDER),
                    new EmptyBorder(0, 10, 0, 10)
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

    // --- LOGIKA DATA ---
    private void loadData() {
        tableModel.setRowCount(0);
        try {
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
        } catch (Exception e) {}
    }

    private String formatRupiah(double number) {
        return String.format("Rp %,.0f", number);
    }

    // --- SIDEBAR (Sama) ---
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

        gbc.gridy = 2; panel.add(createMenuButton("Overview", "📊", false), gbc);
        gbc.gridy = 3; panel.add(createMenuButton("Customer", "👥", false), gbc);
        gbc.gridy = 4; panel.add(createMenuButton("Product", "🛍️", false), gbc);
        gbc.gridy = 5; panel.add(createMenuButton("History", "📜", true), gbc); // Active

        gbc.gridy = 6; gbc.weighty = 1.0; panel.add(Box.createGlue(), gbc);
        gbc.weighty = 0; gbc.gridy = 7; panel.add(createMenuButton("Settings", "🛠️", false), gbc);
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
        if (text.equals("Input Servis")) btn.addActionListener(e -> { new ServiceEntryForm().setVisible(true); dispose(); });

        return btn;
    }
}