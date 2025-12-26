package com.manajemenservis.view;

import com.manajemenservis.controller.AuthController;
import com.manajemenservis.controller.InventoryController; // Aktifkan jika sudah ada

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.stream.Collectors;

public class InventoryTable extends JFrame {

    // --- WARNA TEMA (Sama persis dengan CustomerTable) ---
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

    // --- PAGINATION ---
    private int currentPage = 1;
    private final int rowsPerPage = 10; // Batas 10 Baris

    private List<String[]> allDataCache = new ArrayList<>();
    private List<String[]> filteredData = new ArrayList<>();

    private JTable table;
    private DefaultTableModel tableModel;
    // private InventoryController controller; // Ganti dengan Controller asli Anda
    private JTextField txtSearch;
    private JLabel lblPageInfo;
    private JButton btnPrev, btnNext;

    public InventoryTable() {
        // controller = new InventoryController();
        setTitle("BENGKEL KU - Data Produk");
        setSize(1350, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(createSidebar(), BorderLayout.WEST);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(COLOR_BG_MAIN);

        // --- HEADER ---
        JPanel topHeader = new JPanel(new BorderLayout());
        topHeader.setBackground(COLOR_BG_MAIN);
        topHeader.setBorder(new EmptyBorder(25, 30, 25, 30));

        JLabel lblTitle = new JLabel("Stok Barang / Sparepart");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblTitle.setForeground(COLOR_ACCENT);

        JPanel rightActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightActions.setBackground(COLOR_BG_MAIN);

        txtSearch = createSearchField();
        setupSearchLogic();

        JButton btnAdd = new JButton("New Product");
        styleButtonPrimary(btnAdd);
        btnAdd.addActionListener(e -> {
            new InventoryForm().setVisible(true);
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

        // LOAD DATA (Dummy atau Asli)
        initData();
    }

    private void initData() {
        // --- CONTOH DATA DUMMY (Hapus jika sudah ada Controller) ---
        allDataCache = new ArrayList<>();
        for (int i = 1; i <= 25; i++) {
            allDataCache.add(new String[]{
                    "BRG-" + String.format("%03d", i),
                    "Oli Mesin MPX " + i,
                    "Oli",
                    String.valueOf(10 + i),
                    "Rp 55.000",
                    "Botol"
            });
        }
        // allDataCache = controller.getAllInventory(); // Gunakan ini nanti

        filteredData = new ArrayList<>(allDataCache);
        currentPage = 1;
        refreshTable();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);

        int totalRows = filteredData.size();
        int totalPages = (int) Math.ceil((double) totalRows / rowsPerPage);
        if (totalPages == 0) totalPages = 1;
        if (currentPage > totalPages) currentPage = totalPages;

        int startIndex = (currentPage - 1) * rowsPerPage;
        int endIndex = Math.min(startIndex + rowsPerPage, totalRows);

        int noUrut = startIndex + 1;
        for (int i = startIndex; i < endIndex; i++) {
            String[] row = filteredData.get(i);
            // Kolom: No, Kode, Nama, Kategori, Stok, Harga, Satuan, Action
            tableModel.addRow(new Object[]{
                    noUrut++, row[0], row[1], row[2], row[3], row[4], row[5], ""
            });
        }

        lblPageInfo.setText(" Page " + currentPage + " of " + totalPages + " (" + totalRows + " data) ");
        btnPrev.setEnabled(currentPage > 1);
        btnNext.setEnabled(currentPage < totalPages);
    }

    private void setupSearchLogic() {
        txtSearch.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                if(txtSearch.getText().equals("  Search...")) {
                    txtSearch.setText(""); txtSearch.setForeground(Color.WHITE);
                }
            }
            public void focusLost(FocusEvent e) {
                if(txtSearch.getText().isEmpty()) {
                    txtSearch.setText("  Search..."); txtSearch.setForeground(COLOR_TEXT_GRAY);
                }
            }
        });

        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { doSearch(); }
            public void removeUpdate(DocumentEvent e) { doSearch(); }
            public void changedUpdate(DocumentEvent e) { doSearch(); }
        });
    }

    private void doSearch() {
        String keyword = txtSearch.getText().trim().toLowerCase();
        if (keyword.isEmpty() || keyword.equals("search...")) {
            filteredData = new ArrayList<>(allDataCache);
        } else {
            filteredData = allDataCache.stream()
                    .filter(row ->
                            row[0].toLowerCase().contains(keyword) || // Kode
                                    row[1].toLowerCase().contains(keyword) || // Nama Barang
                                    row[2].toLowerCase().contains(keyword)    // Kategori
                    )
                    .collect(Collectors.toList());
        }
        currentPage = 1;
        refreshTable();
    }

    // --- SETUP TABLE ---
    private JScrollPane createTable() {
        // Kolom khusus Inventory
        String[] columns = {"No.", "Kode", "Nama Barang", "Kategori", "Stok", "Harga Jual", "Satuan", "Actions"};

        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return column == 7; } // Kolom 7 (Action) Editable
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

        // Lebar Kolom (Sesuaikan dengan Data Produk)
        table.getColumnModel().getColumn(0).setMaxWidth(40);  // No
        table.getColumnModel().getColumn(1).setPreferredWidth(100); // Kode
        table.getColumnModel().getColumn(2).setPreferredWidth(200); // Nama Barang
        table.getColumnModel().getColumn(3).setPreferredWidth(100); // Kategori
        table.getColumnModel().getColumn(4).setPreferredWidth(60);  // Stok
        table.getColumnModel().getColumn(5).setPreferredWidth(100); // Harga
        table.getColumnModel().getColumn(6).setPreferredWidth(80);  // Satuan
        table.getColumnModel().getColumn(7).setMinWidth(160);       // Action

        // RENDERER CENTER (Semua Data Rata Tengah)
        BaseTableCellRenderer centerRenderer = new BaseTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        for (int i = 0; i < 7; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Action Buttons
        table.getColumnModel().getColumn(7).setCellRenderer(new ActionButtonRenderer());
        table.getColumnModel().getColumn(7).setCellEditor(new ActionButtonEditor());

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new LineBorder(COLOR_BORDER, 1));
        scroll.getViewport().setBackground(COLOR_BG_MAIN);

        // Matikan Scrollbar
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        return scroll;
    }

    // --- UI COMPONENTS & RENDERERS (Sama Persis) ---

    private JPanel createPaginationFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(COLOR_BG_MAIN);
        footer.setBorder(new EmptyBorder(15, 0, 0, 0));

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        right.setBackground(COLOR_BG_MAIN);

        btnPrev = new JButton("<");
        btnNext = new JButton(">");
        lblPageInfo = new JLabel(" Page 1 of 1 ");
        lblPageInfo.setForeground(COLOR_TEXT_GRAY);
        lblPageInfo.setFont(FONT_BOLD);

        stylePaginationBtn(btnPrev);
        stylePaginationBtn(btnNext);

        btnPrev.addActionListener(e -> {
            if (currentPage > 1) { currentPage--; refreshTable(); }
        });

        btnNext.addActionListener(e -> {
            int totalPages = (int) Math.ceil((double) filteredData.size() / rowsPerPage);
            if (currentPage < totalPages) { currentPage++; refreshTable(); }
        });

        right.add(btnPrev); right.add(lblPageInfo); right.add(btnNext);
        footer.add(right, BorderLayout.EAST);
        return footer;
    }

    private void stylePaginationBtn(JButton btn) {
        btn.setBackground(COLOR_BG_TABLE);
        btn.setForeground(COLOR_TEXT_WHITE);
        btn.setBorder(new LineBorder(COLOR_BORDER));
        btn.setPreferredSize(new Dimension(40, 30));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    class BaseTableCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, false, row, column);
            if (isSelected) setBackground(COLOR_SELECTION);
            else setBackground(COLOR_BG_TABLE);
            setForeground(COLOR_TEXT_WHITE);
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
            setBackground(COLOR_BG_MAIN);
            return this;
        }
    }

    class ActionPanel extends JPanel {
        public JButton btnEdit = new JButton("Edit");
        public JButton btnDelete = new JButton("Delete");

        public ActionPanel() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 12));
            setOpaque(true); setBackground(COLOR_BG_TABLE);
            styleActionBtn(btnEdit, new Color(255, 193, 7));
            styleActionBtn(btnDelete, new Color(220, 53, 69));
            add(btnEdit); add(btnDelete);
        }
        private void styleActionBtn(JButton btn, Color c) {
            btn.setFont(new Font("SansSerif", Font.BOLD, 12));
            btn.setForeground(c);
            btn.setBackground(new Color(45, 45, 45));
            btn.setOpaque(true); btn.setBorderPainted(false); btn.setFocusPainted(false);
            btn.setMargin(new Insets(0, 0, 0, 0));
            btn.setPreferredSize(new Dimension(65, 30));
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
    }

    class ActionButtonRenderer implements TableCellRenderer {
        private ActionPanel panel = new ActionPanel();
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            panel.setBackground(isSelected ? COLOR_SELECTION : COLOR_BG_TABLE);
            panel.setBorder(new MatteBorder(0, 0, 1, 0, COLOR_BORDER));
            return panel;
        }
    }

    class ActionButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private ActionPanel panel = new ActionPanel();
        private String currentKode;
        private String currentNama;

        public ActionButtonEditor() {
            panel.btnEdit.addActionListener(e -> { fireEditingStopped(); openEditForm(); });
            panel.btnDelete.addActionListener(e -> { fireEditingStopped(); deleteData(); });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            panel.setBackground(COLOR_SELECTION);
            panel.setBorder(new MatteBorder(0, 0, 1, 0, COLOR_BORDER));

            this.currentKode = table.getValueAt(row, 1).toString();
            this.currentNama = table.getValueAt(row, 2).toString();
            return panel;
        }

        @Override public Object getCellEditorValue() { return ""; }

        private void openEditForm() {
            // Cari data lengkap berdasarkan kode
            String[] dataToEdit = null;
            for(String[] d : allDataCache) {
                if(d[0].equals(currentKode)) { // d[0] adalah Kode
                    dataToEdit = d;
                    break;
                }
            }

            if(dataToEdit != null) {
                // [BARU] Buka form edit dengan membawa data
                new InventoryForm(dataToEdit).setVisible(true);
                dispose(); // Tutup tabel
            }
        }

        private void deleteData() {
            int confirm = JOptionPane.showConfirmDialog(null,
                    "Hapus barang " + currentNama + "?",
                    "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                // controller.deleteInventory(currentKode);
                JOptionPane.showMessageDialog(null, "Data dihapus (Simulasi)");
            }
        }
    }

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

    // --- SIDEBAR (Menu Product di-Set Aktif) ---
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

        // Menu Buttons
        gbc.gridy = 2; panel.add(createMenuButton("Overview", "📊", false), gbc);
        gbc.gridy = 3; panel.add(createMenuButton("Customer", "👥", false), gbc);
        gbc.gridy = 4; panel.add(createMenuButton("Product", "🛍️", true), gbc); // <-- AKTIF
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

        // Navigasi
        if(text.equals("Overview")) btn.addActionListener(e->{new DashboardView().setVisible(true);dispose();});
        if(text.equals("Customer")) btn.addActionListener(e->{new CustomerTable().setVisible(true);dispose();});
        if(text.equals("History")) btn.addActionListener(e->{new ServiceHistoryTable().setVisible(true);dispose();});

        return btn;
    }
}