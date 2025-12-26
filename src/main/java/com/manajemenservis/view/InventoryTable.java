package com.manajemenservis.view;

import com.manajemenservis.controller.InventoryController;
import com.manajemenservis.model.SparePart;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class InventoryTable extends JFrame {

    private final Color COLOR_BG_MAIN = new Color(18, 18, 18);
    private final Color COLOR_BG_TABLE = new Color(30, 30, 30);
    private final Color COLOR_TEXT_WHITE = new Color(255, 255, 255);
    private final Color COLOR_ACCENT = new Color(65, 105, 225);

    private JTable table;
    private DefaultTableModel tableModel;
    private InventoryController controller;

    public InventoryTable() {
        controller = new InventoryController();
        setTitle("BENGKEL KU - Stok Sparepart (Product)");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(COLOR_BG_MAIN);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(COLOR_BG_MAIN);
        headerPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel title = new JLabel("Stok Sparepart");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(COLOR_ACCENT);
        headerPanel.add(title, BorderLayout.WEST);

        // Tombol Kembali
        JButton btnBack = new JButton("Kembali");
        btnBack.addActionListener(e -> {
            new DashboardView().setVisible(true);
            dispose();
        });
        headerPanel.add(btnBack, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Tabel
        JScrollPane scrollPane = createTable();
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(COLOR_BG_MAIN);
        tableContainer.setBorder(new EmptyBorder(0, 30, 30, 30));
        tableContainer.add(scrollPane);
        add(tableContainer, BorderLayout.CENTER);

        loadData();
    }

    private JScrollPane createTable() {
        String[] columns = {"Nama Barang", "Harga Satuan", "Stok Tersedia"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);

        table.setBackground(COLOR_BG_TABLE);
        table.setForeground(COLOR_TEXT_WHITE);
        table.setRowHeight(40);
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(50, 50, 50));

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(45, 45, 45));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(0, 45));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setBorder(new EmptyBorder(0, 10, 0, 10));
        centerRenderer.setBackground(COLOR_BG_TABLE);
        centerRenderer.setForeground(COLOR_TEXT_WHITE);
        for (int i = 0; i < table.getColumnCount(); i++) table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new LineBorder(new Color(50, 50, 50), 1));
        scroll.getViewport().setBackground(COLOR_BG_MAIN);
        return scroll;
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<SparePart> list = controller.getAllSpareParts();
        for (SparePart p : list) {
            tableModel.addRow(new Object[]{
                    p.getItemName(),
                    String.format("Rp %,.0f", p.getPrice()),
                    p.getStock()
            });
        }
    }
}