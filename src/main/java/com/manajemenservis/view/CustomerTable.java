package com.manajemenservis.view;

import com.manajemenservis.controller.AuthController;
import com.manajemenservis.controller.CustomerController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.EventObject;
import java.util.List;

public class CustomerTable extends JFrame {

    // --- WARNA TEMA ---
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

    private JTable table;
    private DefaultTableModel tableModel;
    private CustomerController controller;
    private JTextField txtSearch;

    public CustomerTable() {
        controller = new CustomerController();
        setTitle("BENGKEL KU - Data Pelanggan");
        setSize(1350, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(createSidebar(), BorderLayout.WEST);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(COLOR_BG_MAIN);

        // --- HEADER HALAMAN ---
        JPanel topHeader = new JPanel(new BorderLayout());
        topHeader.setBackground(COLOR_BG_MAIN);
        topHeader.setBorder(new EmptyBorder(25, 30, 25, 30));

        JLabel lblTitle = new JLabel("Data Pelanggan");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblTitle.setForeground(COLOR_ACCENT);

        JPanel rightActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightActions.setBackground(COLOR_BG_MAIN);

        txtSearch = createSearchField();
        JButton btnAdd = new JButton("+ New Customer");
        styleButtonPrimary(btnAdd);
        btnAdd.addActionListener(e -> {
            new CustomerForm().setVisible(true);
            dispose();
        });

        rightActions.add(txtSearch);
        rightActions.add(btnAdd);

        topHeader.add(lblTitle, BorderLayout.WEST);
        topHeader.add(rightActions, BorderLayout.EAST);
        mainPanel.add(topHeader, BorderLayout.NORTH);

        // --- BODY ---
        JPanel bodyPanel = new JPanel(new BorderLayout());
        bodyPanel.setBackground(COLOR_BG_MAIN);
        bodyPanel.setBorder(new EmptyBorder(0, 30, 20, 30));

        JScrollPane scrollPane = createTable();
        bodyPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel footerPanel = createPaginationFooter();
        bodyPanel.add(footerPanel, BorderLayout.SOUTH);

        mainPanel.add(bodyPanel, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);

        loadData();
    }

    private JScrollPane createTable() {
        String[] columns = {"No.", "Nama", "No HP", "Alamat", "Plat Nomor", "Merk", "Tipe", "Tahun", "Actions"};

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 8;
            }
        };

        table = new JTable(tableModel);
        table.setBackground(COLOR_BG_TABLE);
        table.setForeground(COLOR_TEXT_WHITE);
        table.setRowHeight(55);
        table.setFont(FONT_MAIN);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(false);
        table.setCellSelectionEnabled(false);

        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFillsViewportHeight(true);

        JTableHeader header = table.getTableHeader();
        header.setDefaultRenderer(new HeaderRenderer());
        header.setBackground(COLOR_BG_MAIN);
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(0, 50));
        header.setBorder(null);

        // Lebar Kolom
        table.getColumnModel().getColumn(0).setMaxWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(120);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(150);
        table.getColumnModel().getColumn(4).setPreferredWidth(90);
        table.getColumnModel().getColumn(5).setPreferredWidth(80);
        table.getColumnModel().getColumn(6).setPreferredWidth(80);
        table.getColumnModel().getColumn(7).setPreferredWidth(60);
        table.getColumnModel().getColumn(8).setMinWidth(160);

        BaseTableCellRenderer centerRenderer = new BaseTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        BaseTableCellRenderer leftRenderer = new BaseTableCellRenderer();
        leftRenderer.setHorizontalAlignment(JLabel.LEFT);
        leftRenderer.setPadding(10);

        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        for (int i = 1; i < 8; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(leftRenderer);
        }
        table.getColumnModel().getColumn(7).setCellRenderer(centerRenderer);

        // [UBAH] Menggunakan ActionButtonEditor yang baru (turunan AbstractCellEditor)
        table.getColumnModel().getColumn(8).setCellRenderer(new ActionButtonRenderer());
        table.getColumnModel().getColumn(8).setCellEditor(new ActionButtonEditor());

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new LineBorder(COLOR_BORDER, 1));
        scroll.getViewport().setBackground(COLOR_BG_MAIN);
        return scroll;
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<String[]> list = controller.getAllData();
        int no = 1;
        for (String[] row : list) {
            tableModel.addRow(new Object[]{
                    no++, row[0], row[1], row[2], row[3], row[4], row[5], row[6], ""
            });
        }
    }

    // --- BASE RENDERER ---
    class BaseTableCellRenderer extends DefaultTableCellRenderer {
        private int padding = 0;
        public void setPadding(int padding) { this.padding = padding; }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, false, row, column);

            if (isSelected) setBackground(COLOR_SELECTION);
            else setBackground(COLOR_BG_TABLE);

            setForeground(COLOR_TEXT_WHITE);
            setBorder(BorderFactory.createCompoundBorder(
                    new MatteBorder(0, 0, 1, 0, COLOR_BORDER),
                    new EmptyBorder(0, padding, 0, padding)
            ));
            return this;
        }
    }

    // --- HEADER RENDERER ---
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
            setBackground(COLOR_BG_MAIN);
            return this;
        }
    }

    // --- PANEL TOMBOL ---
    class ActionPanel extends JPanel {
        public JButton btnEdit = new JButton("Edit");
        public JButton btnDelete = new JButton("Delete");

        public ActionPanel() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 12));
            setOpaque(true); // Penting agar warna background bekerja

            styleActionBtn(btnEdit, new Color(255, 193, 7));
            styleActionBtn(btnDelete, new Color(220, 53, 69));

            add(btnEdit);
            add(btnDelete);
        }

        private void styleActionBtn(JButton btn, Color c) {
            btn.setFont(new Font("SansSerif", Font.BOLD, 12));
            btn.setForeground(c);
            btn.setBackground(new Color(45, 45, 45));
            btn.setOpaque(true);
            btn.setBorderPainted(false);
            btn.setFocusPainted(false);
            btn.setMargin(new Insets(0, 0, 0, 0));
            btn.setPreferredSize(new Dimension(65, 30));
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
    }

    // --- ACTION RENDERER (Saat Diam) ---
    class ActionButtonRenderer implements TableCellRenderer {
        private ActionPanel panel = new ActionPanel();
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            panel.setBackground(isSelected ? COLOR_SELECTION : COLOR_BG_TABLE);
            panel.setBorder(new MatteBorder(0, 0, 1, 0, COLOR_BORDER));
            return panel;
        }
    }

    // --- ACTION EDITOR (Saat Diklik) - FIX TOTAL ---
    // Menggunakan AbstractCellEditor alih-alih DefaultCellEditor
    // Ini menghilangkan perilaku aneh "checkbox" dan default styling Java
    class ActionButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private ActionPanel panel = new ActionPanel();
        private String currentPlat;
        private String currentNama;

        public ActionButtonEditor() {
            // Event Listener Tombol
            panel.btnEdit.addActionListener(e -> { fireEditingStopped(); openEditForm(); });
            panel.btnDelete.addActionListener(e -> { fireEditingStopped(); deleteData(); });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            // PAKSA warna background jadi abu-abu seleksi.
            panel.setBackground(COLOR_SELECTION);

            // PAKSA garis bawah tetap ada
            panel.setBorder(new MatteBorder(0, 0, 1, 0, COLOR_BORDER));

            // Ambil data untuk logika edit/hapus
            this.currentNama = table.getValueAt(row, 1).toString();
            this.currentPlat = table.getValueAt(row, 4).toString();
            return panel;
        }

        @Override
        public Object getCellEditorValue() { return ""; }

        private void openEditForm() {
            List<String[]> allData = controller.getAllData();
            String[] dataToEdit = null;
            for(String[] d : allData) {
                if(d[3].equals(currentPlat)) { dataToEdit = d; break; }
            }
            if(dataToEdit != null) {
                new CustomerForm(dataToEdit).setVisible(true);
                dispose();
            }
        }

        private void deleteData() {
            int confirm = JOptionPane.showConfirmDialog(null,
                    "Hapus data " + currentNama + " (" + currentPlat + ")?",
                    "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                controller.deleteData(currentPlat);
                loadData();
            }
        }
    }

    // --- UI HELPER ---
    private JTextField createSearchField() {
        JTextField field = new JTextField(20);
        field.setText("  Search...");
        field.setBackground(new Color(40, 40, 40));
        field.setForeground(COLOR_TEXT_GRAY);
        field.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        field.setFont(FONT_MAIN);
        field.setCaretColor(Color.WHITE);
        return field;
    }

    private void styleButtonPrimary(JButton btn) {
        btn.setBackground(COLOR_ACCENT);
        btn.setForeground(Color.WHITE);
        btn.setFont(FONT_BOLD);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private JPanel createPaginationFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(COLOR_BG_MAIN);
        footer.setBorder(new EmptyBorder(15, 0, 0, 0));
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        right.setBackground(COLOR_BG_MAIN);
        JButton btnPrev = new JButton("<"); JButton btnNext = new JButton(">");
        JLabel lblPage = new JLabel(" Page 1 of 1 "); lblPage.setForeground(COLOR_TEXT_GRAY);
        btnPrev.setBackground(COLOR_BG_TABLE); btnPrev.setForeground(COLOR_TEXT_WHITE); btnPrev.setBorder(new LineBorder(COLOR_BORDER)); btnPrev.setPreferredSize(new Dimension(30, 30));
        btnNext.setBackground(COLOR_BG_TABLE); btnNext.setForeground(COLOR_TEXT_WHITE); btnNext.setBorder(new LineBorder(COLOR_BORDER)); btnNext.setPreferredSize(new Dimension(30, 30));
        right.add(btnPrev); right.add(lblPage); right.add(btnNext);
        footer.add(right, BorderLayout.EAST);
        return footer;
    }

    // --- SIDEBAR ---
    private JPanel createSidebar() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_BG_SIDEBAR);
        panel.setPreferredSize(new Dimension(240, 800));
        panel.setBorder(new EmptyBorder(30, 20, 30, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.gridx = 0;

        JLabel brandLabel = new JLabel("<html><b>BENGKEL</b><br><b>KU</b></html>");
        brandLabel.setFont(new Font("SansSerif", Font.BOLD, 22)); brandLabel.setForeground(COLOR_TEXT_WHITE);
        gbc.gridy = 0; gbc.insets = new Insets(0, 5, 25, 0); panel.add(brandLabel, gbc);

        JPanel searchPanel = new JPanel(new BorderLayout()); searchPanel.setBackground(new Color(30, 30, 30)); searchPanel.setBorder(new EmptyBorder(8, 10, 8, 10));
        JLabel icon = new JLabel("🔍"); icon.setForeground(COLOR_TEXT_GRAY); searchPanel.add(icon, BorderLayout.WEST);
        gbc.gridy = 1; panel.add(searchPanel, gbc);

        gbc.insets = new Insets(0, 0, 8, 0);
        gbc.gridy = 2; panel.add(createMenuButton("Overview", "📊", false), gbc);
        gbc.gridy = 3; panel.add(createMenuButton("Customer", "👥", true), gbc);
        gbc.gridy = 4; panel.add(createMenuButton("Product", "🛍️", false), gbc);
        gbc.gridy = 5; panel.add(createMenuButton("History", "📜", false), gbc);

        gbc.gridy = 6; gbc.weighty = 1.0; panel.add(Box.createGlue(), gbc);
        gbc.weighty = 0; gbc.gridy = 7; panel.add(createMenuButton("Settings", "🛠️", false), gbc);
        gbc.gridy = 8; JButton btnLogout = createMenuButton("Logout", "❓", false); btnLogout.addActionListener(e -> new AuthController().logout(this)); panel.add(btnLogout, gbc);
        return panel;
    }

    private JButton createMenuButton(String text, String icon, boolean isActive) {
        JButton btn = new JButton("  " + icon + "    " + text);
        btn.setFont(FONT_MAIN); btn.setForeground(isActive ? COLOR_TEXT_WHITE : COLOR_TEXT_GRAY);
        btn.setBackground(COLOR_BG_SIDEBAR); btn.setBorderPainted(false); btn.setFocusPainted(false);
        btn.setContentAreaFilled(false); btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); btn.setBorder(new EmptyBorder(8, 5, 8, 0));
        if(text.equals("Overview")) btn.addActionListener(e->{new DashboardView().setVisible(true);dispose();});
        if(text.equals("Product")) btn.addActionListener(e->{new InventoryTable().setVisible(true);dispose();});
        if(text.equals("History")) btn.addActionListener(e->{new ServiceHistoryTable().setVisible(true);dispose();});
        return btn;
    }
}